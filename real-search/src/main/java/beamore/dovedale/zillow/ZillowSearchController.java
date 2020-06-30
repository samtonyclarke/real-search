package beamore.dovedale.zillow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import beamore.dovedale.yamlmodel.ZillowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
public class ZillowSearchController {

	@Qualifier("zillowClient")
	@Autowired
	WebClient zillowClient;
	
	@Autowired
	ObjectMapper mapper;
	
	private String queryParm = "{\"pagination\":{},\"mapBounds\":{\"west\":-97.87848472595215,\"east\":-72.17047691345215,\"south\":26.526877449686918,\"north\":43.562294797762625},\"isMapVisible\":true,\"mapZoom\":6,\"filterState\":{\"price\":{\"min\":0,\"max\":600000},\"sqft\":{\"min\":2000},\"doz\":{\"value\":\"7\"},\"att\":{\"value\":\"icf\"},\"mp\":{\"min\":0,\"max\":2136}},\"isListVisible\":true}";

	@RequestMapping("/zillow")
	public Mono<ZillowResponse> searchZillow() throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException
	{
			String encode = URLEncoder.encode(queryParm, StandardCharsets.UTF_8.toString());
			Mono<ZillowResponse> bodyToMono = zillowClient.get().uri(uriBuilder -> uriBuilder.path("/search/GetSearchPageState.htm")
					.queryParam("searchQueryState", encode).build())
					.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
					.header(HttpHeaders.CONNECTION, "keep-alive")
					.header(HttpHeaders.CACHE_CONTROL, "max-age=0")
					.header("DNT", "1")
					.header("Upgrade-Insecure-Requests", "1")
					.header(HttpHeaders.ACCEPT.toString(),"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
			.retrieve().bodyToMono(ZillowResponse.class);
			return bodyToMono;
	}

}
