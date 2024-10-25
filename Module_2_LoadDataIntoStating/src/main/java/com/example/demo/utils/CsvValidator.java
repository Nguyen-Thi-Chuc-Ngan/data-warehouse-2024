package com.example.demo.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvValidator {
    public boolean isCsvFileValid(String csvFilePath) {
        try {
            Path path = Path.of(csvFilePath);
            // Kiểm tra xem file có tồn tại và không rỗng
            if (!Files.exists(path) || Files.size(path) == 0) {
                return false; // File không tồn tại hoặc rỗng
            }

            // Đọc file CSV và kiểm tra định dạng
            try (FileReader reader = new FileReader(csvFilePath);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                List<CSVRecord> records = csvParser.getRecords();
                return !records.isEmpty(); // Nếu có record nào thì file CSV hợp lệ
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Lỗi khi đọc file
        }
    }
}
