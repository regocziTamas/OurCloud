package com.thomaster.ourcloud.repositories.file;

import com.thomaster.ourcloud.model.filesystem.ContainedFSEInfo;
import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.model.filesystem.UploadedFile;
import com.thomaster.ourcloud.model.filesystem.UploadedFolder;

public class PersistentToDomainConverter {

    public static FileSystemElement convertToDomain(PersistentFileSystemElement persistentFSE) {

        FileSystemElement fse;

        if(persistentFSE instanceof PersistentUploadedFile)
            fse = new UploadedFile();
        else if(persistentFSE instanceof PersistentUploadedFolder)
            fse = new UploadedFolder();
        else
            throw new IllegalArgumentException("Unknown class");

        fse.setId(persistentFSE.getId());
        fse.setOriginalName(persistentFSE.getOriginalName());
        fse.setOwner(persistentFSE.getOwner());
        fse.setParentFolderPath(persistentFSE.getParentFolderPath());
        fse.setRelativePath(persistentFSE.getRelativePath());
        fse.setFileSize(persistentFSE.getFileSize());

        if (fse instanceof UploadedFile)
            ((UploadedFile) fse).setFilenameOnDisk(((PersistentUploadedFile) persistentFSE).getFilenameOnDisk());

        return fse;
    }

    public static ContainedFSEInfo convertToInfo(PersistentFileSystemElement persistentFSE) {

        ContainedFSEInfo fseInfo = new ContainedFSEInfo();

        fseInfo.setId(persistentFSE.getId());
        fseInfo.setOriginalName(persistentFSE.getOriginalName());
        fseInfo.setOwner(persistentFSE.getOwner());
        fseInfo.setParentFolderPath(persistentFSE.getParentFolderPath());
        fseInfo.setRelativePath(persistentFSE.getRelativePath());
        fseInfo.setSize(persistentFSE.getFileSize());

        if(persistentFSE instanceof PersistentUploadedFile)
            fseInfo.setFolder(false);
        else if(persistentFSE instanceof PersistentUploadedFolder)
            fseInfo.setFolder(true);
        else
            throw new IllegalArgumentException("Unknown class");

        return fseInfo;
    }

    public static PersistentFileSystemElement convertToPersistent(FileSystemElement domainFSE) {

        PersistentFileSystemElement fse;

        if (domainFSE instanceof UploadedFolder)
            fse = new PersistentUploadedFolder();
        else if (domainFSE instanceof UploadedFile)
            fse = new PersistentUploadedFile();
        else
            throw new IllegalArgumentException("Unknown class");

        fse.setId(domainFSE.getId());
        fse.setOriginalName(domainFSE.getOriginalName());
        fse.setOwner(domainFSE.getOwner());
        fse.setParentFolderPath(domainFSE.getParentFolderPath());
        fse.setRelativePath(domainFSE.getRelativePath());
        fse.setFileSize(domainFSE.getFileSize());

        if(fse instanceof PersistentUploadedFile)
            ((PersistentUploadedFile) fse).setFilenameOnDisk(((UploadedFile) domainFSE).getFilenameOnDisk());

        return fse;
    }
}
