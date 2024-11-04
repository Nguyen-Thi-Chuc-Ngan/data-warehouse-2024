package com.example.demo.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public List<String> readProductIdsFromCsv(String filePath) {
        List<String> productIds = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                productIds.add(nextLine[0]);
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return productIds;
    }

    public static void main(String[] args) {
        CsvReader csvReader = new CsvReader();
        String currentDirectory = System.getProperty("user.dir");

        // Nối đường dẫn thư mục hiện tại với tên tệp CSV
        String csvFilePath = currentDirectory + "\\data\\products_id.csv";
        List<String> productIds = csvReader.readProductIdsFromCsv(csvFilePath);
        System.out.println("Product IDs: " + productIds);
    }
}
