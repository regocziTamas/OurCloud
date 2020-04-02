package com.thomaster.ourcloud.repositories.file;

import com.thomaster.ourcloud.model.user.OCUser;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersistentFSEHierarchyConverterTest {

    @Test
    void convertAndBuildHierarchy() {
        OCUser user1 = new OCUser();
        user1.setUsername("Thomaster");

        PersistentUploadedFolder folderA = new PersistentUploadedFolder();
        folderA.setId(1L);
        folderA.setOriginalName("Thomaster");
        folderA.setParentFolderPath("");
        folderA.setFileSize(1000L);
        folderA.setRelativePath("Thomaster");
        folderA.setOwner(user1);

        PersistentUploadedFile fileB = new PersistentUploadedFile();
        fileB.setId(2L);
        fileB.setOriginalName("history.txt");
        fileB.setParentFolderPath("Thomaster");
        fileB.setFileSize(500L);
        fileB.setRelativePath("Thomaster.history");
        fileB.setOwner(user1);

        PersistentUploadedFile fileC = new PersistentUploadedFile();
        fileC.setId(3L);
        fileC.setOriginalName("chemistry.txt");
        fileC.setParentFolderPath("Thomaster");
        fileC.setFileSize(500L);
        fileC.setRelativePath("Thomaster.chemistry");
        fileC.setOwner(user1);

        new PersistentFSEHierarchyConverter(Set.of(folderA, fileB, fileC), "Thomaster");

    }
}