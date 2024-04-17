package guru.springframework.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.springframework.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

@Service
public class BeerClientImpl implements BeerClient {


    public static final String BEER_PATH = "/api/v3/beer";
    private static final String BEER_PATH_ID =  BEER_PATH+"/{beerId}";
    private final WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(Map.class);    }

    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDto() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String beerId) {
        return webClient.get().uri(BEER_PATH_ID, beerId)
                .retrieve().bodyToMono(BeerDTO.class);



    }
    @Override
    public Mono<BeerDTO> getBeerByIdWithNoMatchingName(String id) {
        return webClient.get().uri(uriBuilder->
                        uriBuilder.path(BEER_PATH_ID)
                                .build(id))
                .retrieve()
                .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getBeerByStyle(String beerStyle) {
        return webClient.get().uri(uriBuilder->
                        uriBuilder
                                .path(BEER_PATH)
                                .queryParam("beerStyle", beerStyle)
                                .build())
                .retrieve()
                .bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> createNewBeer(BeerDTO beerDTO) {
        Mono<String> location = webClient.post().uri(BEER_PATH)
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> Mono.just(voidResponseEntity.getHeaders().get("Location").get(0)))
                .map(path -> path.split("/")[path.split("/").length - 1]);


        Mono<BeerDTO> beerDTOMono = location.flatMap(this::getBeerById);
        return beerDTOMono;
    }

    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO updatedMono) {
        return webClient.put().uri(uriBuilder ->
                uriBuilder.path(BEER_PATH_ID).build(updatedMono.getId()))
                .body(Mono.just(updatedMono), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(updatedMono.getId()));
    }

    @Override
    public Mono<BeerDTO> patchBeer(BeerDTO updatedMono) {
        return webClient.patch().uri(uriBuilder ->
                        uriBuilder.path(BEER_PATH_ID).build(updatedMono.getId()))
                .body(Mono.just(updatedMono), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(updatedMono.getId()));
    }

    @Override
    public Mono<Void> deletBeer(BeerDTO singleMonoDto) {
        return webClient.delete().uri(uriBuilder->
                        uriBuilder.path(BEER_PATH_ID)
                                .build(singleMonoDto.getId()))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

}
