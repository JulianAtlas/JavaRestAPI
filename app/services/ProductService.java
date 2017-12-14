package services;

import models.Product

import java.util.HashMap;
import java.util.Map;

public class ProductService {
    private int currentId;
    private Map<Integer, Product> products;

public ProductService(){
    products = new HashMap<>();
    currentId = 1;
}

public void createProduct(Product product){
    product.setid(currentId);
    products.put(currentId, product);
    currentId++;
}


}
