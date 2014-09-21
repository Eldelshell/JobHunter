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

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import jobhunter.models.Subscription;

public class SubscriptionListCell extends ListCell<Subscription> {
	
	private static Image DEFAULT;
	
	private static final String label_text = "%s (%d)";
	
	private final AnchorPane container;
	private final Label label;
	private final ImageView image;
	
	public SubscriptionListCell() {
		super();
		this.container = new AnchorPane();
		this.label = new Label();
		this.image = new ImageView();
		
		this.label.setLayoutX(25);
		this.label.setLayoutY(1);
		this.label.getStyleClass().add("feed-list-view-label");
		
		this.image.setLayoutX(0);
		this.image.setLayoutY(1);
		this.image.getStyleClass().add("feed-list-view-icon");
		
		this.container.setPrefSize(200, 20);
		this.container.setLayoutX(0);
		this.container.setLayoutY(0);
		this.container.getChildren().add(label);
		this.container.getChildren().add(image);
		
	}

	@Override
	protected void updateItem(Subscription subscription, boolean empty) {
		super.updateItem(subscription, empty);
		
		if(!empty) {
			image.setImage(getIcon(subscription));
			
			if(subscription.unreadItems() > 0){
				label.getStyleClass().add("feed-list-view-label-highlight");
				label.setText(
					String.format(
						label_text, 
						subscription.getTitle(), 
						subscription.unreadItems()
					)
				);
			}else{
				label.getStyleClass().removeAll("feed-list-view-label-highlight");
				label.setText(subscription.getTitle());
			}
			
			this.setGraphic(container);
		}else{
			this.setText(null);
			this.setGraphic(null);
		}
		
	}
	
	private Image getIcon(final Subscription sub) {
		if(DEFAULT == null)
			DEFAULT = new Image(getClass().getResourceAsStream("/images/rss.png"));
		
		return DEFAULT;
	}
	
	public static class CellCallback 
		implements Callback<ListView<Subscription>, ListCell<Subscription>> {

        @Override
        public ListCell<Subscription> call(ListView<Subscription> p) {
            return new SubscriptionListCell();
        }
        
    }
	
}
