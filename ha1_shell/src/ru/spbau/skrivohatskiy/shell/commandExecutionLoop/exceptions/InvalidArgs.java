/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class InvalidArgs extends CommandExecutionException {
    private static final long serialVersionUID = 1L;

    public InvalidArgs(String message) {
	super(message);
    }

    public InvalidArgs(String message, Throwable cause) {
	super(message, cause);
    }
}
