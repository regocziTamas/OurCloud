//package com.thomaster.ourcloud.helper;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class FSEHierarchyBuilder {
//
//    private UploadedFolder rootFolder;
//    private Set<FileSystemElement> elements;
//    private String rootPath;
//
//    public FSEHierarchyBuilder(Set<FileSystemElement> elements, String rootPath) {
//        this.rootPath = rootPath;
//        this.elements = elements;
//    }
//
//    public FileSystemElement build() {
//        FileSystemElement rootElement = findRootFolder(elements, rootPath);
//
//        return buildHierarchy(rootElement, elements);
//    }
//
//    private FileSystemElement buildHierarchy(FileSystemElement rootElement, Set<FileSystemElement> remainingElements) {
//
//        if (rootElement instanceof UploadedFile)
//            return rootElement;
//
//        UploadedFolder folder = (UploadedFolder) rootElement;
//
//        Set<FileSystemElement> childElements = new HashSet<>();
//
//        Iterator<FileSystemElement> remainingElementIterator = remainingElements.iterator();
//
//        while (remainingElementIterator.hasNext()) {
//            FileSystemElement currentElement = remainingElementIterator.next();
//
//            if (currentElement.getParentFolderPath().equals(rootElement.getRelativePath())) {
//                remainingElementIterator.remove();
//                childElements.add(currentElement);
//                currentElement.setParentFolder((UploadedFolder) rootElement);
//            }
//        }
//
//        folder.setContainedFiles(childElements);
//
//        if(!childElements.isEmpty()) {
//            for (FileSystemElement el : childElements)
//                buildHierarchy(el, remainingElements);
//        }
//
//        return rootElement;
//    }
//
//    private FileSystemElement findRootFolder(Set<FileSystemElement> elements, String rootPath) {
//        Set<FileSystemElement> rootElementFilterResult = elements
//                .stream()
//                .filter(element -> {
//                    return element.getRelativePath().equals(rootPath);
//                })
//                .collect(Collectors.toSet());
//
//        if (rootElementFilterResult.isEmpty())
//            throw new IllegalStateException("No file was found with path: " + rootPath);
//        else if (rootElementFilterResult.size() > 1)
//            throw new IllegalStateException("More than one files were found with path: " + rootPath);
//
//        FileSystemElement rootElement = rootElementFilterResult.iterator().next();
//
//        elements.remove(rootElement);
//
//        return rootElement;
//    }
//
//}
