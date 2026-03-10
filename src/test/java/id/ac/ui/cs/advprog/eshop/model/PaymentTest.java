package id.ac.ui.cs.advprog.eshop.model;

import enums.PaymentMethod;
import enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    Map<String, String> paymentDataVoucher;
    Map<String, String> paymentDataCOD;

    @BeforeEach
    void setup() {
        paymentDataVoucher = new HashMap<>();
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABC5678");

        paymentDataCOD = new HashMap<>();
        paymentDataCOD.put("address", "Jalan Margonda Raya");
        paymentDataCOD.put("deliveryFee", "15000");
    }

    @Test
    void testCreatePaymentVoucherSuccess() {
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER.getValue(), paymentDataVoucher);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedLength() {
        paymentDataVoucher.put("voucherCode", "ESHOP123");
        Payment payment = new Payment("payment-2", PaymentMethod.VOUCHER.getValue(), paymentDataVoucher);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedPrefix() {
        paymentDataVoucher.put("voucherCode", "KSHOP1234ABC5678");
        Payment payment = new Payment("payment-3", PaymentMethod.VOUCHER.getValue(), paymentDataVoucher);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNumChars() {
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABCDEFG");
        Payment payment = new Payment("payment-4", PaymentMethod.VOUCHER.getValue(), paymentDataVoucher);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODSuccess() {
        Payment payment = new Payment("payment-5", PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentDataCOD);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODRejectedEmptyAddress() {
        paymentDataCOD.put("address", "");
        Payment payment = new Payment("payment-6", PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentDataCOD);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentCODRejectedNullFee() {
        paymentDataCOD.put("deliveryFee", null);
        Payment payment = new Payment("payment-7", PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentDataCOD);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }
}
