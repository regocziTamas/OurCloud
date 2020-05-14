package com.thomaster.ourcloud.services.request;


import com.thomaster.ourcloud.model.filesystem.FileSystemElement;
import com.thomaster.ourcloud.services.request.base.BaseRequest;
import com.thomaster.ourcloud.services.request.delete.DeleteRequest;
import com.thomaster.ourcloud.services.request.delete.DeleteRequestValidator;
import com.thomaster.ourcloud.services.request.download.DownloadRequestValidator;
import com.thomaster.ourcloud.services.request.marker.PostQueryRequestValidation;
import com.thomaster.ourcloud.services.request.marker.PostQueryRequestValidationType;
import com.thomaster.ourcloud.services.request.marker.PreQueryRequestValidation;
import com.thomaster.ourcloud.services.request.marker.PreQueryRequestValidationType;
import com.thomaster.ourcloud.services.request.read.ReadRequest;
import com.thomaster.ourcloud.services.request.read.ReadRequestFactory;
import com.thomaster.ourcloud.services.request.read.ReadRequestValidator;
import com.thomaster.ourcloud.services.request.save.file.PreFlightSaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.PreFlightSaveFileRequestValidator;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequest;
import com.thomaster.ourcloud.services.request.save.file.SaveFileRequestValidator;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequest;
import com.thomaster.ourcloud.services.request.save.folder.SaveFolderRequestValidator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
public class RequestValidatorAspect {

    private ReadRequestValidator readRequestValidator;
    private ReadRequestFactory readRequestFactory;

    private SaveFolderRequestValidator saveFolderRequestValidator;

    private PreFlightSaveFileRequestValidator preFlightSaveFileRequestValidator;
    private SaveFileRequestValidator saveFileRequestValidator;

    private DeleteRequestValidator deleteRequestValidator;

    private DownloadRequestValidator downloadRequestValidator;

    public RequestValidatorAspect(ReadRequestValidator readRequestValidator,
                                  ReadRequestFactory readRequestFactory,
                                  SaveFolderRequestValidator saveFolderRequestValidator,
                                  PreFlightSaveFileRequestValidator preFlightSaveFileRequestValidator,
                                  SaveFileRequestValidator saveFileRequestValidator, DeleteRequestValidator deleteRequestValidator,
                                  DownloadRequestValidator downloadRequestValidator) {
        this.readRequestValidator = readRequestValidator;
        this.readRequestFactory = readRequestFactory;
        this.saveFolderRequestValidator = saveFolderRequestValidator;
        this.preFlightSaveFileRequestValidator = preFlightSaveFileRequestValidator;
        this.saveFileRequestValidator = saveFileRequestValidator;
        this.deleteRequestValidator = deleteRequestValidator;
        this.downloadRequestValidator = downloadRequestValidator;
    }

    //@AfterReturning(value = "execution(* com.thomaster.ourcloud.services.FileService.findFSElementWithFullTreeByPath(..))", returning = "fileSystemElement")
    public void validateReadRequest(FileSystemElement fileSystemElement, String requestedPath) {
        ReadRequest readRequest = readRequestFactory.createReadRequest(fileSystemElement, requestedPath);
        readRequestValidator.validateRequest(readRequest);
    }

    public void validateDownloadFileRequest(FileSystemElement fileSystemElement, String requestedPath) {
        ReadRequest downloadFileRequest = readRequestFactory.createReadRequest(fileSystemElement, requestedPath);
        downloadRequestValidator.validateRequest(downloadFileRequest);
    }

    //@Before("execution(* com.thomaster.ourcloud.services.FileService.saveFolder(..)) && args(request,..)")
    public void validateSaveFolderRequest(SaveFolderRequest request) {
        System.out.println("Save File Request Validation");
        saveFolderRequestValidator.validateRequest(request);
    }

    //@Before("execution(* com.thomaster.ourcloud.services.FileService.saveFile(..)) && args(request,..)")
    public void validatePreFlightSaveFileRequest(PreFlightSaveFileRequest request) {
        preFlightSaveFileRequestValidator.validateRequest(request);
    }

    public void validateSaveFileRequest(SaveFileRequest request) {
        saveFileRequestValidator.validateRequest(request);
    }

    //@Before("execution(* com.thomaster.ourcloud.services.FileService.deleteFSElementRecursively(..)) && args(deleteRequest,..)")
    public void validateDeleteFileRequest(DeleteRequest deleteRequest) {
        deleteRequestValidator.validateRequest(deleteRequest);
    }

    @Before("@annotation(com.thomaster.ourcloud.services.request.marker.PreQueryRequestValidation) && args(request,..)")
    public void preQueryRequestValidationHandler(JoinPoint joinPoint, BaseRequest request) {
        PreQueryRequestValidationType value = extractAnnotation(PreQueryRequestValidation.class, joinPoint).value();

        switch (value) {
            case PREFLIGHT_UPLOAD_FLIGHT:
                validatePreFlightSaveFileRequest((PreFlightSaveFileRequest) request);
                break;
            case UPLOAD_FILE:
                validateSaveFileRequest((SaveFileRequest) request);
                break;
            case UPLOAD_FOLDER:
                validateSaveFolderRequest((SaveFolderRequest) request);
                break;
            case DELETE:
                validateDeleteFileRequest((DeleteRequest) request);
                break;

            default: throw new UnsupportedOperationException("Unknown PreQueryRequestValidationType");
        }
    }

    @Around(value = "@annotation(com.thomaster.ourcloud.services.request.marker.PostQueryRequestValidation)")
    public FileSystemElement postQueryRequestValidationHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String filePath = (String) proceedingJoinPoint.getArgs()[0];

        FileSystemElement fileSystemElement = (FileSystemElement) proceedingJoinPoint.proceed();

        PostQueryRequestValidationType value = extractAnnotation(PostQueryRequestValidation.class, proceedingJoinPoint).value();

        switch (value) {
            case READ:
                validateReadRequest(fileSystemElement, filePath);
                break;
            case DOWNLOAD:
                validateDownloadFileRequest(fileSystemElement, filePath);
                break;
            default: throw new UnsupportedOperationException("Unknown PostQueryRequestValidationType");
        }

        return fileSystemElement;
    }

    private static <T extends Annotation> T extractAnnotation(Class<T> annotationClass, JoinPoint joinPoint)
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }

}
