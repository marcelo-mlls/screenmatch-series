package br.com.marcelo.screenmatch.principal;

import br.com.marcelo.screenmatch.model.DadosEpsodio;
import br.com.marcelo.screenmatch.model.DadosSerie;
import br.com.marcelo.screenmatch.model.DadosTemporada;
import br.com.marcelo.screenmatch.service.ConsumoAPI;
import br.com.marcelo.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=c6ce6eb3";
    public void exibeMenu(){
        System.out.println("Digite o nome da serie: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" " , "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" " , "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        /***
         *  o mesmo laço abaixo em lambda
         *  for(int i = 0; i < dados.totalTemporadas(); i++){
         *    List<DadosEpsodio> episodiosTemporada = temporadas.get(i).episodios();
         *    for (int j = 0; j < episodiosTemporada.size(); j++){
         *        System.out.println(episodiosTemporada.get(j).titulo());
         *    }
         *  }
         **/

        temporadas.forEach(t->t.episodios().forEach(e-> System.out.println(e.titulo())));

        List<DadosEpsodio> dadosEpsodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTOP 5 Episodios: ");
        dadosEpsodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpsodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
