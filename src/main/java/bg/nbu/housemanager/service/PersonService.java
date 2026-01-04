package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.PersonDao;
import bg.nbu.housemanager.model.Person;
import bg.nbu.housemanager.util.Validation;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public final class PersonService {
    private final PersonDao dao;
    public PersonService(Connection con) { this.dao = new PersonDao(con); }

    public long create(String first, String last, LocalDate birthDate) throws SQLException {
        Validation.nonBlank(first, "First name");
        Validation.nonBlank(last, "Last name");
        Validation.birthDate(birthDate);
        return dao.create(first.trim(), last.trim(), birthDate);
    }

    public void update(long id, String first, String last, LocalDate birthDate) throws SQLException {
        Validation.nonBlank(first, "First name");
        Validation.nonBlank(last, "Last name");
        Validation.birthDate(birthDate);
        dao.update(id, first.trim(), last.trim(), birthDate);
    }

    public void delete(long id) throws SQLException { dao.delete(id); }
    public Person get(long id) throws SQLException { return dao.get(id); }
    public List<Person> listSorted(String sortMode) throws SQLException { return dao.listSorted(sortMode); }
    public List<Object[]> listResidentsByBuilding(long buildingId, String sortMode) throws SQLException { return dao.listResidentsByBuilding(buildingId, sortMode); }
}
