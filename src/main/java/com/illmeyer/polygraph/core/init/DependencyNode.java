package com.illmeyer.polygraph.core.init;

import java.util.ArrayList;
import java.util.List;

import com.illmeyer.polygraph.core.interfaces.ExtensionDependent;
import com.illmeyer.polygraph.core.interfaces.Module;
import com.illmeyer.polygraph.core.spi.Template;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
class DependencyNode {
	DependencyNode(Module m) {
		name=m.getClass().getName();
		if (m instanceof ExtensionDependent) {
			ExtensionDependent ed  = (ExtensionDependent) m;
			dependencies.addAll(ed.getRequiredExtensions());
		}
		if (m instanceof Template) {
			String mt = ((Template)m).getMessageType();
			if (mt!=null && !mt.isEmpty())dependencies.add(mt);
		}
	}
	DependencyNode(String name, List<String>dependencies){
		this.name=name;
		this.dependencies=dependencies;
	}
	private String name;
	private List<String> dependencies = new ArrayList<String>();
	private boolean satisfied=false;
	private boolean inCheckProcess=false;
	private boolean checked=false;
}
