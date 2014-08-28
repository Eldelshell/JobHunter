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

package jobhunter.api.infojobs;

import jobhunter.api.infojobs.model.Error;

public class InfoJobsAPIException extends Exception {

	private static final long serialVersionUID = -3960021636725516908L;
	
	private final Error error;

	public InfoJobsAPIException(Error error) {
		super();
		this.error = error;
	}
	
	public InfoJobsAPIException(String error) {
		super();
		Error err = new Error();
		err.setErrorDescription(error);
		this.error = err;
	}

	public Error getError() {
		return error;
	}

	@Override
	public String getMessage() {
		return error.getErrorDescription();
	}
	
}
