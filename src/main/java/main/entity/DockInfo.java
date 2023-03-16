package main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class DockInfo {
    private Long id;
    private String name;
    private String lgot;
    private Date validity;

    public String getPodtver() {
        return podtver;
    }

    public void setPodtver(String podtver) {
        this.podtver = podtver;
    }

    private String podtver;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    private String pod;

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

    public String getLgot() {
        return lgot;
    }

    public void setLgot(String lgot) {
        this.lgot = lgot;
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

    public String getByWho() {
        return byWho;
    }

    public void setByWho(String byWho) {
        this.byWho = byWho;
    }

    private Date issue;
    private String byWho;



}
