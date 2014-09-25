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

package jobhunter.gui;

import javafx.scene.control.ListView;

import com.sun.javafx.scene.control.skin.ListViewSkin;

/**
 * Custom ListViewSkin that allows to refresh a ListView without
 * having to reload all the items.
 * @param <T> the items that the ListView holds
 */
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
