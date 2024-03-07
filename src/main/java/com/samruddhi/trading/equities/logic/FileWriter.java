package com.samruddhi.trading.equities.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

public class FileWriter {

    public static  void writeToFile(String data) throws IOException {
        try {
            Files.write(Paths.get(System.getProperty("user.dir")  + "/temp/" +LocalTime.now().getMinute()+ "jsonData.json"), data.getBytes());
        } catch (java.io.IOException e) {
            throw e;
        }
    }
}
