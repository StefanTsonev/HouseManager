package bg.nbu.housemanager.model;

import java.time.LocalDate;

public record Person(long id, String firstName, String lastName, LocalDate birthDate) {
    public String fullName() { return firstName + " " + lastName; }
}
