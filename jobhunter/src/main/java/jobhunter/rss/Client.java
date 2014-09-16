/*
 * Copyright (C) 2014 Alejandro Ayuso
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jobhunter.rss;

import java.io.File;
import java.io.FileOutputStream;
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
import com.thoughtworks.xstream.mapper.CannotResolveClassException;

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
	
	public Optional<Root> execute() {
		l.debug("Retrieving RSS");
		
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
			l.error("Failed to open connection {}", e);
		} catch (CannotResolveClassException e) {
			l.error("Failed to parse response {}", e);
		}
		
		return Optional.empty();
	}
	
	public Optional<File> getIcon() {
		final HttpGet httpGet = new HttpGet(url);
		try(CloseableHttpClient client = HttpClients.createDefault()){
			try(CloseableHttpResponse response = client.execute(httpGet)){
				if(response.getStatusLine().getStatusCode() == 200){
					l.debug("Got Favicon");
					return Optional.of(saveIcon(response));
				} else {
					l.error("Failed to connect with error {}", response.getStatusLine().getStatusCode());
				}
			}
			
		} catch (IOException e) {
			l.error("Failed to open connection {}", e);
		} catch (CannotResolveClassException e) {
			l.error("Failed to parse response {}", e);
		}
		return Optional.empty();
	}
	
	public static class RSSClientException extends RuntimeException {

		private static final long serialVersionUID = -2752620679365445228L;
		
	}
	
	private File saveIcon(final CloseableHttpResponse response) throws IOException{
		final File tmpIcon = File.createTempFile("icon_", ".ico");
		try(FileOutputStream fout = new FileOutputStream(tmpIcon)){
			response.getEntity().writeTo(fout);
		}
		return tmpIcon;
	}
	
}
