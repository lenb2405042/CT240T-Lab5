package com.ctu.lab5.exporter;

/**
 * Chương trình chạy thử cho phần bổ sung của Bài 1.
 * Đặt trong lớp riêng để không phải sửa DocumentExporterService (kiểm chứng OCP).
 */
public class ExporterDemo {

    public static void main(String[] args) {
        DocumentExporterService exporter = new DocumentExporterService();

        // Đăng ký Observers
        exporter.attachObserver(new AuditLogObserver());
        exporter.attachObserver(new ConsoleNotificationObserver());

        // 1. Định dạng mới CSV + thuật toán nén mới TAR
        exporter.setCompressionStrategy(new TarCompressionStrategy());
        System.out.println("🚀 Đang xuất file CSV (nén TAR)...");
        exporter.exportDocument("DoanhThuQuy4.csv", "CSV");

        // 2. Đổi Strategy ngay khi đang chạy
        exporter.setCompressionStrategy(new RarCompressionStrategy());
        System.out.println("\n🚀 Đang xuất file DOCX (nén RAR)...");
        exporter.exportDocument("BienBanHop.docx", "DOCX");

        // 3. Định dạng chưa hỗ trợ -> Factory báo lỗi
        System.out.println("\n🚀 Đang xuất file TXT (chưa hỗ trợ)...");
        try {
            exporter.exportDocument("GhiChu.txt", "TXT");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Bắt được lỗi: " + e.getMessage());
        }
    }
}
