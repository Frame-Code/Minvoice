package com.minvoice.demo.domain.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter @Setter
public class InvoiceFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false)
    public String fileName;

    @Column(nullable = false)
    public String filePath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_invoice")
    public Invoice invoice;
}
