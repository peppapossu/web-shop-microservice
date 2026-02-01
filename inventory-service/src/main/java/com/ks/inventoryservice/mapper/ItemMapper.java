package com.ks.inventoryservice.mapper;

import com.ks.inventoryservice.dto.item.CreateItemRequest;
import com.ks.inventoryservice.dto.item.ItemResponse;
import com.ks.inventoryservice.dto.item.UpdateItemRequest;
import com.ks.inventoryservice.entity.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponse itemToItemResponse(Item item);

    List<ItemResponse> itemsToItemsResponse(List<Item> items);

    Item createItemRequestToItem(CreateItemRequest createItemRequest);

    Item updateItemRequestToItem(UpdateItemRequest updateItemRequest);

}
