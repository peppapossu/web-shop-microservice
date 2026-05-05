package com.ks.inventoryservice.mapper;

import com.ks.common.proto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ProtoMapperTest {

    ProtoMapper protoMapper;

    @BeforeEach
    void setUp() {
        protoMapper = new ProtoMapperImpl();
    }

//    @Test
//    void shouldConvertToProto() {
//        Item item = Item.builder()
//                .id(1L)
//                .name("name")
//                .price(new BigDecimal("100"))
//                .quantity(1)
//                .discount(0)
//                .build();
//
//        ProductResponse proto = protoMapper.toProto(item);
//
//        assertEquals(1L, proto.getProductId());
//        assertEquals("name", proto.getProductName());
//        assertEquals(new BigDecimal("100"),
//                new BigDecimal(new BigInteger(String.valueOf(proto.getPrice().getAmount()))
//                , proto.getPrice().getScale()));
//        assertEquals(1, proto.getQuantity());
//    }

}