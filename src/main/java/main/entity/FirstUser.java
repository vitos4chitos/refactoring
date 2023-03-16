package main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FirstUser {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDockname() {
        return dockname;
    }

    public void setDockname(String dockname) {
        this.dockname = dockname;
    }

    private Long id;
   private String name;
   private String surname;
   private String dockname;

}
