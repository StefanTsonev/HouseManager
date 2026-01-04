package bg.nbu.housemanager.config;

import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    public final String dbUrl;
    public final String dbUser;
    public final String dbPassword;
    public final String paymentsExportFile;

    private AppConfig(String dbUrl, String dbUser, String dbPassword, String paymentsExportFile) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.paymentsExportFile = paymentsExportFile;
    }

    public static AppConfig load() {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) throw new IllegalStateException("Missing application.properties in resources.");
            Properties p = new Properties();
            p.load(in);
            return new AppConfig(
                    required(p, "db.url"),
                    required(p, "db.user"),
                    required(p, "db.password"),
                    p.getProperty("payments.export.file", "payments.csv")
            );
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config: " + e.getMessage(), e);
        }
    }

    private static String required(Properties p, String key) {
        String v = p.getProperty(key);
        if (v == null || v.trim().isEmpty()) throw new IllegalStateException("Missing property: " + key);
        return v.trim();
    }
}
