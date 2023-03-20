package main.entity;

import lombok.*;
import main.database.entity.Signature;
import main.database.entity.Status;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DockInfo extends BaseAnswer{
    private Long id;
    private String name;
    private String lgot;
    private Date validity;

    private List<Signature> signatures;

    private Status status;

    private Date issue;
    private String byWho;



}
