package jobhunter.gui.dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditorDialog {
	
	private final TextArea html = new TextArea();
	private EventHandler<ActionEvent> handler;
	
	public static EditorDialog create() {
		return new EditorDialog();
	}
	
	public String getHtml() {
		return html.getText();
	}

	public EditorDialog setHtml(String html) {
		this.html.setText(html);
		return this;
	}

	public EditorDialog show() {
		html.setPrefSize(600, 500);
		html.setWrapText(true);
		html.setPadding(new Insets(10.0));
		VBox.setVgrow(html, Priority.ALWAYS);
		
		final VBox box = new VBox(1);
		box.getChildren().add(html);
		box.setPrefSize(600, 500);
		
		final Scene scene = new Scene(box, 600, 500);
		
		final Stage stg = new Stage();
        stg.setTitle("Editor");
        stg.setScene(scene);
        stg.initStyle(StageStyle.UTILITY);
        stg.initModality(Modality.APPLICATION_MODAL);
        
        stg.setOnCloseRequest((e) -> {
        	handler.handle(new ActionEvent(this, html));
        });
        
        stg.show();
        return this;
	}
	
	public EditorDialog setOnSaveEvent(EventHandler<ActionEvent> handler){
		this.handler = handler;
		return this;
	}
	
}
