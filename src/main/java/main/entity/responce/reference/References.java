package main.entity.responce.reference;


import lombok.*;
import main.entity.responce.BaseAnswer;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class References extends BaseAnswer {
    private List<Reference> references;

    public void addReference(Reference reference){
        references.add(reference);
    }
}
