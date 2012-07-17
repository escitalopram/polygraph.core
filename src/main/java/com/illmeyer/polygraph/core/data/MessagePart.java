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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessagePart implements Serializable {
	private static final long serialVersionUID = 466508946491072736L;
	
	private byte[] message = null;
	private String encoding = null;
	final private Map<String,Object> properties = new HashMap<String,Object>();
	
	public String getStringMessage() {
		try {
			return new String(message, encoding);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setStringMessage(String s) {
		try {
			setStringMessage(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// never happens
		}
	}
	public void setStringMessage(String message, String encoding) throws UnsupportedEncodingException {
		this.message=message.getBytes(encoding);
		this.encoding=encoding;
	}
}
