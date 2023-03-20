package main.entity;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class References extends BaseAnswer{
    private List<Reference> references;

    public void addReference(Reference reference){
        references.add(reference);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Reference{
        private Long id;
        private String name;
        private String sign;
        private String check;
    }
}
