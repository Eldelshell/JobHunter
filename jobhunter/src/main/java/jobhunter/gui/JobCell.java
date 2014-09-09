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
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import jobhunter.models.Job;

public class JobCell extends ListCell<Job> {
	
	private final AnchorPane container;
	private final Label label1;
	private final Label label2;
	
	public JobCell() {
		super();
		this.container = new AnchorPane();
		this.label1 = new Label();
		this.label2 = new Label();
		
		label1.setId("job-list-view-label-1");
		AnchorPane.setLeftAnchor(label1, 5.0);
		AnchorPane.setRightAnchor(label1, 5.0);
		AnchorPane.setTopAnchor(label1, 5.0);
		
		label2.setId("job-list-view-label-2");
		AnchorPane.setLeftAnchor(label2, 5.0);
		AnchorPane.setRightAnchor(label2, 5.0);
		AnchorPane.setBottomAnchor(label2, 1.0);
		
		this.container.setPrefSize(200, 30);
		this.container.getChildren().add(label1);
		this.container.getChildren().add(label2);
		
	}

	@Override
	protected void updateItem(Job job, boolean empty) {
		super.updateItem(job, empty);
		
		if(!empty) {
			label1.setText(job.getPosition());
			label2.setText(job.getCompany().getName());
			
			if(!job.getActive()) {
				label1.getStyleClass().add("deleted-label");
			}else{
				label1.getStyleClass().remove("deleted-label");
			}
			
			this.setGraphic(container);
		}else{
			this.setText(null);
			this.setGraphic(null);
			label1.getStyleClass().remove("deleted-label");
		}
		
	}
	
	static class JobCellCallback implements Callback<ListView<Job>, ListCell<Job>> {

        @Override
        public ListCell<Job> call(ListView<Job> p) {
            return new JobCell();
        }
        
    }
	
}
