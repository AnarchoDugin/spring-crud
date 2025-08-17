package com.zakuwarrior.crudhttp.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationContextTests {
	private final ApplicationContext applicationContext;

	@Autowired
	public ApplicationContextTests(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Test
	void contextLoads() {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void beansInitialized() {
		List<String> beanDefinitions = Arrays.asList("productController", "productService", "jdbcProductRepository");
		assertThat(applicationContext.getBeanDefinitionNames()).containsAll(beanDefinitions);
	}
}
