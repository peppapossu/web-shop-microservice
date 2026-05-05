package com.ks.orderservice.gateway.inventory.grpc.mapper;

import com.ks.items.v1.Money;
import com.ks.items.v1.ReserveItemResponse;
import com.ks.items.v1.ReserveResponse;
import com.ks.orderservice.service.dto.ReservationItemResult;
import com.ks.orderservice.service.dto.ReservationResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ResultProto {

    @Mapping(target = "price", source = "price" ,qualifiedByName = "moneyToBigDecimal")
    ReservationItemResult toReservationItemResult(ReserveItemResponse itemResponse);

    ReservationResult toReservationResult(ReserveResponse reservationResponse);

    List<ReservationItemResult> toReservationResultList(List<ReserveItemResponse> itemResponseList);


    @Named("moneyToBigDecimal")
    default BigDecimal moneyToBigDecimal(Money money) {
        if (money == null) return null;

        return BigDecimal.valueOf(money.getAmount(), money.getScale());
    }




}
