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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jobhunter.api.infojobs.model.Offer;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfferRequest {

	private static final Logger l = LoggerFactory.getLogger(OfferRequest.class);
	private static final String URL = "https://api.infojobs.net/api/1/offer/%s";
	
	private final String key;
	
	/**
	 * Generates a OfferRequest after parsing the given URL for the
	 * InfoJobs' key.
	 * FTM I know of two URL formats where we can get the key from:
	 * - Using the "of_codigo" query param
	 * - Using the last 30 chars from a path param
	 * This method handles both cases.
	 * @param url
	 * @return OfferRequest
	 * @throws InfoJobsAPIException 
	 */
	public static OfferRequest of(String url) throws InfoJobsAPIException {
		List<NameValuePair> params = new ArrayList<>();
		try {
			params = URLEncodedUtils.parse(new URI(url), "UTF-8");
		} catch (URISyntaxException e) {
			l.error("Failed to build URI", e);
			throw new InfoJobsAPIException("Invalid URL " + url);
		}
		
		NameValuePair key = params.stream()
			.filter(nvp -> nvp.getName().equalsIgnoreCase("of_codigo"))
			.findFirst()
			.orElse(new BasicNameValuePair("of_codigo", StringUtils.right(url, 30)));
		
		return new OfferRequest(key);
	}
	
	public OfferRequest(NameValuePair nvp) {
		super();
		this.key = nvp.getValue();
		
	}

	public OfferRequest(String key) {
		super();
		this.key = key;
	}
	
	public Optional<Offer> execute() throws InfoJobsAPIException {
		final Client client = new Client();
		return client.get(Offer.class, String.format(URL, key));
	}

	public String getKey() {
		return key;
	}
	
}
