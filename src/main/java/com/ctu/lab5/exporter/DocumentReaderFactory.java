package com.ctu.lab5.exporter;

// Factory
class DocumentReaderFactory {
    public static DocumentReader getReader(String fileType) {
        if (fileType == null) throw new IllegalArgumentException("File type null");
        return switch (fileType.toUpperCase()) {
            case "PDF" -> new PDFReader();
            case "DOCX" -> new DocxReader();
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        };
    }
}
