package org.example.Test;


import org.example.DAO.SellerDAO;
import org.example.Exception.SellerException;
import org.example.Model.Seller;
import org.example.Service.SellerService;
import org.example.Util.ConnectionSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class SellerServiceTest {

    @Mock
    Connection conn;
    @Mock
    SellerDAO sellerDAO;
    @Mock
    SellerService sellerService;
    @Mock
    Seller seller;

    @Before
    public void setup(){
        conn = ConnectionSingleton.getConnection();
        ConnectionSingleton.resetTestDatabase();
        sellerDAO = new SellerDAO(conn);
        sellerService = new SellerService(sellerDAO);
    }

    @Test
    public void getAllEmpty(){
        List<Seller> sellerList = sellerService.getAllSellers();
        Assert.assertTrue(sellerList.isEmpty());
    }


    @Test
    public void insertSeller(){
        seller = new Seller(1, "Tommy Wiseau");
        try{
            sellerService.insertSeller(seller);
        } catch (SellerException e) {
            e.printStackTrace();
            Assert.fail();
        }

        List<Seller> sellerList = sellerService.getAllSellers();
        Seller actual = sellerList.get(0);
        Assert.assertEquals("Tommy Wiseau", actual.getName());
        Assert.assertEquals(1, actual.getId());
    }

    @Test
    public void insertDuplicateSeller() throws SellerException {
        seller = new Seller(1, "Tommy Wiseau");
        Seller dupSeller = new Seller(1, "Mark");
        sellerService.insertSeller(seller);
        sellerService.insertSeller(dupSeller);
        Assert.assertEquals(1, sellerService.getAllSellers().size());
    }

    @Test (expected = SellerException.class)
    public void insertEmptySeller() throws SellerException {
        Seller emptySeller = new Seller(1, "");
        sellerService.insertSeller(emptySeller);
    }
}
