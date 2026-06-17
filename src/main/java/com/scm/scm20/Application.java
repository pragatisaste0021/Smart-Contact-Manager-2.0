package com.scm.scm20;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.AppConstants;
import com.scm.scm20.repositories.UserRepo;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class Application implements CommandLineRunner{
	
	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
                              .ignoreIfMissing()
                              .load();

        // Put values into System properties
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

		SpringApplication.run(Application.class, args);
	}

    @Autowired
	private UserRepo userRepo;

	@Value("${server.baseUrl:http://localhost:8081}")
    private String baseUrl;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    @Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setName("admin");
		user.setEmail("admin@gmail.com");
		user.setPassword(passwordEncoder.encode("admin"));
		user.setRoleList(List.of(AppConstants.ROLE_USER));
		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setAbout("This is dummy user created initially");
		user.setPhoneVerified(true);

		userRepo.findByEmail("admin@gmail.com").ifPresentOrElse(user1 -> {},() -> {
			userRepo.save(user);
			System.out.println("user created");
		});

		// System.out.println(baseUrl);

	}

	
}
