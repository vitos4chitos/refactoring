package main.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@NoArgsConstructor
public class DocumentToAddPrior {
    private String login;
    private String name;
    private int sale;

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public long getPrior() {
        return prior;
    }

    public void setPrior(long prior) {
        this.prior = prior;
    }

    private long prior;

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


    public String getBywhom() {
        return bywho;
    }

    public void setBywhom(String bywhom) {
        this.bywho = bywhom;
    }

    private Date date1;
    private String bywho;


}
