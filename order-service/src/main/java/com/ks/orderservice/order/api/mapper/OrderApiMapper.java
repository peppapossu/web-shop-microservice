package com.ks.orderservice.order.api.mapper;

import com.ks.orderservice.order.entity.Item;
import com.ks.orderservice.order.entity.Order;
import com.ks.orderservice.order.api.dto.CreateOrderResponse;
import com.ks.orderservice.order.api.dto.item.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderApiMapper {

    CreateOrderResponse toCreateOrderResponse(Order order);

    @Mapping(source = "quantity", target = "ReservedQuantity")
    ItemResponse toItemsResponse(Item item);


}
