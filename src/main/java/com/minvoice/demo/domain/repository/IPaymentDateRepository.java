package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.PaymentDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentDateRepository extends JpaRepository<PaymentDate, Integer> {

}
