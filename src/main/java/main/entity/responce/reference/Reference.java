package main.entity.responce.reference;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reference{
    private Long id;
    private String name;
    private String sign;
    private String check;
}
