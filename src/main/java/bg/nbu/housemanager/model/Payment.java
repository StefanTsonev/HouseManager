package bg.nbu.housemanager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Payment(long id, long companyId, long employeeId, long buildingId, long apartmentId,
                      BigDecimal amount, LocalDateTime paidAt) {}
