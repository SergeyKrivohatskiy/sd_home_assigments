/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.PrintStream;
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
    public void execute(PrintStream out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	out.println(Paths.get("").toAbsolutePath().toString());
    }

    @Override
    public String getDescription() {
	return "prints current dir name";
    }
}
