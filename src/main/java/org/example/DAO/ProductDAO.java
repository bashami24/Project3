package org.example.DAO;

import org.example.Main;
import org.example.Model.Product;
import org.h2.jdbc.JdbcSQLDataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    Connection conn;
    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertProduct(Product p){
        try{
            PreparedStatement ps = conn.prepareStatement("insert into Product" +
                    " (product_id, name, price, seller) " +
                    "values (?, ?, ?, ?)");
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getPrice());
            ps.setInt(4, p.getSeller());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts(){
        List<Product> resultProducts = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from Product");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int sellerName = rs.getInt("seller");
                Product p = new Product();
                p.setId(productId);
                p.setName(name);
                p.setPrice(price);
                p.setSeller(sellerName);
                resultProducts.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        Main.log.info("Product List returned: " + resultProducts);
        return resultProducts;
    }

    public Product getProductById(int id){
        try{
            PreparedStatement ps = conn.prepareStatement("select * from Product where product_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                int sellerName = rs.getInt("seller");
                Product p = new Product();
                p.setId(productId);
                p.setName(name);
                p.setPrice(price);
                p.setSeller(sellerName);
                return p;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public void updateProduct(Product newProduct) throws JdbcSQLDataException {
        try{
            PreparedStatement ps = conn.prepareStatement("UPDATE Product " +
                    "SET name = ?, price = ?, seller = ?" +
                    "WHERE product_id = ?");

            ps.setString(1, newProduct.getName());
            ps.setInt(2, newProduct.getPrice());
            ps.setInt(3, newProduct.getSeller());
            ps.setInt(4, newProduct.getId());
            ps.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try{
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Product " +
                    "WHERE product_id = ?");
            ps.setInt(1, id);
            ps.execute();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
