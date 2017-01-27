package com.photon.har.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarPage;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;

public class HarReportUtil {
	
	private static File workingFolder = null;
	private static File reportFile = null;
	private static HSSFWorkbook workbook = null;
	private String fileName = "";
	private HashMap<String, Double> onloadTimeMap;
	private HashMap<String, Double> pageloadTimeMap;
	//private static JSONObject harNameObject = new JSONObject();;
	
	public HarReportUtil(String fileName) {
		workingFolder = HarAnalyzerUtil.getDownoadsDirectory();
		if (!workingFolder.exists()) {
			workingFolder.mkdir();
		}
		onloadTimeMap = new HashMap<String, Double>();
		pageloadTimeMap = new HashMap<String, Double>();
		this.fileName = fileName;
		reportFile = new File(workingFolder.getAbsolutePath() + "\\" + fileName);
		workbook = new HSSFWorkbook();
		
		System.out.println(workingFolder.getAbsolutePath());
	}
	
	public void createReportHeader(File harFile) {
		try {
			String harfileName = harFile.getName();
			harfileName = harfileName.replace(".har", "");
			FileOutputStream fileOut = new FileOutputStream(reportFile);
			HSSFSheet worksheet = workbook.createSheet(harfileName);
			HSSFRow row = worksheet.createRow((short) 0);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("Iteration");
			cell = row.createCell(1);
			cell.setCellValue("Page_Load");
			cell = row.createCell(2);
			cell.setCellValue("On_Load");
			cell = row.createCell(3);
			cell.setCellValue("firstPaint");
			cell = row.createCell(4);
			cell.setCellValue("Bytes(bytesin)");
			cell = row.createCell(5);
			cell.setCellValue("Number of Requests");
			cell = row.createCell(6);
			cell.setCellValue("TTFB");
			
			workbook.write(fileOut);

			fileOut.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public JSONObject writeReportData(File harFile, Map<String, HashMap<String, Double>> onloadMap) {
		try {
			HarFileReader harFileReader = new HarFileReader();
			List<HarWarning> harWarnings = new ArrayList<HarWarning>();
			System.out.println(harFile);
			HarLog harLog = harFileReader.readHarFile(harFile, harWarnings);
			
			String sheetName = harFile.getName().replace(".har", "");
			
			FileInputStream fsIP = new FileInputStream(reportFile);
			HSSFSheet worksheet = workbook.getSheet(sheetName);
			
			JSONArray contentArray = new JSONArray();
			JSONObject harNameObject = new JSONObject();

			List<HarPage> pages = harLog.getPages().getPages();
			DescriptiveStatistics fullyLoadedStatistics = new DescriptiveStatistics();
			DescriptiveStatistics onlodStatistics = new DescriptiveStatistics();
			for (int i = 1; i <= pages.size(); i++) {
				JSONObject contentObject = new JSONObject();
				contentObject.put("iterationNo", i);
				
				HSSFRow row = worksheet.createRow(i);
				HSSFCell cell0 = row.createCell(0);
				cell0.setCellValue(i);
				
				HSSFCell cell2 = row.createCell(2);
				double onLoad = pages.get(i-1).getPageTimings().getOnLoad();
				cell2.setCellValue(onLoad);
				onlodStatistics.addValue(onLoad);
				contentObject.put("onLoad", onLoad);
				
				
				HSSFCell cell3 = row.createCell(3);
				String firstPaint = pages.get(i-1).getCustomFields().getCustomFieldValue("_firstPaint");
				cell3.setCellValue(Integer.parseInt(firstPaint));
				contentObject.put("firstPaint", Integer.parseInt(firstPaint));
				
				String bytesIn = pages.get(i-1).getCustomFields().getCustomFieldValue("_bytesIn");
				if (bytesIn != null) {
					HSSFCell cell4 = row.createCell(4);
					cell4.setCellValue(Long.parseLong(bytesIn));
					contentObject.put("bytesin", Integer.parseInt(bytesIn));
				}
				
				List<HarEntry> entries = harLog.getEntries().getEntries();
				List<HarEntry> pageHarEntry = new ArrayList<HarEntry>();
				int entryCount = 0;
				for (HarEntry harEntry : entries) {
					if (pages.get(i-1).getId().equals(harEntry.getPageRef())) {
						entryCount = entryCount + 1;
						pageHarEntry.add(harEntry);
					}
					HSSFCell cell5 = row.createCell(5);
					cell5.setCellValue(entryCount);
					contentObject.put("noOfRequests", entryCount);
				}
				
				String ttfb = pages.get(i-1).getCustomFields().getCustomFieldValue("_TTFB");
				HSSFCell cell6 = row.createCell(6);
				cell6.setCellValue(Integer.parseInt(ttfb));
				contentObject.put("ttfb", ttfb);
				
				HSSFCell cell1 = row.createCell(1);
				double calculatePageLoadTime = calculatePageLoadTime(pages.get(i-1), pageHarEntry);
				cell1.setCellValue(calculatePageLoadTime);
				fullyLoadedStatistics.addValue(calculatePageLoadTime);
				contentObject.put("pageLoad", calculatePageLoadTime);
				contentArray.put(contentObject);
			}	
			harNameObject.put("harName", sheetName);
			harNameObject.put("iterations", contentArray);
			int lastRowNumber = worksheet.getLastRowNum();
			HSSFRow lastRow = worksheet.createRow((short) lastRowNumber + 1);
			HSSFCell lastcell = lastRow.createCell(0);
			lastcell.setCellValue("90th Percentile");
			
			lastcell = lastRow.createCell(1);
			double fullyLoadedPercentile = fullyLoadedStatistics.getPercentile(90);
			lastcell.setCellValue(fullyLoadedPercentile);
			harNameObject.put("fullyLoaded90thPercentile", fullyLoadedPercentile);
			
			lastcell = lastRow.createCell(2);
			double onloadPercentile = onlodStatistics.getPercentile(90);
			lastcell.setCellValue(onloadPercentile);
			harNameObject.put("onload90thPercentile", onloadPercentile);
			
			if (onloadMap != null) {
				onloadTimeMap.put(sheetName, onlodStatistics.getPercentile(90));
				pageloadTimeMap.put(sheetName, fullyLoadedStatistics.getPercentile(90));
				onloadMap.put(fileName + "_onload", onloadTimeMap);
				onloadMap.put(fileName + "_pageload", pageloadTimeMap);
			}
			FileOutputStream output_file = new FileOutputStream(reportFile);
			workbook.write(output_file); // write changes
			
			fsIP.close();
			output_file.close();
			return harNameObject;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void harComparisionReport(Map<String, HashMap<String,Double>> onloadPageloadValueTimeMap, String firstFolderName, String secondFolderName) throws IOException {
		workbook = new HSSFWorkbook();
		HSSFSheet worksheet = workbook.createSheet("HarComparision");
		CellStyle style = workbook.createCellStyle();
		
		FileOutputStream fileOut = new FileOutputStream(workingFolder + "/HarComparision.xls");
		setHeaderHarComparision(firstFolderName, secondFolderName, 0, "Onload Tiles", worksheet, style);
		HashMap<String,Double> firstOnloadMap = onloadPageloadValueTimeMap.get(firstFolderName + "_onload");
		HashMap<String,Double> secondOnloadMap = onloadPageloadValueTimeMap.get(secondFolderName + "_onload");
		int lastRow = compareHar(firstOnloadMap, secondOnloadMap, worksheet, style);
		
		setHeaderHarComparision(firstFolderName, secondFolderName, lastRow +5, "Pageload Times", worksheet, style);
		HashMap<String,Double> firstPageloadMap = onloadPageloadValueTimeMap.get(firstFolderName + "_pageload");
		HashMap<String,Double> secondPageloadMap = onloadPageloadValueTimeMap.get(secondFolderName + "_pageload");
		compareHar(firstPageloadMap, secondPageloadMap, worksheet, style);
		
		workbook.write(fileOut);
		fileOut.close();
	}

	private static int compareHar(HashMap<String, Double> firstOnloadMap,
			HashMap<String, Double> secondOnloadMap, HSSFSheet worksheet, CellStyle style) {
		HSSFRow row;
		HSSFCell cell;
		int sno = 1;
		Set<String> keySet = firstOnloadMap.keySet();
		for (String key : keySet) {
			int lastRowNum = worksheet.getLastRowNum();
			row = worksheet.createRow((short) lastRowNum + 1);
			Double firstOnloadTime = firstOnloadMap.get(key);
			Double secondOnloadTime = secondOnloadMap.get(key);
			cell = row.createCell(0);
			setCellStyle(style, cell);
			cell.setCellValue(sno);
			
			cell = row.createCell(1);
			setCellStyle(style, cell);
			cell.setCellValue(key);
			
			cell = row.createCell(2);
			setCellStyle(style, cell);
			cell.setCellValue(firstOnloadTime);
			
			cell = row.createCell(3);
			setCellStyle(style, cell);
			
			HSSFCell cell4 = row.createCell(4);
			setCellStyle(style, cell4);
			
			HSSFCell cell5 = row.createCell(5);
			setCellStyle(style, cell5);
			
			if (secondOnloadTime != null) {
				cell.setCellValue(secondOnloadTime);
				
				double delta = secondOnloadTime - firstOnloadTime;
				cell4.setCellValue(delta);
				
				double deltaPercent = (secondOnloadTime - firstOnloadTime)/firstOnloadTime*100;
				cell5.setCellValue(deltaPercent);
			}
			sno++;
		}
		for (String key : secondOnloadMap.keySet()) {
			if (!firstOnloadMap.containsKey(key)) {
				int lastRowNum = worksheet.getLastRowNum();
				row = worksheet.createRow((short) lastRowNum + 1);
				
				cell = row.createCell(0);
				setCellStyle(style, cell);
				cell.setCellValue(sno);
				
				cell = row.createCell(1);
				setCellStyle(style, cell);
				cell.setCellValue(key);
				
				cell = row.createCell(2);
				setCellStyle(style, cell);
				
				cell = row.createCell(3);
				setCellStyle(style, cell);
				cell.setCellValue(secondOnloadMap.get(key));
				
				cell = row.createCell(4);
				setCellStyle(style, cell);
				
				cell = row.createCell(5);
				setCellStyle(style, cell);
				sno++;
			}
		}
		return sno;
	}

	private static void setHeaderHarComparision(String firstFolderName, String secondFolderName, 
			int rowNum, String title, HSSFSheet worksheet, CellStyle style)
			throws FileNotFoundException {
		HSSFRow row = worksheet.createRow((short) rowNum);
		CellRangeAddress region = new CellRangeAddress(rowNum, rowNum, 2, 5);
		worksheet.addMergedRegion(region);
		HSSFCell cell = row.createCell(2);
		setCellStyle(style, cell);
		cell.setCellValue(title);
		row = worksheet.createRow((short) rowNum + 1);
		cell = row.createCell(2);
		setCellStyle(style, cell);
		cell.setCellValue(firstFolderName);
		
		cell = row.createCell(3);
		setCellStyle(style, cell);
		cell.setCellValue(secondFolderName);
		
		cell = row.createCell(4);
		setCellStyle(style, cell);
		cell.setCellValue("Delta");
		
		cell = row.createCell(5);
		setCellStyle(style, cell);
		cell.setCellValue("Delta %");
	}

	private static void setCellStyle(CellStyle style, HSSFCell cell) {
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style);
	}
	
	
	
	private double calculatePageLoadTime(HarPage page, List<HarEntry> entryList) {
	    long pageLoadTime = 0;
	    long startTime = page.getStartedDateTime().getTime();
		long loadTime = 0;   
		long entryStartedTime = 0;
		long entrytime = 0;
		for (HarEntry entry : entryList)
		{
			entrytime = entrytime + entry.getTime();
			entryStartedTime = entryStartedTime + entry.getStartedDateTime().getTime();
		    long entryLoadTime = entry.getStartedDateTime().getTime() + entry.getTime();
		    if(entryLoadTime > loadTime){
		        loadTime = entryLoadTime;
		    }
		}
		pageLoadTime = loadTime - startTime;
		Double webLoadTime = ((double)pageLoadTime) / 1000;
        double webLoadTimeInSeconds = Math.round(webLoadTime * 100.0) / 100.0; 
        
	    return webLoadTimeInSeconds;
	}
}
