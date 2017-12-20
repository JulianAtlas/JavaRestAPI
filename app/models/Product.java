package models;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;



@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Column(name = "ID",nullable = false, columnDefinition = "NUMBER(10)", unique = true)
    @JsonProperty("Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_prod_gen")
    @SequenceGenerator(name="id_prod_gen", sequenceName = "id_prod_seq", allocationSize=1)
    private int id;
    @Column(name = "NAME", nullable = false, columnDefinition = "VARCHAR(32)")
    @JsonProperty("Name")
    private String name;
    @Column(name = "DESCRIPTION", nullable = true, columnDefinition = "VARCHAR(32)")
    @JsonProperty("Name")
    private String description;


    public Product(String name){
        this.name = name;
    }

    public Product() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void updateBy(Product other){
        this.id = other.id;
        this.name = other.name;
    }

    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (!(o instanceof Product)) return false;
        return ((Product) o).name.equals(this.name) && ((Product) o).id == this.id;
    }
}

