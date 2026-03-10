package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
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

        if ("VOUCHER".equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null &&
                    voucherCode.length() == 16 &&
                    voucherCode.startsWith("ESHOP") &&
                    voucherCode.replaceAll("[^0-9]", "").length() == 8) {
                this.status = "SUCCESS";
            } else {
                this.status = "REJECTED";
            }
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address == null || address.trim().isEmpty() || deliveryFee == null || deliveryFee.trim().isEmpty()) {
                this.status = "REJECTED";
            } else {
                this.status = "SUCCESS";
            }
        } else {
            this.status = "REJECTED";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
