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

	private StatusCommand() {}

	public static StatusCommand getStatusCommandInstance() {
		if (statusCommandInstance == null) {
			statusCommandInstance = new StatusCommand();
		}
		return statusCommandInstance;
	}

	public void status() {
		boolean diffExists = false;
		Path currentDir = Path.of(System.getProperty("user.dir"));
		Path gitterDir = currentDir.resolve(".gitter");
		Path stagePath = gitterDir.resolve("stage");

		// any file not added
		List<String> untrackedFiles = getUntrackedFiles(currentDir, stagePath);
		// added changes (staged)
		List<String> addedChanges = getChangesToBeCommitted(stagePath);
		// changes not staged for commit (modified)
		List<String> modifiedFiles = getModifiedFiles(currentDir, stagePath);

		addedChanges.removeAll(modifiedFiles);

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
		Path gitterDir = currentDir.resolve(".gitter");
		Path repoPath = gitterDir.resolve("repo");

		List<String> stagedNames = new ArrayList<>();
		if (Files.exists(stagePath)) {
			File[] stagedArr = stagePath.toFile().listFiles();
			if (stagedArr != null) {
				for (File f : stagedArr) stagedNames.add(f.getName());
			}
		}

		for (String name : stagedNames) {
			try {
				Path stagedFile = stagePath.resolve(name);
				Path workingFile = currentDir.resolve(name);
				if (Files.exists(stagedFile) && Files.exists(workingFile) && Files.isRegularFile(workingFile)) {
					List<String> stagedContent = Files.readAllLines(stagedFile);
					List<String> workingContent = Files.readAllLines(workingFile);
					Patch<String> patch = DiffUtils.diff(stagedContent, workingContent);
					if (!patch.getDeltas().isEmpty()) {
						if (!modified.contains(name)) modified.add(name);
					}
				}
			} catch (Exception e) {
				System.out.println("Error comparing staged file: " + name + " -> " + e.getMessage());
			}
		}

		if (Files.exists(repoPath)) {
			File[] repoFiles = repoPath.toFile().listFiles();
			if (repoFiles != null) {
				for (File repoFile : repoFiles) {
					String name = repoFile.getName();
					if (stagedNames.contains(name)) continue;
					try {
						Path workingFile = currentDir.resolve(name);
						if (Files.exists(repoFile.toPath()) && Files.exists(workingFile) && Files.isRegularFile(workingFile)) {
							List<String> repoContent = Files.readAllLines(repoFile.toPath());
							List<String> workingContent = Files.readAllLines(workingFile);
							Patch<String> patch = DiffUtils.diff(repoContent, workingContent);
							if (!patch.getDeltas().isEmpty()) {
								if (!modified.contains(name)) modified.add(name);
							}
						}
					} catch (Exception e) {
						System.out.println("Error comparing repo file: " + name + " -> " + e.getMessage());
					}
				}
			}
		}

		return modified;
	}

	private List<String> getUntrackedFiles(Path currentDir, Path stagePath) {
		List<String> untracked = new ArrayList<>();
		List<String> stagedFiles = getChangesToBeCommitted(stagePath);

		File[] allFiles = currentDir.toFile().listFiles(file -> !file.getName().equals(".gitter"));
		if (allFiles != null) {
			for (File file : allFiles) {
				String name = file.getName();
				if (!stagedFiles.contains(name)) {
					untracked.add(name);
				}
			}
		}
		return untracked;
	}
}
