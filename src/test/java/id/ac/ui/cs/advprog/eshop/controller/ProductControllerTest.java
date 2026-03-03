package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductControllerTest {

    private ProductController controller;
    private ProductServiceStub serviceStub;
    private Model model;

    @BeforeEach
    void setUp() throws Exception {
        controller = new ProductController();
        serviceStub = new ProductServiceStub();

        model = new ConcurrentModel();

        Field serviceField = ProductController.class.getDeclaredField("service");
        serviceField.setAccessible(true);
        serviceField.set(controller, serviceStub);
    }

    @Test
    void testCreateProductPage() {
        String viewName = controller.createProductPage(model);

        assertEquals("CreateProduct", viewName);
        assertTrue(model.containsAttribute("product"));
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");

        String viewName = controller.createProductPost(product, model);

        assertEquals("redirect:list", viewName);
        assertEquals(product, serviceStub.savedProduct);
    }

    @Test
    void testProductListPage() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        serviceStub.returnList = productList;

        String viewName = controller.productListPage(model);

        assertEquals("ListProduct", viewName);
        assertEquals(productList, model.getAttribute("products"));
    }

    @Test
    void testEditProductPage() {
        Product product = new Product();
        product.setProductId("123");
        serviceStub.returnProduct = product;

        String viewName = controller.editProductPage("123", model);

        assertEquals("EditProduct", viewName);
        assertEquals(product, model.getAttribute("product"));
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();
        product.setProductId("123");

        String viewName = controller.editProductPost(product);

        assertEquals("redirect:/product/list", viewName);
        assertEquals(product, serviceStub.updatedProduct);
    }

    @Test
    void testDeleteProduct() {
        String viewName = controller.deleteProduct("123");

        assertEquals("redirect:/product/list", viewName);
        assertEquals("123", serviceStub.deletedId);
    }

    static class ProductServiceStub implements ProductService {
        Product savedProduct;
        Product updatedProduct;
        String deletedId;
        List<Product> returnList;
        Product returnProduct;

        @Override
        public Product create(Product product) {
            this.savedProduct = product;
            return product;
        }

        @Override
        public List<Product> findAll() {
            return returnList;
        }

        @Override
        public Product findById(String id) {
            return returnProduct;
        }

        @Override
        public Product update(Product product) {
            this.updatedProduct = product;
            return product;
        }

        @Override
        public void delete(String id) {
            this.deletedId = id;
        }
    }
}