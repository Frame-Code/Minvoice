package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemRepository extends JpaRepository<Item, Integer> {
}
