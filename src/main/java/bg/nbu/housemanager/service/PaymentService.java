package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.PaymentDao;
import bg.nbu.housemanager.util.CsvPaymentsExporter;
import bg.nbu.housemanager.util.Validation;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public final class PaymentService {
    private final Connection con;
    private final PaymentDao dao;
    private final CsvPaymentsExporter exporter;

    public PaymentService(Connection con, String exportFile) {
        this.con = con;
        this.dao = new PaymentDao(con);
        this.exporter = new CsvPaymentsExporter(exportFile);
    }

    public long recordPayment(long companyId, long employeeId, long buildingId, long apartmentId, BigDecimal amount, LocalDateTime paidAt) throws SQLException {
        Validation.positiveMoney(amount, "Amount");
        if (paidAt == null) paidAt = LocalDateTime.now();

        long id = dao.create(companyId, employeeId, buildingId, apartmentId, amount, paidAt);

        String company = lookupCompanyName(companyId);
        String employee = lookupEmployeeName(employeeId);
        String building = lookupBuildingAddress(buildingId);
        String apartment = lookupApartmentLabel(apartmentId);

        exporter.append(company, employee, building, apartment, amount.toPlainString(), paidAt);
        return id;
    }

    public java.util.List<bg.nbu.housemanager.model.Payment> listByCompany(long companyId) throws SQLException { return dao.listByCompany(companyId); }
    public BigDecimal sumPaidByCompany(long companyId) throws SQLException { return dao.sumPaidByCompany(companyId); }
    public java.util.List<Object[]> paidSumsByEmployee(long companyId) throws SQLException { return dao.paidSumsByEmployee(companyId); }
    public java.util.List<Object[]> paidSumsByBuilding(long companyId) throws SQLException { return dao.paidSumsByBuilding(companyId); }

    private String lookupCompanyName(long companyId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT name FROM companies WHERE id=?")) {
            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getString(1) : String.valueOf(companyId); }
        }
    }

    private String lookupEmployeeName(long employeeId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT first_name, last_name FROM employees WHERE id=?")) {
            ps.setLong(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return String.valueOf(employeeId);
                return rs.getString(1) + " " + rs.getString(2);
            }
        }
    }

    private String lookupBuildingAddress(long buildingId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT address FROM buildings WHERE id=?")) {
            ps.setLong(1, buildingId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getString(1) : String.valueOf(buildingId); }
        }
    }

    private String lookupApartmentLabel(long apartmentId) throws SQLException {
        String sql = "SELECT building_id, apt_number FROM apartments WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, apartmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return String.valueOf(apartmentId);
                return "B" + rs.getLong(1) + " Apt " + rs.getString(2);
            }
        }
    }
}
