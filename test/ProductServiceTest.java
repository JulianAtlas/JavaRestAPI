import java.util.*;

import lombok.SneakyThrows;
import models.Product;
import org.junit.*;
import services.ProductService;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import org.mockito.*;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ProductServiceTest {

    Product p1;
    Product p2;
    Product p3;
    Product p4;



    @Before
    public void createProductsInDB (){
        p1 = new Product();
        p1.setName("pelota");
        p1.setId(1);
        p1.setDescription("para jugar al futbol");

        p2 = new Product();
        p2.setName("soga");
        p2.setId(2);

        p3 = new Product();
        p3.setName("mesa");
        p3.setId(3);
        p3.setDescription("para tomar el te");


        p4 = new Product();
        p4.setName("cama");
        p4.setId(4);
        p4.setDescription("");

    }


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
                    ProductService productService = new ProductService();

                    //operation
                    Collection<Product> products = productService.listProducts();

                    //tests
                    assertThat(products.size()).isEqualTo(4);
                    assertThat(products.contains(p1));
                    assertThat(products.contains(p2));
                    assertThat(products.contains(p3));
                    assertThat(products.contains(p4));
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
            ProductService productService = new ProductService();

            Product searchedProduct = null;
            //operation
            searchedProduct = productService.getProduct(p1.getId());

            //tests
            assertThat(searchedProduct).isEqualTo(p1);
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
            String newName = "pelota";
            ProductService productService = new ProductService();

            p1.setName(newName);
            productService.updateProduct(p1);

            assertThat(productService.getProduct(p1.getId()).getName().equals(newName));
            //operation

        });
    }
}
