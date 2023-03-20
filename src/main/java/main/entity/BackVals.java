package main.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BackVals extends BaseAnswer{
    private List<Vals> valsList;

    public void addVal(Vals val){
        valsList.add(val);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Vals{
        private String bywhom;
        private long id;
        private Date validity;
        private String name;
        private Date issue;
    }
}
