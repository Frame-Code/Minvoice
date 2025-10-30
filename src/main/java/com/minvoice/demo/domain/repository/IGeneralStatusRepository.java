package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.GeneralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGeneralStatusRepository extends JpaRepository<GeneralStatus, Integer> {
}
