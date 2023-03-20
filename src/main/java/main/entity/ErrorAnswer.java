package main.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorAnswer extends BaseAnswer {

    private String message;
}
