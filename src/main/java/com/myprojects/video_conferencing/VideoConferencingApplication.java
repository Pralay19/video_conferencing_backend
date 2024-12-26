package com.myprojects.video_conferencing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class VideoConferencingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoConferencingApplication.class, args);
    }

}