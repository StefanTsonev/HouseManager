package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.BuildingDao;
import bg.nbu.housemanager.dao.EmployeeDao;
import bg.nbu.housemanager.model.Building;
import bg.nbu.housemanager.util.Validation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public final class BuildingService {
    private final BuildingDao bldDao;
    private final EmployeeDao empDao;

    public BuildingService(Connection con) {
        this.bldDao = new BuildingDao(con);
        this.empDao = new EmployeeDao(con);
    }

    public long createAutoAssign(long companyId, String address, int floors, int apartments, BigDecimal built, BigDecimal common) throws SQLException {
        Validation.nonBlank(address, "Address");
        Validation.positiveInt(floors, "Floors");
        Validation.positiveInt(apartments, "Apartments count");
        Validation.positiveMoney(built, "Built area (m2)");
        Validation.nonNegativeMoney(common, "Common parts (m2)");

        long employeeId = empDao.pickLeastLoadedEmployee(companyId);
        if (employeeId < 0) throw new IllegalStateException("Company has no active employees. Create an employee first.");
        return bldDao.create(companyId, employeeId, address.trim(), floors, apartments, built, common);
    }

    public void update(long buildingId, long employeeId, String address, int floors, int apartments, BigDecimal built, BigDecimal common) throws SQLException {
        Validation.nonBlank(address, "Address");
        Validation.positiveInt(floors, "Floors");
        Validation.positiveInt(apartments, "Apartments count");
        Validation.positiveMoney(built, "Built area (m2)");
        Validation.nonNegativeMoney(common, "Common parts (m2)");
        bldDao.update(buildingId, employeeId, address.trim(), floors, apartments, built, common);
    }

    public void delete(long id) throws SQLException { bldDao.delete(id); }

    public Building get(long id) throws SQLException { return bldDao.get(id); }

    public List<Building> listByCompany(long companyId) throws SQLException { return bldDao.listByCompany(companyId); }

    public List<Building> listByEmployee(long employeeId) throws SQLException { return bldDao.listByEmployee(employeeId); }
}
