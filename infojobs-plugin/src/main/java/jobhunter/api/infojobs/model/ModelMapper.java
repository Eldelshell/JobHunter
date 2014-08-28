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

package jobhunter.api.infojobs.model;

import jobhunter.models.Company;
import jobhunter.models.Job;

public class ModelMapper {

	public static Job map(final Offer offer) {
		Job j = Job.of();
		
		j.setCompany(Company.instanceOf()
			.setName(offer.getProfile().getName())
			.setAddress(offer.getProfile().getProvince().getValue())
			.setUrl(offer.getProfile().getUrl())
		);
		
		j.setPosition(offer.getTitle());
		j.setExtId(offer.getId());
		j.setAddress(offer.getCity());
		j.setDescription(mapDescription(offer));
		j.setLink(offer.getLink());
		j.setPortal("InfoJobs"); //This has to come from the API!!!
		j.setPosition(offer.getTitle());
		j.setSalary(mapSalary(offer));
		
		return j;
	}
	
	private static String mapSalary(final Offer offer) {
		StringBuilder b = new StringBuilder();
		if(offer.getMinPay() != null)
			b.append(offer.getMinPay().getAmountValue()).append(" - ");
		
		if(offer.getMaxPay() != null)
			b.append(offer.getMaxPay().getAmountValue());
		
		return b.toString();
	}
	
	private static String mapDescription(final Offer offer) {
		StringBuilder b = new StringBuilder();
		
		b.append(offer.getDescription());
		b.append(System.lineSeparator());
		b.append(offer.getMinRequirements());
		b.append(System.lineSeparator());
		b.append(offer.getDesiredRequirements());
		b.append(System.lineSeparator());
		b.append(offer.getCommissions());
		b.append(System.lineSeparator());
		b.append(offer.getContractDuration());
		b.append(System.lineSeparator());
		b.append(offer.getContractType().getValue());
		b.append(System.lineSeparator());
		b.append(offer.getJourney().getValue());
		
		return b.toString();
	}
	
}
