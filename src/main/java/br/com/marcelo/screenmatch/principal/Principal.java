package br.com.marcelo.screenmatch.principal;

import br.com.marcelo.screenmatch.service.ConsumoAPI;

import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumo = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=c6ce6eb3";
    public void exibeMenu(){
        System.out.println("Digite o nome da serie: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" " , "+") + API_KEY);
        //gilmore+girls
    }
}
