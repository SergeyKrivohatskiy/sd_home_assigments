/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Man implements CommandExecutor {

    @Override
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    if (args.length == 0) {
		out.write("Available commands:");
		out.write(System.lineSeparator());
		Set<String> commandsAvailable = executionCtx
			.getCommandExecutorNames();
		for (String commandName : commandsAvailable) {
		    out.write(commandName);
		    out.write(System.lineSeparator());
		}
		return;
	    }

	    CommandExecutor command = executionCtx.getCommandExecutor(args[0]);
	    if (command == null) {
		out.write("Command " + args[0] + " not found");
		out.write(System.lineSeparator());
	    } else {
		out.write("Command " + args[0]);
		out.write(System.lineSeparator());
		out.write(command.getDescription());
		out.write(System.lineSeparator());
	    }
	} catch (IOException e) {
	    throw new CommandExecutionException("Failed to write output", e);
	}
    }

    @Override
    public String getDescription() {
	return "shows information for command given as the first argument";
    }

}
