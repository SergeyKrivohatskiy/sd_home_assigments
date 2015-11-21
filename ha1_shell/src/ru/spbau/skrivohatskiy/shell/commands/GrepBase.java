/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public abstract class GrepBase {

    protected void doGrep(Writer out, CommandExecutionContext executionCtx,
	    boolean iOption, boolean wOption, int additionalLines,
	    String[] parsedArgs) throws CommandExecutionException, IOException,
	    FileNotFoundException {
	if (parsedArgs.length == 0) {
	    throw new CommandExecutionException("No expression provided");
	}
	String expression = parsedArgs[parsedArgs.length - 1];
	if (parsedArgs.length == 1) {
	    doGrepSingleInput(out, executionCtx.getInput(), iOption, wOption,
		    additionalLines, expression);
	    return;
	}
	for (int idx = 0; idx < parsedArgs.length - 1; idx += 1) {
	    String filename = parsedArgs[idx];
	    Reader inputFile = new FileReader(filename);
	    out.write("File: " + filename);
	    out.write(System.lineSeparator());
	    doGrepSingleInput(out, inputFile, iOption, wOption, additionalLines,
		    expression);
	}
    }

    private void doGrepSingleInput(Writer out, Reader input, boolean iOption,
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

}
