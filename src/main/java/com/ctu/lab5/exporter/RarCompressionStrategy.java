package com.ctu.lab5.exporter;

// Concrete Strategy
class RarCompressionStrategy implements CompressionStrategy {
    @Override
    public byte[] compress(String rawData) {
        System.out.println("🗜️ Nén dữ liệu theo chuẩn RAR...");
        return ("RAR[" + rawData + "]").getBytes();
    }
}
