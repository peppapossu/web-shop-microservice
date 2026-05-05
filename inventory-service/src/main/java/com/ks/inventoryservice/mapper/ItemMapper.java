package com.ks.inventoryservice.mapper;

import com.ks.inventoryservice.dto.item.CreateItemRequest;
import com.ks.inventoryservice.dto.item.ItemResponse;
import com.ks.inventoryservice.dto.item.UpdateItemRequest;
import com.ks.inventoryservice.domain.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponse toItemResponse(Item item);

    List<ItemResponse> toItemsResponse(List<Item> items);

    Item toItem(CreateItemRequest createItemRequest);

    Item toItem(UpdateItemRequest updateItemRequest);

}
