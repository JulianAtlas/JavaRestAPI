import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import jdk.Exported;
import lombok.SneakyThrows;
import models.Product;
import org.junit.*;

import play.db.jpa.Transactional;
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
    public void TestWhenProductIsCreatedThenItsAddedCorrectlyToProductList() {
        running(testServer(9000, fakeApplication()), () -> {
            String name = "pelota";
            Product product = new Product(name);
            ProductService productService = new ProductService();

            assertThat(productService.listProducts().contains(product)).isFalse();


            productService.createProduct(product);
            Product searchedProduct = productService.getProduct(1);

            assertThat(productService.listProducts().contains(product)).isTrue();
        });

    }

    @Test
    @SneakyThrows
    public void TestWhenListItemsThenTheyAreListedCorrectly() {
            running(testServer(9000, fakeApplication()), new Runnable() {
                @Override
                public void run() {
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
                    Collection<Product> products = productService.listProducts();

                    //tests
                    assertThat(products.size()).isEqualTo(3);
                    assertThat(products.contains(product1)).isTrue();
                    assertThat(products.contains(product2)).isTrue();
                    assertThat(products.contains(product3)).isTrue();
                }
            });
    }


    @Test
    @SneakyThrows
    public void TestWhenProductIsDeletedThenItsRemovedFromProductList() {
        running(testServer(9000, fakeApplication()), () ->  {
                String name = "pelota";
                Product product = new Product(name);
                ProductService productService = new ProductService();
                productService.createProduct(product);

                assertThat(productService.listProducts().contains(product)).isTrue();

                productService.deleteProduct(1);

                assertThat(productService.listProducts().contains(product)).isFalse();
        });
    }

    @Test
    @SneakyThrows
    public void TestWhenDeleteProductWithNotFoundIdThenNoProductIsDeleted() {
        running(testServer(9000, fakeApplication()), () -> {
            String name = "pelota";
            Product product1 = new Product(name);
            ProductService productService = new ProductService();
            productService.createProduct(product1);


            int longOriginalDeProds = productService.listProducts().size();
            //operation
            productService.deleteProduct(999);

            assertThat(longOriginalDeProds == productService.listProducts().size()).isTrue();
        });
    }

    @Test
    @SneakyThrows
    public void TestWhenGetProductWithNotFoundIdThenReturnsNull() {
        running(testServer(9000, fakeApplication()), () -> {
            String name = "botella";
            Product product1 = new Product(name);
            ProductService productService = new ProductService();
            productService.createProduct(product1);

            //operation
            Product p = productService.getProduct(999);
            assertThat(p == null).isTrue();
        });
    }




    @Test
    @SneakyThrows
    public void TestWhenGettingProductByIdThenCorrectProductIsReturned() {
        running(testServer(9000, fakeApplication()), () -> {
            String name = "botella";
            Product product = new Product(name);
            ProductService productService = new ProductService();
            productService.createProduct(product);
            Product searchedProduct = null;
            //operation
            searchedProduct = productService.getProduct(1);

            //tests
            assertThat(searchedProduct).isEqualTo(product);
        });
    }


    @Test
    @SneakyThrows
    public void TestWhenProductIsUpdatedThenChangesAreSaved() {
        running(testServer(9000, fakeApplication()), () -> {
            String oldName = "botella";
            String newName = "pelota";
            Product product = new Product(oldName);
            ProductService productService = new ProductService();
            product = productService.createProduct(product);


            //operation
            product.setName(newName);
            productService.updateProduct(product);

            Product searchedProduct = productService.getProduct(product.getId());
            //operation

            assertThat(searchedProduct.getName()).isEqualTo(newName);
        });
    }

    @Test
    @SneakyThrows
    public void TestWhenProductIsUpdatedThoesNotCreateANewProduct() {
        running(testServer(9000, fakeApplication()), () -> {
            String oldName = "botella";
            String newName = "pelota";
            Product product = new Product(oldName);
            ProductService productService = new ProductService();
            product = productService.createProduct(product);



            //operation
            product.setName(newName);
            productService.updateProduct(product);

            assertThat(productService.listProducts().size() == 1).isTrue();
            //operation

        });
    }
}
