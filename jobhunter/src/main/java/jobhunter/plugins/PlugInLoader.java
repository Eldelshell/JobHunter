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

package jobhunter.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlugInLoader extends Service<List<PlugIn>> {
	
	@Override
	protected Task<List<PlugIn>> createTask() {
		return new PluginLoaderTask();
	}
	
	static class PluginLoaderTask extends Task<List<PlugIn>> {
		
		private static final Logger l = LoggerFactory.getLogger(PlugInLoader.class);

		@Override
		protected List<PlugIn> call() throws Exception {
			
			Reflections reflections = new Reflections("jobhunter.plugin");
			
			Set<Class<? extends PlugIn>> subTypes = reflections.getSubTypesOf(PlugIn.class);
			
			List<PlugIn> plugins = new ArrayList<>();
			
			subTypes.forEach(t -> {
				l.info("Loading PlugIn {}", t.getCanonicalName());
				try {
					plugins.add(t.getConstructor().newInstance());
				} catch (Exception e) {
					l.error("Failed to load PlugIn {}", t.getCanonicalName(), e);
				}
			});
			
			return plugins;
		}
	}

	

}
