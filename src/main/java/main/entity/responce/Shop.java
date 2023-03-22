package main.entity.responce;

import lombok.*;
import org.postgresql.util.PGInterval;

import java.math.BigDecimal;
import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Shop {
    private Long shopId;
    private Long quantity;
    private BigDecimal cost;
    private String time;
}
