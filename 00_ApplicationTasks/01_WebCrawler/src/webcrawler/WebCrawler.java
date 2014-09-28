package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler extends Thread {
	private static final String URL_PATTERN = "((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&amp;]*)";
	private static final Pattern URL_REGEX = Pattern.compile(WebCrawler.URL_PATTERN, Pattern.CASE_INSENSITIVE);
	private static final String HREF_PATTERN = "href=\"(.*?)\"";
	private static final Pattern HREF_REGEX = Pattern.compile(WebCrawler.HREF_PATTERN, Pattern.CASE_INSENSITIVE);

	public boolean isFound;
	public String resultUrl;
	public String resultUrlPath;
	
	public static ExecutorService threadPool;

	public String startingUrl;
	public String needle;
	private String scope;

	private BlockingQueue<URL> linksToCrawl;
	private BlockingQueue<URL> crawledLinks;
	private Map<String, String> urlFromPairs;

	public WebCrawler(int threadsCount) {
		WebCrawler.threadPool = Executors.newFixedThreadPool(threadsCount);

		this.linksToCrawl = new LinkedBlockingQueue<URL>();
		this.crawledLinks = new LinkedBlockingQueue<URL>();
		this.urlFromPairs = new HashMap<String, String>();
	}

	public void crawl(URL url, String needle) {
		try {
			this.isFound = false;
			this.scope = url.getHost();
			this.startingUrl = url.toString();
			this.needle = needle;
			this.linksToCrawl.clear();
			this.crawledLinks.clear();
			this.urlFromPairs.clear();
			this.linksToCrawl.put(url);
			this.urlFromPairs.put(url.toString(), null);
		} catch (InterruptedException ex) {
			// should never happen;
		}
		this.start();
	}

	@Override
	public void run() {
		try {
			URL url;
			// TODO: break after waiting too long to take new link
			while (((url = this.linksToCrawl.take()) != null) && !this.isFound) {
				if (this.crawledLinks.contains(url)) {
					continue;
				}
				this.crawledLinks.put(url);
				WebCrawler.threadPool.execute(this);
				String pageContent = this.getPageContent(url);
				
				if (pageContent.contains(this.needle)) {
					this.isFound = true;
					this.resultUrl = url.toString();
					this.resultUrlPath = this.getUrlPath(url.toString());
					
					break;
				}
				
				if (pageContent.length() > 0) {
					this.parseUrls(pageContent, url);
				}
			}
			
			if (this.isFound) {
				WebCrawler.threadPool.shutdownNow();
			}
		} catch (InterruptedException ex) {
			// thread failed;
		}
	}

	private String getPageContent(URL url) {
		BufferedReader pageReader = null;
		StringBuilder buffer = new StringBuilder();
		try {
			URLConnection urlConnection = url.openConnection();
			if (urlConnection.getContentType().startsWith("text/html")) {
				pageReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String pageLine;
				while ((pageLine = pageReader.readLine()) != null) {
					buffer.append(pageLine);
				}
			}
		} catch (IOException ioe) {
			// error reading page;
		} finally {
			try {
				if (pageReader != null)
					pageReader.close();
			} catch (IOException ex) {
			}
		}
		
		return buffer.toString();
	}
	
	private void parseUrls(String page, URL urlFrom) {
		Matcher hrefMatcher = WebCrawler.HREF_REGEX.matcher(page);
		URL newUrl;
		while (hrefMatcher.find()) {
			String urlMatch = this.getUrl(hrefMatcher.group(1));
			try {
				newUrl = new URL(urlMatch);
				if (!newUrl.sameFile(urlFrom) && newUrl.getHost().equals(this.scope) &&
						!this.crawledLinks.contains(newUrl) && !this.linksToCrawl.contains(newUrl)) {
					this.linksToCrawl.offer(newUrl);
					this.urlFromPairs.put(newUrl.toString(), urlFrom.toString());
				}
			} catch (MalformedURLException e) {
				// url error;
			}
		}
	}
	
	private String getUrl(String url) {
		if (this.isValidUrl(url)) {
			return url;
		} else {
			if (this.startingUrl.endsWith("/") && url.startsWith("/")) {
				return this.startingUrl + url.substring(1);
			} else if (!this.startingUrl.endsWith("/") && !url.startsWith("/")) {
				return this.startingUrl + "/" + url;
			}

			return this.startingUrl + url;
		}
	}

	private Boolean isValidUrl(String url) {
		Boolean result = WebCrawler.URL_REGEX.matcher(url).find();
		WebCrawler.URL_REGEX.matcher(url).reset();
		return result;
	}
	
	private String getUrlPath(String keyUrl) {
		List<String> reversedPath = new ArrayList<String>();
		reversedPath.add(keyUrl);

		String url = keyUrl;
		while ((url = this.urlFromPairs.get(url)) != null) {
			reversedPath.add(url);
		}

		StringBuilder result = new StringBuilder(reversedPath.get(reversedPath.size() - 1));
		for (int i = reversedPath.size() - 2; i >= 0; i--) {
			result.append(", " + reversedPath.get(i));
		}

		return result.toString();
	}
}