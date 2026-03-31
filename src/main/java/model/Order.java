package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {

    private LocalDateTime dateTime;
    private String description;
    private int cost;

    public Order(String description, int cost) {
        this.dateTime = LocalDateTime.now();
        this.description = description;
        this.cost = cost;
    }

    public String format() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return "[" + dateTime.format(formatter) + "] "
                + description
                + " — " + cost + " септимов";
    }
}
