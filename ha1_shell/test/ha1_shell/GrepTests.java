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
import ru.spbau.skrivohatskiy.shell.commands.CliGrep;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class GrepTests {
    private static final CommandExecutor grep = new CliGrep();
    private StringWriter output;

    @Before
    public void createOutput() {
	output = new StringWriter();
    }

    @Test
    public void descriptionTest() {
	assertNotNull(grep.getDescription());
    }

    @Test
    public void simpleNotFoundTest() throws CommandExecutionException {
	String[] args = { "toFind" };
	grep.execute(output, args, TestUtils.createExecutionContext(""));
	assertEquals("", output.toString());
    }

    @Test
    public void simpleFoundTest() throws CommandExecutionException {
	String[] args = { "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("string toFind in"));
	assertEquals("string toFind in" + System.lineSeparator(),
		output.toString());
    }

    @Test
    public void ignoreCaseNotFoundTest() throws CommandExecutionException {
	String[] args = { "-i", "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("string toNotFind in"));
	assertEquals("", output.toString());
    }

    @Test
    public void ignoreCaseFoundTest() throws CommandExecutionException {
	String[] args = { "-i", "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("stRing ToFiNd in"));
	assertEquals("stRing ToFiNd in" + System.lineSeparator(),
		output.toString());
    }

    @Test
    public void beginNotFoundTest() throws CommandExecutionException {
	String[] args = { "^toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("string Not toFind in"));
	assertEquals("", output.toString());
    }

    @Test
    public void beginFoundTest() throws CommandExecutionException {
	String[] args = { "^toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("toFind in string"));
	assertEquals("toFind in string" + System.lineSeparator(),
		output.toString());
    }

    @Test
    public void endNotFoundTest() throws CommandExecutionException {
	String[] args = { "toFind$" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("string Not toFind in"));
	assertEquals("", output.toString());
    }

    @Test
    public void endFoundTest() throws CommandExecutionException {
	String[] args = { "toFind$" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("in string toFind"));
	assertEquals("in string toFind" + System.lineSeparator(),
		output.toString());
    }

    @Test
    public void wordNotFoundTest() throws CommandExecutionException {
	String[] args = { "-w", "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("string Not toFind0 in"));
	assertEquals("", output.toString());
    }

    @Test
    public void wordFoundTest1() throws CommandExecutionException {
	String[] args = { "-w", "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("in string toFind"));
	assertEquals("in string toFind" + System.lineSeparator(),
		output.toString());
    }

    @Test
    public void wordFoundTest2() throws CommandExecutionException {
	String[] args = { "-w", "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("in toFind string"));
	assertEquals("in toFind string" + System.lineSeparator(),
		output.toString());
    }

    @Test(expected = CommandExecutionException.class)
    public void invalidOptionTest() throws CommandExecutionException {
	String[] args = { "-a", "toFind" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("in toFind string"));
    }

    @Test(expected = CommandExecutionException.class)
    public void notEnoughtArgsTest() throws CommandExecutionException {
	String[] args = { "-w", "-i" };
	grep.execute(output, args,
		TestUtils.createExecutionContext("in toFind string"));
    }

    @Test
    public void additionalLinesTest() throws CommandExecutionException {
	String[] args = { "-a", "1", "toFind" };
	String input = "in toFind string" + System.lineSeparator() + "nextLine"
		+ System.lineSeparator();
	grep.execute(output, args, TestUtils.createExecutionContext(input));
	assertEquals(input, output.toString());
    }

    @Test(expected = CommandExecutionException.class)
    public void additionalLinesNoValueTest() throws CommandExecutionException {
	String[] args = { "-a", "toFind" };
	String input = "in toFind string" + System.lineSeparator() + "nextLine"
		+ System.lineSeparator();
	grep.execute(output, args, TestUtils.createExecutionContext(input));
    }

    @Test(expected = CommandExecutionException.class)
    public void additionalLinesInvalidValueTest()
	    throws CommandExecutionException {
	String[] args = { "-a", "invalidValue", "toFind" };
	String input = "in toFind string" + System.lineSeparator() + "nextLine"
		+ System.lineSeparator();
	grep.execute(output, args, TestUtils.createExecutionContext(input));
    }

    @Test
    public void complexTest() throws CommandExecutionException {
	String[] args = { "-a", "1", "-w", "string" };
	String input = "in toFind string" + System.lineSeparator() + "nextLine"
		+ System.lineSeparator();
	grep.execute(output, args, TestUtils.createExecutionContext(input));
	assertEquals(input, output.toString());
    }

    @Test
    public void complexFileTest() throws CommandExecutionException {
	String[] args = { "-a", "1", "-w", "Test.txt", "3" };
	String result = "File: Test.txt" + System.lineSeparator() + "1 2 3"
		+ System.lineSeparator() + "4 5" + System.lineSeparator();
	grep.execute(output, args, TestUtils.createExecutionContext(""));
	assertEquals(result, output.toString());
    }

    @Test(expected = CommandExecutionException.class)
    public void invalidFileTest() throws CommandExecutionException {
	String[] args = { "-a", "1", "-w", "NotExistingFile.txt", "3" };
	grep.execute(output, args, TestUtils.createExecutionContext(""));
    }
}
