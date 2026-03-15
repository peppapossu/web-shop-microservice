package com.ks.notificationservice.mapper;

import com.ks.avro.order.Item;
import com.ks.avro.order.OrderCreatedEvent;
import com.ks.notificationservice.dto.order.OrderResponse;
import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvroMapperTest {

    private AvroMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AvroMapper.class);
    }

//    @Test
    void shouldMapFromAvro() {

        List<Item> items = new ArrayList<>();

        items.add(new Item(
                1L,
                "Test product",
                new BigDecimal("100.50"),
                2,
                10,
                new BigDecimal("180.90")
        ));

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.newBuilder()
                .setCustomerId(1)
//                .setOrderId(2)
                .setItems(items)
                .build();

        OrderResponse orderResponse = mapper.toOrderResponse(orderCreatedEvent);


        assertEquals(2L, orderResponse.orderId());
        assertEquals(1, orderResponse.items().size());
        assertEquals(new BigDecimal("100.50"), orderResponse.items().getFirst().price());
        assertEquals(new BigDecimal("180.90"), orderResponse.items().getFirst().totalPrice());
    }


}