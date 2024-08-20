package br.com.estudos.nelson.screenmatch.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.estudos.nelson.screenmatch.model.DadosEpisodio;
import br.com.estudos.nelson.screenmatch.model.DadosSerie;
import br.com.estudos.nelson.screenmatch.model.DadosTemporada;
import br.com.estudos.nelson.screenmatch.model.Episodio;
import br.com.estudos.nelson.screenmatch.service.ConsumoApi;
import br.com.estudos.nelson.screenmatch.service.ConverteDados;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=180a3954";
    

    public void exibeMenu(){
        System.out.println("Digite o nome da série: ");
        var nomeSerie = leitura.nextLine();
        // "https://omdbapi.com/?t=game+of+thrones&season="+i+"&apikey=180a3954"
        consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i<=dados.totalTemporadas(); i++){
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);			
		}

		//temporadas.forEach(System.out::println);

        // A iteração abaixo pode ser substituida pela linha subsequente
        // for(int i=0; i< dados.totalTemporadas(); i++){
        //     List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
        //     for(int j=0; j<episodiosTemporada.size(); j++){
        //         System.out.println(episodiosTemporada.get(j).titulo());
        //     }
        // }

        // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
            .flatMap(t -> t.episodios().stream())
            .collect(Collectors.toList());

        System.out.println("\nTop 5 episodes");
        dadosEpisodios.stream()
            .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
            .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
            .limit(5)
            .forEach(System.out::println);
        
        List<Episodio> episodios = temporadas.stream()
            .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.temporada(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
    }
}
