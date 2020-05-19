package com.thomaster.ourcloud.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.thomaster.ourcloud.json.FileSystemElementJSON;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import com.thomaster.ourcloud.model.user.OCUser;
import com.thomaster.ourcloud.model.user.Role;
import com.thomaster.ourcloud.repositories.UserRepository;
import com.thomaster.ourcloud.repositories.file.FileRepository;
import com.thomaster.ourcloud.services.FileService;
import com.thomaster.ourcloud.services.OCUserService;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("prod")
public class FileBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private OCUserService userService;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private FileService fileService;

    Logger logger = LoggerFactory.getLogger(FileBootstrap.class);

    public FileBootstrap(OCUserService userService,
                         FileRepository fileRepository,
                         FileService fileService,
                         UserRepository userRepository){

        this.fileRepository = fileRepository;
        this.userService = userService;
        this.fileService = fileService;
        this.userRepository = userRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Map<String, Long> lengthMap = copyDefaultFiles();

        OCUser user1 = new OCUser();

        user1.setUsername("Guest01");

        user1.setPassword("$2a$10$.8nhpmny.b3DSfYabD1dle1EK4OeV1n.EdTrvpAXqqiArqpQm8LoG");
        user1.setRole(Role.ADMIN);
        //user1.setUsedBytes(10000000000L);
        user1.setUsedBytes(0L);

        OCUser user2 = new OCUser();

        user2.setUsername("Other");

        user2.setPassword("$2a$10$.8nhpmny.b3DSfYabD1dle1EK4OeV1n.EdTrvpAXqqiArqpQm8LoG");
        user2.setRole(Role.ADMIN);
        user2.setUsedBytes(0L);

        userService.save(user1);
        userService.save(user2);

        UploadedFolder folderA = new UploadedFolder();
        folderA.setParentFolderPath("");
        folderA.setFileSize(5132994L);
        folderA.setOriginalName("Guest01");
        folderA.setRelativePath("Guest01");
        folderA.setOwner(user1);
        folderA = fileRepository.save(folderA);

        UploadedFolder folderB = new UploadedFolder();
        folderB.setOriginalName("homework files");
        folderB.setParentFolderPath("Guest01");
        folderB.setFileSize(3563L);
        folderB.setRelativePath("Guest01.homework_files");
        folderB.setOwner(user1);
        folderB = fileRepository.save(folderB);

        UploadedFolder folderC = new UploadedFolder();
        folderC.setOriginalName("holiday pictures");
        folderC.setParentFolderPath("Guest01");
        folderC.setFileSize(5129431L);
        folderC.setRelativePath("Guest01.holiday_pictures");
        folderC.setOwner(user1);
        folderC = fileRepository.save(folderC);

//        folderA.setContainedFiles(Set.of(folderB, folderC));
//        fileRepository.save(folderA);

        UploadedFile fileD = new UploadedFile();
        fileD.setOriginalName("history.txt");
        fileD.setParentFolderPath("Guest01.homework_files");
        fileD.setFileSize(lengthMap.get("history"));
        logger.info("Setting size of file 'history' to: " + lengthMap.get("history") + " bytes" );
        fileD.setRelativePath("Guest01.homework_files.history");
        fileD.setFilenameOnDisk("history");
        fileD.setOwner(user1);
        fileD.setMimeType("text/plain");
        fileD = fileRepository.save(fileD);

        UploadedFile fileE = new UploadedFile();
        fileE.setOriginalName("chemistry.txt");
        fileE.setParentFolderPath("Guest01.homework_files");
        fileE.setFileSize(lengthMap.get("chemistry"));
        logger.info("Setting size of file 'chemistry' to: " + lengthMap.get("chemistry") + " bytes" );
        fileE.setRelativePath("Guest01.homework_files.chemistry");
        fileE.setFilenameOnDisk("chemistry");
        fileE.setOwner(user1);
        fileE.setMimeType("text/plain");
        fileE = fileRepository.save(fileE);

        UploadedFolder folderF = new UploadedFolder();
        folderF.setOriginalName("2019");
        folderF.setParentFolderPath("Guest01.holiday_pictures");
        folderF.setFileSize(0L);
        folderF.setRelativePath("Guest01.holiday_pictures.2019");
        folderF.setOwner(user1);
        folderF = fileRepository.save(folderF);

        UploadedFile fileG = new UploadedFile();
        fileG.setOriginalName("italy.jpg");
        fileG.setParentFolderPath("Guest01.holiday_pictures");
        fileG.setFileSize(lengthMap.get("italy"));
        logger.info("Setting size of file 'italy' to: " + lengthMap.get("italy") + " bytes" );
        fileG.setRelativePath("Guest01.holiday_pictures.italy");
        fileG.setFilenameOnDisk("italy");
        fileG.setOwner(user1);
        fileG.setMimeType("image/jpg");
        fileG = fileRepository.save(fileG);

        UploadedFolder folderAOther = new UploadedFolder();
        folderAOther.setParentFolderPath("");
        folderAOther.setFileSize(933L);
        folderAOther.setOriginalName("Other");
        folderAOther.setRelativePath("Other");
        folderAOther.setOwner(user2);
        folderAOther = fileRepository.save(folderAOther);

        UploadedFolder folderBOther = new UploadedFolder();
        folderBOther.setOriginalName("books");
        folderBOther.setParentFolderPath("Other");
        folderBOther.setFileSize(600L);
        folderBOther.setRelativePath("Other.books");
        folderBOther.setOwner(user2);
        folderBOther = fileRepository.save(folderBOther);

        UploadedFile fileCOther = new UploadedFile();
        fileCOther.setOriginalName("Harry Potter and Prisoner of Azkaban.pdf");
        fileCOther.setFileSize(600L);
        fileCOther.setParentFolderPath("Other.books");
        fileCOther.setRelativePath("Other.books.Harry_Potter_and_Prisoner_of_Azkaban");
        fileCOther.setOwner(user2);
        fileCOther.setMimeType("application/pdf");
        fileCOther = fileRepository.save(fileCOther);

        UploadedFile fileDOther = new UploadedFile();
        fileDOther.setOriginalName("Backstreet_Boys - I want it that way.mp4");
        fileDOther.setFileSize(333L);
        fileDOther.setParentFolderPath("Other");
        fileDOther.setRelativePath("Other.Backstreet_Boys_I_want_it_that_way");
        fileDOther.setOwner(user2);
        fileDOther.setMimeType("video/mp4");
        fileDOther = fileRepository.save(fileDOther);


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("Guest01", null, Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

//        fileService.findFSElementWithContainedFilesByPath("asd");

//        ArrayList<FileSystemElement> all = new ArrayList<>();
//        all.add(folderA);
//        all.add(folderB);
//        all.add(folderC);
//        all.add(folderF);
//        all.add(fileD);
//        all.add(fileE);
//        all.add(fileG);
//        all.add(folderAOther);
//        all.add(folderBOther);
//        all.add(fileCOther);
//        all.add(fileDOther);


//        List<FileSystemElementJSON> elementJSONS = all.stream().map(e -> {
//            FileSystemElement queried = fileRepository.findOneByPathWithContainedFiles(e.getRelativePath());
//            return FileSystemElementJSON.of(queried);
//        }).collect(Collectors.toList());
//
//        elementJSONS.forEach(e -> {
//            try {
//                System.out.println("\"" + e.getRelativePath() + "\"");
//                System.out.println(new ObjectMapper().writeValueAsString(e));
//            } catch (JsonProcessingException ex) {
//                ex.printStackTrace();
//            }
//        });


//        FileSystemElementJSON jsonFolder = FileSystemElementJSON.of(folder);
//        try {
//            System.out.println(new ObjectMapper().writeValueAsString(jsonFolder));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        //Set<String> filesToDeleteOnDisk = fileRepository.deleteRecursivelyByPath("Thomaster");
        //System.out.println(filesToDeleteOnDisk);
        //System.out.println(FileStructureFormatter.formatRecursive(folderA));

        //FileSystemElement result = fileService.findFSElementWithFullTreeByPath("A");

        //System.out.println(FileStructureFormatter.formatRecursive(result));

        //fileService.deleteFSElementRecursively("A/B/D");
        //fileRepository.updateFileSizeAllAncestorFolders("A.B", -500L);

        //FileSystemElement afterDelete = fileService.findFSElementWithFullTreeByPath("A");

        //System.out.println(FileStructureFormatter.formatRecursive(afterDelete));

    }

    private Map<String, Long> copyDefaultFiles() {
        String storagePath = System.getenv("STORAGE_PATH");
        String testFileLocation = "src/main/resources/test_files";
        List<String> testFileNames = Arrays.asList("chemistry", "history", "italy");

        testFileNames
                .forEach(filename -> {
                    String target = storagePath + "/" + filename;
                    try {
                        //File file = ResourceUtils.getFile("classpath:" + filename);
                        InputStream fileInputStream = new ClassPathResource("classpath:" + filename).getInputStream();

                        //logger.info("Copying " + filename + " from: " + fileInputStream.getAbsolutePath() + " to: " + target);

                        FileOutputStream fileOutputStream = new FileOutputStream(target);
                        fileOutputStream.write(fileInputStream.readAllBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        Map<String, Long> lengthMap = testFileNames
                .stream()
                .map(filename -> {
                    File file = new File(storagePath + "/" + filename);
                    long length = file.length();
                    return Maps.immutableEntry(filename, length);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        logger.info("Test files with size: " + lengthMap);

        return lengthMap;
    }
}
