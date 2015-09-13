/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandsLoadingException;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.InvalidArgs;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.UnknownCommand;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class CommandExecutionLoop implements CommandExecutionContext {
    private final String exitCommand;
    private final SortedMap<String, CommandExecutor> executors;
    private final BufferedReader reader;
    private final PrintStream out;

    public CommandExecutionLoop() throws CommandsLoadingException {
	this("ru.spbau.skrivohatskiy.shell.commands");
    }

    public CommandExecutionLoop(String commandsPackage)
	    throws CommandsLoadingException {
	this("exit", commandsPackage, System.in, System.out);
    }

    public CommandExecutionLoop(String exitCommand, String commandsPackage,
	    InputStream in, PrintStream out) throws CommandsLoadingException {
	this.exitCommand = exitCommand.toLowerCase();
	reader = new BufferedReader(new InputStreamReader(in));
	this.out = out;
	try {
	    SortedMap<String, CommandExecutor> executorsModifiableMap = new TreeMap<>();
	    Class<?>[] classes = Utils.getClasses(commandsPackage);
	    for (Class<?> clazz : classes) {
		try {
		    if (!CommandExecutor.class.isAssignableFrom(clazz)) {
			continue;
		    }
		    CommandExecutor newExecutor;
		    newExecutor = (CommandExecutor) clazz.getConstructor()
			    .newInstance();
		    executorsModifiableMap.put(clazz.getSimpleName()
			    .toLowerCase(), newExecutor);
		} catch (NoSuchMethodException e) {
		    // Ignore class without a default constructor
		} catch (InstantiationException e) {
		    // Ignore abstract classes
		} catch (IllegalAccessException e) {
		    // Ignore classes with private constructors
		} catch (IllegalArgumentException e) {
		    // Cannot be
		    throw new RuntimeException(e);
		}
	    }
	    executors = Collections
		    .unmodifiableSortedMap(executorsModifiableMap);
	} catch (InvocationTargetException | SecurityException
		| ClassNotFoundException | IOException e) {
	    throw new CommandsLoadingException(e);
	}
    }

    public void runLoop() {
	do {
	    try {
		out.print(">>>");
		String cmdWithArgsStr = reader.readLine();
		CommandWithArgs cmdWithArgs = parseCommand(cmdWithArgsStr);
		if (cmdWithArgs == null) {
		    continue;
		}
		if (cmdWithArgs.cmd.equals(exitCommand)) {
		    break;
		}
		executeCommand(cmdWithArgs, out);
	    } catch (IOException e) {
		out.println("Exception: Failed to read input. "
			+ e.getMessage());
	    } catch (UnknownCommand | CommandExecutionException e) {
		out.println(e.getMessage());
	    }
	} while (true);
    }

    private void executeCommand(CommandWithArgs cmdWithArgs, PrintStream out)
	    throws InvalidArgs, CommandExecutionException, UnknownCommand {
	CommandExecutor executor = executors.get(cmdWithArgs.cmd);
	if (executor == null) {
	    throw new UnknownCommand(cmdWithArgs.cmd);
	}
	executor.execute(out, cmdWithArgs.args, this);
    }

    /**
     * @return CommandWithArgs or null if argument is an empty string
     */
    private static CommandWithArgs parseCommand(String cmdWithArgs)
	    throws UnknownCommand {
	String[] cmdWithArgsStrings = cmdWithArgs.trim().split("\\s");
	if (cmdWithArgsStrings.length < 1) {
	    return null;
	}

	return new CommandWithArgs(cmdWithArgsStrings[0].toLowerCase(),
		Arrays.copyOfRange(cmdWithArgsStrings, 1,
			cmdWithArgsStrings.length));
    }

    @Override
    public BufferedReader getInput() {
	return reader;
    }

    @Override
    public CommandExecutor getCommandExecutor(String name) {
	return executors.get(name);
    }

    @Override
    public Set<String> getCommandExecutorNames() {
	return executors.keySet();
    }
}
