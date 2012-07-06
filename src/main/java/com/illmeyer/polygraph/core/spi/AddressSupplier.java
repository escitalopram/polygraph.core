package com.illmeyer.polygraph.core.spi;

import java.util.Enumeration;

import com.illmeyer.polygraph.core.data.Address;
import com.illmeyer.polygraph.core.interfaces.ComponentLifeCycle;

/**
 * Supplies Address objects to the Gun
 * @author escitalopram
 *
 */
public interface AddressSupplier extends Enumeration<Address>, ComponentLifeCycle {
	/** reset enumeration to first element */
	public void reset();
}
