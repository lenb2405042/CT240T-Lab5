package com.ctu.lab5.exporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DocumentExporterService - Factory, Strategy, Observer")
class DocumentExporterTest {

    /** Observer giả: ghi lại mọi sự kiện mà nó nhận được. */
    private static class RecordingObserver implements ExportObserver {
        final List<String> fileNames = new ArrayList<>();
        final List<Integer> sizes = new ArrayList<>();

        @Override
        public void onExportSuccess(String fileName, int compressedSize) {
            fileNames.add(fileName);
            sizes.add(compressedSize);
        }
    }

    private DocumentExporterService exporter;
    private RecordingObserver observer;

    @BeforeEach
    void setUp() {
        exporter = new DocumentExporterService();
        observer = new RecordingObserver();
        exporter.attachObserver(observer);
    }

    private static CompressionStrategy strategyOf(String name) {
        return switch (name) {
            case "ZIP" -> new ZipCompressionStrategy();
            case "RAR" -> new RarCompressionStrategy();
            case "TAR" -> new TarCompressionStrategy();
            default -> throw new IllegalArgumentException("Strategy khong hop le: " + name);
        };
    }

    // ===================== Yêu cầu đề: chưa set Strategy =====================

    @Test
    @DisplayName("exportDocument: chua set Strategy nen -> IllegalStateException")
    void exportDocument_withoutStrategy_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class,
                () -> exporter.exportDocument("report.pdf", "PDF"));
    }

    @Test
    @DisplayName("exportDocument: chua set Strategy nen -> khong phat su kien cho Observer")
    void exportDocument_withoutStrategy_doesNotNotifyObservers() {
        assertThrows(IllegalStateException.class,
                () -> exporter.exportDocument("report.pdf", "PDF"));

        assertTrue(observer.fileNames.isEmpty());
    }

    // ===================== Strategy Pattern =====================

    @ParameterizedTest(name = "{0} + {1}")
    @CsvSource({
            "PDF,  ZIP, ZIP[Content from PDF file: report.pdf]",
            "DOCX, RAR, RAR[Content from DOCX file: report.pdf]",
            "CSV,  TAR, TAR[Content from CSV file: report.pdf]",
            "PDF,  TAR, TAR[Content from PDF file: report.pdf]",
            "csv,  zip, ZIP[Content from CSV file: report.pdf]"
    })
    @DisplayName("exportDocument: Factory doc dung dinh dang, Strategy nen dung chuan")
    void exportDocument_combinesReaderAndStrategy(String fileType, String strategyName, String expected) {
        exporter.setCompressionStrategy(strategyOf(strategyName.toUpperCase()));

        byte[] result = exporter.exportDocument("report.pdf", fileType);

        assertEquals(expected, new String(result));
    }

    @Test
    @DisplayName("Strategy: doi thuat toan nen luc dang chay (Zip -> Rar -> Tar)")
    void setCompressionStrategy_canBeSwappedAtRuntime() {
        exporter.setCompressionStrategy(new ZipCompressionStrategy());
        assertEquals("ZIP[Content from PDF file: a.pdf]", new String(exporter.exportDocument("a.pdf", "PDF")));

        exporter.setCompressionStrategy(new RarCompressionStrategy());
        assertEquals("RAR[Content from PDF file: a.pdf]", new String(exporter.exportDocument("a.pdf", "PDF")));

        exporter.setCompressionStrategy(new TarCompressionStrategy());
        assertEquals("TAR[Content from PDF file: a.pdf]", new String(exporter.exportDocument("a.pdf", "PDF")));
    }

    @Test
    @DisplayName("OCP/DIP: Strategy tu viet (lambda) dung duoc ma khong sua DocumentExporterService")
    void customStrategy_canBeInjected_withoutChangingService() {
        exporter.setCompressionStrategy(raw -> ("CUSTOM:" + raw).getBytes());

        byte[] result = exporter.exportDocument("a.csv", "CSV");

        assertEquals("CUSTOM:Content from CSV file: a.csv", new String(result));
    }

    // ===================== Factory Method Pattern =====================

    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource({
            "PDF,  PDFReader",
            "pdf,  PDFReader",
            "DOCX, DocxReader",
            "CSV,  CsvReader",
            "csv,  CsvReader"
    })
    @DisplayName("DocumentReaderFactory: tra ve dung loai Reader (khong phan biet hoa thuong)")
    void factory_returnsCorrectReader(String fileType, String expectedClassName) {
        DocumentReader reader = DocumentReaderFactory.getReader(fileType);

        assertEquals(expectedClassName, reader.getClass().getSimpleName());
    }

    @Test
    @DisplayName("DocumentReaderFactory: dinh dang chua ho tro -> IllegalArgumentException")
    void factory_unsupportedType_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> DocumentReaderFactory.getReader("TXT"));
    }

    @Test
    @DisplayName("DocumentReaderFactory: fileType null -> IllegalArgumentException")
    void factory_nullType_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> DocumentReaderFactory.getReader(null));
    }

    @Test
    @DisplayName("exportDocument: dinh dang chua ho tro -> loi va khong phat su kien")
    void exportDocument_unsupportedType_doesNotNotifyObservers() {
        exporter.setCompressionStrategy(new ZipCompressionStrategy());

        assertThrows(IllegalArgumentException.class, () -> exporter.exportDocument("note.txt", "TXT"));

        assertTrue(observer.fileNames.isEmpty());
    }

    // ===================== Observer Pattern =====================

    @Test
    @DisplayName("Observer: nhan dung ten file va kich thuoc sau khi nen")
    void exportDocument_notifiesObserverWithFileNameAndSize() {
        exporter.setCompressionStrategy(new ZipCompressionStrategy());

        byte[] result = exporter.exportDocument("BaoCao.pdf", "PDF");

        assertEquals(List.of("BaoCao.pdf"), observer.fileNames);
        assertEquals(List.of(result.length), observer.sizes);
    }

    @Test
    @DisplayName("Observer: nhieu Observer deu duoc goi, theo thu tu dang ky")
    void exportDocument_notifiesAllObserversInOrder() {
        List<String> calls = new ArrayList<>();
        DocumentExporterService service = new DocumentExporterService();
        service.attachObserver((file, size) -> calls.add("A:" + file));
        service.attachObserver((file, size) -> calls.add("B:" + file));
        service.setCompressionStrategy(new RarCompressionStrategy());

        service.exportDocument("x.docx", "DOCX");

        assertEquals(List.of("A:x.docx", "B:x.docx"), calls);
    }

    @Test
    @DisplayName("Observer: moi lan xuat chi phat dung 1 su kien")
    void exportDocument_notifiesOncePerExport() {
        exporter.setCompressionStrategy(new TarCompressionStrategy());

        exporter.exportDocument("1.csv", "CSV");
        exporter.exportDocument("2.csv", "CSV");

        assertEquals(List.of("1.csv", "2.csv"), observer.fileNames);
    }

    @Test
    @DisplayName("Observer: khong co Observer nao van xuat file binh thuong")
    void exportDocument_withoutObservers_stillWorks() {
        DocumentExporterService service = new DocumentExporterService();
        service.setCompressionStrategy(new ZipCompressionStrategy());

        byte[] result = service.exportDocument("a.pdf", "PDF");

        assertEquals("ZIP[Content from PDF file: a.pdf]", new String(result));
    }
}
