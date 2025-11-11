package org.example.gitcommands.service;

import org.example.GitterConstants;

import java.io.File;
import java.nio.file.Paths;

import static org.example.GitterConstants.*;

public class InitCommand {
	private static InitCommand initCommandInstance;

	private InitCommand() {
	}

	public static InitCommand getInitCommandInstance() {
		if (initCommandInstance == null) {
			initCommandInstance = new InitCommand();
			return initCommandInstance;
		} else {
			return initCommandInstance;
		}
	}

	public void init() {
		try {
			String currentDir = System.getProperty("user.dir");
			System.out.println(currentDir);
			File fileObject = Paths.get(currentDir, ".gitter").toFile();
			if (fileObject.exists() && fileObject.isDirectory()) {
				System.out.println("Gitter repository is already initialised in " + fileObject.getAbsolutePath());
			} else {
				// create new dir
				if (fileObject.mkdir()) {
					System.out.println(INIT_MESSAGE + fileObject.getAbsolutePath());
				} else {
					System.out.println(FILE_CREATION_ERR);
				}
			}
		} catch (SecurityException | UnsupportedOperationException e) {
			System.out.println(e.getMessage());
		}
	}
}
