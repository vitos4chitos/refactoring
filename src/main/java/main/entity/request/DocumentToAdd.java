package main.entity.request;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Data
@NoArgsConstructor
@Getter
@Setter
public class DocumentToAdd {
    private String userLogin;
    private String name;
    private Date issueDate;
    private Date validityDate;
    private String issuedBy;


}
