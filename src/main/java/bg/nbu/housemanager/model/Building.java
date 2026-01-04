package bg.nbu.housemanager.model;

import java.math.BigDecimal;

public record Building(long id, long companyId, long employeeId,
                       String address, int floors, int apartmentsCount,
                       BigDecimal builtAreaM2, BigDecimal commonPartsM2) {}
