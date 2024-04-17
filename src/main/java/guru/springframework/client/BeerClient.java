package guru.springframework.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.springframework.model.BeerDTO;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.Flow;

public interface BeerClient {

    Flux<String> listBeer();
    Flux<Map> listBeerMap();

    Flux<JsonNode> listBeerJsonNode();
    Flux<BeerDTO> listBeerDto();

    Mono<BeerDTO> getBeerById(String beerId);
    Mono<BeerDTO> getBeerByIdWithNoMatchingName(String id);

    Flux<BeerDTO>  getBeerByStyle(String beerStyle);

    Mono<BeerDTO> createNewBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(BeerDTO updatedMono);

    Mono<BeerDTO> patchBeer(BeerDTO updatedMono);

    Mono<Void> deletBeer(BeerDTO singleMonoDto);
}
