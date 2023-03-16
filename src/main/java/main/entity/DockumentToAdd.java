package main.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@NoArgsConstructor
public class DockumentToAdd {
    private String login;
    private String name;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public String getBywhom() {
        return bywho;
    }

    public void setBywhom(String bywhom) {
        this.bywho = bywhom;
    }

    private Date date1;
    private Date date2;
    private String bywho;


}
