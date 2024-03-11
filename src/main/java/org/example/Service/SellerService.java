package org.example.Service;

import org.example.DAO.SellerDAO;
import org.example.Exception.SellerException;
import org.example.Main;
import org.example.Model.Seller;

import java.util.*;

public class SellerService {

    Set<String> sellerNames;
    List<Seller> sellerList;
    SellerDAO sellerDAO;

    public SellerService(SellerDAO sellerDAO){
        this.sellerDAO = sellerDAO;
        sellerList = new ArrayList<>();
        sellerNames = new LinkedHashSet<>();
        Main.log.info("New Seller List created");
    }
    public List<Seller> getAllSellers(){
        List<Seller> sellerList = sellerDAO.getaAllSellers();
        Main.log.info("Seller List returned: " + sellerList);
        return sellerList;
    }

    public Seller getSellerById(int id){
        return sellerDAO.getSellerById(id);
    }

    public void insertSeller(Seller seller) throws SellerException {
        if(seller.getName().isEmpty()){
            Main.log.warn("Seller name is empty");
            throw new SellerException("Seller name is empty");
        }
        try{
            sellerDAO.insertSeller(seller);

        }catch (SellerException e){
            Main.log.warn(e.getMessage());
            throw new SellerException(e.getMessage());
        }
    }

    public boolean isVerifiedSeller(int sellerId){
        return sellerDAO.isVerifiedSeller(sellerId);
    }
}