/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.Reader;
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
	    if (args.length != 0) {
		return;
	    }
	    Reader stdin = executionCtx.getInput();
	    int data = stdin.read();
	    while (data != -1) {
		out.write(data);
		data = stdin.read();
		out.flush();
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
