package br.nom.figueiredo.sergio.torreplayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
@EnableScheduling
public class TorrePlayerApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/musica");
		SpringApplication.run(TorrePlayerApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
				.modules(new JavaTimeModule())
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();
	}

	/**
	 * Número gerado uma vez a cada subida para previnir o browser de usar
	 * cache ao ler o conteúdo estático. Um único número é gerado a cada execução para
	 * aproveitar o uso do cache durante a execução onde o conteúdo estático não é atualizado.
	 * @return numero para ser incluído nos links para ler conteúdo estático
	 */
	@Bean("cachePreventNumber")
	@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.NO)
	public Long cachePreventNumber() {
		return Math.round(Math.random()*10000);
	}

}
