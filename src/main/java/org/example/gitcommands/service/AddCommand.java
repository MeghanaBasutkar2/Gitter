package org.example.gitcommands.service;

import org.example.GitterConstants;
import org.example.gitcommands.controller.CommandExecutorImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AddCommand {
	private static AddCommand addCommandInstance;

	private AddCommand() {
	}

	public static AddCommand getAddCommandInstance() {
		if (addCommandInstance == null) {
			addCommandInstance = new AddCommand();
			return addCommandInstance;
		} else {
			return addCommandInstance;
		}
	}

	public void executeAdd(String command) {
		System.out.println(command);
		// create a stage folder for tracking file states(staged/ committed)
		Path currentDir = Path.of(System.getProperty("user.dir"));
		Path gitterDir = currentDir.resolve(".gitter");
		Path stagePath = gitterDir.resolve("stage");

		try {
			if (!Files.exists(stagePath)) {
				Files.createDirectories(stagePath);
			}

			// add all case:
			if (command.contains("add .")) {
				File[] files = currentDir.toFile().listFiles(file -> !file.getName().equals(".gitter"));
				if (files != null) {
					for (File file : files) {
						Files.copy(file.toPath(), stagePath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
					}
				}
			} else if (command.contains("*.")) {
				File[] files = currentDir.toFile().listFiles(file -> file.getName().endsWith(".bat"));
				if (files != null) {
					for (File file : files) {
						Files.copy(file.toPath(), stagePath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
					}
				}
			} else { // add each file case:
				String[] fullCommand = command.split(" ");
				String filePathToAdd = fullCommand[2];
				Path source = currentDir.resolve(filePathToAdd);
				Path destination = stagePath.resolve(filePathToAdd);
				if (Files.exists(source)) {
					Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
				} else {
					System.out.println("File not found at: " + filePathToAdd);
				}
			}
		} catch (IOException e) {
			System.out.println(GitterConstants.FILE_ADD_ERR);
		}
	}
}
