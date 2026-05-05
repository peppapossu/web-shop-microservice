package com.ks.orderservice.dto.order.mapper;

import com.ks.orderservice.domain.order.Item;
import com.ks.orderservice.domain.order.Order;
import com.ks.orderservice.dto.order.CreateOrderResponse;
import com.ks.orderservice.dto.order.item.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    CreateOrderResponse toCreateOrderResponse(Order order);

    @Mapping(source = "quantity", target = "ReservedQuantity")
    ItemResponse toItemsResponse(Item item);


}
