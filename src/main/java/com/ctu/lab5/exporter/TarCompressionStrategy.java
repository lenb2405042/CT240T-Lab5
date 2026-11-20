package com.ctu.lab5.exporter;

// Concrete Strategy MỚI (bổ sung Bài 1): nén theo chuẩn TAR
class TarCompressionStrategy implements CompressionStrategy {
    @Override
    public byte[] compress(String rawData) {
        System.out.println("🗜️ Nén dữ liệu theo chuẩn TAR...");
        return ("TAR[" + rawData + "]").getBytes();
    }
}
