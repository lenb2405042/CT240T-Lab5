package com.ctu.lab5.exporter;

import java.util.ArrayList;
import java.util.List;

// 4. Main Service combining Factory, Strategy, Observer
public class DocumentExporterService {
    private CompressionStrategy compressionStrategy;
    private final List<ExportObserver> observers = new ArrayList<>();

    public void setCompressionStrategy(CompressionStrategy strategy) {
        this.compressionStrategy = strategy;
    }

    public void attachObserver(ExportObserver observer) {
        observers.add(observer);
    }

    public byte[] exportDocument(String filePath, String fileType) {

        if (compressionStrategy == null) {
            throw new IllegalStateException("Chưa thiết lập thuật toán nén!");
        }

        // 1. Dùng Factory Method đọc nội dung
        DocumentReader reader = DocumentReaderFactory.getReader(fileType);
        String rawContent = reader.readContent(filePath);

        // 2. Dùng Strategy Pattern nén dữ liệu
        byte[] compressedData = compressionStrategy.compress(rawContent);

        // 3. Dùng Observer Pattern phát sự kiện
        for (ExportObserver obs : observers) {
            obs.onExportSuccess(filePath, compressedData.length);
        }

        return compressedData;
    }

    // Runner kiểm thử hệ thống
    public static void main(String[] args) {
        DocumentExporterService exporter = new DocumentExporterService();

        // Đăng ký Observers
        exporter.attachObserver(new AuditLogObserver());
        exporter.attachObserver(new ConsoleNotificationObserver());

        // Thiết lập Strategy nén ZIP
        exporter.setCompressionStrategy(new ZipCompressionStrategy());

        System.out.println("🚀 Đang tiến hành xuất file PDF...");
        exporter.exportDocument("BaoCaoTaiChinh2026.pdf", "PDF");
    }
}
