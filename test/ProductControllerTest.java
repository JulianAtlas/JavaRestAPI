
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Product;
import org.junit.*;

import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import static play.libs.Json.newObject;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import org.mockito.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ProductControllerTest
{
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
    public void TestWhenCreateProductGetOkResponseCode() {
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();
            String productName = "pelota";
            json.put("name", productName);

            Response response = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json).get(3000L);

            assertThat(response.getStatus()).isEqualTo(OK);

            JsonNode responseJson = Json.parse(response.getBody());
            assertThat(responseJson.get("name").asText().equals(productName)).isTrue();
            assertThat(responseJson.get("id").asInt() == 5).isTrue();
        });
    }

    @Test
    public void TestWhenCreateProductWithEmptyNameGetBadRequestResponseCode(){
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();
            String productName = "";
            json.put("name", productName);

            Response response = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L);


            assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        });
    }

    @Test
    public void TestWhenCreateProductWithInvalidJsonGetBadRequestResponseCode(){
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();


            Response response = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L);


            assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        });
    }

    @Test
    public void TestWhenListProductsGetAllProductCreated(){
        running(testServer(9000, fakeApplication()), () -> {

            //operation
            Response response =  WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(response.getStatus()).isEqualTo(OK);
            String body = response.getBody();
            JsonNode json = Json.parse(body);

            for(JsonNode aJson : json){
                System.out.println("JSON " + aJson.toString());
                for (JsonNode other : aJson.get("name")){
                    System.out.println("papap" + other.toString());
                }
            }
            assertThat(json.size()).isEqualTo(4);
            assertThat(body.toString()).isEqualTo("[{\"id\":1,\"name\":\"pelota\",\"description\":\"para jugar al futbol\"},{\"id\":2,\"name\":\"soga\",\"description\":null},{\"id\":3,\"name\":\"mesa\",\"description\":\"para tomar el te\"},{\"id\":4,\"name\":\"cama\",\"description\":\"\"}]");
        });
    }

    @Test
    public void TestWhenGetProductReturnsCorrectOne(){
        running(testServer(9000, fakeApplication()), () -> {

            //operation
            Response response1 =  WS.url("http://localhost:9000/products/1").setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(response1.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(response1.getBody()).get("name").asText()).isEqualTo(p1.getName());
            assertThat(Json.parse(response1.getBody()).get("id").asInt()).isEqualTo(p1.getId());
            assertThat(Json.parse(response1.getBody()).get("name").asText()).isEqualTo(p1.getName());

        });
    }

    @Test
    public void TestWhenGetProductWithInexistingIdThenReturnsNotFound(){
        running(testServer(9000, fakeApplication()), () -> {

            //operation
            Response response1 =  WS.url("http://localhost:9000/products/999").setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(response1.getStatus()).isEqualTo(NOT_FOUND);

        });
    }

    @Test
    public void TestWhenUpdateAProductNameThenChangesCorrectly(){
        running(testServer(9000, fakeApplication()), () -> {
            String productNewName = "puerta";

            ObjectNode jsonNewProduct = newObject();
            jsonNewProduct.put("name", productNewName);
            jsonNewProduct.put("id", p1.getId());

            //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);
            Response responseGet = WS.url("http://localhost:9000/products/"+p1.getId()).setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(responseUpdate.getStatus()).isEqualTo(OK);
            assertThat(responseGet.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(responseUpdate.getBody()).get("name").asText()).isEqualTo(productNewName);
            assertThat(Json.parse(responseUpdate.getBody()).get("id").asInt() == p1.getId()).isTrue();
            assertThat((Json.parse(responseUpdate.getBody()).get("description") == null && p1.getDescription() == null)  || Json.parse(responseUpdate.getBody()).get("description").asText().equals(p1.getDescription())).isTrue();


        });
    }

    @Test
    public void TestWhenUpdateAProductDescriptionThenChangesCorrectly(){
        running(testServer(9000, fakeApplication()), () -> {
            String productNewDescription = "para nose";

            ObjectNode jsonNewProduct = newObject();
            jsonNewProduct.put("description", productNewDescription);
            jsonNewProduct.put("id", p1.getId());

            //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);
            Response responseGet = WS.url("http://localhost:9000/products/"+p1.getId()).setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(responseUpdate.getStatus()).isEqualTo(OK);
            assertThat(responseGet.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(responseUpdate.getBody()).get("name").asText()).isEqualTo(p1.getName());
            assertThat(Json.parse(responseUpdate.getBody()).get("id").asInt() == p1.getId()).isTrue();
            assertThat(Json.parse(responseUpdate.getBody()).get("description").asText().equals(productNewDescription)).isTrue();

        });
    }

    @Test
    public void TestWhenTryUpdateProductThatNotExistThenReturnsNotFound(){
        running(testServer(9000, fakeApplication()), () -> {

            ObjectNode jsonNewProduct = newObject();
            jsonNewProduct.put("name", "pelota");
            jsonNewProduct.put("id", 999);

             //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);

            //asserts


            assertThat(responseUpdate.getStatus()).isEqualTo(NOT_FOUND);

        });
    }

    @Test
    public void TestWhenTryUpdateProductWithEmptyNameThenReturnsBadRequest(){
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode jsonNewProduct = newObject();

            String productNewName = "";

            jsonNewProduct.put("name", productNewName);
            jsonNewProduct.put("id", p1.getId());


            //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);

            //asserts


            assertThat(responseUpdate.getStatus()).isEqualTo(BAD_REQUEST);

        });
    }

    @Test
    public void TestWhenTryUpdateProductWithInvalidJsonThenReturnsBadRequest(){
        running(testServer(9000, fakeApplication()), () -> {

            ObjectNode jsonOldProduct = newObject();
            ObjectNode jsonNewProduct = newObject();

            String productOldName = "pelota";
            String productNewName = "";

            jsonOldProduct.put("name", productOldName);

            JsonNode jsonProduct1 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(jsonOldProduct.toString()).get(3000L)).getBody());

            //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);

            //asserts


            assertThat(responseUpdate.getStatus()).isEqualTo(BAD_REQUEST);

        });
    }

    @Test
    public void TestWhenDeleteProductThenCantGetItAnymore(){
        running(testServer(9000, fakeApplication()), () -> {

            ObjectNode json = newObject();
            String productName1 = "pelota";

            json.put("name", productName1);

            int id = p1.getId();
            Response responseDelete = WS.url("http://localhost:9000/products/"+ id).delete().get(3000L);
            Response responseGetAfterDelete = WS.url("http://localhost:9000/products/"+ id).setHeader("Content-Type", "application/json").get().get(3000L);


            //asserts
            assertThat(responseDelete.getStatus()).isEqualTo(OK);
            assertThat(responseGetAfterDelete.getStatus()).isEqualTo(NOT_FOUND);

        });
    }


}
