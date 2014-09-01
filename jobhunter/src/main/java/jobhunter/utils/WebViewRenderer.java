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

package jobhunter.utils;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebView;
import jobhunter.models.Job;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

public class WebViewRenderer {

	public static void render(final WebView view, Job job) {
		
		final Optional<Object> obj = HTMLRenderer.render(job);
		if(obj.isPresent()){
			final String str = (String) obj.get();
			view.getEngine().loadContent(str);
		}
		
		// We want to handle all anchor click events after the view has loaded
		
		view.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> obs, State old, State current) {
				if(State.SUCCEEDED == current) {
					NodeList nodeList = view.getEngine().getDocument().getElementsByTagName("a");
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node node = nodeList.item(i);
						EventTarget eventTarget = (EventTarget) node;
						OnLinkClickHandler handler = new OnLinkClickHandler();
						eventTarget.addEventListener("click", handler, false);
					}
				}
			}
		});
	}
	
	private static class OnLinkClickHandler implements EventListener {
		@Override
		public void handleEvent(Event evt) {
			final EventTarget target = evt.getCurrentTarget();
			final HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
			final String href = anchorElement.getHref();
			evt.preventDefault();
			JavaFXUtils.openWebpage(href);
		}
	}
}
