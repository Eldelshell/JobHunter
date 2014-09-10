package jobhunter.gui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public interface Localizable {

	public ResourceBundle getBundle();
	
	default String getTranslation(String str) {
		try {
			return getBundle().getString(str);
		} catch (Exception e) {
			return str;
		}
	}
	
	default String getTranslation(String str, Object...objects) {
		final String i18n = getBundle().getString(str);
		return MessageFormat.format(i18n, objects);
	}
	
	default String[] getTranslationArray(String str) {
		return getBundle().getString(str).split(",");
	}
}
