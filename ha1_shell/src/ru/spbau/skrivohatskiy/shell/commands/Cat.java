/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Cat implements CommandExecutor {

    @Override
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	if (args.length < 1) {
	    throw new CommandExecutionException("Need at least one argument");
	}
	try {
	    for (int idx = 0; idx < args.length; idx += 1) {
		String fileName = args[idx];
		List<String> fileLines = Files
			.readAllLines(Paths.get(fileName));
		for (String line : fileLines) {
		    out.write(line);
		    out.write(System.lineSeparator());
		}
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
