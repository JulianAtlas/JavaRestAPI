package services;


import lombok.SneakyThrows;
import models.Product;
import play.db.jpa.JPA;

import javax.persistence.Query;
import java.util.*;


public class ProductService {

    @SneakyThrows
    public Product createProduct(Product product){
        JPA.withTransaction(()->JPA.em().persist(product));

        return product;
    }

    @SneakyThrows
    public Product getProduct(int id){

        return JPA.withTransaction(()->{
            Product p = JPA.em().find(Product.class, id);
            if(p != null) return p;
            else return null;
        });
    }

    @SneakyThrows
    public Collection<Product> listProducts() {

        return JPA.withTransaction(
                ()->{
                    Query query = JPA.em().createNativeQuery("select * from PRODUCTS", Product.class);
                    return (List<Product>)query.getResultList();
                });
    }

    @SneakyThrows
    public Product updateProduct(Product product) {
        int id = product.getId();

        return JPA.withTransaction(()->{
            Product p = JPA.em().find(Product.class, id);
            if(p != null){
                p.updateBy(product);
                JPA.em().persist(p);
                return p;
            }
            else return null;
        });
    }

    @SneakyThrows
    public Product deleteProduct(int id){

        return JPA.withTransaction(()->{
            Product p = JPA.em().find(Product.class, id);
            if(p != null){
                JPA.em().remove(p);
                return p;
            }
            else return null;
        });
    }

}