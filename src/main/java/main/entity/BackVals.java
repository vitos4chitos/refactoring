package main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class BackVals {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public Date getIssue() {
        return issue;
    }

    public void setIssue(Date issue) {
        this.issue = issue;
    }

    private String name;

    public String getBywhom() {
        return bywhom;
    }

    public void setBywhom(String bywhom) {
        this.bywhom = bywhom;
    }

    private String bywhom;
    private long id;
    private Date validity;
    private Date issue;
}
