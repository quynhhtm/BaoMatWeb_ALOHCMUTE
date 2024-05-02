package hcmute.alohcmute;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hcmute.alohcmute.services.FilesStorageService;
import jakarta.annotation.Resource;

@SpringBootApplication
public class SpringBootThymeleafJpaAlohcmuteApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootThymeleafJpaAlohcmuteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		storageService.init();
	}
}
