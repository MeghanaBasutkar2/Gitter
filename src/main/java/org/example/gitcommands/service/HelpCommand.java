package org.example.gitcommands.service;

import org.example.GitterConstants;

import java.util.Map;

public class HelpCommand {
	private static HelpCommand helpCommandInstance;

	private HelpCommand(){}

	public static HelpCommand getHelpCommandInstance() {
		if (helpCommandInstance == null) {
			helpCommandInstance = new HelpCommand();
			return helpCommandInstance;
		} else {
			return helpCommandInstance;
		}
	}

	public void executeHelp(String command) {
		try {
			// git help <command>
			String afterHelp = command.substring("gitter help".trim().length()).trim(); // help, commit
			if (GitterConstants.GIT_HELP_MAP.containsKey(afterHelp) && !afterHelp.isEmpty()) {
				System.out.println(GitterConstants.GIT_HELP_DETAIL_MAP.get(afterHelp));
			} else {
				// git help
				System.out.println("These are common gitter commands: ");
				// get static list of all commands and their desc
				for (Map.Entry<String, String> commandDesc : GitterConstants.GIT_HELP_MAP.entrySet()) {
					System.out.println(commandDesc.getKey() + " -> " + commandDesc.getValue());
				}
			}
		} catch (Exception e) {
			System.out.println("Check the help command, something is not right!");
			System.out.println(e.getMessage());
		}
	}
}