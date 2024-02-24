package br.com.marcelo.screenmatch;

import br.com.marcelo.screenmatch.model.DadosEpsodio;
import br.com.marcelo.screenmatch.model.DadosSerie;
import br.com.marcelo.screenmatch.model.DadosTemporada;
import br.com.marcelo.screenmatch.service.ConsumoAPI;
import br.com.marcelo.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=c6ce6eb3");
		//System.out.println(json);
		//json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=c6ce6eb3");
		DadosEpsodio dadosEpsodio = conversor.obterDados(json,DadosEpsodio.class);
		System.out.println(dadosEpsodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=c6ce6eb3");
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);
	}
}
