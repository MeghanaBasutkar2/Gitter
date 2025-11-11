package org.example.gitcommands.service;

import org.example.gitcommands.CommitUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CommitCommand {
	private static CommitCommand commitCommandInstance;

	private CommitCommand() {
	}

	public static CommitCommand getCommitCommandInstance() {
		if (commitCommandInstance == null) {
			commitCommandInstance = new CommitCommand();
			return commitCommandInstance;
		} else {
			return commitCommandInstance;
		}
	}

	public void commit(String command) {
		Path currentDir = Path.of(System.getProperty("user.dir"));
		Path gitterDir = currentDir.resolve(".gitter");
		Path stagePath = gitterDir.resolve("stage");
		// path to store files that will finally be committed
		Path repoPath = gitterDir.resolve("repo");

		try {
			if (!Files.exists(stagePath)) Files.createDirectories(stagePath);
			if (!Files.exists(repoPath)) Files.createDirectories(repoPath);

			// stage all files not added but when tried with -am command to directly stage and commit
			if (command.contains("-a")) {
				stageAllModifiedFiles(currentDir, stagePath);
			}

			// for internal logging only
			String commitMsg = "";
			if (command.contains("-am")) {
				commitMsg = command.split("-am".trim())[1].trim();
			} else if (command.contains("-m")) {
				commitMsg = command.split("-m".trim())[1].trim();
			}
			System.out.println("Commiting changes with message: " + commitMsg);

			// get all staged filed and commit by adding to the final repo path
			File[] stagedFiles = stagePath.toFile().listFiles();
			if (stagedFiles != null) {
				for (File file : stagedFiles) {
					Path destination = repoPath.resolve(file.getName());
					Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			formatAndStoreToCommitsFile(gitterDir, commitMsg);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void formatAndStoreToCommitsFile(Path gitterDir, String commitMsg) {
		Path commitLogPath = gitterDir.resolve("commits.log");
		String logEntry = CommitUtils.generateUUID() + "|" + CommitUtils.getDate() + "|" + commitMsg + System.lineSeparator();
		try {
			Files.writeString(commitLogPath, logEntry, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			System.out.println("Something went wrong while writing to commits.log");
		}
	}

	private void stageAllModifiedFiles(Path currentDir, Path stagePath) {
		// todo: can move such methods to a sep class
		List<String> modifiedFiles = StatusCommand.getStatusCommandInstance().getModifiedFiles(currentDir, stagePath);

		for (String fileName : modifiedFiles) {
			try {
				Path source = currentDir.resolve(fileName);
				Path destination = stagePath.resolve(fileName);
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				System.out.println("Error while adding file to stage");
			}
		}
	}
}