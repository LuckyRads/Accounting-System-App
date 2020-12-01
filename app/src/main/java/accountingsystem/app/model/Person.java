package accountingsystem.app.model;

import java.io.Serializable;
import java.util.List;

public class Person extends User implements Serializable {

    private long id;

    private String name;

    private String surname;

    private String phoneNumber;

    private List<Category> managedCategories;

    public Person() {

    }

    public Person(String email, String password, String name, String surname, String phoneNumber) {
        super(email, password);
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Category> getManagedCategories() {
        return managedCategories;
    }

    public void setManagedCategories(List<Category> managedCategories) {
        this.managedCategories = managedCategories;
    }

    @Override
    public String toString() {
        return this.email;
    }

}
