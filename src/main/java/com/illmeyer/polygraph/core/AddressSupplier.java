package com.illmeyer.polygraph.core;

import java.util.Enumeration;

/**
 * Supplies Address objects to the Gun
 * @author escitalopram
 *
 */
public interface AddressSupplier extends Enumeration<Address>, ComponentLifeCycle {
	/** reset enumeration to first element */
	public void reset();
}
