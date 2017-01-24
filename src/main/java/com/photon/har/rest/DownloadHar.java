package com.photon.har.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class DownloadHar {
	static final Logger log = Logger.getLogger("debugLogger");
	
	public static void main(String[] args) {
		
		//String release, browser / Device ,environmen, bandwidth,  "Android, IOS, IE10, IE11, chrome, Firefox" ;
		
		String release = "ARYA";
		String browser = "chrome";
		String env = "m-perf";
		String bandwidth = "20";
		downloadHarFile("http://10.237.138.39/result/170117_BF_2/", 
				"D:/work/WPT Analyzer/harFiles/", release, browser, env, bandwidth);
	}

	public static String downloadHarFile(String URL, String directory, String release, String browser, String env, String bandwidth) {
		String line = null;
		Map<String, String> fileList = new HashMap<String, String>();
		try {
			String pageContext = URL.substring(0, URL.indexOf("/result"));

			File file = new File(directory + "/source.txt");
			FileUtils.copyURLToFile(new URL(URL), file);

			BufferedReader reader = new BufferedReader(new FileReader(file));

			boolean start = false;
			boolean end = false;
			while (((line = reader.readLine()) != null) && (!end)) {
				if ((line.contains("table")) && (line.contains("batchResults"))) {
					start = true;
				}
				if ((start) && (line.contains("</table>"))) {
					end = true;
				}
				if (start) {
					String testUrl = getTestURL(line);
					if (testUrl != null) {
						testUrl = getUrlName(testUrl);
					}
					String harLink = getHarFileLink(line);
					harLink = pageContext + harLink;
					if ((testUrl != null) && (harLink != null)) {
						fileList.put(harLink, testUrl);
					}
				}
			}
			reader.close();
			file.delete();
			for (Map.Entry<String, String> entrySet : fileList.entrySet()) {
				String key = (String) entrySet.getKey();
				String value = (String)entrySet.getValue();
				String fileName = value + "_" + release + "_"+ browser + "_" +env + "_" + bandwidth + ".har";
				System.out.println(fileName);
				File pageData = new File(directory + "/" + fileName);
				if (!pageData.exists()) {
					FileUtils.copyURLToFile(new URL(key), pageData);
					log.info("Downloaded : " + key);
				} else {
					log.info("File exists already! : " + key);
				}
			}
			log.info("Application Ended!----------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error : " + e.getMessage());
			return "Error";
		}
		return "Success";
	}

	public static String getTestURL(String line) {
		if (line.contains("title") && line.contains("<a href")) {
			int titleIndex = line.indexOf("<a href");
			line = line.substring(titleIndex);
			int startIndex = line.indexOf("title=\"");
			line = line.substring(startIndex + 7);
			int endIndex = line.indexOf("\"");
			line = line.substring(0, endIndex);
			System.out.println(line);
			return line;
		}
		return null;
	}

	public static String getHarFileLink(String line) {
		if (line.contains("Download HAR")) {
			int pageIndex = line.indexOf("Download HAR");
			line = line.substring(0, pageIndex);
			int anchorIndex = line.lastIndexOf("<a ");
			line = line.substring(anchorIndex);
			int startIndex = line.indexOf("=\"");
			line = line.substring(startIndex + 2);
			int endIndex = line.indexOf("\"");
			line = line.substring(0, endIndex);
			return line;
		}
		return null;
	}
	
	public static String getUrlName(String url) {
		String urlName = "";
		String[] split = url.split("/");
		int length = split.length;
		urlName = split[length -1].replace(".jsp", "");
		String[] tierPage = urlName.split("-");
		if (tierPage.length > 0) {
			urlName = tierPage[tierPage.length - 1].replace("general", "");
		}
		if (urlName.contains("walgreens.com")) {
			urlName = "Home";
		} else if ("product".equals(urlName)) {
			urlName = "VPD";
		} else if (urlName.contains("results?Ntt")) {
			urlName = "searchResult";
		} else if (urlName.contains("result?requestType=locator")) {
			urlName = "storeLoctor";
		} else if (urlName.contains("id=")) {
			urlName = "storeDetail";
		}
		return urlName;
	}

	/*
	 * public static void parseJsonHarFile(File file) { try { if (file.exists())
	 * { JSONParser parser = new JSONParser();
	 * 
	 * Object obj = parser.parse(new FileReader(file)); JSONObject jsonObject =
	 * (JSONObject)obj; JSONObject log = (JSONObject)jsonObject.get("log");
	 * JSONArray pages = (JSONArray)log.get("pages"); for (int i = 0; i <
	 * pages.size(); i++) { JSONObject page = (JSONObject)pages.get(i); long
	 * loadTime = ((Long)page.get("_loadTime")).longValue(); long fullLoadTime =
	 * ((Long)page.get("_fullyLoaded")).longValue(); System.out.println(loadTime
	 * + "  " + fullLoadTime); } } } catch (Exception e) { e.printStackTrace();
	 * } }
	 */
}
