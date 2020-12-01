package accountingsystem.app.model;

import java.io.Serializable;

public class Company extends User implements Serializable {

    private long id;

    private String name;

    private Person responsiblePerson;

    public Company() {

    }

    public Company(String email, String password, String name, Person responsiblePerson) {
        super(email, password);
        this.name = name;
        this.responsiblePerson = responsiblePerson;
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

    public Person getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    @Override
    public String toString() {
        return this.email;
    }

}
