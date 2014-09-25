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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobhunter.models.SubscriptionItem;

/**
 * Custom TableRow to be used by the subscriptionItemsTable 
 * in the FXMLController to display SubscriptionItems
 */
public class SubscriptionRow extends TableRow<SubscriptionItem> {
	
	public static final PropertyValueFactory<SubscriptionItem, LocalDateTime> 
		DATE_VALUE = new PropertyValueFactory<SubscriptionItem, LocalDateTime>("created");
	
	public static final PropertyValueFactory<SubscriptionItem, String>
		POSITION_VALUE = new PropertyValueFactory<SubscriptionItem, String>("position");
	
	public static TableCell<SubscriptionItem, LocalDateTime> getCellFactory(
			TableColumn<SubscriptionItem, LocalDateTime> e) {
		return new Cell();
	}
	
	public static SubscriptionRow create(TableView<SubscriptionItem> table) {
		return new SubscriptionRow();
	}

	public static class Cell extends TableCell<SubscriptionItem, LocalDateTime> {
		@Override
		protected void updateItem(LocalDateTime item, boolean empty) {
			super.updateItem(item, empty);
			
			if(!empty){
				setText(item.format(DateTimeFormatter.ISO_LOCAL_DATE));
			}else{
				setText(null);
			}
		}

	}

	@Override
	protected void updateItem(SubscriptionItem item, boolean empty) {
		super.updateItem(item, empty);
		
		if(empty) return;
		
		if(item.getActive()){
			setStyle("-fx-font-weight: bold;");
		}else{
			setStyle("-fx-font-weight: normal;");
		}
	}

	@Override
	public void updateSelected(boolean empty) {
		super.updateSelected(empty);
		
		if(empty) return;
		
		if(getItem() == null) return;
		
		// When the user clicks, we want to make it look 
		// like the element has been read
		if(getItem().getActive()){
			setStyle("-fx-font-weight: bold;");
		}else{
			setStyle("-fx-font-weight: normal;");
		}
		
	}
	
}
