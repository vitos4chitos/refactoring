package main.entity.responce;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class FirstUser extends BaseAnswer {
    private Long id;
    private String name;
    private String surname;
    private String dockname;

}
