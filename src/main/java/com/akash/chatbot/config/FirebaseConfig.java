package com.akash.chatbot.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FirebaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.service.account.path}")
    private String serviceAccountPath;

    @Value("${firebase.service.account.json:}")
    private String serviceAccountJson;

    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials;

        // Try JSON string first
        if (StringUtils.hasText(serviceAccountJson)) {
            logger.info("Initializing Firebase with service account JSON string");
            try (InputStream serviceAccount = new ByteArrayInputStream(serviceAccountJson.getBytes())) {
                credentials = GoogleCredentials.fromStream(serviceAccount);
            }
        }
        // Then try file path
        else {
            Path credentialsPath = Paths.get(serviceAccountPath);
            if (!Files.exists(credentialsPath)) {
                throw new FileNotFoundException(
                    "Firebase credentials file not found at: " + serviceAccountPath + 
                    ". Please set FIREBASE_CREDENTIALS_JSON environment variable or ensure the credentials file exists."
                );
            }
            logger.info("Initializing Firebase with service account file: {}", serviceAccountPath);
            try (FileInputStream serviceAccount = new FileInputStream(credentialsPath.toFile())) {
                credentials = GoogleCredentials.fromStream(serviceAccount);
            }
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Initialize Firebase app if not already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            logger.info("Firebase application has been initialized successfully");
        } else {
            logger.info("Firebase application was already initialized");
        }

        return FirestoreClient.getFirestore();
    }
} 