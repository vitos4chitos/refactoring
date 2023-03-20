package main.entity;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class BackQueue extends BaseAnswer{
    private List<User> queue;

    public void addUser(User user){
        queue.add(user);
    }

    @Data
    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    public static class User{
        private Long id;
        private String name;
        private Long prior;
        private Long place;
    }
}
