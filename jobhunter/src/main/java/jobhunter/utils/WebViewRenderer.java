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

import javafx.animation.FadeTransition;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import jobhunter.models.Job;
import jobhunter.models.SubscriptionItem;

public class WebViewRenderer {
	
	private final WebView view;
	private final FadeTransition anim;
	
	public WebViewRenderer(WebView view) {
		super();
		this.view = view;
		anim = new FadeTransition();
		anim.setDuration(Duration.millis(300));
		anim.setFromValue(0);
		anim.setToValue(1);
		anim.setNode(this.view);
	}
	
	public static WebViewRenderer create(WebView view) {
		return new WebViewRenderer(view);
	}

	public void render(Job job) {
		HTMLRenderer.of().render(job).ifPresent(obj -> {
			view.getEngine().loadContent((String) obj);
			anim.playFromStart();
		});
	}
	
	public void render(SubscriptionItem item) {
		HTMLRenderer.of().render(item).ifPresent(obj -> {
			view.getEngine().loadContent((String) obj);
			anim.playFromStart();
		});
	}
	
}
