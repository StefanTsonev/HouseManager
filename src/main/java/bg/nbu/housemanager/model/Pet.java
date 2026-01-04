package bg.nbu.housemanager.model;

public record Pet(long id, long apartmentId, String name, String type, boolean usesCommonParts) {}
