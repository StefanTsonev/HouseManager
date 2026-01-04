package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.Payment;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PaymentDao {
    private final Connection con;
    public PaymentDao(Connection con) { this.con = con; }

    public long create(long companyId, long employeeId, long buildingId, long apartmentId, BigDecimal amount, LocalDateTime paidAt) throws SQLException {
        String sql = "INSERT INTO payments(company_id, employee_id, building_id, apartment_id, amount, paid_at) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, companyId);
            ps.setLong(2, employeeId);
            ps.setLong(3, buildingId);
            ps.setLong(4, apartmentId);
            ps.setBigDecimal(5, amount);
            ps.setTimestamp(6, Timestamp.valueOf(paidAt));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); return rs.getLong(1); }
        }
    }

    public List<Payment> listByCompany(long companyId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE company_id=? ORDER BY paid_at DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Payment> out = new ArrayList<>();
                while (rs.next()) out.add(map(rs));
                return out;
            }
        }
    }

    public BigDecimal sumPaidByCompany(long companyId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount),0) AS s FROM payments WHERE company_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getBigDecimal("s"); }
        }
    }

    public List<Object[]> paidSumsByEmployee(long companyId) throws SQLException {
        String sql =
                "SELECT e.id, e.first_name, e.last_name, COALESCE(SUM(p.amount),0) AS paid " +
                "FROM employees e " +
                "LEFT JOIN payments p ON p.employee_id = e.id " +
                "WHERE e.company_id=? " +
                "GROUP BY e.id, e.first_name, e.last_name " +
                "ORDER BY paid DESC, e.last_name, e.first_name";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                while (rs.next()) out.add(new Object[]{rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getBigDecimal("paid")});
                return out;
            }
        }
    }

    public List<Object[]> paidSumsByBuilding(long companyId) throws SQLException {
        String sql =
                "SELECT b.id, b.address, COALESCE(SUM(p.amount),0) AS paid " +
                "FROM buildings b " +
                "LEFT JOIN payments p ON p.building_id = b.id " +
                "WHERE b.company_id=? " +
                "GROUP BY b.id, b.address " +
                "ORDER BY paid DESC, b.address";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                while (rs.next()) out.add(new Object[]{rs.getLong("id"), rs.getString("address"), rs.getBigDecimal("paid")});
                return out;
            }
        }
    }

    private Payment map(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getLong("id"),
                rs.getLong("company_id"),
                rs.getLong("employee_id"),
                rs.getLong("building_id"),
                rs.getLong("apartment_id"),
                rs.getBigDecimal("amount"),
                rs.getTimestamp("paid_at").toLocalDateTime()
        );
    }
}
