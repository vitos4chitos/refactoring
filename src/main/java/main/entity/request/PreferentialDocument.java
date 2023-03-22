package main.entity.request;


import lombok.*;

import java.sql.Date;


@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PreferentialDocument {
    private String userLogin;
    private String name;
    private int sale;
    private long priority;
    private Date issueDate;
    private String issuedBy;


}
