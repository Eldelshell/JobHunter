package jobhunter.gui.dialog;

import jobhunter.gui.Localizable;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public enum DialogFactory implements Localizable {
	
	_INSTANCE;
	
	private Boolean _delete(final String message) {
		Action deleteAction = Dialogs.create()
			.title(getTranslation(message))
			.message(getTranslation("message.confirmation"))
			.showConfirm();
		
		return deleteAction.equals(Dialog.Actions.YES);
	}

	public static Boolean deleteItems() {
		return _INSTANCE._delete("message.delete.item.confirmation");
	}
	
	public static Boolean deleteFeed() {
		return _INSTANCE._delete("menu.delete.feed");
	}

}
