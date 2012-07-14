package com.illmeyer.polygraph.core;

public final class CoreConstants {
	/**
	 * Used as key to a custom attribute in the freemarker Environment to store a map of MessageParts used in the current message
	 */
	public static final String ECA_PARTS="polygraph.syslib.parts";
	
	/**
	 * Used as key to a custom attribute in the freemarker Environment to store the currently active MessagePart
	 */
	public static final String ECA_CURRENT_PART="polygraph.syslib.currentPart";
	
	/**
	 * Prevent instantiation
	 */
	private CoreConstants() {
	}
}
