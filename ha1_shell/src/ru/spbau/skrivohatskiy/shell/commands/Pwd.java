/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Pwd implements CommandExecutor {

    @Override
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    out.write(Paths.get("").toAbsolutePath().toString());
	    out.write(System.lineSeparator());
	} catch (IOException e) {
	    throw new CommandExecutionException("Failed to write output", e);
	}
    }

    @Override
    public String getDescription() {
	return "prints current dir name";
    }
}
