package com.sparta.no1delivery.domain.payment.domain;

public enum PaymentStatus {
    READY, //결제 객체를 생성하면 가지게 되는 초기 상태
    IN_PROGRESS, // 결제 수단 정보와 해당 결제 수단의 소유자가 맞는지 인증을 마친 상태, 결제 승인 API를 호출하면 결제가 완료
    WAITING_FOR_DEPOSIT, // 가상계좌 결제 흐름에만 있는 상태, 발급된 가상계좌에 구매자가 아직 입급을 하지 않은 상태
    DONE, // 인증된 결제수단으로 요청한 결제가 승인된 상태
    CANCELLED, // 승인된 결제가 취소된 상태
    PARTIAL_CANCELLED, // 승인된 결제가 일부분 취소가 된 상태
    ABORTED, // 결제 승인이 실패한 상태
    EXPIRED // 결제 유효 시간 30분이 지나 거래가 취소된 상태, IN_PROGRESS에서 결제 승인 API를 호출하지 않고 있으면 이 상태가 됨.

}
