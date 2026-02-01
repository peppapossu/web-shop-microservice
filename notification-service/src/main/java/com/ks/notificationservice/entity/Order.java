package com.ks.notificationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, name = "order_id")
    private Long orderId;

    @Column(nullable = false, name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer sale;

    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @Column(nullable = false, name = "user_id")
    private Long customerId;
}
