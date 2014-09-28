package globexpressions;

import java.util.regex.Pattern;

public class Glob {
	private Pattern glob;
	private boolean allowExtraDirs;
	private int directoryCount;
	
	private Glob(String pattern) {
		this.glob = this.preparePattern(pattern);
		this.allowExtraDirs = this.charCount(pattern, '*') > 1 ? true : false;
		this.directoryCount = this.charCount(pattern, '/');
	}
	
	public static Glob compile(String pattern) {
		return new Glob(pattern);
	}
	
	public boolean matches(String input) {
		if (!this.allowExtraDirs && this.charCount(input, '/') != this.directoryCount) {
			return false;
		} else {
			return this.glob.matcher(input).matches();
		}
	}
	
	private Pattern preparePattern(String pattern) {
		StringBuilder regexPattern = new StringBuilder();
		for (int i = 0; i < pattern.length(); i++) {
			char currentChar = pattern.charAt(i);
			switch (currentChar) {
			case '*':
				regexPattern.append(".*");
				break;
				
			case '?':
				regexPattern.append('.');
				break;
				
			case '{':				
				regexPattern.append('(');
				char inBrackets;
				for (i = i + 1, inBrackets = pattern.charAt(i); inBrackets != '}'; i++, inBrackets = pattern.charAt(i)) {
					if (inBrackets == ',') {
						regexPattern.append('|');
					} else {
						regexPattern.append(inBrackets);
					}
				}
				regexPattern.append(')');				
				break;
				
			default:
				regexPattern.append(currentChar);
			}
		}
		
	//	System.out.println(regexPattern.toString());
		return Pattern.compile(regexPattern.toString());
	}
	
	private int charCount(String str, char symbol) {
		int counter = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == symbol) {
				counter++;
			}
		}
		
		return counter;
	}
}