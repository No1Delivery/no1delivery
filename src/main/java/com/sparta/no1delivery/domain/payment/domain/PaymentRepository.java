package com.sparta.no1delivery.domain.payment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, PaymentId> {
    Optional<Payment>findByPaymentInfoOrderId(UUID orderId);
}