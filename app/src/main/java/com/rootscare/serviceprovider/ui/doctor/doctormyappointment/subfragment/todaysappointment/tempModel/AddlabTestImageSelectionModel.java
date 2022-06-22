package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel;


import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public class AddlabTestImageSelectionModel {

    private String fileName;
    private String fileNameAsOriginal;
    private String filePath;
    private String type;
    private File file;

    private String rawFileName;
    private String imageNameGivenFromApiAfterImageUpload;

    private CropImage.ActivityResult imageDataFromCropLibrary;


    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileNameAsOriginal(String fileNameAsOriginal) {
        this.fileNameAsOriginal = fileNameAsOriginal;
    }

    public String getFileNameAsOriginal() {
        return fileNameAsOriginal;
    }

    public String getRawFileName() {
        return rawFileName;
    }

    public void setRawFileName(String rawFileName) {
        this.rawFileName = rawFileName;
    }

    public String getImageNameGivenFromApiAfterImageUpload() {
        return imageNameGivenFromApiAfterImageUpload;
    }

    public void setImageNameGivenFromApiAfterImageUpload(String imageNameGivenFromApiAfterImageUpload) {
        this.imageNameGivenFromApiAfterImageUpload = imageNameGivenFromApiAfterImageUpload;
    }


    public CropImage.ActivityResult getImageDataFromCropLibrary() {
        return imageDataFromCropLibrary;
    }

    public void setImageDataFromCropLibrary(CropImage.ActivityResult imageDataFromCropLibrary) {
        this.imageDataFromCropLibrary = imageDataFromCropLibrary;
    }
}
