package csp2sat.compiler_server.compiler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CompilerApplication {
	public static void main(String[] args) {
		SpringApplication.run(CompilerApplication.class, args);
	}
}
