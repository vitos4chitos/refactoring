package main.entity;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserInfo extends BaseAnswer{
    private Long id;
    private BigDecimal money;
}
