package models;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Table(name = "PRODUCTS")
public class Product {
    @Column(name = "ID",nullable = false, columnDefinition = "NUMBER(10)")
    @JsonProperty("Id")
    private int id;
    @Column(name = "NAME", nullable = false, columnDefinition = "VARCHAR(32)")
    @JsonProperty("Name")
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

