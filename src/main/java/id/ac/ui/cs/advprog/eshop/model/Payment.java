package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import enums.PaymentMethod;
import enums.PaymentStatus;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if (PaymentMethod.VOUCHER.getValue().equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null &&
                    voucherCode.length() == 16 &&
                    voucherCode.startsWith("ESHOP") &&
                    voucherCode.replaceAll("[^0-9]", "").length() == 8) {
                this.status = PaymentStatus.SUCCESS.getValue();
            } else {
                this.status = PaymentStatus.REJECTED.getValue();
            }
        } else if (PaymentMethod.CASH_ON_DELIVERY.getValue().equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address == null || address.trim().isEmpty() || deliveryFee == null || deliveryFee.trim().isEmpty()) {
                this.status = PaymentStatus.REJECTED.getValue();
            } else {
                this.status = PaymentStatus.SUCCESS.getValue();
            }
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
        }
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
