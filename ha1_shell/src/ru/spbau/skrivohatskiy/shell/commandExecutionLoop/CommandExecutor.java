/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

import java.io.Writer;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public interface CommandExecutor {

    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException;

    public String getDescription();
}
