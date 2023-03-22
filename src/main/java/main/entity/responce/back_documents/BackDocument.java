package main.entity.responce.back_documents;

import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BackDocument{
    private String issuedBy;
    private long documentId;
    private Date validityDate;
    private String name;
    private Date issueDate;
}