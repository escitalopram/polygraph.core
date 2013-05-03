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

import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * A PolygraphTagFactory creates Tag object as soon as it is encountered by the
 * template engine. This allows for stateful tag objects.
 * 
 * @author escitalopram
 * 
 */
public interface PolygraphTagFactory {
	PolygraphTag createTag(Map<String, TemplateModel> params, Environment env)	throws InstantiationException, IllegalAccessException, TemplateException;
	String getTagName();
}
