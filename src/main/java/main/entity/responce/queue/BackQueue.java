package main.entity.responce.queue;

import lombok.*;
import main.entity.responce.BaseAnswer;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class BackQueue extends BaseAnswer {
    private List<UserInQueue> queue;

    public void addUser(UserInQueue user){
        queue.add(user);
    }
}
