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

package jobhunter.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;

import jobhunter.models.ActivityLog;
import jobhunter.models.Company;
import jobhunter.models.Contact;
import jobhunter.models.Job;
import jobhunter.models.Profile;
import jobhunter.models.Salary;
import jobhunter.models.Subscription;
import jobhunter.models.SubscriptionItem;
import jobhunter.persistence.ObjectId;

public class Random {

    public static final int qr_code_length = 16;
    public static final int nfc_code_length = 16;
    private static final SecureRandom random = new SecureRandom();
    
    public static String String(Integer length){
        return new BigInteger(130, random).toString(24).substring(1, length);
    }
    
    public static String String(){
        return String(8);
    }
    
    public static Integer Integer(final Integer min, final Integer max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    
    public static <T> T Item(final Collection<T> collection){
        final int max = collection.size() - 1;
        final Integer rand = Integer(0, max);
        int i = 0;
        for(T t: collection){
            if(i == rand)
                return t;
            i++;
        }
        return null;
    }
    
    public static <T extends Enum<?>> T Enum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
    
    @SafeVarargs
	public static <T extends Enum<?>> T Enum(T...ts){
        int x = random.nextInt(ts.length);
        return ts[x];
    }
    
    public static BigDecimal BigDecimal(){
        return new BigDecimal(random.nextDouble());
    }
    
    public static BigDecimal Money(){
        return new BigDecimal(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_EVEN);
    }
    
    public static Boolean Boolean(){
        return random.nextBoolean();
    }
    
    public static <T> T element(final Collection<T> list){
    	final Integer rand = Integer(0, list.size());
    	Integer i = list.size() - 1;
    	for(T t : list){
    		if(i == rand) return t;
    		i--;
    	}
    	return null;
    	
    }
    
    public static String QR(){
        final char[] randomString = new BigInteger(130, random).toString(32).toUpperCase().toCharArray();
        final char[] result = new char[qr_code_length];
        for(int i = 0; i < qr_code_length; i++){
            Integer j = Integer(0, randomString.length - 1);
            result[i] = randomString[j];
        }
        return new String(result);
    }
    
    public static String NFC(){
        final char[] randomString = new BigInteger(130, random).toString(32).toUpperCase().toCharArray();
        final char[] result = new char[nfc_code_length];
        for(int i = 0; i < nfc_code_length; i++){
            Integer j = Integer(0, randomString.length - 1);
            result[i] = randomString[j];
        }
        return new String(result);
    }
    
    public static String ScanCode(){
        return String().substring(0, 6).toUpperCase();
    }
    
    public static double[] coordinates(){
        return new double [] {40 + random.nextDouble(), (3 + random.nextDouble()) * -1 };
    }
    
    public static String email(){
        return Random.String() + "@hibu.com";
    }
    
    public static LocalDate birthday(){
        final int year = Random.Integer(1950, 1995);
        final int month = Random.Integer(1, 12);
        final int day = Random.Integer(1, 25);
        return LocalDate.of(year, month, day);
    }
    
    public static LocalDateTime LocalDateTime() {
        final int month = Random.Integer(1, 12);
        final int day = Random.Integer(1, 25);
        final int hour = Random.Integer(1, 23);
        final LocalDateTime rand = LocalDateTime.of(2014, month, day, hour, 0);
        return rand.isBefore(LocalDateTime.now()) ? rand : LocalDateTime();
    }
    
    public static Company Company() {
    	return new Company()
			.setName(Random.String(20))
			.setAddress(Random.String(20))
			.setUrl("www." + Random.String(25) + ".biz");
    }
    
    public static Salary Salary() {
    	return Salary.of(
			40000, 
			Currency.getInstance("EUR"), 
			Salary.Period.YEARLY
		);
    }
    
    public static Contact Contact() {
    	return Contact.of()
    		.setName(Random.String())
    		.setPhone(Random.String())
    		.setEmail(Random.email());
    }
    
    public static Job Job() {
    	return Job.of()
    		.setCreated(LocalDateTime())
    		.setActive(Boolean())
	    	.setPosition(Random.String(15))
			.setDescription(new LoremIpsum().getParagraphsHTML(3))
			.setLink("http://www." + Random.String(25) + ".com/" + Random.String(25))
			.setPortal(element(Arrays.asList(new String[]{
				"Xing", "LinkedIn", "Monster", "Direct Contact"
			})))
			.addContact(Contact())
			.addContact(Contact())
			.addContact(Contact())
			.addLog(ActivityLog())
			.addLog(ActivityLog())
			.addLog(ActivityLog())
			.setSalary("40.000€/B/A")
			.setCompany(Company())
			.setAddress(String())
			.setRating(Integer(1, 5));
    }
    
    public static Profile Profile(){
    	Profile p = new Profile();
		p.setId(new ObjectId());
		for(int i = 0; i < 10; i++)
			p.addJob(Job());
		return p;
    }
    
    public static ActivityLog ActivityLog(){
    	return ActivityLog.of()
			.setCreated(LocalDate.now())
			.setType(Enum(ActivityLog.Type.class))
			.setDescription(String());
    }
    
    public static Subscription Subscription(){
    	return Subscription.create()
			.setURI(String())
			.setTitle(String())
			.addItem(SubscriptionItem())
			.addItem(SubscriptionItem())
			.addItem(SubscriptionItem())
			.addItem(SubscriptionItem());
    }
    
    public static SubscriptionItem SubscriptionItem(){
    	return SubscriptionItem.create()
    		.setPosition(String())
			.setCreated(LocalDateTime())
			.setLink(String())
			.setPortal(element(Arrays.asList(new String[]{
				"Xing", "LinkedIn", "Monster", "Direct Contact"
			})))
			.setDescription(new LoremIpsum().getParagraphsHTML());
    }
}
