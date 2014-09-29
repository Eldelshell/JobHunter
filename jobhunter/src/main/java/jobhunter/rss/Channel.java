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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Model to describe the XML Channel element in a RSS feed
 */
@XStreamAlias("channel")
public class Channel implements Comparable<Channel>{

	private String title;
	private String link;
	private String description;
	private String language;
	private Integer ttl;
	
	@XStreamConverter(value=DateTimeConverter.class)
	private LocalDateTime pubDate;
	
	@XStreamConverter(value=DateTimeConverter.class)
	private LocalDateTime lastBuildDate;
	
	private Image image;
	
	@XStreamImplicit(itemFieldName="item")
	private List<Item> items;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public LocalDateTime getPubDate() {
		return pubDate;
	}

	public void setPubDate(LocalDateTime pubDate) {
		this.pubDate = pubDate;
	}

	public LocalDateTime getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(LocalDateTime lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<Item> getItems() {
		if(items == null)
			items = new ArrayList<>();
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public Integer getTtl() {
		return ttl;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastBuildDate == null) ? 0 : lastBuildDate.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((pubDate == null) ? 0 : pubDate.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (lastBuildDate == null) {
			if (other.lastBuildDate != null)
				return false;
		} else if (!lastBuildDate.equals(other.lastBuildDate))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (pubDate == null) {
			if (other.pubDate != null)
				return false;
		} else if (!pubDate.equals(other.pubDate))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public int compareTo(Channel o) {
		if(o == null) throw new IllegalArgumentException();
		return this.pubDate.compareTo(o.getPubDate());
	}
	
}
