/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
	    Stats stats = new Stats();
	    for (String fileName : args) {
		List<String> fileLines = Files
			.readAllLines(Paths.get(fileName));
		cllectStats(fileLines, stats);
	    }
	    if (args.length == 0) {
		List<String> inputLines = new BufferedReader(
			executionCtx.getInput()).lines().collect(
			Collectors.toList());
		cllectStats(inputLines, stats);
	    }
	    out.write(String.format(
		    "%d files contains %d lines, %d words and %d bytes",
		    args.length, stats.lc, stats.wc, stats.bc));
	    out.write(System.lineSeparator());
	} catch (IOException e) {
	    throw new CommandExecutionException("Failed to read file", e);
	}
    }

    private void cllectStats(List<String> lines, Stats stats) {
	for (String line : lines) {
	    stats.bc += line.getBytes().length;
	    stats.wc += line.trim().split("\\s+").length;
	    stats.lc += 1;
	}
    }

    @Override
    public String getDescription() {
	return "prints lines and chars count for files given as arguments";
    }

    private static class Stats {
	public int bc = 0;
	public int wc = 0;
	public int lc = 0;
    }
}
