package com.mongs.whatsappclone.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Slf4j
@Service
public class FileService {

    @Value("${application.file.uploads.media-output-path}")
    private String filePath;

    public String saveFile(
            @NonNull MultipartFile file,
            @NonNull String senderId)
    {

        final String fileUploadSubPath = "users" + separator + senderId;
        return uploadFile(file, fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile file, @NonNull String fileUploadSubPath) {

        final String finalUploadPath = filePath + separator + fileUploadSubPath;
        final File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean isFileCreated = targetFolder.mkdirs();
            if (!isFileCreated) {
                log.warn("Failed to create directory: {}", targetFolder.getAbsolutePath());
                return null;
            }
        }
        final String fileExtension = getFileExtension(file.getOriginalFilename());
        final String targetFilepath = finalUploadPath + separator + currentTimeMillis() + fileExtension;
        Path path = Paths.get(targetFilepath);
        try{
            Files.write(path, file.getBytes());
            log.info("File saved to: {}", targetFilepath);
            return targetFilepath;
        }catch (IOException e){
            log.error("Failed to upload file: {}", e.getMessage());
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return null;
        }
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot == -1 || lastDot == originalFilename.length() - 1) {
            return null; // No extension found
        }

        return originalFilename.substring(lastDot + 1).toLowerCase();
    }
}
