package main.entity.request;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;


@Data
@NoArgsConstructor
@Getter
@Setter
public class RegUserForm {

    private String username;
    private String password;
    private String name;
    private String surname;
    private BigDecimal money;
    private Date date;


}