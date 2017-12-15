import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import jdk.Exported;
import lombok.SneakyThrows;
import models.Product;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import services.ProductService;

import static org.fest.assertions.Fail.fail;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ProductServiceTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }


    @Test
    @SneakyThrows
    public void TestWhenProductIsCreatedThenItsAddedCorrectlyToProductList() {
        String name = "pelota";
        Product product = new Product(name);
        ProductService productService = new ProductService();

        assertThat(productService.listProducts().contains(product)).isFalse();

        productService.createProduct(product);
        Product searchedProduct = productService.getProduct(1);

        assertThat(productService.listProducts().contains(product)).isTrue();


    }

    @Test
    @SneakyThrows
    public void TestWhenListItemsThenTheyAreListedCorrectly() {
        String name1 = "pelota";
        String name2 = "botella";
        String name3 = "computadora";

        Product product1 = new Product(name1);
        Product product2 = new Product(name2);
        Product product3 = new Product(name3);

        ProductService productService = new ProductService();
        productService.createProduct(product1);
        productService.createProduct(product2);
        productService.createProduct(product3);

        //operation
        Set<Product> products = productService.listProducts();

        //tests
        assertThat(products.size()).isEqualTo(3);
        assertThat(products.contains(product1)).isTrue();
        assertThat(products.contains(product2)).isTrue();
        assertThat(products.contains(product3)).isTrue();
    }


    @Test
    @SneakyThrows

    public void TestWhenProductIsDeletedThenItsRemovedFromProductList() {
        String name = "pelota";
        Product product = new Product(name);
        ProductService productService = new ProductService();
        productService.createProduct(product);

        assertThat(productService.listProducts().contains(product)).isTrue();

        productService.deleteProduct(1);

        assertThat(productService.listProducts().contains(product)).isFalse();

    }

    @Test(expected = Exception.class)
    @SneakyThrows
    public void TestWhenDeleteProductWithNotFoundIdThenNoProductIsDeleted() {
        String name = "pelota";
        Product product1 = new Product(name);
        ProductService productService = new ProductService();
        productService.createProduct(product1);

        //operation
        productService.deleteProduct(999);
    }

    @Test(expected = Exception.class)
    @SneakyThrows
    public void TestWhenGetProductWithNotFoundIdThenThrowsError() {
        String name = "botella";
        Product product1 = new Product(name);
        ProductService productService = new ProductService();
        productService.createProduct(product1);

        //operation
        productService.getProduct(999);
    }




    @Test
    @SneakyThrows
    public void TestWhenGettingProductByIdThenCorrectProductIsReturned() {
        String name = "botella";
        Product product = new Product(name);
        ProductService productService = new ProductService();
        productService.createProduct(product);
        Product searchedProduct = null;
        //operation
        searchedProduct = productService.getProduct(1);

        //tests
        assertThat(searchedProduct).isEqualTo(product);
    }


    @Test
    @SneakyThrows
    public void TestWhenProductIsUpdatedThenChangesAreSaved() {
        String name = "botella";
        String newName = "pelota";
        Product product = new Product(name);
        ProductService productService = new ProductService();
        productService.createProduct(product);



        //operation
        product.setName(newName);
        productService.updateProduct(product);
        Product searchedProduct = null;
        //operation
        searchedProduct = productService.getProduct(1);

        assertThat(searchedProduct.getName()).isEqualTo(newName);
    }
}
