package codepred;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import codepred.user.service.UserService;

@SpringBootApplication
@RequiredArgsConstructor
public class TakeMeWithBackend implements CommandLineRunner {

  final UserService userService;

  public static void main(String[] args) {
    SpringApplication.run(TakeMeWithBackend.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Override
  public void run(String... params) throws Exception {
      // actions before start application
  }

}
