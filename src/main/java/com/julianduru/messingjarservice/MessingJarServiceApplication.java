package com.julianduru.messingjarservice;

//import com.github.cloudyrock.spring.v5.EnableMongock;
import com.julianduru.fileuploader.FileUploaderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@Import({
	FileUploaderConfig.class,
})
//@EnableMongock
public class MessingJarServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessingJarServiceApplication.class, args);
	}

}
