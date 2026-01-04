package bg.nbu.housemanager.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public final class ConsoleIO {
    private final Scanner sc = new Scanner(System.in);

    public String prompt(String label) {
        System.out.print(label + ": ");
        return sc.nextLine();
    }

    public long promptLong(String label) {
        while (true) {
            try {
                return Long.parseLong(prompt(label).trim());
            } catch (Exception e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public int promptInt(String label) {
        while (true) {
            try {
                return Integer.parseInt(prompt(label).trim());
            } catch (Exception e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    public BigDecimal promptBigDecimal(String label) {
        while (true) {
            try {
                return new BigDecimal(prompt(label).trim());
            } catch (Exception e) {
                System.out.println("Invalid decimal. Try again.");
            }
        }
    }

    public boolean promptYesNo(String label) {
        while (true) {
            String v = prompt(label + " (y/n)").trim().toLowerCase();
            if (v.equals("y") || v.equals("yes")) return true;
            if (v.equals("n") || v.equals("no")) return false;
            System.out.println("Please type y or n.");
        }
    }

    public LocalDate promptDate(String label) {
        while (true) {
            try {
                String v = prompt(label + " (YYYY-MM-DD)").trim();
                return LocalDate.parse(v);
            } catch (Exception e) {
                System.out.println("Invalid date.");
            }
        }
    }

    public LocalDateTime promptDateTimeOrNow(String label) {
        String v = prompt(label + " (YYYY-MM-DD HH:MM:SS) or empty for now").trim();
        if (v.isEmpty()) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(v.replace(" ", "T"));
        } catch (Exception e) {
            System.out.println("Invalid datetime, using now.");
            return LocalDateTime.now();
        }
    }

    public void line() { System.out.println("--------------------------------------------------"); }
}
