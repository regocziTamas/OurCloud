//package com.thomaster.ourcloud.helper;
//
//import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
//import com.thomaster.ourcloud.model.filesystem.UploadedFile;
//import com.thomaster.ourcloud.model.filesystem.UploadedFolder;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class FileSystemElementHelper {
//
//    public static Set<FileSystemElement> flattenChildrenHierarchy(FileSystemElement element) {
//        return getChildren(element);
//    }
//
//    private static Set<FileSystemElement> getChildren(FileSystemElement element) {
//
//        if(element instanceof UploadedFile)
//            return Set.of(element);
//        else {
//            UploadedFolder folder = (UploadedFolder) element;
//
////            if(folder.getFiles().isEmpty())
////                return Set.of(folder);
//
//            Set<FileSystemElement> children = folder.getFiles()
//                    .stream()
//                    .flatMap(file -> getChildren(file).stream())
//                    .collect(Collectors.toSet());
//            children.add(folder);
//            return children;
//        }
//
//    }
//
//}
