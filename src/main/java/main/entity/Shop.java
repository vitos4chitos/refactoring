package main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.util.PGInterval;

import java.math.BigDecimal;
import java.sql.Time;

@Data
@NoArgsConstructor
public class Shop {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private Long id;
    private Long quantity;
    private BigDecimal cost;
    private String time;
}
