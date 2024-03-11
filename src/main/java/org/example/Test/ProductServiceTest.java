package org.example.Test;

import org.example.DAO.ProductDAO;
import org.example.DAO.SellerDAO;
import org.example.Exception.ProductException;
import org.example.Exception.ProductNotFoundException;
import org.example.Exception.SellerException;
import org.example.Model.Product;
import org.example.Model.Seller;
import org.example.Service.ProductService;
import org.example.Service.SellerService;
import org.example.Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.util.List;

public class ProductServiceTest {
    @Mock
    ProductService productService;
    @Mock
    ProductDAO productDAO;

    Seller testSeller;

    @Mock
    Connection conn;
    @Mock
    SellerDAO sellerDAO;
    @Mock
    SellerService sellerService;
    @Mock
    Seller seller;

    @Before
    public void setup() throws SellerException {
        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        sellerDAO = new SellerDAO(conn);
        sellerService = new SellerService(sellerDAO);
        productDAO = new ProductDAO(conn);
        productService = new ProductService(productDAO);
        productService.sellerService = sellerService;

        testSeller = new Seller(7, "Tommy Wiseau");
        sellerService.insertSeller(testSeller);
    }

    @Test
    public void emptyAtStart(){
        List<Product> productList = productService.getAllProducts();
        Assert.assertTrue(productList.isEmpty());
    }

    @Test
    public void insertProduct(){
        String name = "Football";
        int price = 15;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(7);

        try{
            productService.insertProduct(testProduct);
        } catch (ProductException e) {
            e.printStackTrace();
            Assert.fail("Exception should not be thrown");
        }

        List<Product> productList = productService.getAllProducts();
        Product actual = productList.get(0);
        Assert.assertTrue(actual.getId() > 0);
        Assert.assertEquals(name, actual.getName());
        Assert.assertEquals(price, actual.getPrice());
        Assert.assertEquals(testSeller.getId(), actual.getSeller());
    }

    @Test
    public void insertProductEmptyName(){
        String name = "";
        int price = 15;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(7);

        try{
            productService.insertProduct(testProduct);
            Assert.fail();
        } catch (ProductException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertProductNegativePrice(){
        String name = "Football";
        int price = -15;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(7);

        try{
            productService.insertProduct(testProduct);
            Assert.fail();
        } catch (ProductException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertProductInvalidSeller(){
        String name = "Football";
        int price = 15;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(3);

        try{
            productService.insertProduct(testProduct);
            Assert.fail();
        } catch (ProductException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getProductById(){
        insertProduct();

        List<Product> productList = productService.getAllProducts();
        int id = productList.get(0).getId();

        try{
            productService.getProductById(id);
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void getProductByIdNotFound(){
        insertProduct();

        List<Product> productList = productService.getAllProducts();

        try{
            productService.getProductById(42);
            Assert.fail();
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateProduct(){

        String name = "Gun";
        int price = 2000;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(7);

        insertProduct();
        List<Product> productList = productService.getAllProducts();
        testProduct.setId(productList.get(0).getId());
       try{
            productService.updateProduct(testProduct);

        } catch (ProductException e) {
            e.printStackTrace();
            Assert.fail();
        }
        productList = productService.getAllProducts();
        Product actual = productList.get(0);
        Assert.assertTrue(actual.getId() > 0);
        Assert.assertEquals(name, actual.getName());
        Assert.assertEquals(price, actual.getPrice());
        Assert.assertEquals(testSeller.getId(), actual.getSeller());
    }

    @Test
    public void updateProductEmptyName(){

        String name = "";
        int price = 2000;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(7);

        insertProduct();

        try{
            productService.updateProduct(testProduct);
            Assert.fail();

        } catch (ProductException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateProductNegativePrice(){

        String name = "Gun";
        int price = -2000;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(7);

        insertProduct();

        try{
            productService.updateProduct(testProduct);
            Assert.fail();

        } catch (ProductException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateProductInvalidSeller(){

        String name = "Gun";
        int price = 2000;

        Product testProduct = new Product();
        testProduct.setName(name);
        testProduct.setPrice(price);
        testProduct.setSeller(79);

        insertProduct();
        List<Product> productList = productService.getAllProducts();
        testProduct.setId(productList.get(0).getId());

        try{
            productService.updateProduct(testProduct);

        } catch (ProductException e) {
            e.printStackTrace();
        }
        productList = productService.getAllProducts();
        Product actual = productList.get(0);
        Assert.assertTrue(actual.getId() > 0);
        Assert.assertEquals("Football", actual.getName());
        Assert.assertEquals(15, actual.getPrice());
        Assert.assertEquals(7, actual.getSeller());
    }

    @Test
    public void deleteProduct(){
        insertProduct();

        List<Product> productList = productService.getAllProducts();
        int id = productList.get(0).getId();

        try{
            productService.deleteProduct(id);

        } catch (ProductException e) {
            e.printStackTrace();
            Assert.fail();
        }
        productList = productService.getAllProducts();
        Assert.assertTrue(productList.isEmpty());
    }
}
