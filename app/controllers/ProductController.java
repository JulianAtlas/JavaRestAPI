package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import models.Product;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.stereotype.Controller;
import play.db.jpa.Transactional;
import play.mvc.Result;
import services.ProductService;

@Controller
public class ProductController extends play.mvc.Controller {

    // luego pasarlo a sprin
    private static ProductService productService = new ProductService();


    @Transactional
    public static Result createProduct(){
        JsonNode body = request().body().asJson();

        Product p = new Product(body.get("Name").asText());

        String jsonInString;
        if (p.getName().trim().length() == 0 ){
            return badRequest("The name of the product can't be empty");
        }
        try {
            Product ps = productService.createProduct(p); // aca hay excepcion
            jsonInString = new ObjectMapper().writeValueAsString(ps);
        }catch(Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok(jsonInString);
    }

    public static Result updateProduct(){
        String jsonRequest = request().body().asJson().asText();
        Product p;
        String jsonInString;

        try {
            ObjectMapper mapper = new ObjectMapper();
            p= mapper.readValue( jsonRequest , Product.class);
        }catch (Exception e){
            return badRequest(e.getMessage());
        }
        if (p.getName().trim().length() == 0 ){
            return badRequest("The name of the product can't be empty");
        }


        try {
            jsonInString = new ObjectMapper().writeValueAsString(productService.updateProduct(p));
        }catch(NotFoundException e ){
            return badRequest(e.getMessage());
        }catch (Exception e ){
            return internalServerError(e.getMessage());
        }

        return ok(jsonInString);
    }

    public static Result listProducts(){
        String jsonInString;
        try {
            jsonInString = new ObjectMapper().writeValueAsString(productService.listProducts());
        }catch(Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok(jsonInString);
    }

    public static Result getProduct(Integer id){
        String productInString ;
        try {
            productInString = new ObjectMapper().writeValueAsString(productService.getProduct(id));
        }catch(NotFoundException e ){
            return badRequest(e.getMessage());
        }catch (Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok(productInString);
    }

    public static Result deleteProduct(Integer id){
        try {
            productService.deleteProduct(id);
        }catch(NotFoundException e ){
            return badRequest(e.getMessage());
        }catch (Exception e ){
            return internalServerError(e.getMessage());
        }
        return ok();
    }

}
