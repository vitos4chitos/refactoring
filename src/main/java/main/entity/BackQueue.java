package main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BackQueue {
    private Long id;
    private String name;

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

    public Long getPrior() {
        return prior;
    }

    public void setPrior(Long prior) {
        this.prior = prior;
    }

    public Long getPlace() {
        return place;
    }

    public void setPlace(Long place) {
        this.place = place;
    }

    private Long prior;
    private Long place;
}
