package com.ctu.lab5.exporter;

// 3. Observer Pattern - Observer Interface
interface ExportObserver {
    void onExportSuccess(String fileName, int compressedSize);
}
