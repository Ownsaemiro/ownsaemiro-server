package org.dongguk.ownsaemiro.ownsaemiroserver.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FCMConfig {
    @PostConstruct
    public void init() {
        try {
            InputStream aboutFirebaseFile = new ClassPathResource("ownsaemiro-fcm.json").getInputStream();
            FirebaseOptions options = FirebaseOptions
                    .builder()
                    .setCredentials(GoogleCredentials.fromStream(aboutFirebaseFile))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp initialized {}", FirebaseApp.getInstance().getName());
            }
        } catch (IOException e) {
            log.error("FirebaseApp initialize failed : {}", e.getMessage());
            throw new CommonException(ErrorCode.FIREBASE_JSON_EMPTY);
        }
    }
}
