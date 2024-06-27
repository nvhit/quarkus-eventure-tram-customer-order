package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return money != null ? money.getAmount() : null;
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal dbData) {
        return dbData != null ? new Money(dbData) : null;
    }
}
