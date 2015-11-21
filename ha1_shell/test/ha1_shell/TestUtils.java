/**
 * 
 */
package ha1_shell;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Set;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutionContext;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class TestUtils {

    public static CommandExecutionContext createExecutionContext(
	    String inputString) {
	return new CommandExecutionContext() {

	    @Override
	    public Reader getInput() {
		return new StringReader(inputString);
	    }

	    @Override
	    public Set<String> getCommandExecutorNames() {
		return Collections.emptySet();
	    }

	    @Override
	    public CommandExecutor getCommandExecutor(String name) {
		return null;
	    }
	};
    }
}
