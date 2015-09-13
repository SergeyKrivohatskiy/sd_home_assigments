/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class UnknownCommand extends Exception {

    private static final long serialVersionUID = 1L;

    public UnknownCommand(String commandName) {
	super("Command " + commandName + " not found");
    }

}
