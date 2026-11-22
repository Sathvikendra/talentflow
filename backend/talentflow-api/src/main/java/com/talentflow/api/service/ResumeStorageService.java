package com.talentflow.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.talentflow.api.config.ResumeStorageProperties;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service 
public class ResumeStorageService {
    private final Path storageLocation;

    public ResumeStorageService(ResumeStorageProperties properties) {
        this.storageLocation = Paths.get(properties.getResumeDirectory())
                .toAbsolutePath()
                .normalize();
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        initializeStorage();
    }

    public void initializeStorage() {
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not initialize resume storage directory",
                    exception);
        }
    }

    public Path saveFile(String fileName, byte[] fileBytes) throws IOException {
        Path targetPath = storageLocation.resolve(fileName).normalize();

        if (!targetPath.startsWith(storageLocation)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        return Files.write(targetPath, fileBytes);
    }
}
