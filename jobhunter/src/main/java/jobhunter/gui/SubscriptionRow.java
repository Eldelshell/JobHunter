package jobhunter.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobhunter.models.SubscriptionItem;

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
		
		if(getItem().getActive()){
			setStyle("-fx-font-weight: bold;");
		}else{
			setStyle("-fx-font-weight: normal;");
		}
		
	}
	
}
