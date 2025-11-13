package org.example;

import org.example.gitcommands.controller.CommandExecutor;
import org.example.gitcommands.controller.CommandExecutorImpl;
import org.example.gitcommands.service.InitCommand;

public class Main {
	public static void main(String[] args) {
		CommandExecutor commandExecutor = CommandExecutorImpl.getExecutor();
//		commandExecutor.execute("gitter init");
//		commandExecutor.execute("gitter add");
//		commandExecutor.execute("gitter help commit");
//		commandExecutor.execute("gitter commit");
//		commandExecutor.execute("gitter help");
//		commandExecutor.execute("gitter help commit");
//		commandExecutor.execute("gitter help init");
//		commandExecutor.execute("gitter checkout -b feature");
//		commandExecutor.execute("gitter checkout -b feature");
//		commandExecutor.execute("gitter checkout -b feature");
//		commandExecutor.execute("gitter checkout feature");
//		commandExecutor.execute("gitter add .");
//		commandExecutor.execute("gitter add *.bat"); // instead of py files
//		commandExecutor.execute("gitter commit -m \" bat file\" ");
//		commandExecutor.execute("gitter add build.gradle");
//		commandExecutor.execute("gitter add src");
//		commandExecutor.execute("gitter status");
//		commandExecutor.execute("gitter add settings.gradle");
//		commandExecutor.execute("gitter commit -m \" modify gradle file\" ");
//		commandExecutor.execute("gitter add build.gradle");
//		commandExecutor.execute("gitter commit -m \"modify file test 2\" ");
//		commandExecutor.execute("gitter commit -m \"test line seperator commit format\" ");
//		commandExecutor.execute("gitter commit -m \"test line seperator commit format 2\" ");
//		commandExecutor.execute("gitter commit -am \" testing commit msg\" ");
//		commandExecutor.execute("gitter status");
//		commandExecutor.execute("gitter log");
//		commandExecutor.execute("gitter reset HEAD");
//		commandExecutor.execute("gitter reset HEAD~2");
//		commandExecutor.execute("gitter commit -m \"latest commit to test head changes\" ");
//		commandExecutor.execute("gitter commit -m \"latest commit to test head changes 2\" ");
//		commandExecutor.execute("gitter add .");
		commandExecutor.execute("gitter add build.gradle");
		commandExecutor.execute("gitter add src");
//		commandExecutor.execute("gitter commit -m \"modify all test\" ");
//		commandExecutor.execute("gitter add src");
		commandExecutor.execute("gitter commit -m \"modify src,build test11\" ");
//		commandExecutor.execute("gitter add .");
//		commandExecutor.execute("gitter commit -m \"modify test\" ");
		commandExecutor.execute("gitter status");
//		commandExecutor.execute("gitter commit -m \"modify build.gradle test112\" ");
//		commandExecutor.execute("gitter reset HEAD~1");

	}
}