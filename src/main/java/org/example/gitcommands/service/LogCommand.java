package org.example.gitcommands.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogCommand {
	private static LogCommand logCommandInstance;
	// todo: change to 10
	private static final int commitSize = 2;

	private LogCommand() {
	}

	public static LogCommand getLogCommandInstance() {
		if (logCommandInstance == null) {
			logCommandInstance = new LogCommand();
			return logCommandInstance;
		} else {
			return logCommandInstance;
		}
	}

	public void log() {
		try {
			Path gitterDir = Path.of(System.getProperty("user.dir")).resolve(".gitter");
			Path commitLog = gitterDir.resolve("commits.log");

			// no commits exist
			if (!Files.exists(commitLog)) {
				System.out.println("your current branch does not have any commits yet");
			} else {
				// read strings from commit.log:
				List<String> commitList = Files.readAllLines(commitLog); // ex: 20 commits
				// System.out.println(commitList);

				// show last 10 commits:
				// to get 11 to 20:
				int startIndex = 0;
				if (commitList.size() > commitSize) {
					startIndex = commitList.size() - commitSize; // 20-10 = 10
				}
				List<String> commitListWithCapacity = commitList.subList(startIndex, commitList.size());

				// iterate from latest to oldest
				for (int i = commitListWithCapacity.size() - 1; i >= 0; i--) {
					String[] logParts = commitListWithCapacity.get(i).split("\\|");
					printLog(logParts);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * format:
	 * commit 67@a84c7cb81c8c98cf5516b2a919123d7@a5a8b
	 * Author: user
	 * Date: Sat Jan 25 00:27:00 2025 +0530
	 * <p>
	 * <p>
	 * updates documentation and schema definition
	 * <p>
	 * commit 67@a84c7cb81c8c98cf5516b2a919123d7@a5a8b
	 * Author: user
	 * Date: Sat Jan 25 00:27:00 2025 +0530
	 * <p>
	 * <p>
	 * Adds feature for listing commit logs
	 */
	private void printLog(String[] logParts) {
		System.out.println("commit " + logParts[0] + "\n");
		System.out.println("Author: user");
		System.out.println("Date: " + logParts[1] + "\n");
		System.out.println("    " + logParts[2].replace("\"", ""));
	}
}
