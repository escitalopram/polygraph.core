package com.illmeyer.polygraph.core;

/**
 * Implement this interface if you want to receive messages from the Gun
 * @author escitalopram
 *
 */
public interface MessageDispatcher extends ComponentLifeCycle {
	/**
	 * Called whenever the Gun has a complete message. You must take care of dispatching the Messager to the matching Sender.
	 * @param message the message to dispatch
	 */
	public void dispatchMessage(String message);
}
