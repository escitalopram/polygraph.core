package com.illmeyer.polygraph.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		for(String dep : currentNode.getDependencies()) {
			if(!modules.containsKey(dep))
				dependencyFailed=true;
			else {
				DependencyNode toCheck = modules.get(dep);
				checkDependenciesOf(dep);
				if (!toCheck.isSatisfied() && !toCheck.isInCheckProcess()) dependencyFailed=true;
			}
		}
		currentNode.setInCheckProcess(false);
		currentNode.setChecked(true);
		if (!dependencyFailed)
			currentNode.setSatisfied(true);
	}
}
