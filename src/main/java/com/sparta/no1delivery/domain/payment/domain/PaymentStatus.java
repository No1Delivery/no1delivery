package com.sparta.no1delivery.domain.payment.domain;

import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;

public enum PaymentStatus {
    READY, //결제 객체를 생성하면 가지게 되는 초기 상태
    IN_PROGRESS, // 결제 수단 정보와 해당 결제 수단의 소유자가 맞는지 인증을 마친 상태, 결제 승인 API를 호출하면 결제가 완료
    WAITING_FOR_DEPOSIT, // 가상계좌 결제 흐름에만 있는 상태, 발급된 가상계좌에 구매자가 아직 입급을 하지 않은 상태
    DONE, // 인증된 결제수단으로 요청한 결제가 승인된 상태
    CANCELLED, // 승인된 결제가 취소된 상태
    PARTIAL_CANCELLED, // 승인된 결제가 일부분 취소가 된 상태
    ABORTED, // 결제 승인이 실패한 상태
    EXPIRED; // 결제 유효 시간 30분이 지나 거래가 취소된 상태, IN_PROGRESS에서 결제 승인 API를 호출하지 않고 있으면 이 상태가 됨.


    //도메인 로직 만들기
    // 결제 승인 상태 체크 (결제 승인을 위해 상태가 준비상태이거나 인증이 끝나서 결제 승인API를 호출하면 되는 상태)
    public void verifyNotProcessed(){
        if(!(this == PaymentStatus.READY || this == PaymentStatus.IN_PROGRESS)){
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }
    }
    // 결제 실패 상태 체크 (상태가 이미 결제가 완료되었거나, 취소했거나, 부분 취소가 되었다면 실패가 되면 안됌)
    public void verifyAbortable(){
        if(this == PaymentStatus.DONE || this == PaymentStatus.CANCELLED || this == PaymentStatus.PARTIAL_CANCELLED){
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }
    }

    // 결제 취소 상태 체크
    public void verifyCancelable(){
        if(!(this == PaymentStatus.DONE || this == PaymentStatus.PARTIAL_CANCELLED)){
            throw new CustomException(ErrorCode.PAYMENT_CANCEL_FAILED);
        }
    }
}
