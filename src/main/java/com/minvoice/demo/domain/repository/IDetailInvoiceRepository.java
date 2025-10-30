package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.DetailInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetailInvoiceRepository extends JpaRepository<DetailInvoice, Integer> {
}
