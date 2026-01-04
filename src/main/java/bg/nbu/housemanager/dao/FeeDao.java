package bg.nbu.housemanager.dao;

import bg.nbu.housemanager.model.BuildingFee;

import java.math.BigDecimal;
import java.sql.*;

public final class FeeDao {
    private final Connection con;
    public FeeDao(Connection con) { this.con = con; }

    public void upsert(long buildingId, BigDecimal pricePerM2, BigDecimal perRes, BigDecimal perPet) throws SQLException {
        String sql =
            "INSERT INTO building_fees(building_id, price_per_m2, add_per_resident_over7_elevator, add_per_pet_common_parts) " +
            "VALUES(?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE " +
            "price_per_m2=VALUES(price_per_m2), " +
            "add_per_resident_over7_elevator=VALUES(add_per_resident_over7_elevator), " +
            "add_per_pet_common_parts=VALUES(add_per_pet_common_parts)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, buildingId);
            ps.setBigDecimal(2, pricePerM2);
            ps.setBigDecimal(3, perRes);
            ps.setBigDecimal(4, perPet);
            ps.executeUpdate();
        }
    }

    public BuildingFee get(long buildingId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM building_fees WHERE building_id=?")) {
            ps.setLong(1, buildingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new BuildingFee(
                        rs.getLong("building_id"),
                        rs.getBigDecimal("price_per_m2"),
                        rs.getBigDecimal("add_per_resident_over7_elevator"),
                        rs.getBigDecimal("add_per_pet_common_parts")
                );
            }
        }
    }
}
