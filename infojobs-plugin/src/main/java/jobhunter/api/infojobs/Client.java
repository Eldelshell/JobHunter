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

package jobhunter.api.infojobs;

import java.io.IOException;
import java.util.Optional;

import jobhunter.api.infojobs.model.Error;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

	private static final Logger l = LoggerFactory.getLogger(Client.class);
	private static final String clientId = null; // REPLACE-ME!!!
	private static final String privateKey = null; // REPLACE-ME!!!
	private static final HttpHost targetHost = new HttpHost("api.infojobs.net", 443, "https");
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final CredentialsProvider provider;
	
	public Client() {
		provider = new BasicCredentialsProvider();
		provider.setCredentials(
			new AuthScope(targetHost.getHostName(), targetHost.getPort()),
			new UsernamePasswordCredentials(clientId, privateKey)
		);
	}

	public <T> Optional<T> get(Class<T> clazz, String url) throws InfoJobsAPIException{
		HttpGet httpGet = new HttpGet(url);
		try(CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(provider).build()){
			try(CloseableHttpResponse response = client.execute(httpGet)){
				if(response.getStatusLine().getStatusCode() == 200){
					T entity = mapper.readValue(response.getEntity().getContent(), clazz);
					return Optional.of(entity);
				} else {
					Error error = mapper.readValue(response.getEntity().getContent(), Error.class);
					throw new InfoJobsAPIException(error);
				}
			}
		} catch (IOException e) {
			l.error("Failed to GET {} from {}", clazz.getCanonicalName(), url, e);
			throw new InfoJobsAPIException(e.getLocalizedMessage());
		}
	}
	
}
