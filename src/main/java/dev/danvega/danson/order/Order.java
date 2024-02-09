package dev.danvega.danson.order;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String items;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String info) {
        this.items = info;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", items='" + items + '\'' +
                '}';
    }
}
