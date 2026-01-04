package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.BuildingDao;
import bg.nbu.housemanager.dao.EmployeeDao;
import bg.nbu.housemanager.model.Building;
import bg.nbu.housemanager.model.Employee;
import bg.nbu.housemanager.util.Validation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public final class EmployeeService {
    private final EmployeeDao empDao;
    private final BuildingDao bldDao;

    public EmployeeService(Connection con) {
        this.empDao = new EmployeeDao(con);
        this.bldDao = new BuildingDao(con);
    }

    public long create(long companyId, String first, String last, String phone, String email) throws SQLException {
        Validation.nonBlank(first, "First name");
        Validation.nonBlank(last, "Last name");
        return empDao.create(companyId, first.trim(), last.trim(), phone, email);
    }

    public void update(long id, String first, String last, String phone, String email, boolean active) throws SQLException {
        Validation.nonBlank(first, "First name");
        Validation.nonBlank(last, "Last name");
        empDao.update(id, first.trim(), last.trim(), phone, email, active);
    }

    public List<Employee> listByCompany(long companyId) throws SQLException { return empDao.listByCompany(companyId); }

    public List<Object[]> listSorted(long companyId, String sortMode) throws SQLException { return empDao.listEmployeesSorted(companyId, sortMode); }

    public void deactivateAndRedistribute(long employeeId) throws SQLException {
        Employee emp = empDao.get(employeeId);
        if (emp == null) throw new IllegalArgumentException("Employee not found.");
        long companyId = emp.companyId();

        List<Employee> active = empDao.listActiveByCompany(companyId);
        active.removeIf(e -> e.id() == employeeId);

        if (active.isEmpty()) {
            throw new IllegalStateException("Cannot deactivate: company must have at least one other active employee to take buildings.");
        }

        List<Building> buildings = bldDao.listByEmployee(employeeId);

        empDao.deactivate(employeeId);

        for (Building b : buildings) {
            long pick = pickLeastLoaded(active, companyId);
            bldDao.reassignBuilding(b.id(), pick);
        }
    }

    private long pickLeastLoaded(List<Employee> active, long companyId) throws SQLException {
        long id = empDao.pickLeastLoadedEmployee(companyId);
        if (id < 0) throw new IllegalStateException("No active employee found.");
        return id;
    }
}
