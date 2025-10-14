package com.ufersa.CodePublish;

import com.ufersa.CodePublish.commons.domain.services.InitialMetadataPopulationService;
import com.ufersa.CodePublish.commons.domain.services.TagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CodePublishApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodePublishApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			InitialMetadataPopulationService service
	) {
		return args -> {
			service.persistInitialTags();
			service.persistInitialCategories();
			service.persistInitialProgramingLanguages();
		};
	}
}
