package com.sparta.no1delivery.domain.payment.infrastructure.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.no1delivery.domain.payment.domain.PaymentLog;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Converter(autoApply = true)
public class PaymentLogConverter implements AttributeConverter<List<PaymentLog>, String> {
    private static final ObjectMapper om;
    static {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule()); // LocalDateTime
    }
    @Override
    public String convertToDatabaseColumn(List<PaymentLog> attribute) {
        try {
            return  om.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Converter Error: {}", e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<PaymentLog> convertToEntityAttribute(String dbData) {

        if (StringUtils.hasText(dbData)) {
            try {
                return new ArrayList<>( om.readValue(dbData, new TypeReference<>() {}));
            } catch (JsonProcessingException e) {
                log.error("Converter Error: {}", e.getMessage(), e);
            }
        }
        return new ArrayList<>();
    }
}
