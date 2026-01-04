package bg.nbu.housemanager.model;

import java.math.BigDecimal;

public record Apartment(long id, long buildingId, int floor, String aptNumber, BigDecimal areaM2, Long ownerPersonId) {}
