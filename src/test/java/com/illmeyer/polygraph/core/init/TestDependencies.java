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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.junit.Assert;
import org.junit.Test;

import com.illmeyer.polygraph.core.data.DependencySpecification;
import com.illmeyer.polygraph.core.data.VersionNumber;
import com.illmeyer.polygraph.core.init.DependencyResolver;
import com.illmeyer.polygraph.core.interfaces.Module;
import com.illmeyer.polygraph.core.spi.Extension;

public class TestDependencies {
	static final VersionNumber v010 = new VersionNumber(0, 1, 0);
	@Test
	public void testSimpleDependency() {
		Extension e1 = new E1(new DependencySpecification[]{new DependencySpecification(E2.class.getName(),v010)});
		Extension e2 = new E2(new DependencySpecification[]{});

		DependencyResolver r = new DependencyResolver(Arrays.asList(new Module[]{e1,e2}));
		r.checkDependencies();
		Assert.assertEquals(0, r.getUnsatisfiedModules().size());
	}
	@Test
	public void testSimpleDependencyFail() {
		Extension e1 = new E1(new DependencySpecification[]{new DependencySpecification(E3.class.getName(),v010)});
		Extension e2 = new E2(new DependencySpecification[]{});

		DependencyResolver r = new DependencyResolver(Arrays.asList(new Module[]{e1,e2}));
		r.checkDependencies();
		Assert.assertEquals(1, r.getUnsatisfiedModules().size());
		Assert.assertTrue(r.getUnsatisfiedModules().get(0).endsWith(".E1"));
	}
	@Test
	public void testCircularDependency() {
		Extension e1 = new E1(new DependencySpecification[]{new DependencySpecification(E2.class.getName(),v010)});
		Extension e2 = new E2(new DependencySpecification[]{new DependencySpecification(E1.class.getName(),v010)});
		DependencyResolver r = new DependencyResolver(Arrays.asList(new Module[]{e1,e2}));
		r.checkDependencies();
		Assert.assertEquals(0, r.getUnsatisfiedModules().size());
	}
}
@AllArgsConstructor
class E1 implements Extension {

	DependencySpecification[] dependencies;
	
	@Override
	public void initialize() {
	}

	@Override
	public Map<String, Object> createContext() {
		return null;
	}

	@Override
	public void destroy() {
	}

	@Override
	public List<DependencySpecification> getRequiredExtensions() {
		return Arrays.asList(dependencies);
	}

	@Override
	public VersionNumber getVersionNumber() {
		return TestDependencies.v010;
	}
	
}
class E2 extends E1 {
	public E2(DependencySpecification[] dependencies) {
		super(dependencies);
	}
}
class E3 extends E1 {

	public E3(DependencySpecification[] dependencies) {
		super(dependencies);
	}
}