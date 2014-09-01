package jobhunter.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebView;
import jobhunter.utils.JavaFXUtils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

public class WebViewOnClickListener implements EventListener {

	@Override
	public void handleEvent(Event evt) {
		final EventTarget target = evt.getCurrentTarget();
		final HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
		final String href = anchorElement.getHref();
		evt.preventDefault();
		JavaFXUtils.openWebpage(href);
	}
	
	public static void set(final WebView view){
		// We want to handle all anchor click events after the view has loaded
		
		view.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> obs, State old, State current) {
				if(State.SUCCEEDED == current) {
					NodeList nodeList = view.getEngine().getDocument().getElementsByTagName("a");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						EventTarget eventTarget = (EventTarget) node;
						WebViewOnClickListener handler = new WebViewOnClickListener();
						eventTarget.addEventListener("click", handler, false);
					}
				}
			}
		});
	}

}
