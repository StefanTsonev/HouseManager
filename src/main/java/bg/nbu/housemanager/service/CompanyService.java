package bg.nbu.housemanager.service;

import bg.nbu.housemanager.dao.CompanyDao;
import bg.nbu.housemanager.model.Company;
import bg.nbu.housemanager.util.Validation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public final class CompanyService {
    private final CompanyDao dao;

    public CompanyService(Connection con) { this.dao = new CompanyDao(con); }

    public long create(String name, String vat, String phone, String email) throws SQLException {
        name = Validation.nonBlank(name, "Company name");
        return dao.create(name, vat, phone, email);
    }

    public void update(long id, String name, String vat, String phone, String email) throws SQLException {
        name = Validation.nonBlank(name, "Company name");
        dao.update(id, name, vat, phone, email);
    }

    public void delete(long id) throws SQLException { dao.delete(id); }

    public Company get(long id) throws SQLException { return dao.get(id); }

    public List<Company> list() throws SQLException { return dao.list(); }

    public List<Object[]> listByRevenue() throws SQLException { return dao.listCompaniesByRevenue(); }
}
