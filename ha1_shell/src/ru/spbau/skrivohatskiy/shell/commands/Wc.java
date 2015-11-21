/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.IntSummaryStatistics;
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
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    int wc = 0;
	    int lc = 0;
	    for (String fileName : args) {
		List<String> fileLines = Files
			.readAllLines(Paths.get(fileName));
		IntSummaryStatistics stats = fileLines.stream()
			.mapToInt(line -> {
			    return line.trim().split("\\s+").length;
			}).summaryStatistics();
		wc += stats.getSum();
		lc += stats.getCount();
	    }
	    if (args.length == 0) {
		BufferedReader reader = new BufferedReader(
			executionCtx.getInput());
		IntSummaryStatistics stats = reader.lines().mapToInt(line -> {
		    return line.trim().split("\\s").length;
		}).summaryStatistics();
		wc += stats.getSum();
		lc += stats.getCount();
	    }
	    out.write(String.format("%d files contains %d lines and %d words",
		    args.length, lc, wc));
	    out.write(System.lineSeparator());
	} catch (IOException e) {
	    throw new CommandExecutionException("Failed to read file", e);
	}
    }

    @Override
    public String getDescription() {
	return "prints lines and chars count for files given as arguments";
    }
}
