package services;

import javassist.NotFoundException;
import models.Product;
import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
import play.db.jpa.JPA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class ProductService {
    private int currentId;
    private Map<Integer, Product> products;
    private Semaphore createProductMutex = new Semaphore(1);

    public ProductService(){
        products = new ConcurrentHashMap<>();
        currentId = 1;
    }

    public Product createProduct(Product product) throws Exception {
        createProductMutex.acquire();
        product.setid(currentId);
        currentId++;
        createProductMutex.release();

        JPA.em().createNativeQuery("select * from PRODUCTS");

        products.put(product.getId(), product);
        return product;
    }

    public Product getProduct(int id) throws Exception{
        validateIdExists(id);
        return products.get(id);
    }

    public Set<Product> listProducts() {
        return new HashSet<>(products.values());
    }

    public Product updateProduct(Product product) throws Exception {
        int id = product.getId();
        validateIdExists(id);

        products.put(id, product);
        return product;
    }

    public void deleteProduct(int id) throws Exception{
        validateIdExists(id);

        products.remove(id);
    }

    private void validateIdExists(int id) throws Exception {
        if(!products.containsKey(id)){
            throw new NotFoundException("id not found");
        }
    }
}