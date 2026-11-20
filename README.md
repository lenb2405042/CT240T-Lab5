# CT240 - TH05: Document Exporter System (Bài tập 1 - cá nhân)

**Sinh viên:** Trần Văn Lên - B2405042 - Nhóm 04

Hệ thống xuất báo cáo tài liệu, kết hợp 3 Design Pattern (GoF):

| Pattern | Thành phần | Vai trò |
|---|---|---|
| Factory Method | `DocumentReader`, `PDFReader`, `DocxReader`, `CsvReader`, `DocumentReaderFactory` | Tạo bộ đọc đúng theo định dạng tệp |
| Strategy | `CompressionStrategy`, `ZipCompressionStrategy`, `RarCompressionStrategy`, `TarCompressionStrategy` | Chọn thuật toán nén khi xuất file |
| Observer | `ExportObserver`, `AuditLogObserver`, `ConsoleNotificationObserver` | Nhận sự kiện mỗi khi xuất báo cáo thành công |

`DocumentExporterService` là lớp dịch vụ kết hợp cả ba mẫu.

## Phần bổ sung so với skeleton (yêu cầu của đề)

- Thêm `CsvReader` và dòng `case "CSV"` trong `DocumentReaderFactory`.
- Thêm `TarCompressionStrategy`.
- Viết `DocumentExporterTest` (JUnit 5).
- **`DocumentExporterService` không bị sửa dòng nào** (kiểm chứng nguyên tắc OCP): xem bằng `git diff main -- src/main/java/com/ctu/lab5/exporter/DocumentExporterService.java` (không có kết quả).

## Cấu trúc

```
src/main/java/com/ctu/lab5/exporter/   # mã nguồn (mỗi lớp một file)
src/test/java/com/ctu/lab5/exporter/   # DocumentExporterTest
pom.xml                                # JUnit 5, Mockito, JaCoCo
```

## Chạy

- Chạy skeleton gốc: lớp `DocumentExporterService` (có `main`).
- Chạy phần bổ sung (CSV + TAR): lớp `ExporterDemo`.
- Chạy kiểm thử: `mvn clean test` (hoặc cửa sổ Maven của IntelliJ: Lifecycle, `clean` rồi `test`).
- Báo cáo độ bao phủ JaCoCo: `target/site/jacoco/index.html`.

Yêu cầu: JDK 21.
