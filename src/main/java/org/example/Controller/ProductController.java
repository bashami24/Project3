package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Header;
import org.example.Exception.ProductException;
import org.example.Exception.ProductNotFoundException;
import org.example.Exception.SellerException;
import org.example.Main;
import org.example.Model.Product;
import org.example.Model.Seller;
import org.example.Service.ProductService;
import org.example.Service.SellerService;

import java.util.*;

import static java.lang.Integer.parseInt;

public class ProductController {
    ProductService productService;
    SellerService sellerService;
    static final String SERVICENAME_PRODUCT = "Product Service";
    static final String SERVICENAME_SELLER = "Seller Service";

    public ProductController(SellerService sellerService, ProductService productService){
        this.sellerService = sellerService;
        this.productService = productService;
        productService.sellerService = sellerService;
    }

    /**
     * Define endpoints here
         * GET all sellers
         * POST add new seller
         * GET all products
         * GET product by id
         * POST add new product
         * PUT update single product by id
         * DELETE single product by id
     */
    public Javalin getAPI(){
        Javalin api = Javalin.create();

        api.get("health", context -> {
            context.result("Server is UP");
        });

        api.before (ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "*");
        });

        //Javalin to handle preflight requests (sent via OPTIONS)
        api.options("/*", ctx -> {
            ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000");
            ctx.header(Header.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header(Header.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");
            ctx.status(200);
        });
        //Seller endpoints
        api.get("/seller", context -> {
            List<Seller> sellerList = sellerService.getAllSellers();
            context.json(sellerList);
        });
        api.get("/seller/{id}", context -> {
            int id = parseInt(Objects.requireNonNull(context.pathParam("id")));
            try{
                Seller seller = sellerService.getSellerById(id);
                context.json(seller).status(200);
            } catch (NullPointerException e) {
                context.status(404);
                context.result("Seller not found");
                Main.log.error("Seller not found");
            }
        });
        api.post("/seller", context -> {
            ObjectMapper om = new ObjectMapper();
            try{
                Seller s = om.readValue(context.body(), Seller.class);
                sellerService.insertSeller(s);
                context.json(s).status(201);
                Main.log.info("Successful POST to " + SERVICENAME_SELLER + ". Status = 201");

            } catch (JsonProcessingException e) {
                context.status(400);
                Main.log.warn("JsonProcessingException - Failed POST to " + SERVICENAME_SELLER + ". Status = 400");
            } catch (SellerException e) {
                context.status(400);
                Main.log.warn("Failed POST to " + SERVICENAME_SELLER + " due to duplicate Seller. Status = 400");
            }
        });

        // Product endpoints
        api.get("/product", context -> {
            List<Product> productList = productService.getAllProducts();
            context.json(productList).status(200);
        });
        api.get("/product/{id}", context -> {
            try{
                int id = parseInt(Objects.requireNonNull(context.pathParam("id")));
                context.json(productService.getProductById(id)).status(200);
            } catch (NullPointerException e){
                context.status(404);
                Main.log.error("Product List is empty!");
            } catch (ProductException e) {
                context.status(404);
                Main.log.error(e.getMessage());
            }catch (NumberFormatException e) {
                context.result("Product id is formatted incorrectly").status(400);
                Main.log.error("Product id is formatted incorrectly");
            }
        });
        api.post("/product", context -> {
            ObjectMapper om = new ObjectMapper();
            try{
                Product p = om.readValue(context.body(), Product.class);
                productService.insertProduct(p);
                context.json(p).status(201);
                String msg = "Successful POST to " + SERVICENAME_PRODUCT + ". Status = 201";
                Main.log.info(msg);

            } catch (JsonProcessingException e) {
                context.status(400);
                Main.log.warn("Failed POST to " + SERVICENAME_PRODUCT + ". Status = 400");
            } catch (ProductException e) {
                context.status(400);
                Main.log.warn("Failed POST to " + SERVICENAME_PRODUCT + ". Status = 400");
            }
        });
        api.put("/product/{id}", context -> {
            ObjectMapper om = new ObjectMapper();
            int id = parseInt(context.pathParam("id"));
            try {
                Product p = om.readValue(context.body(), Product.class);
                p.setId(id);
                Product updatedProduct = productService.updateProduct(p);
                context.json(updatedProduct).status(201);
                Main.log.info("Successful PUT to " + SERVICENAME_PRODUCT);

            } catch (JsonProcessingException | ProductException e) {
                Main.log.warn("Failed PUT to " + SERVICENAME_PRODUCT);
            }
        });
        api.delete("/product/{id}", context -> {
            int id = parseInt(context.pathParam("id"));
            try {
                productService.deleteProduct(id);
                context.json("Success").status(200);
                Main.log.info("Successful DELETE on " + SERVICENAME_PRODUCT + ". Status = 200");

            } catch (ProductNotFoundException e) {
                context.json("Fail - Product does not exist").status(200);
                Main.log.info("Failed DELETE on " + SERVICENAME_PRODUCT + ". Status = 200");
            }
        });

        return api;
    }
}
