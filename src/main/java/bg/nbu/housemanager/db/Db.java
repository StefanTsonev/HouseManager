package bg.nbu.housemanager.db;

import bg.nbu.housemanager.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db implements AutoCloseable {
    private final Connection con;

    private Db(Connection con) {
        this.con = con;
    }

    public static Db connect(AppConfig cfg) {
        try {
            Connection c = DriverManager.getConnection(cfg.dbUrl, cfg.dbUser, cfg.dbPassword);
            c.setAutoCommit(true);
            return new Db(c);
        } catch (SQLException e) {
            throw new RuntimeException("DB connection failed. Check application.properties. " + e.getMessage(), e);
        }
    }

    public Connection con() { return con; }

    @Override
    public void close() throws Exception {
        if (con != null) con.close();
    }
}
