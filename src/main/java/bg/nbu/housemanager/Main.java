package bg.nbu.housemanager;

import bg.nbu.housemanager.config.AppConfig;
import bg.nbu.housemanager.db.Db;
import bg.nbu.housemanager.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        AppConfig config = AppConfig.load();
        try (Db db = Db.connect(config)) {
            ConsoleApp app = new ConsoleApp(db, config);
            app.run();
        } catch (Exception ex) {
            System.err.println("Fatal error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
