package com.ks.orderservice.gateway.inventory.grpc.mapper;

import com.ks.orderservice.order.entity.Item;
import com.ks.orderservice.gateway.inventory.grpc.dto.ReservationItemResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {

    @Mapping(source = "reserved", target = "quantity")
    @Mapping(target = "order",  ignore = true)

    Item toItem(ReservationItemResult reservationResult);

    List<Item> toItems(List<ReservationItemResult> reservationResults);

}