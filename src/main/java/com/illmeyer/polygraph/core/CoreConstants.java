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

package com.illmeyer.polygraph.core;

public final class CoreConstants {
	/**
	 * Used as key to a custom attribute in the freemarker Environment to store a map of MessageParts used in the current message
	 */
	public static final String ECA_PARTS="polygraph.syslib.parts";
	
	/**
	 * Stack of Tags
	 */
	public static final String ECA_TAGSTACK="polygraph.core.tagStack";

	/**
	 * Set a value for this key in the environment to skip processing the current message
	 */
	public static final String ECA_SKIP = "polygraph.core.skip";
	
	/**
	 * Prevent instantiation
	 */
	private CoreConstants() {
	}
}
