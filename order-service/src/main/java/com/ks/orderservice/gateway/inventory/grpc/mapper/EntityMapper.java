package com.ks.orderservice.gateway.inventory.grpc.mapper;

import com.ks.orderservice.domain.order.Item;
import com.ks.orderservice.service.dto.ReservationItemResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(source = "reserved", target = "quantity")
    @Mapping(target = "order",  ignore = true)

    Item toItem(ReservationItemResult reservationResult);

    List<Item> toItems(List<ReservationItemResult> reservationResults);

}
