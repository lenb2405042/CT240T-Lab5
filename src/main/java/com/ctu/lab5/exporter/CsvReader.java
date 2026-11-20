package com.ctu.lab5.exporter;

// Concrete Product MỚI (bổ sung Bài 1): bộ đọc tệp CSV
class CsvReader implements DocumentReader {
    @Override
    public String readContent(String filePath) {
        return "Content from CSV file: " + filePath;
    }
}
