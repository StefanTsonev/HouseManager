package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class EmployeeDao {
    private final Connection con;

    public EmployeeDao(Connection con) { this.con = con; }

    public long create(long companyId, String first, String last, String phone, String email) throws SQLException {
        String sql = "INSERT INTO employees(company_id, first_name, last_name, phone, email, is_active) VALUES(?,?,?,?,?,true)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, companyId);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public void update(long id, String first, String last, String phone, String email, boolean active) throws SQLException {
        String sql = "UPDATE employees SET first_name=?, last_name=?, phone=?, email=?, is_active=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setBoolean(5, active);
            ps.setLong(6, id);
            ps.executeUpdate();
        }
    }

    public Employee get(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return map(rs);
            }
        }
    }

    public List<Employee> listByCompany(long companyId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE company_id=? ORDER BY last_name, first_name";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Employee> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public List<Employee> listActiveByCompany(long companyId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE company_id=? AND is_active=true ORDER BY last_name, first_name";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Employee> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public void deactivate(long employeeId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE employees SET is_active=false WHERE id=?")) {
            ps.setLong(1, employeeId);
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM employees WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public long pickLeastLoadedEmployee(long companyId) throws SQLException {
        String sql =
                "SELECT e.id, COUNT(b.id) AS cnt " +
                "FROM employees e " +
                "LEFT JOIN buildings b ON b.employee_id = e.id " +
                "WHERE e.company_id=? AND e.is_active=true " +
                "GROUP BY e.id " +
                "ORDER BY cnt ASC, e.last_name, e.first_name " +
                "LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return -1;
                return rs.getLong("id");
            }
        }
    }

    public List<Object[]> listEmployeesSorted(long companyId, String sortMode) throws SQLException {
        String sql;
        if ("buildings".equalsIgnoreCase(sortMode)) {
            sql =
                "SELECT e.id, e.first_name, e.last_name, e.is_active, COUNT(b.id) AS buildings " +
                "FROM employees e " +
                "LEFT JOIN buildings b ON b.employee_id = e.id " +
                "WHERE e.company_id=? " +
                "GROUP BY e.id, e.first_name, e.last_name, e.is_active " +
                "ORDER BY buildings DESC, e.last_name, e.first_name";
        } else {
            sql =
                "SELECT e.id, e.first_name, e.last_name, e.is_active, COUNT(b.id) AS buildings " +
                "FROM employees e " +
                "LEFT JOIN buildings b ON b.employee_id = e.id " +
                "WHERE e.company_id=? " +
                "GROUP BY e.id, e.first_name, e.last_name, e.is_active " +
                "ORDER BY e.last_name, e.first_name";
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                while (rs.next()) {
                    out.add(new Object[]{
                            rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getBoolean("is_active"),
                            rs.getLong("buildings")
                    });
                }
                return out;
            }
        }
    }

    private Employee map(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getLong("id"),
                rs.getLong("company_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getBoolean("is_active")
        );
    }
}
