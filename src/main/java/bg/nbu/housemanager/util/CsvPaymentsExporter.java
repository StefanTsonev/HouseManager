package bg.nbu.housemanager.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CsvPaymentsExporter {

    private final String filename;
    private final DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CsvPaymentsExporter(String filename) {
        this.filename = filename;
    }

    public void append(
            String company,
            String employee,
            String building,
            String apartment,
            String amount,
            LocalDateTime paidAt
    ) {
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write(
                    csv(company) + "," +
                            csv(employee) + "," +
                            csv(building) + "," +
                            csv(apartment) + "," +
                            csv(amount) + "," +
                            csv(fmt.format(paidAt)) +
                            "\n"
            );
        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot write payments export file: " + e.getMessage(), e
            );
        }
    }

    private String csv(String s) {
        if (s == null) s = "";
        boolean needsQuotes =
                s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");

        String v = s.replace("\"", "\"\"");
        return needsQuotes ? "\"" + v + "\"" : v;
    }
}
