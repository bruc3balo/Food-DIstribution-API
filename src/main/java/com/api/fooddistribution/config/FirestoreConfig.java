package com.api.fooddistribution.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirestoreConfig {

    private final Logger logger = LoggerFactory.getLogger(FirestoreConfig.class);
    public static Firestore firestore;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseDatabase firebaseDatabase;


    @Bean
    public Firestore getFireStore() {

        InputStream serviceAccountStream = this.getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

        GoogleCredentials credentials;
        FirestoreOptions firestoreOptions = null;
        FirebaseOptions options;
        try {
            if (serviceAccountStream != null) {
                credentials = GoogleCredentials.fromStream(serviceAccountStream);
                options = FirebaseOptions.builder().setCredentials(credentials).build();

                logger.info("CONFIGURING FIRESTORE");

                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
                logger.info("Firebase Initialized");

                firestoreOptions = FirestoreOptions.newBuilder().setCredentials(credentials).build();

                firestore = FirestoreClient.getFirestore();
                logger.info("Firestore Initialized");

                firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp, "https://foodconnect-2df96-default-rtdb.firebaseio.com/");
                logger.info("Realtime database Initialized");


                firebaseAuth = FirebaseAuth.getInstance();
                logger.info("Firebase Auth Initialized");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return firestoreOptions != null ? firestoreOptions.getService() : null;
    }

    /*@Bean
    public UserAuthenticationServiceImpl userAuthenticationServiceImpl() {
        return new UserAuthenticationServiceImpl();
    }
*/
}
