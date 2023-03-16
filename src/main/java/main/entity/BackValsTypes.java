package main.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BackValsTypes {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInstnance() {
        return instnance;
    }

    public void setInstnance(long instnance) {
        this.instnance = instnance;
    }

    private long id;
    private String name;
    private long instnance;
}
