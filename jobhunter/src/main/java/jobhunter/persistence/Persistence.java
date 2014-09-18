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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import jobhunter.models.Profile;
import jobhunter.models.Subscription;
import jobhunter.utils.ApplicationState;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

/**
 * Singleton for persistence operations.
 */
public enum Persistence {
	
	_INSTANCE;
	
	private static final Logger l = LoggerFactory.getLogger(Persistence.class);
	private final XStream xstream = new XStream();
	
	private Long lastModification;
	private byte[] md5sum;
	
	private Persistence() {
		xstream.registerConverter(new LocalDateTimeConverter());
		xstream.registerConverter(new ObjectIdConverter());
		xstream.registerConverter(new LocalDateConverter());
	}
	
	@SuppressWarnings("unchecked")
    static <T> List<T> cast(Object obj) {
        return (List<T>)obj;
    }
	
	private void _save(final File file, final Boolean rewrite) {
		if(wasModified(file) && !rewrite)
			throw new ConcurrentModificationException("File has been modified");

		ApplicationState.instanceOf().changesPending(false);
		zip(file);
	}
	
	private Optional<Profile> _readProfile(final File file) {
		
		try(ZipFile zfile = new ZipFile(file)) {
			l.debug("Reading profile from JHF File");
			final InputStream in = zfile.getInputStream(new ZipEntry("profile.xml"));
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(in, md);
			
			final Object obj = xstream.fromXML(dis);
			
			updateLastMod(file, md);
			
			return Optional.of((Profile)obj);
		}catch(Exception e){
			l.error("Failed to read file: {}", e.getMessage());
		}
		
		return Optional.empty();
	}
	
	
	private Optional<List<Subscription>> _readSubscriptions(final File file) {
		
		try(ZipFile zfile = new ZipFile(file)) {
			l.debug("Reading subscriptions from JHF File");
			final InputStream in = zfile.getInputStream(new ZipEntry("subscriptions.xml"));
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(in, md);
			
			final Object obj = xstream.fromXML(dis);
			
			updateLastMod(file, md);
			
			return Optional.of(cast(obj));
		}catch(Exception e){
			l.error("Failed to read file: {}", e.getMessage());
		}
		
		return Optional.empty();
	}
	
	private void zip(final File file) {
		
		try(ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(file))){
			l.debug("Generating JHF file");
			zout.setMethod(ZipOutputStream.DEFLATED);
			zout.setLevel(9);
			
			l.debug("Inserting profile XML");
			zout.putNextEntry(new ZipEntry("profile.xml"));
			xstream.toXML(ProfileRepository.instanceOf().getProfile(), zout);
			
			l.debug("Inserting subscriptions XML");
			zout.putNextEntry(new ZipEntry("subscriptions.xml"));
			xstream.toXML(SubscriptionRepository.instanceOf().getSubscriptions(), zout);
			
			updateLastMod(file);
		}catch(IOException e){
			l.error("Failed to generate file", e);
		}
		
	}
	
	private void updateLastMod(final File file) {
		this.lastModification = file.lastModified();
		try(InputStream in = new FileInputStream(file)){
			this.md5sum = DigestUtils.md5(in);
		} catch (IOException e) {
			l.error("Failed to read MD5 checksum from {}", file.toString(), e);
		}
		l.debug("File was last modified on {} with MD5 {}", lastModification, this.md5sum.toString());
	}
	
	@SuppressWarnings("unused")
	private void updateLastMod(final File file, final InputStream in) throws IOException {
		this.lastModification = file.lastModified();
		this.md5sum = DigestUtils.md5(in);
	}
	
	private void updateLastMod(final File file, final MessageDigest md) throws IOException {
		this.lastModification = file.lastModified();
		this.md5sum = md.digest();
		l.debug("File was last modified on {} with MD5 {}", lastModification, this.md5sum.toString());
	}
	
	private Boolean wasModified(final File file) {
		if(this.lastModification != file.lastModified())
			return true;
		
		try(InputStream in = new FileInputStream(file)){
			return Arrays.equals(this.md5sum, DigestUtils.md5(in));
		} catch (IOException e) {
			l.error("Failed to read MD5 checksum from {}", file.toString(), e);
		}
		
		return false;
	}
	
	public static void save(final File file) {
		_INSTANCE._save(file, false);
	}
	
	public static void rewrite(final File file) {
		_INSTANCE._save(file, true);
	}
	
	public static Optional<Profile> readProfile(final File file) {
		return _INSTANCE._readProfile(file);
	}
	
	public static Optional<List<Subscription>> readSubscriptions(final File file) {
		return _INSTANCE._readSubscriptions(file);
	}
	
	public static String debugProfile() {
		return _INSTANCE.xstream.toXML(ProfileRepository.instanceOf().getProfile());
	}
	
	public static String debugSubscriptions() {
		return _INSTANCE.xstream.toXML(SubscriptionRepository.instanceOf().getSubscriptions());
	}
	
}
