package com.ks.notificationservice.mapper;

import com.ks.avro.order.Item;
import com.ks.avro.order.OrderCreatedEvent;
import com.ks.notificationservice.dto.order.ItemResponse;
import com.ks.notificationservice.dto.order.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AvroMapper {


    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "items", target = "items")
    OrderResponse toOrderResponse(OrderCreatedEvent event);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "quantity", target = "amount")
    @Mapping(source = "sale", target = "sale")
    ItemResponse toItemResponse(Item item);


}

