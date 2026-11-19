package com.ctu.lab5.exporter;

// Concrete Observer
class ConsoleNotificationObserver implements ExportObserver {
    @Override
    public void onExportSuccess(String fileName, int compressedSize) {
        System.out.println("🔔 [Thông báo] Xuất báo cáo thành công file " + fileName);
    }
}
