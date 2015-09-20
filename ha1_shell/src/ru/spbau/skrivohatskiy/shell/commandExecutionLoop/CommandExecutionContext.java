/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

import java.io.Reader;
import java.util.Set;

/**
 * @author Sergey Krivohatskiy
 *
 */
public interface CommandExecutionContext {
    public Reader getInput();

    public CommandExecutor getCommandExecutor(String name);

    public Set<String> getCommandExecutorNames();
}
