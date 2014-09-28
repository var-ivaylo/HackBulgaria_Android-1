package webcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WebCrawlerRunner {
	public static void main(String[] args) {
		if (args.length != 2)
			throw new IllegalArgumentException("You must provide a URL as the first parameter and needle as second.");
		else {
			WebCrawler crawler = new WebCrawler(100);
			try {
				crawler.crawl(new URL(args[0]), args[1]);
			} catch (MalformedURLException e) {
				// error with starting url;
				System.out.println(e);
			}			
			
			try {
				if (WebCrawler.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
					System.out.println(crawler.resultUrl);
					System.out.println(crawler.resultUrlPath);
					
					System.exit(1);
				} else {
					System.out.printf(
							"Needle \"%s\" was not found on the domain \"%s\"\n",
							crawler.needle, crawler.startingUrl);
					
					System.exit(-1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}