package beamore.dovedale;

import javax.net.ssl.SSLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.ProxyProvider;
import reactor.netty.tcp.TcpClient;

@SpringBootApplication
public class Search{

	public static void main(String[] args) {
		SpringApplication.run(Search.class, args);
	}

	public Search() {
	}

	@Configuration
	public class Conf {
		
		@Bean
		public ClientHttpConnector ClientHttpConnector() throws SSLException
		{
			SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			
			TcpClient proxy = TcpClient.create().proxy(ops -> ops.type(ProxyProvider.Proxy.HTTP).host("genproxy").port(8080));
//			HttpClient httpClient = HttpClient.create();
			HttpClient httpClient = HttpClient.from(proxy).secure(t -> t.sslContext(sslContext)).wiretap(true);
			ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
			return connector;
		}
		
		@Bean
		public WebClient zillowClient(ClientHttpConnector ClientHttpConnector) {
			return WebClient.builder().baseUrl("https://www.zillow.com").clientConnector(ClientHttpConnector).build();
		}

		@Bean
		public WebClient myhomeClient(ClientHttpConnector ClientHttpConnector) {
			return WebClient.builder().baseUrl("https://api.myhome.ie").clientConnector(ClientHttpConnector).build();
		}
				
	}

}
