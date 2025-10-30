package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.InvoiceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceFile extends JpaRepository<InvoiceFile, Integer> {
}
