package bg.nbu.housemanager.util;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Validation {
    private Validation() {}

    public static String nonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
        return value.trim();
    }

    public static int positiveInt(int v, String fieldName) {
        if (v <= 0) throw new IllegalArgumentException(fieldName + " must be > 0.");
        return v;
    }

    public static BigDecimal positiveMoney(BigDecimal v, String fieldName) {
        if (v == null || v.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be > 0.");
        }
        return v;
    }

    public static BigDecimal nonNegativeMoney(BigDecimal v, String fieldName) {
        if (v == null || v.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " must be >= 0.");
        }
        return v;
    }

    public static LocalDate birthDate(LocalDate d) {
        if (d == null) throw new IllegalArgumentException("birthDate cannot be null.");
        if (d.isAfter(LocalDate.now())) throw new IllegalArgumentException("birthDate cannot be in the future.");
        return d;
    }
}
