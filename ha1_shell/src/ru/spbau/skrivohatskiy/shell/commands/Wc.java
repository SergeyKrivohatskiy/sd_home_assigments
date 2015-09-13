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

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Wc implements CommandExecutor {

    @Override
    public void execute(PrintStream out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    int wc = 0;
	    int lc = 0;
	    for (String fileName : args) {
		List<String> fileLines = Files
			.readAllLines(Paths.get(fileName));
		for (String line : fileLines) {
		    wc += line.trim().split("\\s").length;
		    lc += 1;
		}
	    }
	    out.println(String.format(
		    "%d files contains %d lines and %d words", args.length, lc,
		    wc));
	} catch (IOException e) {
	    throw new CommandExecutionException("Failed to read file", e);
	}
    }

    @Override
    public String getDescription() {
	return "prints lines and chars count for files given as arguments";
    }
}
