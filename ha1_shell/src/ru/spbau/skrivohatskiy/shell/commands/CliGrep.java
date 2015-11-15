/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commands;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    }

    @Override
    public void execute(Writer out, String[] args,
	    CommandExecutionContext executionCtx)
	    throws CommandExecutionException {
	try {
	    CommandLine cl = parser.parse(options, args);
	    List<String> argsList = cl.getArgList();
	    String expression = argsList.get(1);
	    String filename = argsList.get(0);
	    if (cl.hasOption('i')) {
		expression = expression.toLowerCase();
	    }
	    List<String> fileContent = Files.readAllLines(Paths.get(filename));
	    if (i) {
		fileContent = fileContent.stream().map(l -> l.toLowerCase())
			.collect(Collectors.toList());
	    }

	    Iterator<String> lines = fileContent.iterator();
	    while (lines.hasNext()) {
		boolean ok = true;
		String line = lines.next();
		if (startsWith) {
		    ok = ok && line.startsWith(expression);
		    if (endsWith) {
			ok = ok && line.equals(expression);
		    }
		    if (w) {
			ok = ok && line.startsWith(expression + " ");
		    }
		}
		if (endsWith) {
		    ok = ok && line.endsWith(expression);
		    if (w) {
			ok = ok && line.endsWith(" " + expression);
		    }
		}
		if (((startsWith || endsWith) && ok)
			|| (" " + line + " ").indexOf(w ? " " + expression
				+ " " : expression) != -1) {
		    writer.write(line + System.lineSeparator());
		    for (int idx = 0; idx < n && lines.hasNext(); idx += 1) {
			writer.write(lines.next() + System.lineSeparator());
		    }
		}
	    }
	    writer.flush();
	} catch (IOException e) {
	    writer.write("Failed to read file " + filename);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public String getDescription() {
	return null;
    }

}
