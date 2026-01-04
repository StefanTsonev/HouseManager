package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.FeeDao;
import bg.nbu.housemanager.model.BuildingFee;
import bg.nbu.housemanager.util.Validation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public final class FeeService {
    private final FeeDao dao;
    public FeeService(Connection con) { this.dao = new FeeDao(con); }

    public void setFee(long buildingId, BigDecimal pricePerM2, BigDecimal perResidentOver7Elevator, BigDecimal perPetCommon) throws SQLException {
        Validation.positiveMoney(pricePerM2, "Price per m2");
        Validation.nonNegativeMoney(perResidentOver7Elevator, "Add per resident");
        Validation.nonNegativeMoney(perPetCommon, "Add per pet");
        dao.upsert(buildingId, pricePerM2, perResidentOver7Elevator, perPetCommon);
    }

    public BuildingFee get(long buildingId) throws SQLException { return dao.get(buildingId); }
}
