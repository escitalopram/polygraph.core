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

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Represents a recipient dataset. Accessible to the template as ${addr}. You can subclass it and add other fields & methods if you like 
 * @author escitalopram
 */
@Data
public class Address {
	/**
	 * represents addressable endpoints (like e-mail address, telephone number, etc.) suitable for sending the generated message.
	 */
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> addrs = new HashMap<String, Object>();
	/**
	 * represents other informations about the recipient, e.g. name, etc.
	 */
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> fields = new HashMap<String, Object>();
}
