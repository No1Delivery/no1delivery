// 토스 테스트 클라이언트 키 (수정하지 마세요)
const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
const customerKey = "USER_" + Math.random().toString(36).substring(2, 11);
const paymentWidget = PaymentWidget(clientKey, customerKey);

// 1. 초기 금액 설정 및 위젯 렌더링
let amountInput = document.getElementById("order-amount-input");
let amount = parseInt(amountInput.value);

const paymentMethodsWidget = paymentWidget.renderPaymentMethods("#payment-method", {value: amount});
paymentWidget.renderAgreement("#agreement");

// 2. 금액 수정 시 위젯 업데이트 버튼 이벤트
document.getElementById("apply-btn").addEventListener("click", () => {
    amount = parseInt(amountInput.value);
    paymentMethodsWidget.updateAmount(amount);
    alert("결제 금액이 " + amount + "원으로 갱신되었습니다.");
});

// 3. 결제하기 버튼 클릭
document.getElementById("payment-button").addEventListener("click", () => {
    const orderId = document.getElementById("order-id-input").value;
    const orderName = document.getElementById("order-name-input").value;

    if (!orderId) {
        alert("DB에 등록된 주문 번호를 입력해주세요!");
        return;
    }

    paymentWidget.requestPayment({
        orderId: orderId,
        orderName: orderName,
        customerName: "주문자",
        // [중요] 결제 성공 시 이동할 우리 백엔드 API 주소
        successUrl: window.location.origin + "/v1/payments/success",
        failUrl: window.location.origin + "/v1/payments/fail",
    }).catch(error => {
        if (error.code === 'USER_CANCEL') {
            alert("결제를 취소하셨습니다.");
        } else {
            alert(error.message);
        }
    });
});