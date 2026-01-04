package bg.nbu.housemanager.model;

import java.math.BigDecimal;

public record BuildingFee(long buildingId, BigDecimal pricePerM2,
                          BigDecimal addPerResidentOver7Elevator,
                          BigDecimal addPerPetCommonParts) {}
