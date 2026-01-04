package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.ApartmentDao;
import bg.nbu.housemanager.dao.PetDao;
import bg.nbu.housemanager.dao.ResidentDao;
import bg.nbu.housemanager.model.Apartment;
import bg.nbu.housemanager.model.Pet;
import bg.nbu.housemanager.util.Validation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public final class ApartmentService {
    private final ApartmentDao aptDao;
    private final ResidentDao resDao;
    private final PetDao petDao;

    public ApartmentService(Connection con) {
        this.aptDao = new ApartmentDao(con);
        this.resDao = new ResidentDao(con);
        this.petDao = new PetDao(con);
    }

    public long create(long buildingId, int floor, String aptNumber, BigDecimal areaM2, Long ownerPersonId) throws SQLException {
        Validation.positiveInt(floor, "Floor");
        Validation.nonBlank(aptNumber, "Apartment number");
        Validation.positiveMoney(areaM2, "Apartment area (m2)");
        return aptDao.create(buildingId, floor, aptNumber.trim(), areaM2, ownerPersonId);
    }

    public void update(long id, int floor, String aptNumber, BigDecimal areaM2, Long ownerPersonId) throws SQLException {
        Validation.positiveInt(floor, "Floor");
        Validation.nonBlank(aptNumber, "Apartment number");
        Validation.positiveMoney(areaM2, "Apartment area (m2)");
        aptDao.update(id, floor, aptNumber.trim(), areaM2, ownerPersonId);
    }

    public void delete(long id) throws SQLException { aptDao.delete(id); }

    public Apartment get(long id) throws SQLException { return aptDao.get(id); }

    public List<Apartment> listByBuilding(long buildingId) throws SQLException { return aptDao.listByBuilding(buildingId); }

    // Residents
    public void addResident(long apartmentId, long personId, boolean usesElevator) throws SQLException { resDao.addResident(apartmentId, personId, usesElevator); }
    public void updateResident(long apartmentId, long personId, boolean usesElevator) throws SQLException { resDao.updateResident(apartmentId, personId, usesElevator); }
    public void removeResident(long apartmentId, long personId) throws SQLException { resDao.removeResident(apartmentId, personId); }
    public List<Object[]> listResidents(long apartmentId) throws SQLException { return resDao.listByApartment(apartmentId); }

    // Pets
    public long addPet(long apartmentId, String name, String type, boolean usesCommonParts) throws SQLException {
        Validation.nonBlank(name, "Pet name");
        Validation.nonBlank(type, "Pet type");
        return petDao.create(apartmentId, name.trim(), type.trim(), usesCommonParts);
    }
    public List<Pet> listPets(long apartmentId) throws SQLException { return petDao.listByApartment(apartmentId); }
}
