/**
 * 
 */
package ru.spbau.skrivohatskiy.shell.commandExecutionLoop;

/**
 * @author Sergey Krivohatskiy
 *
 */
public class CommandWithArgs {
    public final String cmd;
    public final String[] args;

    public CommandWithArgs(String cmd, String[] args) {
	this.cmd = cmd;
	this.args = args;
    }
}
