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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import freemarker.template.TemplateModel;
import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.utility.DeepUnwrap;

import lombok.Getter;

/**
 * Creates Tag objects based on the annotation of the chosen tag class.
 * 
 * @author escitalopram
 * 
 */
public class DefaultTagFactory implements PolygraphTagFactory {

	@Getter
	final Class<? extends PolygraphTag> tagClass;

	public DefaultTagFactory(Class<? extends PolygraphTag> tagClass) {
		this.tagClass=tagClass;
	}

	@Override
	public PolygraphTag createTag(Map<String, TemplateModel> params, Environment env) throws InstantiationException, IllegalAccessException, TemplateException {
		PolygraphTag tag = tagClass.newInstance();
		Map<Field, TagParameter> fields = getAnnotatedFields(tag);
		Set<String> validParamNames=new HashSet<>();
		for (Entry<Field, TagParameter> e : fields.entrySet()) {
			String name = e.getKey().getName();
			String requestedParameterName = e.getValue().name(); 
			if (!"".equals(requestedParameterName) && requestedParameterName!=null) {
				name=e.getValue().name();
			}
			validParamNames.add(name);
			if(!e.getValue().optional() && !params.containsKey(name))
				throw new TemplateException(String.format("required parameter '%s' not set",name),env);
			if(params.containsKey(name)) {
				e.getKey().setAccessible(true);
				Object unwrapped=(String)DeepUnwrap.unwrap(params.get(name));
				if (e.getKey().getType().isEnum()) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Object enumValue = Enum.valueOf((Class<? extends Enum>)(e.getKey().getType()), (String)unwrapped);
					e.getKey().set(tag, enumValue);
				} else {
					e.getKey().set(tag, unwrapped);
				}
			}
		}
		for (String pname : params.keySet())
			if (!validParamNames.contains(pname))
				throw new TemplateException(String.format("invalid parameter name '%s'", pname), env);
		return tag;
	}
	/**
	 * returns fields annotated with TagParameter. TODO: Implement proper inheritance and use properties instead of fields
	 */
	private Map<Field,TagParameter> getAnnotatedFields(PolygraphTag tag) {
		HashMap<Field,TagParameter> result = new HashMap<>();
		for (Field f: tag.getClass().getDeclaredFields()) {
			TagParameter tp = f.getAnnotation(TagParameter.class);
			if(tp!=null)
				result.put(f, tp);
		}
		return result;
	}
	@Override
	public String getTagName() {
		return tagClass.getAnnotation(TagInfo.class).name();
	}
}
