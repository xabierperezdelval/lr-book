package com.mpower.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationUtil {
	
	public static List<String> extractEmails(String text) {
		final Pattern linePattern = Pattern.compile("([\\w+|\\.?]+)\\w+@([\\w+|\\.?]+)\\.(\\w{2,8}\\w?)");
		List<String> emails = new ArrayList<String>();
		Matcher pattern = linePattern.matcher(text); // Line matcher
		
		while (pattern.find()) {
			emails.add(pattern.group());
		}
		
		// remove duplicates from the list. 
		Set<String> uniqueEmails = new HashSet<String>(emails);
		
		return new ArrayList<String>(uniqueEmails);
	}
}
