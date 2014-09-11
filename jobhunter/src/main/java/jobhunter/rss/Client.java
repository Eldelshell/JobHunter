package jobhunter.rss;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class Client {

	private static final Logger l = LoggerFactory.getLogger(Client.class);
	private static final XStream xstream = new XStream();
	
	static{
		xstream.processAnnotations(new Class<?>[]{
			Root.class, Channel.class, Image.class, Item.class
		});
		xstream.ignoreUnknownElements();
	}
	
	private final URI url;

	private Client(URI url) {
		super();
		this.url = url;
	}
	
	public static Client create(String url){
		try {
			final URI ur = new URI(url);
			return new Client(ur);
		} catch (URISyntaxException e) {
			l.error("Invalid URI {}", url, e);
		}
		throw new RSSClientException();
	}
	
	public Optional<Root> execute(){
		l.debug("Connecting");
		final HttpGet httpGet = new HttpGet(url);
		try(CloseableHttpClient client = HttpClients.createDefault()){
			try(CloseableHttpResponse response = client.execute(httpGet)){
				if(response.getStatusLine().getStatusCode() == 200){
					l.debug("Got 200 Response");
					Root entity = (Root) xstream.fromXML(response.getEntity().getContent());
					return Optional.of(entity);
				} else {
					l.error("Failed to connect with error {}", response.getStatusLine().getStatusCode());
				}
			}
			
		} catch (IOException e) {
			l.error("Failed to open connection", e);
		}
		return Optional.empty();
	}
	
	public static class RSSClientException extends RuntimeException {

		private static final long serialVersionUID = -2752620679365445228L;
		
	}
	
}
