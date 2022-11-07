package com.shopper.walnut.walnut.converter;

import com.shopper.walnut.walnut.model.status.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        if(status == null){
            return null;
        }
        return status.getValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String statusName) {
        if(statusName == null){
            return null;
        }
        return Stream.of(OrderStatus.values())
                .filter(m -> m.getValue().equals(statusName))
                .findFirst().orElse(null);
    }
}
