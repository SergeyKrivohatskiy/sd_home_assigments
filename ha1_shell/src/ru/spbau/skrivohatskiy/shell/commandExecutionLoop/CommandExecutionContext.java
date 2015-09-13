/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

import java.io.BufferedReader;
import java.util.Set;

/**
 * @author Sergey Krivohatskiy
 *
 */
public interface CommandExecutionContext {
    public BufferedReader getInput();

    public CommandExecutor getCommandExecutor(String name);

    public Set<String> getCommandExecutorNames();
}
