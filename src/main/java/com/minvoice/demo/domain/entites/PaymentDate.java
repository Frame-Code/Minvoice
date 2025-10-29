package com.minvoice.demo.domain.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class PaymentDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false)
    public double amount;

    public String observation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generalStatus")
    public GeneralStatus type;

    @Column(nullable = false)
    public LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_invoice")
    public Invoice invoice;
}
