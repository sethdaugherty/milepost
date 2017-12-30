package io.sethdaugherty.milepost;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

@SpringBootApplication
public class MilePostApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilePostApplication.class, args);
	}
}
