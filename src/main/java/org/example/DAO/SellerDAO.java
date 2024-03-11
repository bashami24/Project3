package org.example.DAO;

import org.example.Exception.SellerException;
import org.example.Main;
import org.example.Model.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SellerDAO {
    Connection conn;
    public SellerDAO(Connection conn){
        this.conn = conn;
    }
    public List<Seller> getaAllSellers(){
        List<Seller> sellerResults = new ArrayList<>();
        try{
            PreparedStatement ps = conn.prepareStatement("select * from Seller");
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                int sellerId = resultSet.getInt("seller_id");
                String sellerName = resultSet.getString("name");
                Seller s = new Seller();
                s.setId(sellerId);
                s.setName(sellerName);
                sellerResults.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sellerResults;
    }

    public void insertSeller(Seller s) throws SellerException {
        try{
            PreparedStatement ps = conn.prepareStatement("insert into " +
                    "Seller (seller_id, name) values (?, ?)");
            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.executeUpdate();
            Main.log.info("Seller added: " + s);
        }catch(SQLException e){
            e.printStackTrace();
            throw new SellerException("Seller id invalid");
        }
    }

    public Seller getSellerById(int id){
        try{
            PreparedStatement ps = conn.prepareStatement(
                    "select * from Seller where seller_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int sellerId = rs.getInt("seller_id");
                String name = rs.getString("name");
                Seller s = new Seller();
                s.setId(sellerId);
                s.setName(name);
                return s;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean isVerifiedSeller(int id) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "select * from Seller where seller_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // If id is in the seller list, return true
                // if not, return false
                return id == rs.getInt("seller_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
