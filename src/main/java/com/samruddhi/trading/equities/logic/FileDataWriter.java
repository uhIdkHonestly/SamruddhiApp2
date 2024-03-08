package com.samruddhi.trading.equities.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Properties;

public class FileDataWriter {

    public static  void writeToFile(String data) throws IOException {
        try {
            Files.write(Paths.get(System.getProperty("user.dir")  + "/temp/" +LocalTime.now().getMinute()+ "jsonData.json"), data.getBytes());
        } catch (java.io.IOException e) {
            throw e;
        }
    }


    public static void updateProperty(String key, String value) throws IOException {
        // Load the properties file
        Properties properties = new Properties();
        try (InputStream inputStream = FileDataWriter.class.getClassLoader().getResourceAsStream("../target/classes/application.properties")) {
            properties.load(inputStream);
            // Replace the key "oldKey" with the value "newValue"
            properties.setProperty(key, value);

            // Save the properties file
            properties.store(new FileOutputStream(new File(System.getProperty("user.dir") + "/target/classes/application.properties")), "Updated properties");
        }
    }
}
