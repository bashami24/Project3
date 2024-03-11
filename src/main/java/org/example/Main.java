package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import io.javalin.Javalin;
import org.example.Controller.ProductController;
import org.example.DAO.ProductDAO;
import org.example.DAO.SellerDAO;
import org.example.Service.ProductService;
import org.example.Service.SellerService;
import org.example.Util.ConnectionSingleton;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class Main {
    public static Logger log = LoggerFactory.getLogger(Main.class);
    Mockito mockito = new Mockito();
    public static void main(String[] args) {
        Connection conn = ConnectionSingleton.getConnection();
        SellerDAO sellerDAO = new SellerDAO(conn);
        ProductDAO productDAO = new ProductDAO(conn);
        SellerService sellerService = new SellerService(sellerDAO);
        ProductService productService = new ProductService(productDAO);
        ProductController productController = new ProductController(sellerService, productService);
    Javalin api = productController.getAPI();
        api.start(9017);
    }


}

