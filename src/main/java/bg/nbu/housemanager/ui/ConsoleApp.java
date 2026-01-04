package bg.nbu.housemanager.ui;

import bg.nbu.housemanager.config.AppConfig;
import bg.nbu.housemanager.db.Db;
import bg.nbu.housemanager.model.*;
import bg.nbu.housemanager.service.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class ConsoleApp {
    private final Connection con;
    private final ConsoleIO io = new ConsoleIO();

    private final CompanyService companies;
    private final EmployeeService employees;
    private final BuildingService buildings;
    private final PersonService persons;
    private final ApartmentService apartments;
    private final FeeService fees;
    private final BillingService billing;
    private final PaymentService payments;

    public ConsoleApp(Db db, AppConfig cfg) {
        this.con = db.con();
        this.companies = new CompanyService(con);
        this.employees = new EmployeeService(con);
        this.buildings = new BuildingService(con);
        this.persons = new PersonService(con);
        this.apartments = new ApartmentService(con);
        this.fees = new FeeService(con);
        this.billing = new BillingService(con);
        this.payments = new PaymentService(con, cfg.paymentsExportFile);
    }

    public void run() throws Exception {
        System.out.println("Electronic House Manager (Console)");
        while (true) {
            io.line();
            System.out.println("Main menu");
            System.out.println("1) Companies");
            System.out.println("2) Employees");
            System.out.println("3) Buildings");
            System.out.println("4) Persons (Owners/Residents)");
            System.out.println("5) Apartments / Residents / Pets");
            System.out.println("6) Building fees");
            System.out.println("7) Record payment");
            System.out.println("8) Filters & sorting");
            System.out.println("9) Reports");
            System.out.println("0) Exit");
            int c = io.promptInt("Choose");
            try {
                switch (c) {
                    case 1 -> menuCompanies();
                    case 2 -> menuEmployees();
                    case 3 -> menuBuildings();
                    case 4 -> menuPersons();
                    case 5 -> menuApartments();
                    case 6 -> menuFees();
                    case 7 -> menuPayments();
                    case 8 -> menuFilters();
                    case 9 -> menuReports();
                    case 0 -> { System.out.println("Bye."); return; }
                    default -> System.out.println("Unknown choice.");
                }
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
    }

    private void menuCompanies() throws Exception {
        io.line();
        System.out.println("Companies");
        System.out.println("1) List");
        System.out.println("2) Create");
        System.out.println("3) Update");
        System.out.println("4) Delete");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                List<Company> list = companies.list();
                list.forEach(x -> System.out.println(x.id() + " | " + x.name()));
            }
            case 2 -> {
                String name = io.prompt("Name");
                String vat = io.prompt("VAT (optional)");
                String phone = io.prompt("Phone (optional)");
                String email = io.prompt("Email (optional)");
                long id = companies.create(name, vat, phone, email);
                System.out.println("Created company id=" + id);
            }
            case 3 -> {
                long id = io.promptLong("Company id");
                Company cur = companies.get(id);
                if (cur == null) { System.out.println("Not found."); return; }
                String name = io.prompt("Name [" + cur.name() + "]");
                if (name.trim().isEmpty()) name = cur.name();
                String vat = io.prompt("VAT [" + safe(cur.vatNumber()) + "]");
                if (vat.trim().isEmpty()) vat = cur.vatNumber();
                String phone = io.prompt("Phone [" + safe(cur.phone()) + "]");
                if (phone.trim().isEmpty()) phone = cur.phone();
                String email = io.prompt("Email [" + safe(cur.email()) + "]");
                if (email.trim().isEmpty()) email = cur.email();
                companies.update(id, name, vat, phone, email);
                System.out.println("Updated.");
            }
            case 4 -> {
                long id = io.promptLong("Company id");
                companies.delete(id);
                System.out.println("Deleted.");
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuEmployees() throws Exception {
        io.line();
        System.out.println("Employees");
        long companyId = io.promptLong("Company id");
        System.out.println("1) List");
        System.out.println("2) Create");
        System.out.println("3) Update");
        System.out.println("4) Deactivate + redistribute buildings");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                List<Employee> list = employees.listByCompany(companyId);
                list.forEach(e -> System.out.println(e.id() + " | " + e.fullName() + " | active=" + e.isActive()));
            }
            case 2 -> {
                String first = io.prompt("First name");
                String last = io.prompt("Last name");
                String phone = io.prompt("Phone (optional)");
                String email = io.prompt("Email (optional)");
                long id = employees.create(companyId, first, last, phone, email);
                System.out.println("Created employee id=" + id);
            }
            case 3 -> {
                long id = io.promptLong("Employee id");
                Employee emp = null;
                for (Employee e : employees.listByCompany(companyId)) if (e.id() == id) emp = e;
                if (emp == null) { System.out.println("Not found."); return; }
                String first = io.prompt("First name [" + emp.firstName() + "]");
                if (first.trim().isEmpty()) first = emp.firstName();
                String last = io.prompt("Last name [" + emp.lastName() + "]");
                if (last.trim().isEmpty()) last = emp.lastName();
                String phone = io.prompt("Phone [" + safe(emp.phone()) + "]");
                if (phone.trim().isEmpty()) phone = emp.phone();
                String email = io.prompt("Email [" + safe(emp.email()) + "]");
                if (email.trim().isEmpty()) email = emp.email();
                boolean active = io.promptYesNo("Active?");
                employees.update(id, first, last, phone, email, active);
                System.out.println("Updated.");
            }
            case 4 -> {
                long id = io.promptLong("Employee id");
                employees.deactivateAndRedistribute(id);
                System.out.println("Deactivated and redistributed buildings.");
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuBuildings() throws Exception {
        io.line();
        System.out.println("Buildings");
        long companyId = io.promptLong("Company id");
        System.out.println("1) List by company");
        System.out.println("2) Create (auto assign to least-loaded employee)");
        System.out.println("3) Update");
        System.out.println("4) Delete");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                List<Building> list = buildings.listByCompany(companyId);
                list.forEach(b -> System.out.println(b.id() + " | emp=" + b.employeeId() + " | " + b.address()));
            }
            case 2 -> {
                String address = io.prompt("Address");
                int floors = io.promptInt("Floors");
                int apts = io.promptInt("Apartments count");
                BigDecimal built = io.promptBigDecimal("Built area (m2)");
                BigDecimal common = io.promptBigDecimal("Common parts (m2)");
                long id = buildings.createAutoAssign(companyId, address, floors, apts, built, common);
                Building b = buildings.get(id);
                System.out.println("Created building id=" + id + ", assigned to employee=" + b.employeeId());
            }
            case 3 -> {
                long id = io.promptLong("Building id");
                Building b = buildings.get(id);
                if (b == null) { System.out.println("Not found."); return; }
                long employeeId = io.promptLong("Employee id [" + b.employeeId() + "]");
                String address = io.prompt("Address [" + b.address() + "]");
                if (address.trim().isEmpty()) address = b.address();
                int floors = io.promptInt("Floors [" + b.floors() + "]");
                int apts = io.promptInt("Apartments count [" + b.apartmentsCount() + "]");
                BigDecimal built = io.promptBigDecimal("Built area (m2) [" + b.builtAreaM2() + "]");
                BigDecimal common = io.promptBigDecimal("Common parts (m2) [" + b.commonPartsM2() + "]");
                buildings.update(id, employeeId, address, floors, apts, built, common);
                System.out.println("Updated.");
            }
            case 4 -> {
                long id = io.promptLong("Building id");
                buildings.delete(id);
                System.out.println("Deleted.");
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuPersons() throws Exception {
        io.line();
        System.out.println("Persons");
        System.out.println("1) List sorted by name");
        System.out.println("2) List sorted by age");
        System.out.println("3) Create");
        System.out.println("4) Update");
        System.out.println("5) Delete");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> persons.listSorted("name").forEach(p -> System.out.println(p.id() + " | " + p.fullName() + " | " + p.birthDate()));
            case 2 -> persons.listSorted("age").forEach(p -> System.out.println(p.id() + " | " + p.fullName() + " | " + p.birthDate()));
            case 3 -> {
                String first = io.prompt("First name");
                String last = io.prompt("Last name");
                LocalDate bd = io.promptDate("Birth date");
                long id = persons.create(first, last, bd);
                System.out.println("Created person id=" + id);
            }
            case 4 -> {
                long id = io.promptLong("Person id");
                Person p = persons.get(id);
                if (p == null) { System.out.println("Not found."); return; }
                String first = io.prompt("First name [" + p.firstName() + "]");
                if (first.trim().isEmpty()) first = p.firstName();
                String last = io.prompt("Last name [" + p.lastName() + "]");
                if (last.trim().isEmpty()) last = p.lastName();
                LocalDate bd = io.promptDate("Birth date [" + p.birthDate() + "]");
                persons.update(id, first, last, bd);
                System.out.println("Updated.");
            }
            case 5 -> {
                long id = io.promptLong("Person id");
                persons.delete(id);
                System.out.println("Deleted.");
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuApartments() throws Exception {
        io.line();
        System.out.println("Apartments / Residents / Pets");
        System.out.println("1) List apartments in building");
        System.out.println("2) Create apartment");
        System.out.println("3) Manage residents");
        System.out.println("4) Add pet");
        System.out.println("5) Calculate monthly fee for apartment");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                long buildingId = io.promptLong("Building id");
                apartments.listByBuilding(buildingId).forEach(a ->
                        System.out.println(a.id() + " | floor=" + a.floor() + " | apt=" + a.aptNumber() + " | area=" + a.areaM2() + " | ownerId=" + a.ownerPersonId())
                );
            }
            case 2 -> {
                long buildingId = io.promptLong("Building id");
                int floor = io.promptInt("Floor");
                String num = io.prompt("Apartment number");
                BigDecimal area = io.promptBigDecimal("Area (m2)");
                String owner = io.prompt("Owner person id (optional)");
                Long ownerId = owner.trim().isEmpty() ? null : Long.parseLong(owner.trim());
                long id = apartments.create(buildingId, floor, num, area, ownerId);
                System.out.println("Created apartment id=" + id);
            }
            case 3 -> manageResidents();
            case 4 -> {
                long aptId = io.promptLong("Apartment id");
                String name = io.prompt("Pet name");
                String type = io.prompt("Pet type");
                boolean uses = io.promptYesNo("Uses common parts?");
                long id = apartments.addPet(aptId, name, type, uses);
                System.out.println("Added pet id=" + id);
            }
            case 5 -> {
                long aptId = io.promptLong("Apartment id");
                BigDecimal fee = billing.calculateMonthlyFee(aptId);
                System.out.println("Monthly fee: " + fee);
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void manageResidents() throws Exception {
        io.line();
        long aptId = io.promptLong("Apartment id");
        System.out.println("Residents for apartment " + aptId);
        apartments.listResidents(aptId).forEach(r ->
                System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | birth=" + r[3] + " | elevator=" + r[4])
        );
        System.out.println("1) Add resident");
        System.out.println("2) Update resident elevator flag");
        System.out.println("3) Remove resident");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                long personId = io.promptLong("Person id");
                boolean elevator = io.promptYesNo("Uses elevator?");
                apartments.addResident(aptId, personId, elevator);
                System.out.println("Added.");
            }
            case 2 -> {
                long personId = io.promptLong("Person id");
                boolean elevator = io.promptYesNo("Uses elevator?");
                apartments.updateResident(aptId, personId, elevator);
                System.out.println("Updated.");
            }
            case 3 -> {
                long personId = io.promptLong("Person id");
                apartments.removeResident(aptId, personId);
                System.out.println("Removed.");
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuFees() throws Exception {
        io.line();
        System.out.println("Building fees");
        long buildingId = io.promptLong("Building id");
        System.out.println("1) View");
        System.out.println("2) Set/Update");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                BuildingFee f = fees.get(buildingId);
                if (f == null) System.out.println("No fees set.");
                else System.out.println("price/m2=" + f.pricePerM2() + ", perResidentOver7Elev=" + f.addPerResidentOver7Elevator() + ", perPetCommon=" + f.addPerPetCommonParts());
            }
            case 2 -> {
                BigDecimal price = io.promptBigDecimal("Price per m2");
                BigDecimal perRes = io.promptBigDecimal("Add per resident over 7 using elevator");
                BigDecimal perPet = io.promptBigDecimal("Add per pet using common parts");
                fees.setFee(buildingId, price, perRes, perPet);
                System.out.println("Saved.");
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuPayments() throws Exception {
        io.line();
        System.out.println("Record payment");
        long companyId = io.promptLong("Company id");
        long buildingId = io.promptLong("Building id");
        long apartmentId = io.promptLong("Apartment id");
        long employeeId = io.promptLong("Employee id (building service employee)");
        System.out.println("1) Enter amount manually");
        System.out.println("2) Auto-calc monthly fee for apartment");
        int c = io.promptInt("Choose");
        BigDecimal amount;
        if (c == 2) {
            amount = billing.calculateMonthlyFee(apartmentId);
            System.out.println("Calculated amount=" + amount);
        } else {
            amount = io.promptBigDecimal("Amount");
        }
        LocalDateTime paidAt = io.promptDateTimeOrNow("Paid at");
        long pid = payments.recordPayment(companyId, employeeId, buildingId, apartmentId, amount, paidAt);
        System.out.println("Payment recorded id=" + pid + " and exported to CSV.");
    }

    private void menuFilters() throws Exception {
        io.line();
        System.out.println("Filters & sorting");
        System.out.println("1) Companies by revenue");
        System.out.println("2) Employees by name");
        System.out.println("3) Employees by number of buildings");
        System.out.println("4) Residents in building by name");
        System.out.println("5) Residents in building by age");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                companies.listByRevenue().forEach(r -> System.out.println(r[0] + " | " + r[1] + " | revenue=" + r[2]));
            }
            case 2 -> {
                long companyId = io.promptLong("Company id");
                employees.listSorted(companyId, "name").forEach(r ->
                        System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | active=" + r[3] + " | buildings=" + r[4]));
            }
            case 3 -> {
                long companyId = io.promptLong("Company id");
                employees.listSorted(companyId, "buildings").forEach(r ->
                        System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | active=" + r[3] + " | buildings=" + r[4]));
            }
            case 4 -> {
                long buildingId = io.promptLong("Building id");
                persons.listResidentsByBuilding(buildingId, "name").forEach(r ->
                        System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | birth=" + r[3]));
            }
            case 5 -> {
                long buildingId = io.promptLong("Building id");
                persons.listResidentsByBuilding(buildingId, "age").forEach(r ->
                        System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | birth=" + r[3]));
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private void menuReports() throws Exception {
        io.line();
        System.out.println("Reports");
        System.out.println("1) Buildings per employee (count + list)");
        System.out.println("2) Apartments in building");
        System.out.println("3) Residents in building");
        System.out.println("4) Paid sums by company");
        System.out.println("5) Paid sums by building");
        System.out.println("6) Paid sums by employee");
        int c = io.promptInt("Choose");
        switch (c) {
            case 1 -> {
                long companyId = io.promptLong("Company id");
                for (Employee e : employees.listByCompany(companyId)) {
                    List<Building> b = buildings.listByEmployee(e.id());
                    System.out.println("Employee " + e.fullName() + " (id=" + e.id() + ") -> buildings=" + b.size());
                    b.forEach(x -> System.out.println("  - " + x.id() + " | " + x.address()));
                }
            }
            case 2 -> {
                long buildingId = io.promptLong("Building id");
                apartments.listByBuilding(buildingId).forEach(a -> System.out.println(a.id() + " | floor=" + a.floor() + " | apt=" + a.aptNumber() + " | area=" + a.areaM2()));
            }
            case 3 -> {
                long buildingId = io.promptLong("Building id");
                persons.listResidentsByBuilding(buildingId, "name").forEach(r -> System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | birth=" + r[3]));
            }
            case 4 -> {
                long companyId = io.promptLong("Company id");
                System.out.println("Paid total=" + payments.sumPaidByCompany(companyId));
            }
            case 5 -> {
                long companyId = io.promptLong("Company id");
                payments.paidSumsByBuilding(companyId).forEach(r -> System.out.println(r[0] + " | " + r[1] + " | paid=" + r[2]));
            }
            case 6 -> {
                long companyId = io.promptLong("Company id");
                payments.paidSumsByEmployee(companyId).forEach(r -> System.out.println(r[0] + " | " + r[1] + " " + r[2] + " | paid=" + r[3]));
            }
            default -> System.out.println("Unknown choice.");
        }
    }

    private String safe(String s) { return s == null ? "" : s; }
}
