package globexpressions;

public class GlobRunner {
	public static void main(String[] args) {
		Glob glob = Glob.compile("abc?");
	    System.out.println(glob.matches("abc")); // assertFalse
	    System.out.println(glob.matches("abcd")); // assertTrue
	    System.out.println(glob.matches("abcde")); // assertFalse
	    
	    Glob glob2 = Glob.compile("a*d");
	    System.out.println(glob2.matches("abcd")); // assertTrue
	    System.out.println(glob2.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd")); // assertTrue
	    System.out.println(glob2.matches("abcdej")); // assertFalse
	    System.out.println(glob2.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd1")); // assertFalse
	    
	    Glob glob3 = Glob.compile("*.html");
	    System.out.println(glob3.matches("index.html")); // assertTrue
	    System.out.println(glob3.matches("index.htm")); // assertFalse
	    System.out.println(glob3.matches("directory/index.html")); // assertFalse
	    
	    Glob glob4 = Glob.compile("Di{nko,mitur}");
	    System.out.println(glob4.matches("Dimitur")); // assertTrue
	    System.out.println(glob4.matches("Dinko")); // assertTrue
	    System.out.println(glob4.matches("Divna")); // assertFalse
	    
	    Glob glob5 = Glob.compile("/home/georgi/**index.html");
	    System.out.println(glob5.matches("/home/georgi/testme/testme2/index.html")); // assertTrue
	    System.out.println(glob5.matches("/home/georgi/testme/testme2/testME_index.html")); // assertTrue
	    System.out.println(glob5.matches("/home/index.html")); // assertFalse
	    
	    Glob glob6 = Glob.compile("/home/papka/*innerPapka*/fil?.java");
	    System.out.println(glob6.matches("/home/papka/vutrevinnerPapkawe/fild.java")); // assertTrue
	}
}