package org.example.gitcommands.service;

import org.example.GitterConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ResetCommand {
	private static ResetCommand resetCommandInstance;

	private ResetCommand() {
	}

	public static ResetCommand getResetCommandInstance() {
		if (resetCommandInstance == null) {
			resetCommandInstance = new ResetCommand();
			return resetCommandInstance;
		} else {
			return resetCommandInstance;
		}
	}

	// gitter reset HEAD~2
	public void resetHead(String command) {
		try {
			int resetCount = getResetCount(command);
			Path gitterDirPath = Path.of(System.getProperty("user.dir")).resolve(".gitter");
			Path commitFilePath = gitterDirPath.resolve("commits.log");

			// if no commits, we don't have anything to revert
			if (!Files.exists(commitFilePath)) {
				return;
			}
			// read commits file and revert last n commits
			rewriteCommitFile(commitFilePath, resetCount, gitterDirPath);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private void rewriteCommitFile(Path commitFilePath, int resetCount, Path gitterPath) {
		try {
			List<String> lines = Files.readAllLines(commitFilePath);
			if (lines.isEmpty()) return;
			int retainCommitsSize = lines.size() > resetCount ? (lines.size() - resetCount) : 0; // 10, 2 = 8; 3, 4 = 0
			List<String> rewrittenCommits = lines.subList(0, retainCommitsSize);
			Files.write(commitFilePath, rewrittenCommits);
			// delete files that were reset from stage and repo folders
			deleteResettableFiles(gitterPath, lines);
		} catch (IOException e) {
			System.out.println("Error while rewriting commits to commit file in head resetting");
		}
	}

	// copies into new dir, the changes that can be retained and removes the rest
	private void deleteResettableFiles(Path gitterPath, List<String> lines) {
		// todo: can move paths to a utils method
		Path repoDir = gitterPath.resolve("repo");
		Path stageDir = gitterPath.resolve("stage");
		Path currentDir = Path.of(System.getProperty("user.dir"));

		for (String commitLine : lines) {
			try {
				Path repoFile = repoDir.resolve(commitLine);
				Path stageFile = stageDir.resolve(commitLine);
				Path workFile = currentDir.resolve(commitLine);

				// if the committed version exists, restore it to working dir
				if (Files.exists(repoFile)) {
					Path parent = workFile.getParent();
					if (parent != null) {
						Files.createDirectories(parent);
					}
					Files.copy(repoFile, workFile, StandardCopyOption.REPLACE_EXISTING);
					// remove from repo
					Files.deleteIfExists(repoFile);
				}
				// remove from stage
				Files.deleteIfExists(stageFile);
			} catch (IOException ex) {
				System.out.println("Something went wrong while untracking files");
			}
		}
	}

	private int getResetCount(String command) {
		String[] splitStr = command.split(" ");
		if (!splitStr[2].startsWith("HEAD~")) {
			throw new IllegalArgumentException(GitterConstants.INVALID_COMMAND);
		}
		String digits = splitStr[2].substring(splitStr[2].indexOf("~") + 1);
		try {
			int resetCount = splitStr[2].contains("~") && !digits.isEmpty()
					? Integer.parseInt(splitStr[2].substring(splitStr[2].indexOf("~") + 1)) : 1;
			System.out.println("reset ct: " + resetCount);
			return resetCount;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid reset head count.");
		}
	}
}
