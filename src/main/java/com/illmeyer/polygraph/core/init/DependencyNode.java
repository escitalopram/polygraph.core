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

package com.illmeyer.polygraph.core.init;

import java.util.ArrayList;
import java.util.List;

import com.illmeyer.polygraph.core.data.DependencySpecification;
import com.illmeyer.polygraph.core.data.VersionNumber;
import com.illmeyer.polygraph.core.interfaces.ExtensionDependent;
import com.illmeyer.polygraph.core.interfaces.Module;
import com.illmeyer.polygraph.core.spi.Template;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
class DependencyNode {
	DependencyNode(Module m) {
		name=m.getClass().getName();
		versionNumber=m.getVersionNumber();
		if (m instanceof ExtensionDependent) {
			ExtensionDependent ed  = (ExtensionDependent) m;
			dependencies.addAll(ed.getRequiredExtensions());
		}
		if (m instanceof Template) {
			DependencySpecification mt = ((Template)m).getMessageType();
			if (mt!=null)dependencies.add(mt);
		}
	}
	DependencyNode(String name, List<DependencySpecification>dependencies){
		this.name=name;
		this.dependencies=dependencies;
	}
	private String name;
	private List<DependencySpecification> dependencies = new ArrayList<DependencySpecification>();
	private boolean satisfied=false;
	private boolean inCheckProcess=false;
	private boolean checked=false;
	private VersionNumber versionNumber; 
}
