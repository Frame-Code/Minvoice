package com.minvoice.demo.domain.entites;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String code;

    @Column(nullable = false)
    public double price;
}
