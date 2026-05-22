package com.ks.inventoryservice.transport.grpc.mapper;

import com.ks.inventoryservice.item.domain.Item;
import com.ks.items.v1.Money;
import com.ks.items.v1.ReserveItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", imports = BigDecimal.class)
public interface ItemProtoMapper {

    @Mapping(source = "price", target = "price", qualifiedByName = "bigDecimalToMoney")
    ReserveItemResponse toProto(Item item);

    List<ReserveItemResponse> toProto(List<Item> items);

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

}
