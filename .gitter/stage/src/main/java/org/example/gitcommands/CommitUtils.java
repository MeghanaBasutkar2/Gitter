package org.example.gitcommands;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommitUtils {

	public static String getDate() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy Z");
		return now.format(formatter);
	}

	public static String generateUUID() {
		String uuidStr = UUID.randomUUID().toString();
		return uuidStr.length() > 40 ? uuidStr.substring(0, 40) : uuidStr;
	}
}
