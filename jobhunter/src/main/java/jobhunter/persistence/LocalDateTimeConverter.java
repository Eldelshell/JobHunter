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

package jobhunter.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class LocalDateTimeConverter implements Converter {

	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class arg0) {
		return LocalDateTime.class.isAssignableFrom(arg0);
	}

	@Override
	public void marshal(Object input, HierarchicalStreamWriter sw, MarshallingContext arg2) {
		LocalDateTime localDateTime = (LocalDateTime) input;
        sw.setValue(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
		return LocalDateTime.parse(arg0.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

}
