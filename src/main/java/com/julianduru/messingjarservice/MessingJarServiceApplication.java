package com.julianduru.messingjarservice;

//import com.github.cloudyrock.spring.v5.EnableMongock;
import com.julianduru.fileuploader.FileUploaderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableReactiveMongoRepositories
@Import({
	FileUploaderConfig.class,
})
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
//@EnableMongock
public class MessingJarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessingJarServiceApplication.class, args);
	}

}
