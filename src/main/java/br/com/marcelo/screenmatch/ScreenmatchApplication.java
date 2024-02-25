package br.com.marcelo.screenmatch;

import br.com.marcelo.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.exibeMenu();


		//var consumoAPI = new ConsumoAPI();
		//var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=c6ce6eb3");
		//System.out.println(json);
		//json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);
		//ConverteDados conversor = new ConverteDados();
		//DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		//System.out.println(dados);

		//json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=c6ce6eb3");
		//DadosEpsodio dadosEpsodio = conversor.obterDados(json,DadosEpsodio.class);
		//System.out.println(dadosEpsodio);


	}
}
