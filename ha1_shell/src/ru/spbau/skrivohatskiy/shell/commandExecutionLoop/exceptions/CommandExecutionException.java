/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class CommandExecutionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CommandExecutionException(String message, Throwable cause) {
	super(message, cause);
    }

    public CommandExecutionException(String message) {
	super(message);
    }

}
