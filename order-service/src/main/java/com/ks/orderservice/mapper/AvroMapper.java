package com.ks.orderservice.mapper;

import com.ks.avro.order.Item;
import com.ks.common.proto.Money;
import com.ks.common.proto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AvroMapper {

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "sale", source = "sale")
    @Mapping(target = "price", source = "price", qualifiedByName = "moneyToByteBuffer")
    @Mapping(target = "totalPrice", source = "totalPrice", qualifiedByName = "moneyToByteBuffer")
    Item toItemAvro(ProductResponse product);

    @Named("moneyToByteBuffer")
    default BigDecimal moneyToByteBuffer(Money money) {
        if (money == null) return null;

        return BigDecimal.valueOf(money.getAmount(), money.getScale());
    }
}




