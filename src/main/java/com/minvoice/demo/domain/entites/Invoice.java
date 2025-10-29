package com.minvoice.demo.domain.entites;

import com.minvoice.demo.domain.enums.TypeInvoice;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Enumerated(EnumType.STRING)
    public TypeInvoice typeInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_generalStatus")
    public GeneralStatus status;

    @Column(nullable = false)
    public String description;

    public String observation;

    @Column(nullable = false)
    public LocalDateTime  issueDate;

    @Column(nullable = false)
    public double total;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<DetailInvoice> detailInvoices;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PaymentDate> paymentDates;

    public void addDetail(DetailInvoice detailInvoice) {
        detailInvoices.add(detailInvoice);
    }

    public void addPayment(PaymentDate paymentDate) {
        paymentDates.add(paymentDate);
    }
}
