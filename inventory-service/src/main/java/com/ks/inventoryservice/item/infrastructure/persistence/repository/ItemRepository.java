package com.ks.inventoryservice.item.infrastructure.persistence.repository;

import com.ks.inventoryservice.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface ItemRepository extends JpaRepository<Item, Long>, PagingAndSortingRepository<Item, Long> {

    @Modifying
    @Query(value = """
                        UPDATE items
                        SET stock = stock - :quantity,
                            reservation = reservation + :quantity
                        WHERE id = :id AND stock >= :quantity
            """, nativeQuery = true)
    int checkAndReserve(@Param("id") Long id, @Param("quantity") Integer requestQuantity);


    Page<Item> findAll(Pageable pageable);

}