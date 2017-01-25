package com.photon.har.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

//@Path("/")
public class DownloadHar {
	
	static final Logger log = Logger.getLogger("debugLogger");
	
	public static void main(String[] args) {
		
		//String release, browser / Device ,environmen, bandwidth,  "Android, IOS, IE10, IE11, chrome, Firefox" ;
		
		System.out.println(System.getProperty("user.dir"));
		
		String release = "ARYA";
		String browser = "chrome";
		String env = "m-perf";
		String bandwidth = "20";
		//downloadHarFile("http://10.237.138.39/result/170117_BF_2/", 
				//"D:/work/WPT Analyzer/harFiles/", release, browser, env, bandwidth);
		System.out.println(DownloadHar.class.getProtectionDomain().getCodeSource().getLocation());
	}
	

	public static String downloadHarFile(String URL, String directory, String release, String browser, String env, String bandwidth) {
		String line = null;
		Map<String, String> fileList = new HashMap<String, String>();
		String currentTime;
		try {
			String pageContext = URL.substring(0, URL.indexOf("/result"));

			File sourceFile = new File(directory + "/source.txt");
			FileUtils.copyURLToFile(new URL(URL), sourceFile);

			BufferedReader reader = new BufferedReader(new FileReader(sourceFile));

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
			sourceFile.delete();
			File temp  = new File(directory + File.separator + "temp");
			if (!temp.isDirectory()) {
				temp.mkdirs();
			}
			for (Map.Entry<String, String> entrySet : fileList.entrySet()) {
				String key = (String) entrySet.getKey();
				String value = (String)entrySet.getValue();
				String fileName = value + "_" + release + "_"+ browser + "_" +env + "_" + bandwidth + ".har";
				System.out.println(fileName);
				File pageData = new File(temp + File.separator + fileName);
				if (!pageData.exists()) {
					FileUtils.copyURLToFile(new URL(key), pageData);
					log.info("Downloaded : " + key);
				} else {
					log.info("File exists already! : " + key);
				}
			}
			DateFormat df = new SimpleDateFormat("ddMMYYYYHHmmss");
			Date dateobj = new Date();
			currentTime = df.format(dateobj);
			zipIt(directory + File.separator + "harFiles" + currentTime +".zip", temp);
			
			log.info("Application Ended!----------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error : " + e.getMessage());
			return "Error";
		}
		return "harFiles" + File.separator + "harFiles" + currentTime + ".zip";
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
	
	public static void zipIt(String zipFile, File sourceFolder){
	     byte[] buffer = new byte[1024];
	     try{
	    	FileOutputStream fos = new FileOutputStream(zipFile);
	    	ZipOutputStream zos = new ZipOutputStream(fos);

	    	File[] listFiles = sourceFolder.listFiles();
	    	for(File file : listFiles){
	    		System.out.println("File Added : " + file);
	    		ZipEntry ze= new ZipEntry(generateZipEntry(file.getAbsolutePath(), sourceFolder.getAbsolutePath()));
	        	zos.putNextEntry(ze);
	        	FileInputStream in = new FileInputStream(file);
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	        	in.close();
	    	}
	    	zos.closeEntry();
	    	zos.close();
	    	System.out.println("Done");
	    }catch(IOException ex){
	       ex.printStackTrace();
	    }
	   }
	
	private static String generateZipEntry(String file, String sourceFolder){
    	return file.substring(sourceFolder.length()+1, file.length());
    }
	
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

