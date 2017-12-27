package spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import services.ProductService;

@Configuration
@Profile({ "PROD", "TEST" })
public class SpringConfigurationForProd extends CommonSpringConfiguration {
	@Bean
	public ProductService productService(){
		return new ProductService();
	}
}
