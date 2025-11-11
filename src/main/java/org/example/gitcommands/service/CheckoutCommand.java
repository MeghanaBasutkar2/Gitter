package org.example.gitcommands.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static org.example.GitterConstants.*;

public class CheckoutCommand {
	private static CheckoutCommand checkoutCommandInstance;

	public static CheckoutCommand getCheckoutCommandInstance() {
		if (checkoutCommandInstance == null) {
			checkoutCommandInstance = new CheckoutCommand();
			return checkoutCommandInstance;
		} else {
			return checkoutCommandInstance;
		}
	}

	public void checkout(String command) {
		System.out.println(command); // gitter checkout -b feature (or) gitter checkout feature
		try {
			String[] afterCheckoutParams = command.split(" ");
			String afterCheckout = afterCheckoutParams[2];
			boolean isNewBranch = afterCheckout.equals("-b");
			Path dirPath = Path.of(System.getProperty("user.dir"), ".gitter");
			Path branchesDir = dirPath.resolve("branches");
			Path headDir = dirPath.resolve("HEAD");

			if (!Files.exists(branchesDir)) {
				Files.createDirectories(branchesDir);
			}
			if (!Files.exists(headDir)) {
				Files.createFile(headDir);
			}

			if (isNewBranch) {
				String newBranch = afterCheckoutParams[3];
				System.out.println("branch name " + command.split(" ")[3]);
				Path newBranchPath = branchesDir.resolve(newBranch);
				// branch already exists but trying to add as a new one
				if (Files.exists(newBranchPath)) {
					System.out.println("A branch already exists with the name " + newBranch);
				} else {
					// branch does not exist
					Files.createFile(newBranchPath);
					Files.writeString(headDir, newBranch);
					System.out.println(CHECKOUT_MESSAGE + newBranch);
				}
			} else {
				// checkout to existing branch case:
				String existingBranch = afterCheckout;
				Path branchPath = branchesDir.resolve(existingBranch);
				if (Files.exists(branchPath)) {
					Files.writeString(headDir, existingBranch);
					System.out.println(CHECKOUT_MESSAGE_EXISTING + existingBranch);
				} else {
					System.out.println(existingBranch + CHECKOUT_MESSAGE_EXISTING_ERROR);
				}
			}
		} catch (IndexOutOfBoundsException | InvalidPathException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
