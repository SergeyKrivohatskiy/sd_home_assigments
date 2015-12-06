/**
 * 
 */
package ha1_shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.CommandExecutor;
import ru.spbau.skrivohatskiy.shell.commandExecutionLoop.exceptions.CommandExecutionException;
import ru.spbau.skrivohatskiy.shell.commands.Cat;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class CatTests {
    private static final CommandExecutor cat = new Cat();
    private StringWriter output;

    @Before
    public void createOutput() {
	output = new StringWriter();
    }

    @Test
    public void descriptionTest() {
	assertNotNull(cat.getDescription());
    }

    @Test
    public void fileCatTest() throws CommandExecutionException {
	String[] args = { "Test.txt" };
	cat.execute(output, args, TestUtils.createExecutionContext(""));
	assertEquals(
		"1 2 3" + System.lineSeparator() + "4 5"
			+ System.lineSeparator() + "6" + System.lineSeparator(),
		output.toString());
    }

    @Test
    public void multyFileCatTest() throws CommandExecutionException {
	String[] args = { "Test.txt", "Test.txt" };
	cat.execute(output, args, TestUtils.createExecutionContext(""));
	String testContent = "1 2 3" + System.lineSeparator() + "4 5"
		+ System.lineSeparator() + "6" + System.lineSeparator();
	assertEquals(testContent + testContent, output.toString());
    }

    @Test
    public void noArgsCatTest() throws CommandExecutionException {
	String[] args = {};
	cat.execute(output, args, TestUtils.createExecutionContext("2124123"));
	assertEquals("2124123", output.toString());
    }
}
