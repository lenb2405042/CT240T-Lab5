package com.ctu.lab5.exporter;

// 1. Factory Pattern - Product Interface
interface DocumentReader {
    String readContent(String filePath);
}
