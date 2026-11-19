package com.ctu.lab5.exporter;

// 2. Strategy Pattern - Strategy Interface
interface CompressionStrategy {
    byte[] compress(String rawData);
}
