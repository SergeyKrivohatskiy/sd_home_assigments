/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

import java.io.PrintStream;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public interface CommandExecutor {

    public void execute(PrintStream out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException;

    public String getDescription();
}
