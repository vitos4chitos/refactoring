package main.entity.responce.back_documents;

import lombok.*;
import main.entity.responce.BaseAnswer;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BackDocuments extends BaseAnswer {
    private List<BackDocument> documentList;

    public void addVal(BackDocument val){
        documentList.add(val);
    }
}
