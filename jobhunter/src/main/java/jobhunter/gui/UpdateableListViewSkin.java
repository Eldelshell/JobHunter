package jobhunter.gui;

import javafx.scene.control.ListView;

import com.sun.javafx.scene.control.skin.ListViewSkin;

public class UpdateableListViewSkin<T> extends ListViewSkin<T> {

	public UpdateableListViewSkin(ListView<T> arg0) {
		super(arg0);
	}
	
	public void refresh() {
		super.flow.rebuildCells();
	}
	
	@SuppressWarnings("unchecked")
    static <T> UpdateableListViewSkin<T> cast(Object obj) {
        return (UpdateableListViewSkin<T>)obj;
    }

}
