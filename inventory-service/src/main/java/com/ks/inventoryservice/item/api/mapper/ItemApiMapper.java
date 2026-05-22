package com.ks.inventoryservice.item.api.mapper;

import com.ks.inventoryservice.item.api.dto.PagedResponse;
import com.ks.inventoryservice.item.api.dto.item.CreateItemRequest;
import com.ks.inventoryservice.item.api.dto.item.ItemResponse;
import com.ks.inventoryservice.item.api.dto.item.UpdateItemRequest;
import com.ks.inventoryservice.item.domain.Item;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemApiMapper {

    ItemResponse toItemResponse(Item item);

    List<ItemResponse> toItemsResponse(List<Item> items);

    Item toItem(CreateItemRequest createItemRequest);

    Item toItem(UpdateItemRequest updateItemRequest);

    PagedResponse<ItemResponse> getPagedResponse(Page<Item> page);


}
