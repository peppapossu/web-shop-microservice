package com.ks.orderservice.mapper;

import com.ks.avro.order.Item;
import com.ks.common.proto.Money;
import com.ks.common.proto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AvroMapperTest {

    AvroMapper avroMapper = Mappers.getMapper(AvroMapper.class);


    @Test
    void testProductMapping() {
        ProductResponse product = ProductResponse.newBuilder()
                .setProductId(1L)
                .setProductName("Test")
                .setQuantity(2)
                .setPrice(Money.newBuilder().setAmount(10050).setScale(2).build())
                .setSale(10)
                .setTotalPrice(Money.newBuilder().setAmount(18090).setScale(2).build())
                .build();

        Item item = avroMapper.toItemAvro(product);

//        BigDecimal price = new BigDecimal(new BigInteger(item.getPrice().array()), 2);
        assertEquals(new BigDecimal("100.50"), item.getPrice());

//        BigDecimal totalPrice = new BigDecimal(new BigInteger(item.getTotalPrice().array()), 2);
        assertEquals(new BigDecimal("180.90"), item.getTotalPrice());
    }

}
