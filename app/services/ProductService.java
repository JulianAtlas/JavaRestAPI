package services;

import models.Product;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public Product getProduct(int id) {
        return products.get(id);
    }

    public Set<Product> listProducts() {
        return new HashSet<>(products.values());
    }

    public Product updateProduct(Product product) {
        int id = product.getId();
        if (products.containsKey(id)) {
            products.put(id, product);
            return product;
        }
        return null;
    }

    public boolean deleteProduct(int id) {
        return products.remove(id) != null;
    }

}
