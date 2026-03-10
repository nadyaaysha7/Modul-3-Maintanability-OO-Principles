package id.ac.ui.cs.advprog.eshop.service;

import enums.OrderStatus;
import enums.PaymentMethod;
import enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

    Order order;
    Map<String, String> paymentData;

    @BeforeEach
    void setup() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-123", products, 1708560000L, "Safira");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPayment() {
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER.getValue(), paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(order.getId(), result.getPaymentData().get("orderId"));
    }

    @Test
    void testSetStatusSuccessUpdatesOrder() {
        paymentData.put("orderId", order.getId());
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER.getValue(), paymentData);

        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.SUCCESS.getValue());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejectedUpdatesOrder() {
        paymentData.put("orderId", order.getId());
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER.getValue(), paymentData);

        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), updatedPayment.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), OrderStatus.FAILED.getValue());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusInvalidStatus() {
        Payment payment = new Payment("pay-123", PaymentMethod.VOUCHER.getValue(), paymentData);
        assertThrows(IllegalArgumentException.class, () -> paymentService.setStatus(payment, "MEOW"));
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }
}
