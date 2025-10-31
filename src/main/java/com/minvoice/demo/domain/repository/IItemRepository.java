package com.minvoice.demo.domain.repository;

import com.minvoice.demo.domain.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IItemRepository extends JpaRepository<Item, Integer> {
    @Query(value = "SELECT i FROM Item i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Optional<Item> getItemByName(@Param("name") String name);

}
