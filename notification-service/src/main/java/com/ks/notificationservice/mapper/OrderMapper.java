package com.ks.notificationservice.mapper;

import com.ks.notificationservice.dto.order.OrderResponse;
import com.ks.notificationservice.entity.Order;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    List<OrderResponse> toOrderResponseList(List<Order> orders);

    default Page<OrderResponse> toOrderResponsePage(Page<Order> page) {
        return new PageImpl<>(
                toOrderResponseList(page.getContent()),
                page.getPageable(),
                page.getTotalElements()
        );
    }
}
