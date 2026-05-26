package com.utcn.demo.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class DriveService {

    // Luăm automat ID-ul folderului din application.properties
    @Value("${google.drive.folder.id}")
    private String folderId;

    public String uploadImageToDrive(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // 1. Citim "legitimația" (credentials.json) din resurse
            GoogleCredentials credentials = GoogleCredentials.fromStream(new ClassPathResource("credentials.json").getInputStream())
                    .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

            // 2. Creăm instanța serviciului Google Drive
            Drive driveService = new Drive.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials))
                    .setApplicationName("StackOverflow Clone")
                    .build();

            // 3. Setăm metadatele fișierului (cum se numește și în ce folder îl punem)
            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList(folderId));

            // 4. Pregătim conținutul fișierului pentru upload
            InputStreamContent mediaContent = new InputStreamContent(
                    file.getContentType(),
                    file.getInputStream()
            );

            // 5. Facem upload-ul propriu-zis către Google
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // 6. Returnăm link-ul "păcălit" pentru a vedea direct pixelii pozei
            return "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();

        } catch (IOException e) {
            throw new RuntimeException("Eroare la încărcarea imaginii pe Google Drive: " + e.getMessage());
        }
    }
}