package org.example.gitcommands.controller;

import org.example.gitcommands.service.*;

import static org.example.GitterConstants.*;
import static org.example.GitterConstants.INVALID_COMMAND;

public class CommandExecutorImpl implements CommandExecutor {
	private static final String PREFIX_STR = "gitter";
	private static CommandExecutorImpl commandExecutorImplInstance;

	private CommandExecutorImpl() {
	}

	public static CommandExecutorImpl getExecutor() {
		if (commandExecutorImplInstance == null) {
			commandExecutorImplInstance = new CommandExecutorImpl();
			return commandExecutorImplInstance;
		} else {
			return commandExecutorImplInstance;
		}
	}

	@Override
	public void execute(String command) {
		try {
			switch (validateCommand(command)) {
				case INIT:
					InitCommand.getInitCommandInstance().init();
					break;
				case ADD:
					AddCommand.getAddCommandInstance().executeAdd(command);
					break;
				case HELP:
					HelpCommand.getHelpCommandInstance().executeHelp(command);
					break;
				case CHECKOUT:
					CheckoutCommand.getCheckoutCommandInstance().checkout(command);
					break;
				case STATUS:
					StatusCommand.getStatusCommandInstance().status();
					break;
				case COMMIT:
					CommitCommand.getCommitCommandInstance().commit(command);
					break;
				case LOG:
					LogCommand.getLogCommandInstance().log();
					break;
				case RESET:
					ResetCommand.getResetCommandInstance().resetHead(command);
					break;
				default:
					System.out.println(INVALID_COMMAND);
					throw new IllegalArgumentException(INVALID_COMMAND);
			}
		} catch (Exception e) {
			System.out.println(INVALID_COMMAND);
//			System.out.println(e.getMessage());
		}
	}

	// add basic validations for an input command
	private String validateCommand(String command) {
		String commandTrimmed = command.trim();
		// Must start with prefix
		if (!commandTrimmed.startsWith(PREFIX_STR)) {
			throw new IllegalArgumentException(INVALID_COMMAND);
		}
		String executeCommand = commandTrimmed.substring(PREFIX_STR.trim().length()).trim(); // help commit
		String[] finalCommand = executeCommand.split("\\s+"); // split at space
		return finalCommand[0];
	}
}
