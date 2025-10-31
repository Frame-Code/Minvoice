package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.GeneralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGeneralStatusRepository extends JpaRepository<GeneralStatus, Integer> {
    @Query(value = "SELECT g FROM GeneralStatus g WHERE LOWER(g.statusGroup) = :statusGroup ")
    List<GeneralStatus> FindByGroup(@Param("statusGroup") String statusGroup);
}
