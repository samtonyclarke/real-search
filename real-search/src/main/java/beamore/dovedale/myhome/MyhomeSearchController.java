package beamore.dovedale.myhome;

import beamore.dovedale.yamlmodel.MyhomeFavouritesRequest;
import beamore.dovedale.yamlmodel.MyhomeFavouritesResponse;
import beamore.dovedale.yamlmodel.MyhomePricechangeRequest;
import beamore.dovedale.yamlmodel.MyhomePricechangeResponse;
import beamore.dovedale.yamlmodel.MyhomeSearchRequest;
import beamore.dovedale.yamlmodel.MyhomeSearchResponse;
import beamore.dovedale.yamlmodel.PropertyHistorySearchRequest;
import beamore.dovedale.yamlmodel.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class MyhomeSearchController {

    @Qualifier("myhomeClient")
    @Autowired
    WebClient myhomeClient;

    @RequestMapping("/myhomerecovery")
    public Mono<MyhomeSearchResponse> searchMyhomeRecovery() {
        MyhomeSearchRequest request = buildRecoverySearchRequest();
        return myhomeClient.post().uri("/search")
                .body(Mono.just(request), MyhomeSearchRequest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().bodyToMono(MyhomeSearchResponse.class);
    }

    @RequestMapping("/myhomebera")
    public Mono<MyhomeSearchResponse> searchMyhomeBerA() {
        MyhomeSearchRequest request = buildBerASearchRequest(1);

        Mono<MyhomeSearchResponse> resp = myhomeClient.post().uri("/search")
                .body(Mono.just(request), MyhomeSearchRequest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().bodyToMono(MyhomeSearchResponse.class);

        // we need to make a request for each page, seems to be bug with myhome as you cannot set arbitary page size
        return resp.flatMap(myhomeSearchResponse -> {
            int numberPages = myhomeSearchResponse.getResultCount().intValue() / 20;
            if (numberPages * 20 < myhomeSearchResponse.getResultCount().intValue()) {
                numberPages++; // the last page, not full
            }
            List<Mono<MyhomeSearchResponse>> furtherRequests = new ArrayList<>();
            for (int i = 2; i < numberPages + 1; i++) {
                furtherRequests.add(getByPage(i));
            }
            return Mono.zip(furtherRequests, objects -> {
                for (int j = 0; j < objects.length; j++) {
                    MyhomeSearchResponse k = (MyhomeSearchResponse) objects[j];
                    myhomeSearchResponse.getSearchResults().addAll(k.getSearchResults());
                }
                return myhomeSearchResponse;
            });
        });
    }

    private Mono<MyhomeSearchResponse> getByPage(int pageNumber) {
        MyhomeSearchRequest request = buildBerASearchRequest(pageNumber);

        return myhomeClient.post().uri("/search")
                .body(Mono.just(request), MyhomeSearchRequest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().bodyToMono(MyhomeSearchResponse.class);
    }

    @RequestMapping("/myhomepricechange")
    public Mono<MyhomePricechangeResponse> priceChangeMyhome() {
        MyhomePricechangeRequest request = buildPriceChangeRequest();

        return myhomeClient.post().uri("/json/reply/LocalityPropertyHistorySearch")
                .body(Mono.just(request), MyhomePricechangeRequest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().bodyToMono(MyhomePricechangeResponse.class);
    }

    @RequestMapping("/myhomefavourites")
    public Mono<MyhomeFavouritesResponse> favouritesMyhome() {
        MyhomeFavouritesRequest request = buildFavouritesRequest();

        return myhomeClient.post().uri("/user/favourites")
                .body(Mono.just(request), MyhomeFavouritesRequest.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve().bodyToMono(MyhomeFavouritesResponse.class);
    }

    private MyhomeFavouritesRequest buildFavouritesRequest() {
        MyhomeFavouritesRequest request = new MyhomeFavouritesRequest();
        request.setApiKey("5f4bc74f-8d9a-41cb-ab85-a1b7cfc86622");
        request.setCorrelationId("91a90568-acfe-4185-81de-df712a37e602");
        request.setSessionId("11ff1047e4e64c9aba937183b051841b");

        request.setRequestTypeId(BigInteger.valueOf(32));
        request.setRequestVerb("POST");
        request.setEndpoint("https://api.myhome.ie/user/favourite");
        request.setUserId(BigInteger.valueOf(406984));
        request.setEmail("tony.clarke@gmail.com");
        request.setPropertyClassIds(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2)));
        request.setPropertyStatusIds(Collections.singletonList(BigInteger.valueOf(2)));

        request.setPageSize(BigInteger.valueOf(20));
        request.setPage(BigInteger.valueOf(1));
        return request;
    }

    private MyhomePricechangeRequest buildPriceChangeRequest() {
        MyhomePricechangeRequest request = new MyhomePricechangeRequest();
        request.setApiKey("5f4bc74f-8d9a-41cb-ab85-a1b7cfc86622");
        request.setCorrelationId("91a90568-acfe-4185-81de-df712a37e602");
        request.setSessionId("11ff1047e4e64c9aba937183b051841b");
        request.setRequestTypeId(BigInteger.valueOf(6));
        request.setRequestVerb("POST");
        request.setEndpoint("https://api.myhome.ie/json/reply/LocalityPropertyHistorySearch");
        request.setPageSize(BigInteger.valueOf(500));
        request.setPage(BigInteger.valueOf(1));
        PropertyHistorySearchRequest historyRequest = new PropertyHistorySearchRequest();
        historyRequest.setPage(BigInteger.valueOf(1));
        historyRequest.setPageSize(BigInteger.valueOf(20));
        historyRequest.setChannelIds(Collections.singletonList(BigInteger.valueOf(1)));
        historyRequest.setPropertyClassIds(Collections.singletonList(BigInteger.valueOf(1)));
        historyRequest.setPropertyStatusIds(Arrays.asList(BigInteger.valueOf(2), BigInteger.valueOf(3)));
        historyRequest.setPriceHasChanged(true);
        historyRequest.setIsActive(true);
        request.setPropertyHistorySearchRequest(historyRequest);

        return request;
    }

    private MyhomeSearchRequest buildRecoverySearchRequest() {
        MyhomeSearchRequest request = buildCommonSearchRequest();
        request.getSearchRequest().setQuery("Recovery ventillation");
        return request;
    }

    private MyhomeSearchRequest buildBerASearchRequest(int pageNumber) {
        MyhomeSearchRequest request = buildCommonSearchRequest();
        request.getSearchRequest().setMinEnergyRating(BigInteger.valueOf(130));
        request.setPage(String.valueOf(pageNumber));
        request.setPageSize(BigInteger.valueOf(20));
        return request;
    }

    private MyhomeSearchRequest buildCommonSearchRequest() {
        SearchRequest searchDetails = new SearchRequest();
        searchDetails.setPropertyClassIds(Collections.singletonList(BigInteger.valueOf(1)));
        searchDetails.setPropertyStatusIds(Arrays.asList(BigInteger.valueOf(2), BigInteger.valueOf(12)));
        searchDetails.setPropertyTypeIds(Arrays.asList(BigInteger.valueOf(39), BigInteger.valueOf(40)));
        searchDetails.setMaxPrice(BigInteger.valueOf(500000));
        searchDetails.setMinBeds(BigInteger.valueOf(2));
        searchDetails.setMinBathrooms(BigInteger.valueOf(2));
        searchDetails.setMinSize(BigInteger.valueOf(200));
        searchDetails.setMinEnergyRating(BigInteger.valueOf(100));
        searchDetails.setChannelIds(Collections.singletonList(BigInteger.ONE));
        searchDetails.setIsBoundsSearch(false);
        MyhomeSearchRequest request = new MyhomeSearchRequest();
        request.setApiKey("5f4bc74f-8d9a-41cb-ab85-a1b7cfc86622");
        request.setCorrelationId("91a90568-acfe-4185-81de-df712a37e602");
        request.setSessionId("11ff1047e4e64c9aba937183b051841b");
        request.setRequestTypeId(BigInteger.valueOf(2));
        request.setRequestVerb("POST");
        request.setEndpoint("https://api.myhome.ie/search");
        request.setPage("1");
        request.setPageSize(BigInteger.valueOf(20));
        request.setSortColumn(BigInteger.valueOf(2));
        request.setSortDirection(BigInteger.valueOf(2));
        request.setSearchRequest(searchDetails);
        return request;
    }

}
