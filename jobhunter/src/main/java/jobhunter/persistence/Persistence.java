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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import jobhunter.models.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public final class Persistence {
	
	private static final Logger l = LoggerFactory.getLogger(Persistence.class);
	private static final XStream xstream = new XStream();
	
	static {
		xstream.registerConverter(new LocalDateTimeConverter());
		xstream.registerConverter(new ObjectIdConverter());
		xstream.registerConverter(new LocalDateConverter());
	}
	
	public static void save(final Profile profile, final File file) {
		zip(profile, file);
	}
	
	public static Optional<Profile> read(final File file) {
		
		try(ZipFile zfile = new ZipFile(file)) {
			l.debug("Reading JHF File");
			final Object obj = xstream.fromXML(zfile.getInputStream(new ZipEntry("profile.xml")));
			return Optional.of((Profile)obj);
		}catch(IOException | ClassCastException e){
			l.error("Failed to read file: {}", e.getMessage());
		}
		
		return Optional.empty();
	}
	
	private static void zip(final Profile profile, final File file) {
		try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(file))){
			l.debug("Generating JHF file");
			zout.setMethod(ZipOutputStream.DEFLATED);
			zout.setLevel(9);
			
			l.debug("Inserting profile XML");
			zout.putNextEntry(new ZipEntry("profile.xml"));
			xstream.toXML(profile, zout);
		}catch(IOException e){
			l.error("Failed to generate file", e);
		}
		
	}
	
	public static String debugProfile(final Profile profile) {
		return xstream.toXML(profile);
	}
	
}
