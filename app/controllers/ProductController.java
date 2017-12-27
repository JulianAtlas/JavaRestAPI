package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.mvc.Result;
import services.ProductService;

@Controller
public class ProductController extends play.mvc.Controller {

    // luego pasarlo a spring
    @Autowired
    private ProductService productService;

    public Result createProduct(){
        String responseBody;

        JsonNode requestBody = request().body().asJson();
        JsonNode nameJson = requestBody.get("name");
        JsonNode descriptionJson = requestBody.get("description");

        Result errorResponse = validationCreateRequest(nameJson);
        if (errorResponse != null) return errorResponse;


        Product p = new Product();
        p.setName(nameJson.asText());
        if(descriptionJson != null){
            p.setDescription(descriptionJson.asText());
        }

        try {
            p = productService.createProduct(p);
            responseBody = new ObjectMapper().writeValueAsString(p);
        }catch(Exception e ){
            Logger.error(e.getMessage());
            return internalServerError();
        }
        return ok(responseBody);
    }


    public Result updateProduct(){
        Product p;
        JsonNode requestBody = request().body().asJson();

        JsonNode nameInJson = requestBody.get("name");
        JsonNode idJson = requestBody.get("id");
        JsonNode descriptionJson = requestBody.get("description");
        p = new Product();


        Result errorResponse = validateUpdateRequest(nameInJson, idJson);
        if (errorResponse != null) return errorResponse;


        if (nameInJson != null){
            p.setName(nameInJson.asText());
        }
        if(descriptionJson != null){
            p.setDescription(descriptionJson.asText());
        }
        p.setId(idJson.asInt());


        try {

            p = productService.updateProduct(p);
            if(p == null)  return notFound("Not found");
            String responseBody = new ObjectMapper().writeValueAsString(p);

            return ok(responseBody);
        }catch (Exception e ){
            Logger.error(e.getMessage());
            return internalServerError();
        }
    }



    public Result listProducts(){
        String responseBody;
        try {

            responseBody = new ObjectMapper().writeValueAsString(productService.listProducts());

        }catch(Exception e ){
            Logger.error(e.getMessage());
            return internalServerError();
        }
        return ok(responseBody);
    }

    public Result getProduct(Integer id){
        String responseBody ;
        try {

            Product p = productService.getProduct(id);
            if (p == null) return notFound("Not found");
            responseBody = new ObjectMapper().writeValueAsString(p);

        }catch (Exception e ){
            Logger.error(e.getMessage());
            return internalServerError();
        }
        return ok(responseBody);
    }

    public Result deleteProduct(Integer id){
        try {

            Product productDeleted = productService.deleteProduct(id);
            if ( productDeleted == null ) {
                return notFound("Not found");
            }

        }catch (Exception e ){
            Logger.error(e.getMessage());
            return internalServerError();
        }
        return ok();
    }

/////////////////////////////////////////////////

    private Result validateUpdateRequest(JsonNode nameInJson, JsonNode idJson) {
        if(idJson == null) {
            return badRequest("Invalid Json sent - doesn't have Id field");
        }

        if (nameInJson != null && nameInJson.asText().trim().length() == 0 ){
            return badRequest("The name of the product can't be empty");
        }
        return null;
    }

    private static Result validationCreateRequest(JsonNode productNameInJson) {
        if( productNameInJson == null) {
            return badRequest("Invalid Json sent - doesn't have Name field");
        }

        if (productNameInJson.asText().trim().length() == 0){
            return badRequest("The name of the product can't be empty");
        }
        return null;
    }


}
