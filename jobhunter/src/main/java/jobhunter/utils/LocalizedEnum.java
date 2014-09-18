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

import jobhunter.models.ActivityLog.Type;
import jobhunter.models.Job.Status;

/**
 * An special class to store an Enum with its translation.
 * @param <T>
 */
public class LocalizedEnum<T extends Enum<?>> {

	private final T _enum;
	private final String message;
	
	public static LocalizedEnum<Status> of(Status status){
		String message = ApplicationState.getBundle().getString("job.status." + status.name().toLowerCase());
		return new LocalizedEnum<Status>(status, message);
	}
	
	public static LocalizedEnum<Type> of(Type status){
		String message = ApplicationState.getBundle().getString("activity.log." + status.name().toLowerCase());
		return new LocalizedEnum<Type>(status, message);
	}
	
	private LocalizedEnum(T status, String message) {
		super();
		this._enum = status;
		this.message = message;
	}

	public T getEnum() {
		return _enum;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_enum == null) ? 0 : _enum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalizedEnum<?> other = (LocalizedEnum<?>) obj;
		if (_enum != other._enum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return message;
	}
	
}
