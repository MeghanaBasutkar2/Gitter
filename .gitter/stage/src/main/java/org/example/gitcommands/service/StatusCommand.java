package org.example.gitcommands.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;

public class StatusCommand {
	private static StatusCommand statusCommandInstance;

	private StatusCommand() {
	}

	public static StatusCommand getStatusCommandInstance() {
		if (statusCommandInstance == null) {
			statusCommandInstance = new StatusCommand();
			return statusCommandInstance;
		} else {
			return statusCommandInstance;
		}
	}

	public void status() {
		boolean diffExists = false;
		Path currentDir = Path.of(System.getProperty("user.dir"));
		Path gitterDir = currentDir.resolve(".gitter");
		Path stagePath = gitterDir.resolve("stage");

		List<String> untrackedFiles = getUntrackedFiles(currentDir, stagePath); // any file not added
		List<String> addedChanges = getChangesToBeCommitted(stagePath); // added changes
		List<String> modifiedFiles = getModifiedFiles(currentDir, stagePath); // not added

		try {
			if (!untrackedFiles.isEmpty()) {
				System.out.println("Untracked files:");
				for (String file : untrackedFiles) {
					System.out.println(file);
				}
				System.out.println();
			}

			if (!modifiedFiles.isEmpty()) {
				diffExists = true;
				System.out.println("Changes not staged for commit:");
				for (String file : modifiedFiles) {
					System.out.println("modified: " + file + "\n");
				}
				System.out.println();
			}

			if (!addedChanges.isEmpty()) {
				diffExists = true;
				System.out.println("Changes to be committed:");
				for (String file : addedChanges) {
					System.out.println("modified: " + file + "\n");
				}
				System.out.println();
			}

			if (!diffExists) {
				System.out.println("nothing to commit, working tree clean");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// fetch staged files
	private List<String> getChangesToBeCommitted(Path stagePath) {
		List<String> staged = new ArrayList<>();
		if (Files.exists(stagePath)) {
			File[] files = stagePath.toFile().listFiles();
			if (files != null) {
				for (File file : files) {
					staged.add(file.getName());
				}
			}
		}
		return staged;
	}

	protected List<String> getModifiedFiles(Path currentDir, Path stagePath) {
		List<String> modified = new ArrayList<>();

		// if no files in the stage path or if the list of files obj is null, return empty list
		if (!Files.exists(stagePath)) return modified;

		File[] stagedFiles = stagePath.toFile().listFiles();
		if (stagedFiles == null) return modified;

		try {
			for (File stagedFile : stagedFiles) {
				Path filePathInCurrentDir = currentDir.resolve(stagedFile.getName());
				if (Files.exists(filePathInCurrentDir)) {
					System.out.println("----stage file path----" + stagedFile.toPath());
					System.out.println("----corresponding file path in current dir----" +filePathInCurrentDir);
					List<String> stagedContent = Files.readAllLines(stagedFile.toPath());
					List<String> currentDirContent = Files.readAllLines(filePathInCurrentDir);

					Patch<String> patch = DiffUtils.diff(stagedContent, currentDirContent);

					if (!patch.getDeltas().isEmpty()) { // diffs found
						modified.add(stagedFile.getName());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error in getting modified files: " + e.getMessage());
		}
		return modified;
	}

	private List<String> getUntrackedFiles(Path currentDir, Path stagePath) {
		List<String> untracked = new ArrayList<>();

		try {
			// fetch staged files and add to a list
			List<String> stagedFiles = getChangesToBeCommitted(stagePath);

			// files in current dir
			File[] allFiles = currentDir.toFile().listFiles(file -> !file.getName().equals(".gitter"));

			if (allFiles != null) {
				for (File file : allFiles) {
					if (!stagedFiles.contains(file.getName())) {
						untracked.add(file.getName());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error in getting untracked files: " + e.getMessage());
		}
		return untracked;
	}
}
