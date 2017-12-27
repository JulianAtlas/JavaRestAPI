package models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;



@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@AllArgsConstructor
public class Product {
    @Column(name = "ID",nullable = false, columnDefinition = "NUMBER(10)", unique = true)
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_prod_gen")
    @SequenceGenerator(name="id_prod_gen", sequenceName = "id_prod_seq", allocationSize=5)
    private int id;
    @Column(name = "NAME", nullable = false, columnDefinition = "VARCHAR(32)")
    @JsonProperty("name")
    private String name;
    @Column(name = "DESCRIPTION",  columnDefinition = "VARCHAR(32)")
    @JsonProperty("description")
    private String description;


    public Product(String name){
        this.name = name;
    }

    public Product() {
    }


    public void updateBy(Product other){
        this.id = other.id;
        if(other.name != null){
            this.name = other.name;
        }
        if(other.description != null){
            this.description = other.description;
        }
    }

    @Override
    public boolean equals(Object o){
        if (o == null) {
            return false;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return ((Product) o).name.equals(this.name) && ((Product) o).id == this.id && ( ((Product)o).description == (this.description) || ((Product) o).description.equals(this.description));
    }
}

