/*
This file is part of the Polygraph bulk messaging framework
Copyright (C) 2013 Wolfgang Illmeyer

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

package com.illmeyer.polygraph.template;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TagAdapter implements TemplateDirectiveModel {

	private PolygraphTagFactory tf;
	
	public TagAdapter(PolygraphTagFactory tf) {
		this.tf=tf;
	}

	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map rawparams, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		PolygraphEnvironment pe = new PolygraphEnvironment(loopVars, env, body);
		@SuppressWarnings("unchecked")
		Map<String,TemplateModel> params=(Map<String,TemplateModel>)rawparams;
		PolygraphTag tag;
		try {
			tag = tf.createTag(params,env);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Could not instantiate Tag",e);
		}
		TagInfo ti=tag.getClass().getAnnotation(TagInfo.class);
		if (ti==null)
			throw new TemplateException(String.format("%s is not a valid Tag class", tag.getClass().getName()),env);
		if (ti.loopVarCount()!=loopVars.length)
			throw new TemplateException(String.format("%s needs exactly %i loop variables, but got %i",tf.getClass().getName(),ti.loopVarCount(),loopVars.length), env);
		if (!ti.nestable()) 
			for (PolygraphTag st : pe.getTagStack())
				if (tag.getClass().equals(st.getClass()))
					throw new TemplateException("Nesting this tag is not allowed", env);
		pe.getTagStack().push(tag);
		try {
			tag.execute(pe);
		} catch (PolygraphTemplateException e) {
			throw new TemplateException(e, env);
		} finally {
			pe.getTagStack().pop();
		}
	}
	
	public void register(Map<String,Object> env) {
		env.put(tf.getTagName(),this);
	}
}
