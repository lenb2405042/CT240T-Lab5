package com.ctu.lab5.exporter;

// Concrete Product
class DocxReader implements DocumentReader {
    @Override
    public String readContent(String filePath) {
        return "Content from DOCX file: " + filePath;
    }
}
