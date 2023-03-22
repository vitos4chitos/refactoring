package main.entity.responce;

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
public class DockInfo extends BaseAnswer {
    private Long documentId;
    private String name;
    private String lgots;
    private Date validityDate;

    private List<Signature> signatures;

    private Status status;

    private Date issueDate;
    private String isuedBy;



}
