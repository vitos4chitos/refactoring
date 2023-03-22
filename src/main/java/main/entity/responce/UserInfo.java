package main.entity.responce;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserInfo extends BaseAnswer {
    private Long instanceId;
    private BigDecimal userMoney;
}
