package com.illmeyer.polygraph.core;

public interface AddressSupplier {
	public void initialize();
	public boolean hasNextAddress();
	public Address getNextAddress();
}
