package com.ks.inventoryservice.mapper;


import com.ks.common.proto.Money;
import com.ks.common.proto.ProductResponse;
import com.ks.inventoryservice.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface ProtoMapper {


    @Mapping(source = "id", target = "productId", qualifiedByName = "longToString")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price", qualifiedByName = "bigDecimalToMoney")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(item))")
    @Mapping(source = "discount", target = "sale")
    ProductResponse toProto(Item item);

    // --- кастомные методы для конверсии ---
    @Named("longToString")
    default String longToString(Long id) {
        return id == null ? null : id.toString();
    }

    @Named("bigDecimalToMoney")
    default Money bigDecimalToMoney(BigDecimal value) {
        if (value == null) return null;
        int scale = value.scale();
        long amount = value.movePointRight(scale).longValueExact();
        return Money.newBuilder()
                .setAmount(amount)
                .setScale(scale)
                .build();
    }

    // totalPrice = price * quantity * (1 - discount/100)
    default Money calculateTotalPrice(Item item) {
        if (item == null || item.getPrice() == null || item.getQuantity() == null) {
            return null;
        }
        BigDecimal discountMultiplier = BigDecimal.valueOf(100 - (item.getDiscount() != null ? item.getDiscount() : 0))
                .divide(BigDecimal.valueOf(100));
        BigDecimal total = item.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .multiply(discountMultiplier);
        return bigDecimalToMoney(total);
    }
}
