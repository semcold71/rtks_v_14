package ru.samcold.rtks;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@SpringBootApplication
public class RtksApplication {

    public static void main(String[] args) {

        // set icon into dock
        Taskbar taskbar = Taskbar.getTaskbar();
        BufferedImage image = null;
        try {
            image = ImageIO.read(RtksApplication.class.getResource("/images/big-logo.png"));
            taskbar.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // launch
        Application.launch(FormApplication.class, args);
    }
}
