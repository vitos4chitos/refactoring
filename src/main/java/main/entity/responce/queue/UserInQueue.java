package main.entity.responce.queue;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class UserInQueue{
    private Long id;
    private String name;
    private Long prior;
    private Long place;
}
