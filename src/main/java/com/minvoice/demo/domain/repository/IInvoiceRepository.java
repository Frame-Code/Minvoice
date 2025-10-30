package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.DetailInvoice;
import com.minvoice.demo.domain.model.Invoice;
import com.minvoice.demo.domain.model.InvoiceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(value = "SELECT d FROM DetailInvoice d WHERE d.Invoice.id = :id")
    List<DetailInvoice> findDetailsByInvoiceId(@Param("id") int id);

    @Query(value = "SELECT d FROM InvoiceFile d WHERE d.Invoice.id = :id")
    Optional<InvoiceFile> findInvoiceFileByInvoiceId(@Param("id") int id);
}
