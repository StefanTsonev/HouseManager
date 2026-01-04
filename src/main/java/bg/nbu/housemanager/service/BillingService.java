package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.ApartmentDao;
import bg.nbu.housemanager.dao.FeeDao;
import bg.nbu.housemanager.dao.PetDao;
import bg.nbu.housemanager.dao.ResidentDao;
import bg.nbu.housemanager.model.Apartment;
import bg.nbu.housemanager.model.BuildingFee;
import bg.nbu.housemanager.model.Pet;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public final class BillingService {
    private final ApartmentDao aptDao;
    private final ResidentDao resDao;
    private final PetDao petDao;
    private final FeeDao feeDao;

    public BillingService(Connection con) {
        this.aptDao = new ApartmentDao(con);
        this.resDao = new ResidentDao(con);
        this.petDao = new PetDao(con);
        this.feeDao = new FeeDao(con);
    }

    public BigDecimal calculateMonthlyFee(long apartmentId) throws SQLException {
        Apartment apt = aptDao.get(apartmentId);
        if (apt == null) throw new IllegalArgumentException("Apartment not found.");
        BuildingFee fee = feeDao.get(apt.buildingId());
        if (fee == null) throw new IllegalStateException("No fee settings for this building. Set building fees first.");

        BigDecimal base = apt.areaM2().multiply(fee.pricePerM2());

        int eligibleResidents = 0;
        List<Object[]> residents = resDao.listByApartment(apartmentId);
        for (Object[] r : residents) {
            LocalDate birth = (LocalDate) r[3];
            boolean usesElevator = (boolean) r[4];
            int age = Period.between(birth, LocalDate.now()).getYears();
            if (age > 7 && usesElevator) eligibleResidents++;
        }

        int eligiblePets = 0;
        List<Pet> pets = petDao.listByApartment(apartmentId);
        for (Pet p : pets) {
            if (p.usesCommonParts()) eligiblePets++;
        }

        BigDecimal addRes = fee.addPerResidentOver7Elevator().multiply(BigDecimal.valueOf(eligibleResidents));
        BigDecimal addPet = fee.addPerPetCommonParts().multiply(BigDecimal.valueOf(eligiblePets));

        return base.add(addRes).add(addPet);
    }
}
