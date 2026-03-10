package id.ac.ui.cs.advprog.eshop.repository;

import enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setup() {
        paymentRepository = new PaymentRepository();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("pay-123", PaymentMethod.VOUCHER.getValue(), paymentData);
    }

    @Test
    void testSaveCreateAndFindById() {
        paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById("pay-123");
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(payment);
        Payment newPayment = new Payment(payment.getId(), PaymentMethod.CASH_ON_DELIVERY.getValue(), payment.getPaymentData());
        Payment result = paymentRepository.save(newPayment);
        Payment findResult = paymentRepository.findById(payment.getId());

        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(PaymentMethod.CASH_ON_DELIVERY.getValue(), findResult.getMethod());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        Payment findResult = paymentRepository.findById("invalid-id");
        assertNull(findResult);
    }

    @Test
    void testGetAllPayments() {
        paymentRepository.save(payment);
        List<Payment> allPayments = paymentRepository.getAllPayments();
        assertEquals(1, allPayments.size());
        assertEquals(payment.getId(), allPayments.get(0).getId());
    }
}
