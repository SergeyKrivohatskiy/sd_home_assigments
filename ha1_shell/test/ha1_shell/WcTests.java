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
import ru.spbau.skrivohatskiy.shell.commands.Wc;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class WcTests {
    private static final CommandExecutor wc = new Wc();
    private StringWriter output;

    @Before
    public void createOutput() {
	output = new StringWriter();
    }

    @Test
    public void descriptionTest() {
	assertNotNull(wc.getDescription());
    }

    @Test
    public void inputWcTest() throws CommandExecutionException {
	String[] args = {};
	wc.execute(
		output,
		args,
		TestUtils.createExecutionContext("word1 word2 word3"
			+ System.lineSeparator() + "word4 word5"));
	assertEquals(
		"0 files contains 2 lines, 5 words and 28 bytes"
			+ System.lineSeparator(), output.toString());
    }

    @Test
    public void fileWcTest() throws CommandExecutionException {
	String[] args = { "Test.txt" };
	wc.execute(output, args, TestUtils.createExecutionContext(""));
	assertEquals(
		"1 files contains 3 lines, 6 words and 9 bytes"
			+ System.lineSeparator(), output.toString());
    }
}
