package bg.nbu.housemanager.model;

public record Employee(long id, long companyId, String firstName, String lastName, String phone, String email, boolean isActive) {
    public String fullName() { return firstName + " " + lastName; }
}
