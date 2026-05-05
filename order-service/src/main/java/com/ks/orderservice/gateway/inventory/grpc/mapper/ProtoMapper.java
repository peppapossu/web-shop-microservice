package com.ks.orderservice.gateway.inventory.grpc.mapper;

import com.ks.items.v1.ReserveItemRequest;
import com.ks.orderservice.dto.order.item.ItemRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProtoMapper {


    ReserveItemRequest toReserveItemRequest(ItemRequest itemRequest);

    List<ReserveItemRequest> toReserveItemRequestList(List<ItemRequest> itemRequests);
}
