package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import models.Product;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Result;
import services.ProductService;
import utils.RunTimeNotFoundException;

@Controller
public class ProductController extends play.mvc.Controller {

    // luego pasarlo a spring
    private static ProductService productService = new ProductService();

    public static Result createProduct(){
        String responseBody;

        JsonNode requestBody = request().body().asJson();
        JsonNode productNameInJson = requestBody.get("Name");
        if( productNameInJson == null) return badRequest("Invalid Json sent - doesn't have Name field");


        String  productName = productNameInJson.asText();
        if (productName == null || productName.trim().length() == 0){
            return badRequest("The name of the product can't be empty");
        }

        Product p = new Product(productName);
        try {
            p = productService.createProduct(p);
            responseBody = new ObjectMapper().writeValueAsString(p);
        }catch(Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok(responseBody);
    }

    public static Result updateProduct(){
        Product p;
        String responseBody;
        JsonNode requestBody = request().body().asJson();

        JsonNode productNameInJson = requestBody.get("Name");
        if( productNameInJson == null) return badRequest("Invalid Json sent - doesn't have Name field ");
         String  productNewName = productNameInJson.asText();

        JsonNode idInJson = requestBody.get("Id");
        if(idInJson == null) return badRequest("Invalid Json sent - doesn't have Id field");
        int productId = requestBody.get("Id").asInt();

        if (productNewName == null || productNewName.trim().length() == 0 ){
            return badRequest("The name of the product can't be empty");
        }

        p = new Product();
        p.setName(productNewName);
        p.setId(productId);
        try {

            p = productService.updateProduct(p);
            if(p == null)  return notFound("Not found");
            responseBody = new ObjectMapper().writeValueAsString(p);

        }catch (Exception e ){
            return internalServerError(e.getMessage());
        }

        return ok(responseBody);
    }

    public static Result listProducts(){
        String responseBody;
        try {

            responseBody = new ObjectMapper().writeValueAsString(productService.listProducts());

        }catch(Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok(responseBody);
    }

    public static Result getProduct(Integer id){
        String responseBody ;
        try {

            Product p = productService.getProduct(id);
            if (p == null) return notFound("Not found");
            responseBody = new ObjectMapper().writeValueAsString(p);

        }catch (Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok(responseBody);
    }

    public static Result deleteProduct(Integer id){
        try {

            Product productDeleted = productService.deleteProduct(id);
            if ( productDeleted == null ) {
                return notFound("Not found");
            }

        }catch (Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok();
    }

}
