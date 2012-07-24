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

import org.junit.Test;

import junit.framework.Assert;

public class VersionNumberTest {
	static final VersionNumber v123 = new VersionNumber(1, 2, 3);
	static final VersionNumber v231 = new VersionNumber(2, 3, 1);
	static final VersionNumber v100 = new VersionNumber(1, 0, 0);

	static final VersionNumber v310 = new VersionNumber(3, 1, 0);
	static final VersionNumber v320 = new VersionNumber(3, 2, 0);

	static final VersionNumber v311 = new VersionNumber(3, 1, 1);

	@Test
	public void testMajorRequirements() {
		Assert.assertFalse(v123.meetsRequirement(v231));
		Assert.assertFalse(v231.meetsRequirement(v123));
		Assert.assertTrue(v123.meetsRequirement(v100));
	}

	@Test
	public void testMinorRequirements() {
		Assert.assertFalse(v310.meetsRequirement(v320));
		Assert.assertTrue(v320.meetsRequirement(v310));
		Assert.assertTrue(v320.meetsRequirement(v320));
	}

	@Test
	public void testPatchRequireMents() {
		Assert.assertFalse(v310.meetsRequirement(v311));
		Assert.assertTrue(v311.meetsRequirement(v311));
		Assert.assertTrue(v320.meetsRequirement(v311));
	}
}
