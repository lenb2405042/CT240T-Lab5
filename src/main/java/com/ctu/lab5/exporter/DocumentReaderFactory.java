package com.ctu.lab5.exporter;

// Factory: chỉ thêm 1 dòng case "CSV" (đúng yêu cầu đề). DocumentExporterService KHÔNG bị sửa.
class DocumentReaderFactory {
    public static DocumentReader getReader(String fileType) {
        if (fileType == null) throw new IllegalArgumentException("File type null");
        return switch (fileType.toUpperCase()) {
            case "PDF" -> new PDFReader();
            case "DOCX" -> new DocxReader();
            case "CSV" -> new CsvReader();
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        };
    }
}
