package spring.config;

import models.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import services.ProductService;

import static org.mockito.Mockito.mock;

@Configuration
@Profile({ "DEV", "TEST", "PAUSEDCAMPAIGN" })
public class SpringConfigurationForDevAndTest extends CommonSpringConfiguration {
	@Bean
	public ProductService productService(){
		return mock(ProductService.class);
	}
}
