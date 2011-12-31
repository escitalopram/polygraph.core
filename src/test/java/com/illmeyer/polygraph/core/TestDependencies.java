package com.illmeyer.polygraph.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;

import org.junit.Assert;
import org.junit.Test;

public class TestDependencies {
	@Test
	public void testSimpleDependency() {
		Extension e1 = new E1(new String[]{E2.class.getName()});
		Extension e2 = new E2(new String[]{});

		DependencyResolver r = new DependencyResolver(Arrays.asList(new Module[]{e1,e2}));
		r.checkDependencies();
		Assert.assertEquals(0, r.getUnsatisfiedModules().size());
	}
	@Test
	public void testSimpleDependencyFail() {
		Extension e1 = new E1(new String[]{E3.class.getName()});
		Extension e2 = new E2(new String[]{});

		DependencyResolver r = new DependencyResolver(Arrays.asList(new Module[]{e1,e2}));
		r.checkDependencies();
		Assert.assertEquals(1, r.getUnsatisfiedModules().size());
		Assert.assertTrue(r.getUnsatisfiedModules().get(0).endsWith(".E1"));
	}
	@Test
	public void testCircularDependency() {
		Extension e1 = new E1(new String[]{E2.class.getName()});
		Extension e2 = new E2(new String[]{E1.class.getName()});
		DependencyResolver r = new DependencyResolver(Arrays.asList(new Module[]{e1,e2}));
		r.checkDependencies();
		Assert.assertEquals(0, r.getUnsatisfiedModules().size());
	}
}
@AllArgsConstructor
class E1 implements Extension {

	String[] dependencies;
	
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
	public List<String> getRequiredExtensions() {
		return Arrays.asList(dependencies);
	}
	
}
class E2 extends E1 {
	public E2(String[] dependencies) {
		super(dependencies);
	}
}
class E3 extends E1 {

	public E3(String[] dependencies) {
		super(dependencies);
	}
}