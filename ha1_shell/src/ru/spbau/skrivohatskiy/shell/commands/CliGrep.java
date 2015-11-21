/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
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
public class CliGrep extends GrepBase implements CommandExecutor {
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

	    doGrep(out, executionCtx, cl.hasOption("i"), cl.hasOption("w"),
		    Integer.valueOf(cl.getOptionValue("a", "0")), cl.getArgs());
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

    @Override
    public String getDescription() {
	return "Grep description";
    }
}
