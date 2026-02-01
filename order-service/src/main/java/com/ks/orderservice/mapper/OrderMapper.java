package com.ks.orderservice.mapper;

import com.ks.avro.order.Item;
import com.ks.avro.order.OrderCreatedEvent;
import com.ks.orderservice.dto.order.CreateOrderResponse;
import com.ks.orderservice.dto.order.item.ItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    CreateOrderResponse toCreateOrderResponse(OrderCreatedEvent orderCreatedEvent);

    @Mapping(source = "sale", target = "discount")
    ItemResponse toItemResponse(Item item);
}
