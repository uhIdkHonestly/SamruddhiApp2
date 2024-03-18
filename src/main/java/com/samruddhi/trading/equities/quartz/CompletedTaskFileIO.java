package com.samruddhi.trading.equities.quartz;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CompletedTaskFileIO {


    private BufferedWriter writer;

    /**
     * Opens the file writer.
     * @throws IOException if an I/O error occurs
     */
    public void openWriter() throws IOException {
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        File file = Paths.get(System.getProperty("user.dir")  + "/temp/" +  "completedTrades-" + todayStr + ".txt").toFile();
        // true indicates append mode. Set to false if you want to overwrite the file.
        writer = new BufferedWriter(new FileWriter(file, true));
    }

    /**
     * Writes a single row to the file.
     *
     * @param row the row to write
     * @throws IOException if an I/O error occurs
     */
    public void writeRow(String row) throws IOException {
        writer.write(row);
        writer.newLine(); // Adds a new line after the row
    }

    /**
     * Closes the file writer.
     *
     * @throws IOException if an I/O error occurs
     */
    public void closeWriter() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

}