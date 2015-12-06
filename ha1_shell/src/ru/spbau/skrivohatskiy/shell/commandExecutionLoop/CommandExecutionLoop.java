/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandsLoadingException;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.UnknownCommand;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class CommandExecutionLoop {
    private final String exitCommand;
    private final SortedMap<String, CommandExecutor> executors;
    private final BufferedReader in;
    private final OutputStreamWriter out;
    private final PrintStream err;
    private final ExecutorService commandsThreadPool;

    public CommandExecutionLoop(Collection<String> commandsPackages)
	    throws CommandsLoadingException {
	this("exit", commandsPackages, System.in, System.out, System.err);
    }

    public CommandExecutionLoop(String exitCommand,
	    Collection<String> commandsPackages, InputStream in,
	    PrintStream out, PrintStream err) throws CommandsLoadingException {
	this.exitCommand = exitCommand.toLowerCase();
	this.out = new OutputStreamWriter(out);
	this.err = err;
	this.in = new BufferedReader(new InputStreamReader(in));
	commandsThreadPool = Executors.newCachedThreadPool();
	SortedMap<String, CommandExecutor> executorsModifiableMap = new TreeMap<>();
	for (String commandsPackage : commandsPackages) {
	    addClassesFromPackage(commandsPackage, executorsModifiableMap);
	}
	executors = Collections.unmodifiableSortedMap(executorsModifiableMap);
    }

    private void addClassesFromPackage(String commandsPackage,
	    SortedMap<String, CommandExecutor> executorsModifiableMap)
	    throws CommandsLoadingException {
	Class<?>[] classes;
	try {
	    classes = Utils.getClasses(commandsPackage);

	} catch (SecurityException | ClassNotFoundException | IOException e) {
	    throw new CommandsLoadingException(e);
	}
	for (Class<?> clazz : classes) {
	    try {
		if (!CommandExecutor.class.isAssignableFrom(clazz)) {
		    err.println("Class loading warning: class "
			    + clazz.getSimpleName()
			    + " is not a CommandExecutor. Class ignored");
		    continue;
		}
		CommandExecutor newExecutor;
		newExecutor = (CommandExecutor) clazz.getConstructor()
			.newInstance();
		executorsModifiableMap.put(clazz.getSimpleName().toLowerCase(),
			newExecutor);
	    } catch (NoSuchMethodException e) {
		err.println("Class loading warning: class "
			+ clazz.getSimpleName()
			+ " hasn't a default constructor. Class ignored");
	    } catch (InstantiationException e) {
		err.println("Class loading warning: class "
			+ clazz.getSimpleName() + " is abstract. Class ignored");
	    } catch (IllegalAccessException e) {
		err.println("Class loading warning: class "
			+ clazz.getSimpleName()
			+ " has unaccessible(private for example) default constructor");
	    } catch (InvocationTargetException e) {
		err.println("Class loading warning: default constructor for class "
			+ clazz.getSimpleName()
			+ " throwed an exception: "
			+ e.getMessage());
	    } catch (IllegalArgumentException e) {
		// Cannot be
		throw new RuntimeException(e);
	    }
	}
    }

    public void runLoop() {
	do {
	    try {
		out.write(">>>");
		out.flush();
		String commandsStr = in.readLine();
		if (commandsStr == null
			|| commandsStr.equalsIgnoreCase(exitCommand)) {
		    break;
		}
		CommandWithArgs[] commands = parseCommands(commandsStr);
		executeCommands(commands);
	    } catch (IOException e) {
		err.println("Exception: Failed to read input. "
			+ e.getMessage());
	    } catch (UnknownCommand e) {
		err.println(e.getMessage());
	    }
	} while (true);
	commandsThreadPool.shutdown();
    }

    private void executeCommands(CommandWithArgs[] commands)
	    throws UnknownCommand {
	Reader[] readers = new Reader[commands.length];
	Writer[] writers = new Writer[commands.length];
	try {
	    CommandWrapper[] wrappers = new CommandWrapper[commands.length];
	    Future<?>[] futures = new Future<?>[commands.length];
	    readers[0] = in;
	    writers[commands.length - 1] = out;
	    for (int idx = 0; idx < commands.length - 1; idx += 1) {
		PipedWriter writer = new PipedWriter();
		writers[idx] = writer;
		readers[idx + 1] = new PipedReader(writer);
	    }
	    for (int idx = 0; idx < commands.length; idx += 1) {
		CommandExecutor cmdExecutor = executors.get(commands[idx].cmd);
		if (cmdExecutor == null) {
		    throw new UnknownCommand(commands[idx].cmd);
		}
		wrappers[idx] = new CommandWrapper(readers[idx], writers[idx],
			cmdExecutor, commands[idx].args);
	    }
	    for (int idx = 0; idx < commands.length; idx += 1) {
		futures[idx] = commandsThreadPool.submit(wrappers[idx]);
	    }
	    for (int idx = 0; idx < commands.length; idx += 1) {
		futures[idx].get();
	    }
	    out.flush();
	} catch (IOException e) {
	    err.println("Io exception while executing command: "
		    + e.getMessage());
	} catch (InterruptedException e) {
	} catch (ExecutionException e) {
	    throw new RuntimeException("Unhandled command exception",
		    e.getCause());
	}
    }

    private static CommandWithArgs[] parseCommands(String commandsStr)
	    throws UnknownCommand {
	String[] commandsStrArray = commandsStr.split("\\|");
	CommandWithArgs[] commands = new CommandWithArgs[commandsStrArray.length];
	for (int i = 0; i < commandsStrArray.length; i += 1) {
	    commands[i] = parseCommand(commandsStrArray[i]);
	}
	return commands;
    }

    private static CommandWithArgs parseCommand(String cmdWithArgs)
	    throws UnknownCommand {
	String[] cmdWithArgsStrings = cmdWithArgs.trim().split("\\s");

	return new CommandWithArgs(cmdWithArgsStrings[0].toLowerCase(),
		Arrays.copyOfRange(cmdWithArgsStrings, 1,
			cmdWithArgsStrings.length));
    }

    public class CommandWrapper implements Runnable, CommandExecutionContext {

	private final Reader in;
	private final Writer out;
	private final CommandExecutor executor;
	private final String[] args;

	public CommandWrapper(Reader in, Writer out, CommandExecutor executor,
		String[] args) {
	    this.in = in;
	    this.out = out;
	    this.executor = executor;
	    this.args = args;
	}

	@Override
	public void run() {
	    try (Reader readerToClose = in == CommandExecutionLoop.this.in ? null
		    : in;
		    Writer writerToClose = out == CommandExecutionLoop.this.out ? null
			    : out;) {
		executor.execute(out, args, this);
	    } catch (CommandExecutionException e) {
		synchronized (err) {
		    err.println(e.getMessage());
		}
	    } catch (IOException e) {
		synchronized (err) {
		    err.println("Io exception occured while closing stream: "
			    + e.getMessage());
		}
	    }
	}

	@Override
	public Reader getInput() {
	    return in;
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
}
