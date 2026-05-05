package com.ks.inventoryservice.repository;

import com.ks.inventoryservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ItemRepository extends JpaRepository<Item, Long> {
    int getStockById(Long id);

    @Query(value = """
                        UPDATE items
                        SET stock = stock - :quantity,
                            reservation = reservation + :quantity
                        WHERE stock >= :quantity AND id = :id
            """, nativeQuery = true)
    @Modifying
    int checkAndReserve(@Param("id") Long id, @Param("quantity") Integer requestQuantity);

}