package pl.edu.agh.model;

import javax.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String basicInformation;

    public User() {
    }

    public User(String name, String description, String basicInformation) {
        this.name = name;
        this.description = description;
        this.basicInformation = basicInformation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBasicInformation() {
        return basicInformation;
    }

    public void setBasicInformation(String basicInformation) {
        this.basicInformation = basicInformation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", basicInformation='" + basicInformation + '\'' +
                '}';
    }
}
