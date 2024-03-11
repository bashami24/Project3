package org.example.Service;
import org.example.DAO.ProductDAO;
import org.example.Exception.ProductException;
import org.example.Exception.ProductFormatException;
import org.example.Exception.ProductNotFoundException;
import org.example.Main;
import org.example.Model.Product;
import org.h2.jdbc.JdbcSQLDataException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    ProductDAO productDAO;
    List<Product> productList;
    public SellerService sellerService;

    public ProductService(ProductDAO productDAO){
        this.productDAO = productDAO;
        productList = new ArrayList<>();
        Main.log.info("New Product List created");
    }
    public List<Product> getAllProducts(){
        return productDAO.getAllProducts();
    }
    public void insertProduct(Product product) throws ProductException {
        //when Product is created, set the ID to a randomized value
        product.setId((int) (Math.random() * 100000000));
        int sellerId = product.getSeller();
        if(product.getName() == null || product.getName().isEmpty()){
            Main.log.warn("Product name is empty");
            throw new ProductException("Product name is empty");
        }
        if(product.getPrice() <= 0){
            Main.log.warn("Product price is less than or equal to 0");
            throw new ProductException("Product price is less than or equal to 0");
        }
        if(!sellerService.isVerifiedSeller(sellerId)){
            Main.log.warn("Seller with id " + sellerId + " is not a verified Seller");
            throw new ProductException("Seller " + sellerId + " is not a verified Seller");
        }

        if(productDAO.getProductById(product.getId()) == null){
            productDAO.insertProduct(product);
            Main.log.info("Product added: " + product.toString());
        }
    }

    public Product getProductById(int id) throws ProductNotFoundException {
        Product product = productDAO.getProductById(id);
        if(product == null){
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        return product;
    }

    public Product updateProduct(Product product) throws ProductException {
        if(product.getName() == null || product.getName().isEmpty()){
            Main.log.warn("Product name is empty");
            throw new ProductFormatException("Product name is empty");
        }
        if(product.getPrice() <= 0){
            Main.log.warn("Product price is less than or equal to 0");
            throw new ProductFormatException("Product price is less than or equal to 0");
        }

        try {
            productDAO.updateProduct(product);
            Main.log.info("Product updated. New values: " + product);

        }catch (JdbcSQLDataException e){
            e.printStackTrace();
            throw new ProductException("Invalid Seller");
        }

        return product;
    }

    public void deleteProduct(int id) throws ProductNotFoundException {

        productDAO.delete(id);
    }
}
