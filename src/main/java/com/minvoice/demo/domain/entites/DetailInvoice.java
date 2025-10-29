package com.minvoice.demo.domain.entites;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class DetailInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_invoice")
    public Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item")
    public Item item;
}
