package guru.springframework.client;

import guru.springframework.model.BeerDTO;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;
    @Test
    void testDeleteBeer() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerDto()
                .next()
                .flatMap(singleMonoDto-> beerClient.deletBeer(singleMonoDto))
                .doOnSuccess(x->atomicBoolean.set(true))
                .subscribe();

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);

    }
    @Test
    void testPatchBeer() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        final String NAME="PATCHEDTEST";
        beerClient.listBeerDto()
                .next()
                .map(dto-> BeerDTO.builder().beerName(NAME).id(dto.getId()).build())
                .flatMap(updatedMono-> beerClient.patchBeer(updatedMono))
                .subscribe(beerDto-> {
                    System.out.println(beerDto.getBeerName());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);

    }

    @Test
    void testUpdateBeer() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        final String NAME="TEST";
        beerClient.listBeerDto()
                .next()
                .doOnNext(dto-> dto.setBeerName(NAME))
                .flatMap(updatedMono-> beerClient.updateBeer(updatedMono))
                .subscribe(beerDto-> {
                    System.out.println(beerDto.getBeerName());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);

    }
    @Test
    void testCreateNewBeer() throws InterruptedException {
        BeerDTO beerDTO= BeerDTO.builder()
                .beerName("hello")
                .beerStyle("IPA")
                .upc("asda")
                .price(new BigDecimal(12))
                .quantityOnHand(12)
                .build();
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);

        beerClient.createNewBeer(beerDTO)
                .subscribe(beerDto-> {
                    System.out.println(beerDto.getId());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);

    }
    @Test
    void testgetBeerByBeerStyle() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerDto()
                .flatMap(dto-> beerClient.getBeerByStyle(dto.getBeerStyle()))
                .subscribe(beerDto-> {
                    System.out.println(beerDto.getBeerStyle());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerByIdWithNoMatchingName() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerDto()
                .flatMap(dto-> beerClient.getBeerByIdWithNoMatchingName(dto.getId()))
                .subscribe(beerDto-> {
                    System.out.println(beerDto.getBeerName());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerById() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerDto()
                .flatMap(dto-> beerClient.getBeerById(dto.getId()))
                .subscribe(beerDto-> {
                    System.out.println(beerDto.getBeerName());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }

    @Test
    void listGetBeerDto() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerDto()
                .subscribe(beerDto-> {
                    System.out.println(beerDto);
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }

    @Test
    void listGetBeerJson() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerJsonNode()
                .subscribe(jsonNode-> {
                    System.out.println(jsonNode.toPrettyString());
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }

    @Test
    void listBeerMap() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        beerClient.listBeerMap()
                .subscribe(response-> {
                    System.out.println(response);
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }
    @Test
    void listBeer() throws InterruptedException {
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);

        beerClient.listBeer()
                .subscribe(response-> {
                    System.out.println(response);
                    atomicBoolean.set(true);
                });
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilTrue(atomicBoolean);
    }
}