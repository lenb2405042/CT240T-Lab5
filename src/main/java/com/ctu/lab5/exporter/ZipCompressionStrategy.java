package com.ctu.lab5.exporter;

// Concrete Strategy
class ZipCompressionStrategy implements CompressionStrategy {
    @Override
    public byte[] compress(String rawData) {
        System.out.println("🗜️ Nén dữ liệu theo chuẩn ZIP...");
        return ("ZIP[" + rawData + "]").getBytes();
    }
}
