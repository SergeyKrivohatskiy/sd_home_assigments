/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class CliGrep implements CommandExecutor {
    private final CommandLineParser parser = new DefaultParser();
    private final Options options = new Options();

    public CliGrep() {
	options.addOption("w", false, "w description");
	options.addOption("i", false, "i description");
	options.addOption("a", true, "a description");
    }

    @Override
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    CommandLine cl = parser.parse(options, args);
	    boolean iOption = cl.hasOption("i");
	    boolean wOption = cl.hasOption("w");
	    int additionalLines = Integer.valueOf(cl.getOptionValue("a", "0"));

	    if (cl.getArgs().length == 0) {
		throw new CommandExecutionException("No expression provided");
	    }
	    String expression = cl.getArgs()[cl.getArgs().length - 1];
	    if (cl.getArgs().length == 1) {
		doGrep(out, executionCtx.getInput(), iOption, wOption,
			additionalLines, expression);
		return;
	    }
	    for (int idx = 0; idx < cl.getArgs().length - 1; idx += 1) {
		String filename = cl.getArgs()[idx];
		Reader inputFile = new FileReader(filename);
		out.write("File: " + filename);
		out.write(System.lineSeparator());
		doGrep(out, inputFile, iOption, wOption, additionalLines,
			expression);
	    }
	} catch (ParseException e) {
	    // hide the implementation
	    throw new CommandExecutionException(e.getMessage());
	} catch (IOException e) {
	    throw new CommandExecutionException(
		    "Failed to read input or file or write output", e);
	} catch (NumberFormatException e) {
	    throw new CommandExecutionException("Failed to parse 'a' option", e);
	}
    }

    private void doGrep(Writer out, Reader input, boolean iOption,
	    boolean wOption, int additionalLines, String expression)
	    throws IOException {
	BufferedReader in = new BufferedReader(input);
	boolean starts = expression.startsWith("^");
	boolean ends = expression.endsWith("$");
	expression = expression.substring(starts ? 1 : 0, expression.length()
		- (ends ? 1 : 0));
	if (iOption) {
	    expression = expression.toLowerCase();
	}
	if (wOption) {
	    expression = " " + expression + " ";
	}
	String lineRed;
	while ((lineRed = in.readLine()) != null) {
	    String lineToCheck = iOption ? lineRed.toLowerCase() : lineRed;
	    if (wOption) {
		lineToCheck = " " + lineToCheck + " ";
	    }
	    if ((starts && !lineToCheck.startsWith(expression))
		    || (ends && !lineToCheck.endsWith(expression))
		    || (!lineToCheck.contains(expression))) {
		continue;
	    }
	    out.write(lineRed);
	    out.write(System.lineSeparator());
	    for (int i = 0; i < additionalLines; i += 1) {
		if ((lineRed = in.readLine()) == null) {
		    return;
		}
		out.write(lineRed);
		out.write(System.lineSeparator());
	    }
	}
    }

    @Override
    public String getDescription() {
	return options.toString();
    }
}
