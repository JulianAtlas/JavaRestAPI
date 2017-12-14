package models;

public class Product {
    private int id;
    private String name;


    public Product(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setid(int id) {
        this.id = id;
    }
}

