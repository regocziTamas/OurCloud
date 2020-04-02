//package com.thomaster.ourcloud.repositories.file;
//
//import java.text.MessageFormat;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class PersistentFileStructureFormatter {
//
//    public static String formatRecursive(PersistentFileSystemElement file) {
//        return formatContents(file, 0, 4, true);
//    }
//
//    public static String formatFirstLevel(PersistentFileSystemElement file) {
//        return formatContents(file, 0, 4, false);
//    }
//
//
//    private static String formatContents(PersistentFileSystemElement fileToFormat, int indent, int indentIncrement, boolean recursive) {
//        StringBuilder stringBuilder = new StringBuilder((MessageFormat.format("- {0}", fileToFormat.nameAndSize()).indent(indent)));
//
//        if (fileToFormat instanceof PersistentUploadedFolder) {
//
//            indent = indent + indentIncrement;
//
//            for (PersistentUploadedFolder folder : getFolders(fileToFormat)) {
//                if(!recursive)
//                    stringBuilder.append(MessageFormat.format("- {0}", folder.nameAndSize()).indent(indent));
//                else
//                    stringBuilder.append(formatContents(folder, indent, indentIncrement, true));
//            }
//
//            for (PersistentUploadedFile file: getFiles(fileToFormat)) {
//                stringBuilder.append(MessageFormat.format("- {0}", file.nameAndSize()).indent(indent));
//            }
//        }
//
//        return stringBuilder.toString();
//
//    }
//
//    private static Set<PersistentUploadedFolder> getFolders(PersistentFileSystemElement file) {
//        return ((PersistentUploadedFolder)file).getFiles()
//                .stream()
//                .filter(el -> el instanceof PersistentUploadedFolder)
//                .map(PersistentUploadedFolder.class::cast)
//                .collect(Collectors.toSet());
//    }
//
//    private static Set<PersistentUploadedFile> getFiles(PersistentFileSystemElement file) {
//        return ((PersistentUploadedFolder)file).getFiles()
//                .stream()
//                .filter(el -> el instanceof PersistentUploadedFile)
//                .map(PersistentUploadedFile.class::cast)
//                .collect(Collectors.toSet());
//    }
//
//
//}
