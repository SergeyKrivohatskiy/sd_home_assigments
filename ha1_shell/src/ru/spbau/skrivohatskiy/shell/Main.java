/**
 * 
 */
package ru.spbau.skrivohatskiy.shell;

import java.util.Arrays;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionLoop;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandsLoadingException;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class Main {
    public static void main(String[] args) {
	CommandExecutionLoop commandExecutionLoop;
	try {
	    commandExecutionLoop = new CommandExecutionLoop(
		    Arrays.asList("ru.spbau.skrivohatskiy.shell.commands"));
	    commandExecutionLoop.runLoop();
	} catch (CommandsLoadingException e) {
	    System.err.println("Failed to load commands");
	    System.exit(1);
	}
    }
}
