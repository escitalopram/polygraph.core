/*
This file is part of the Polygraph bulk messaging framework
Copyright (C) 2012 Wolfgang Illmeyer

The Polygraph framework is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.illmeyer.polygraph.core.data;

import lombok.Data;

/**
 * Specifies a dependency. Equality is reduced to the module name so dependency
 * specifications can be searched for more easily
 *
 */
@Data
public class DependencySpecification {
	private final String moduleName;
	private final VersionNumber version;
	
	@Override
	public boolean equals(Object o) {
		if (o==null) return false;
		if (!o.getClass().equals(DependencySpecification.class)) return false;
		return ((DependencySpecification)o).moduleName.equals(moduleName);
	}

	@Override
	public int hashCode() {
		return moduleName.hashCode();
	}

}
