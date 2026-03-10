package enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    VOUCHER("VOUCHER"),
    CASH_ON_DELIVERY("CASH_ON_DELIVERY"),
    BANK_TRANSFER("BANK_TRANSFER");

    private final String value;

    private PaymentMethod(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
