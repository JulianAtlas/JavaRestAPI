
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.*;

import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import static play.libs.Json.newObject;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ProductControllerTest
{
    @Test
    public void TestWhenCreateProductGetOkResponseCode() {
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();
            String productName = "pelota";
            json.put("Name", productName);

            Response response = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json).get(3000L);


            assertThat(response.getStatus()).isEqualTo(OK);

            JsonNode responseJson = Json.parse(response.getBody());
            assertThat(responseJson.get("Name").asText().equals(productName)).isTrue();
            assertThat(responseJson.get("Id").asInt() == 1).isTrue();
        });
    }

    @Test
    public void TestWhenCreateProductWithEmptyNameGetBadRequestResponseCode(){
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();
            String productName = "";
            json.put("Name", productName);

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
            ObjectNode json = newObject();
            String productName1 = "pelota";
            String productName2 = "soga";
            String productName3 = "mesa";

            json.put("Name", productName1);
            WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L);

            json.put("Name", productName2);
            WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L);

            json.put("Name", productName3);
            WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L);

            //operation
            Response response =  WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(response.getStatus()).isEqualTo(OK);
            assertThat(response.getBody().toString()).isEqualTo("[{\"Id\":1,\"Name\":\"pelota\"},{\"Id\":2,\"Name\":\"soga\"},{\"Id\":3,\"Name\":\"mesa\"}]");
        });
    }

    @Test
    public void TestWhenGetProductReturnsCorrectOne(){
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();
            String productName1 = "pelota";
            String productName2 = "soga";
            String productName3 = "mesa";

            json.put("Name", productName1);
            JsonNode jsonProduct1 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L)).getBody());

            json.put("Name", productName2);
            JsonNode jsonProduct2 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L)).getBody());

            json.put("Name", productName3);
            JsonNode jsonProduct3 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L)).getBody());

            //operation
            Response response1 =  WS.url("http://localhost:9000/products/1").setHeader("Content-Type", "application/json").get().get(3000L);
            Response response2 =  WS.url("http://localhost:9000/products/2").setHeader("Content-Type", "application/json").get().get(3000L);
            Response response3 =  WS.url("http://localhost:9000/products/3").setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(response1.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(response1.getBody()).toString()).isEqualTo(jsonProduct1.toString());
            assertThat(response2.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(response2.getBody()).toString()).isEqualTo(jsonProduct2.toString());
            assertThat(response3.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(response3.getBody()).toString()).isEqualTo(jsonProduct3.toString());
        });
    }

    @Test
    public void TestWhenGetProductWithInexistingIdThenReturnsNotFound(){
        running(testServer(9000, fakeApplication()), () -> {
            ObjectNode json = newObject();
            String productName1 = "pelota";
            String productName2 = "soga";
            String productName3 = "mesa";

            json.put("Name", productName1);
            JsonNode jsonProduct1 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L)).getBody());

            json.put("Name", productName2);
            JsonNode jsonProduct2 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L)).getBody());

            json.put("Name", productName3);
            JsonNode jsonProduct3 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L)).getBody());

            //operation
            Response response1 =  WS.url("http://localhost:9000/products/999").setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(response1.getStatus()).isEqualTo(NOT_FOUND);

        });
    }

    @Test
    public void TestWhenUpdateAProductThenChangesCorrectly(){
        running(testServer(9000, fakeApplication()), () -> {

            ObjectNode jsonOldProduct = newObject();
            ObjectNode jsonNewProduct = newObject();

            String productOldName = "pelota";
            String productNewName = "soga";

            jsonOldProduct.put("Name", productOldName);

            JsonNode jsonProduct1 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(jsonOldProduct.toString()).get(3000L)).getBody());

            jsonNewProduct.put("Name", productNewName);
            jsonNewProduct.put("Id", jsonProduct1.get("Id").asInt());


            //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);
            Response responseGet = WS.url("http://localhost:9000/products/"+jsonProduct1.get("Id").asInt()).setHeader("Content-Type", "application/json").get().get(3000L);

            //asserts

            assertThat(responseUpdate.getStatus()).isEqualTo(OK);
            assertThat(responseGet.getStatus()).isEqualTo(OK);
            assertThat(Json.parse(responseUpdate.getBody()).get("Name").asText()).isEqualTo(productNewName);
            assertThat(Json.parse(responseUpdate.getBody()).get("Id").asInt() == jsonProduct1.get("Id").asInt()).isTrue();
            assertThat(responseUpdate.getBody().toString()).isEqualTo(responseGet.getBody().toString());

        });
    }

    @Test
    public void TestWhenTryUpdateProductThatNotExistThenReturnsNotFound(){
        running(testServer(9000, fakeApplication()), () -> {

            ObjectNode jsonOldProduct = newObject();
            ObjectNode jsonNewProduct = newObject();

            String productOldName = "pelota";
            String productNewName = "soga";

            jsonOldProduct.put("Name", productOldName);

            JsonNode jsonProduct1 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(jsonOldProduct.toString()).get(3000L)).getBody());

            jsonNewProduct.put("Name", productNewName);
            jsonNewProduct.put("Id", 9999);


            //operation
            Response responseUpdate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").put(jsonNewProduct.toString()).get(3000L);

            //asserts


            assertThat(responseUpdate.getStatus()).isEqualTo(NOT_FOUND);

        });
    }

    @Test
    public void TestWhenTryUpdateProductWithEmptyNameThenReturnsBadRequest(){
        running(testServer(9000, fakeApplication()), () -> {

            ObjectNode jsonOldProduct = newObject();
            ObjectNode jsonNewProduct = newObject();

            String productOldName = "pelota";
            String productNewName = "";

            jsonOldProduct.put("Name", productOldName);

            JsonNode jsonProduct1 = Json.parse((WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(jsonOldProduct.toString()).get(3000L)).getBody());

            jsonNewProduct.put("Name", productNewName);
            jsonNewProduct.put("Id", jsonProduct1.get("Id").asInt());


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

            jsonOldProduct.put("Name", productOldName);

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

            json.put("Name", productName1);
            Response responseCreate = WS.url("http://localhost:9000/products").setHeader("Content-Type", "application/json").post(json.toString()).get(3000L);


            JsonNode jsonProduct1 = Json.parse(responseCreate.getBody());
            int id = jsonProduct1.get("Id").asInt();
            Response responseGetBeforeDelete = WS.url("http://localhost:9000/products/"+ id).setHeader("Content-Type", "application/json").get().get(3000L);
            Response responseDelete = WS.url("http://localhost:9000/products/"+ id).setHeader("Content-Type", "application/json").delete().get(3000L);
            Response responseGetAfterDelete = WS.url("http://localhost:9000/products/"+ id).setHeader("Content-Type", "application/json").get().get(3000L);


            //asserts
            assertThat(responseCreate.getStatus()).isEqualTo(OK);
            assertThat(responseGetBeforeDelete.getStatus()).isEqualTo(OK);
            assertThat(responseDelete.getStatus()).isEqualTo(OK);
            assertThat(responseGetAfterDelete.getStatus()).isEqualTo(NOT_FOUND);

        });
    }


}
