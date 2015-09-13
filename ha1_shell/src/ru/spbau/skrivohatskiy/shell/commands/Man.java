/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.PrintStream;
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
    public void execute(PrintStream out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	if (args.length == 0) {
	    out.println("Available commands:");
	    Set<String> commandsAvailable = executionCtx
		    .getCommandExecutorNames();
	    for (String commandName : commandsAvailable) {
		out.println(commandName);
	    }
	    return;
	}

	CommandExecutor command = executionCtx.getCommandExecutor(args[0]);
	if (command == null) {
	    out.println("Command " + args[0] + " not found");
	} else {
	    out.println("Command " + args[0]);
	    out.println(command.getDescription());
	}

    }

    @Override
    public String getDescription() {
	return "shows information for command given as the first argument";
    }

}
