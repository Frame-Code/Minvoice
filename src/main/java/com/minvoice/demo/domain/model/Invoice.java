package com.minvoice.demo.domain.model;

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
    private Integer id;

    @Enumerated(EnumType.STRING)
    private TypeInvoice typeInvoice;

    @Column(nullable = false, unique = true)
    private String noInvoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generalStatus")
    private GeneralStatus status;

    @Column(nullable = false)
    private String description;

    private String observation;

    @Column(nullable = false)
    private LocalDateTime  issueDate;

    @Column(nullable = false)
    private double total;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetailInvoice> detailInvoices;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PaymentDate> paymentDates;

    public void addDetail(DetailInvoice detailInvoice) {
        detailInvoices.add(detailInvoice);
    }

    public void addPayment(PaymentDate paymentDate) {
        paymentDates.add(paymentDate);
    }

    public double getTotal() {
        for (DetailInvoice detail : detailInvoices) {
            this.total += detail.getItem().getPrice();
        }
        return this.total;
    }

    public double getTotalPayments() {
        double total = 0;
        for (PaymentDate payment : paymentDates) {
            total += payment.getAmount();
        }
        return total;
    }

}
