package br.com.marcelo.screenmatch.principal;

import br.com.marcelo.screenmatch.model.DadosEpsodio;
import br.com.marcelo.screenmatch.model.DadosSerie;
import br.com.marcelo.screenmatch.model.DadosTemporada;
import br.com.marcelo.screenmatch.model.Episodio;
import br.com.marcelo.screenmatch.service.ConsumoAPI;
import br.com.marcelo.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d-> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

/*        System.out.println("Digite um trecho do titulo do episodio ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episodio Encontrado ");
            System.out.println("Temporada " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episodio não encontrado ");
        }*/

/*        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1,1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e->e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódio: " + e.getTitulo() +
                                " Data Lançamento: " + e.getDataLancamento().format(formatador)
                ));*/

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor Episódio: " + est.getMax());
        System.out.println("Pior Episódio: " + est.getMin());
        System.out.println("Quantidade Avaliada: " + est.getCount());

    }
}
