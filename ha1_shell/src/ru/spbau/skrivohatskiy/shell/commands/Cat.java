/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.InvalidArgs;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Cat implements CommandExecutor {

    @Override
    public void execute(PrintStream out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	if (args.length < 1) {
	    throw new InvalidArgs("Need at least one argument");
	}
	try {
	    String fileName = args[0];
	    List<String> fileLines = Files.readAllLines(Paths.get(fileName));
	    for (String line : fileLines) {
		out.println(line);
	    }
	} catch (IOException e) {
	    throw new CommandExecutionException("Failed to read file", e);
	}
    }

    @Override
    public String getDescription() {
	return "prints a content of each filename given as an argument";
    }
}
