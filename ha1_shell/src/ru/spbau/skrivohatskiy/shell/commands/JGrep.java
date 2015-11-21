/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.internal.Lists;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class JGrep extends GrepBase implements CommandExecutor {
    private static class JGrepParameters {
	@Parameter
	public List<String> parameters = Lists.newArrayList();

	@Parameter(names = { "-a" }, description = "a description")
	public Integer additionalLines = 1;

	@Parameter(names = "-i", description = "i description")
	public boolean iOption = false;

	@Parameter(names = "-w", description = "w description")
	public boolean wOption = false;
    }

    @Override
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    JGrepParameters params = new JGrepParameters();
	    new JCommander(params, args);
	    String parsedArgs[] = new String[params.parameters.size()];
	    params.parameters.toArray(parsedArgs);
	    doGrep(out, executionCtx, params.iOption, params.wOption,
		    params.additionalLines, parsedArgs);
	} catch (ParameterException e) {
	    // hide the implementation
	    throw new CommandExecutionException("Illegal arguments");
	} catch (IOException e) {
	    throw new CommandExecutionException(
		    "Failed to read input or file or write output", e);
	}
    }

    @Override
    public String getDescription() {
	return "Grep description";
    }
}
