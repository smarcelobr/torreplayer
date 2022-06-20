package br.nom.figueiredo.sergio.torreplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
public class TorrePlayerApplication {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/musica");
		SpringApplication.run(TorrePlayerApplication.class, args);
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
