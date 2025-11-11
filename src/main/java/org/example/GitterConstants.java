package org.example;

import java.util.Map;

public class GitterConstants {
	// commands
	public static final String INIT = "init";
	public static final String INIT_HELP_DESC = "Create an empty Gitter repository";
	public static final String INIT_MESSAGE = "Initialized empty Gitter repository in ";
	public static final String ADD = "add";
	public static final String ADD_HELP_DESC = "Add file contents to the index";
	public static final String STATUS = "status";
	public static final String STATUS_HELP_DESC = "Show the working tree status";
	public static final String CHECKOUT = "checkout";
	public static final String LOG = "log";
	public static final String RESET = "reset";
	public static final String CHECKOUT_HELP_DESC = "Switches the current working branch to the one given in the argument, else returns error if branch not present";
	public static final String CHECKOUT_MESSAGE = "Switched to a new branch ";
	public static final String CHECKOUT_MESSAGE_EXISTING = "Switched to branch ";
	public static final String CHECKOUT_MESSAGE_EXISTING_ERROR = " does not exist.";
	public static final String COMMIT = "commit";
	public static final String COMMIT_HELP_DESC = "Show details about usage of commit command";
	public static final String COMMIT_HELP_DETAIL_DESC = "NAME:\n" +
			"commit - Record changes to the repository\n" +
			"\n" +
			"SYNOPSIS:\n" +
			"gitter commit -m [-a] <msg>\n" +
			"\n" +
			"DESCRIPTION:\n" +
			"Create a new commit containing the current contents of the index and the given log\n" +
			"message describing the changes. The new commit is a direct child of HEAD, usually the\n" +
			"tip of the current branch, and the branch is updated to point to it.\n" +
			"\n" +
			"OPTIONS:\n" +
			"-a: Tell the command to automatically stage files that have been modified and deleted, \n" +
			"but new files you have not told Git about are not affected.\n" +
			"\n" +
			"-m: Use the given <msg> as the commit message. If multiple -m options are given, their\n" +
			"values are concatenated as separate paragraphs.";

	// help command
	public static final String HELP = "help";

	// for getting list of commands and their desc, can be extended to include all possible git commands
	public static final Map<String, String> GIT_HELP_MAP = Map.ofEntries(
			Map.entry(INIT, INIT_HELP_DESC),
			Map.entry(ADD, ADD_HELP_DESC),
			Map.entry(STATUS, STATUS_HELP_DESC),
			Map.entry(COMMIT, COMMIT_HELP_DESC),
			Map.entry(CHECKOUT, CHECKOUT_HELP_DESC)
	);

	// can add detail descriptions for other commands too like for "commit", can be extended to include all possible git commands
	public static final Map<String, String> GIT_HELP_DETAIL_MAP = Map.ofEntries(
			Map.entry(INIT, INIT_HELP_DESC),
			Map.entry(ADD, ADD_HELP_DESC),
			Map.entry(STATUS, STATUS_HELP_DESC),
			Map.entry(COMMIT, COMMIT_HELP_DETAIL_DESC)
	);

	public static final String INVALID_COMMAND = "Invalid command. Try with git help for more info";
	public static final String FILE_CREATION_ERR = "Something went wrong while creating a gitter dir!";
	public static final String FILE_ADD_ERR = "Something went wrong while adding a file!";
}
