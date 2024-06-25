package br.com.estudos.nelson.screenmatch.principal;

import java.util.Scanner;

import br.com.estudos.nelson.screenmatch.service.ConsumoApi;

public class Principal {

    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=180a3954";
    private ConsumoApi consumoApi = new ConsumoApi();

    public void exibeMenu(){
        System.out.println("Digite o nome da série: ");
        var nomeSerie = leitura.nextLine();
        // "https://omdbapi.com/?t=game+of+thrones&season="+i+"&apikey=180a3954"
        consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    }

}
