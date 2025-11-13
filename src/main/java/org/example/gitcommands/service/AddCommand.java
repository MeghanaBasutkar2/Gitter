package org.example.gitcommands.service;

import org.example.GitterConstants;

import java.nio.file.DirectoryStream;

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

			String[] splitCommand = command.trim().split("\\s+");
			String splitStr = splitCommand[2].trim();

			// gitter add . case:
			if (splitStr.equals(".")) {
				addAllFiles(splitStr, currentDir, stagePath, gitterDir);
			}

			// gitter add *.py or *.bat case:--
			else if (splitStr.startsWith("*.bat")) {
				String ext = splitStr.substring(1);
				addAllExtFiles(ext, splitStr, currentDir, stagePath);
			} else {
				// git add <single file> case:
				Path source = currentDir.resolve(splitStr);
				if (!Files.exists(source)) {
					System.out.println("File not found at: " + source.toAbsolutePath());
				}
				addSingleFile(splitStr, stagePath, source);
			}
		} catch (IOException e) {
			System.out.println(GitterConstants.FILE_ADD_ERR);
		}
	}

	private void addSingleFile(String splitStr, Path stagePath, Path source) {
		Path destination = stagePath.resolve(splitStr);

		try {
			if (Files.isDirectory(source)) {
				for (Path p : Files.walk(source).toList()) {
					if (Files.isRegularFile(p) && !p.toAbsolutePath().toString().contains(".gitter/stage")) {
						Path rel = source.relativize(p);
						Path dest = destination.resolve(rel);
						Path parent = dest.getParent();
						if (parent != null && !Files.exists(parent)) {
							Files.createDirectories(parent);
						}
						try {
							Files.copy(p, dest, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ex) {
							System.out.println("Error adding file");
						}
					}
				}
				return;
			}

			Path parent = destination.getParent();
			if (parent != null && !Files.exists(parent)) {
				Files.createDirectories(parent);
			}
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			// test123
			System.out.println(e.getMessage());
		}
	}

	private void addAllFiles(String splitStr, Path currentDir, Path stagePath, Path gitterDir) {
		if (splitStr.equals(".")) {
			try {
				Files.walk(currentDir)
						.filter(p -> Files.isRegularFile(p) &&
								!p.startsWith(gitterDir) &&
								!p.toAbsolutePath().toString().contains(".gitter/stage"))
						.forEach(p -> {
							try {
								Path rel = currentDir.relativize(p);
								Path destination = stagePath.resolve(rel);
								Path parent = destination.getParent();
								if (parent != null && !Files.exists(parent)) {
									Files.createDirectories(parent);
								}
								Files.copy(p, destination, StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException ex) {
								System.out.println("Something went wrong while staging all files");
							}
						});
			} catch (IOException e) {
				System.out.println("Error while adding all files: " + e.getMessage());
			}
		}
	}


	private void addAllExtFiles(String ext, String splitStr, Path currentDir, Path stagePath) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir, "*" + ext)) {
			for (Path p : stream) {
				Path destination = stagePath.resolve(p.getFileName());
				Files.copy(p, destination, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException ex) {
			System.out.println("Error while adding wildcard files: " + ex.getMessage());
		}
	}
}
