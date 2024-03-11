package org.example.Model;

public class Product {
    private int id;
    private String name;
    private int price;
    private int seller;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeller(){
        return seller;
    }
    public void setSeller(int seller_id){
        this.seller = seller_id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", sellerId='" + seller + '\'' +
                '}';
    }
}
