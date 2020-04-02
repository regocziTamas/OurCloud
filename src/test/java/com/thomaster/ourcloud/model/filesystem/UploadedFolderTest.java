package com.thomaster.ourcloud.model.filesystem;

import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UploadedFolderTest {

    @Test
    void testToString() {
        UploadedFolder folderA = new UploadedFolder();
        folderA.setParentFolderPath("");
        folderA.setFileSize(1583L);
        folderA.setOriginalName("Thomaster");
        folderA.setRelativePath("Thomaster");

        ContainedFSEInfo folderB = new ContainedFSEInfo();
        folderB.setOriginalName("homework files");
        folderB.setParentFolderPath("Thomaster");
        folderB.setSize(1250L);
        folderB.setRelativePath("Thomaster.homework_files");

        ContainedFSEInfo folderC = new ContainedFSEInfo();
        folderC.setOriginalName("holiday pictures");
        folderC.setParentFolderPath("Thomaster");
        folderC.setSize(333L);
        folderC.setRelativePath("Thomaster.holiday_pictures");

        folderA.getContainedFileInfos().add(folderB);
        folderA.getContainedFileInfos().add(folderC);

        System.out.println(folderA);

        assertThat(folderA.toString()).isEqualTo("[Folder] Thomaster | 1583 bytes\n" +
                "    - Contained File: holiday pictures | 333 bytes\n" +
                "    - Contained File: homework files | 1250 bytes");
    }
}