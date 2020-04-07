package com.thomaster.ourcloud.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile("prod")
public class FileBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private OCUserService userService;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private FileService fileService;

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

        OCUser user1 = new OCUser();

        user1.setUsername("Thomaster");

        user1.setPassword("$2a$10$.8nhpmny.b3DSfYabD1dle1EK4OeV1n.EdTrvpAXqqiArqpQm8LoG");
        user1.setRole(Role.ADMIN);
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
        folderA.setFileSize(1583L);
        folderA.setOriginalName("Thomaster");
        folderA.setRelativePath("Thomaster");
        folderA.setOwner(user1);
        fileRepository.save(folderA);

        UploadedFolder folderB = new UploadedFolder();
        folderB.setOriginalName("homework files");
        folderB.setParentFolderPath("Thomaster");
        folderB.setFileSize(1250L);
        folderB.setRelativePath("Thomaster.homework_files");
        folderB.setOwner(user1);
        folderB = fileRepository.save(folderB);

        UploadedFolder folderC = new UploadedFolder();
        folderC.setOriginalName("holiday pictures");
        folderC.setParentFolderPath("Thomaster");
        folderC.setFileSize(333L);
        folderC.setRelativePath("Thomaster.holiday_pictures");
        folderC.setOwner(user1);
        folderC = fileRepository.save(folderC);

//        folderA.setContainedFiles(Set.of(folderB, folderC));
        //fileRepository.save(folderA);

        UploadedFile fileD = new UploadedFile();
        fileD.setOriginalName("history.txt");
        fileD.setParentFolderPath("Thomaster.homework_files");
        fileD.setFileSize(500L);
        fileD.setRelativePath("Thomaster.homework_files.history");
        fileD.setOwner(user1);
        fileD = fileRepository.save(fileD);

        UploadedFile fileE = new UploadedFile();
        fileE.setOriginalName("chemistry.txt");
        fileE.setParentFolderPath("Thomaster.homework_files");
        fileE.setFileSize(750L);
        fileE.setRelativePath("Thomaster.homework_files.chemistry");
        fileE.setOwner(user1);
        fileE = fileRepository.save(fileE);

        UploadedFolder folderF = new UploadedFolder();
        folderF.setOriginalName("2019");
        folderF.setParentFolderPath("Thomaster.holiday_pictures");
        folderF.setFileSize(0L);
        folderF.setRelativePath("Thomaster.holiday_pictures.2019");
        folderF.setOwner(user1);
        folderF = fileRepository.save(folderF);

        UploadedFile fileG = new UploadedFile();
        fileG.setOriginalName("italy.jpg");
        fileG.setParentFolderPath("Thomaster.holiday_pictures");
        fileG.setFileSize(333L);
        fileG.setRelativePath("Thomaster.holiday_pictures.italy");
        fileG.setOwner(user1);
        fileG = fileRepository.save(fileG);

        UploadedFolder folderAOther = new UploadedFolder();
        folderAOther.setParentFolderPath("");
        folderAOther.setFileSize(933L);
        folderAOther.setOriginalName("Other");
        folderAOther.setRelativePath("Other");
        folderAOther.setOwner(user2);
        fileRepository.save(folderAOther);

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
        fileCOther = fileRepository.save(fileCOther);

        UploadedFile fileDOther = new UploadedFile();
        fileDOther.setOriginalName("Backstreet_Boys - I want it that way.mp4");
        fileDOther.setFileSize(333L);
        fileDOther.setParentFolderPath("Other");
        fileDOther.setRelativePath("Other.Backstreet_Boys_I_want_it_that_way");
        fileDOther.setOwner(user2);
        fileDOther = fileRepository.save(fileDOther);


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("Thomaster", null, Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

//        FileSystemElementJSON jsonFolder = FileSystemElementJSON.of(folder);
//        try {
//            System.out.println(new ObjectMapper().writeValueAsString(jsonFolder));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


        //System.out.println(FileStructureFormatter.formatRecursive(folderA));

        //FileSystemElement result = fileService.findFSElementWithFullTreeByPath("A");

        //System.out.println(FileStructureFormatter.formatRecursive(result));

        //fileService.deleteFSElementRecursively("A/B/D");
        //fileRepository.updateFileSizeAllAncestorFolders("A.B", -500L);

        //FileSystemElement afterDelete = fileService.findFSElementWithFullTreeByPath("A");

        //System.out.println(FileStructureFormatter.formatRecursive(afterDelete));

    }
}
