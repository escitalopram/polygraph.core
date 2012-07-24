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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.illmeyer.polygraph.core.data.DependencySpecification;
import com.illmeyer.polygraph.core.interfaces.Module;

import lombok.Getter;

public class DependencyResolver {
	private Map<String,DependencyNode> modules;
	@Getter
	private List<String> unsatisfiedModules = new ArrayList<String>();
	public DependencyResolver(List<Module> modules) {
		this.modules=new HashMap<String,DependencyNode>();
		for(Module m:modules) this.modules.put(m.getClass().getName(),new DependencyNode(m));
	}
	public void checkDependencies() {
		for (Entry<String, DependencyNode> e :modules.entrySet()) {
			String currentName=e.getKey();
			checkDependenciesOf(currentName);
		}
		for (Entry<String, DependencyNode> m :modules.entrySet()) {
			if(!m.getValue().isSatisfied()) {
				unsatisfiedModules.add(m.getKey());
			}
		}
	}
	private void checkDependenciesOf(String currentName) {
		DependencyNode currentNode=modules.get(currentName);
		if (currentNode.isChecked()) return;
		if (currentNode.isInCheckProcess()) return; // ???

		currentNode.setInCheckProcess(true);
		boolean dependencyFailed=false;
		for(DependencySpecification dep : currentNode.getDependencies()) {
			if(!modules.containsKey(dep.getModuleName())) 
				dependencyFailed=true;
			else {
				DependencyNode toCheck = modules.get(dep.getModuleName());
				if (!toCheck.getVersionNumber().meetsRequirement(dep.getVersion())) {
					dependencyFailed=true;
				} else {
					checkDependenciesOf(dep.getModuleName());
					if (!toCheck.isSatisfied() && !toCheck.isInCheckProcess()) dependencyFailed=true;
				}
			}
		}
		currentNode.setInCheckProcess(false);
		currentNode.setChecked(true);
		if (!dependencyFailed)
			currentNode.setSatisfied(true);
	}
}
