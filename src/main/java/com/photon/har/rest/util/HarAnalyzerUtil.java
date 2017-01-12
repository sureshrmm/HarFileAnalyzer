package com.photon.har.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.json.JSONString;

import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;
import de.sstoehr.harreader.model.HarPage;

public class HarAnalyzerUtil {

	private static File f;
	private static File workingFolder;
	private static int firstPageOnload = 0;
	private static int secondPageOnLoad = 0;
	private static List<String> sheet1cellHeaders;
	private static List<String> sheet2CellHeader;
	private static List<String> sheet3Cells;
	private static String firstHarName;
	private static String secondHarName;
	private static boolean firstwritten = false;
	private static boolean secondwritten = false;
	private static JSONObject firstReleaseObject = new JSONObject();
	private static JSONArray firstContentArray = new JSONArray();
	private static JSONArray secondContentArray = new JSONArray();
	private static JSONObject rootObject = new JSONObject();
	private static JSONArray contentDifference = new JSONArray();
	

	static {
		File currentFolder = new File(".");
		workingFolder = new File(currentFolder, "WebPageReport_XLS");
		if (!workingFolder.exists()) {
			workingFolder.mkdir();
		}
		System.out.println(workingFolder.getAbsolutePath());
		f = new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls");

		sheet1cellHeaders = new ArrayList<String>();
		sheet1cellHeaders.add("URL");
		sheet1cellHeaders.add("ResponseCode");
		sheet1cellHeaders.add("Time Taken");
		sheet1cellHeaders.add("File Size");
		sheet1cellHeaders.add("Onload");
		sheet1cellHeaders.add("URL");
		sheet1cellHeaders.add("ResponseCode");
		sheet1cellHeaders.add("Time Taken");
		sheet1cellHeaders.add("File Size");
		sheet1cellHeaders.add("Onload");
		sheet1cellHeaders.add("Time in diff");
		sheet1cellHeaders.add("Size in diff");

		sheet2CellHeader = new ArrayList<String>();
		sheet2CellHeader.add("URL");
		sheet2CellHeader.add("ResponseCode");
		sheet2CellHeader.add("Time Taken");
		sheet2CellHeader.add("File Size");
		sheet2CellHeader.add("Onload");

		sheet3Cells = new ArrayList<String>();
		sheet3Cells.add("Total Request");
		sheet3Cells.add("HTML / Text - Count");
		sheet3Cells.add("HTML / Text - Size");
		sheet3Cells.add("JavaScript Count");
		sheet3Cells.add("JavaScript Size");
		sheet3Cells.add("CSS - Count");
		sheet3Cells.add("CSS - Size");
		sheet3Cells.add("Image - Count");
		sheet3Cells.add("Image - Size");
		sheet3Cells.add("Others- Count");
		sheet3Cells.add("Others - Size");
	}

	public static void createOutputFolderNewXlsFile(HarPage firstPage, String FirstHarFileName, HarPage secondPage, String secondHarFileName) throws IOException {

		firstPageOnload = firstPage.getPageTimings().getOnLoad();
		secondPageOnLoad = secondPage.getPageTimings().getOnLoad();

		firstHarName = FirstHarFileName;
		secondHarName = secondHarFileName;
		
		if (f.exists()) {

			System.out.println("File already exists>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			f.delete();
			System.out.println("File Deleted Succesfully.....................................................");
			createXLSHeaderColumnName(); // create new file
											// folder.........................
		} else {

			System.out.print("FILE NOT FOUND!!!!: ");
			createXLSHeaderColumnName(); // create new file
											// folder.........................
			System.out.println("SUCCESSFULLY Created New XlS File............................................");
		}
	}

	public static void createXLSHeaderColumnName() {
		try {
			FileOutputStream fileOut = new FileOutputStream(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls");
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet1 = workbook.createSheet("Matched_Urls");
			HSSFRow sheetoneTitleRow = worksheet1.createRow((short) 0);
			HSSFRow row1 = worksheet1.createRow((short) 1);
			CellStyle style = workbook.createCellStyle();
			HSSFSheet worksheet2 = workbook.createSheet("Not_Matched_Urls");
			HSSFRow row2 = worksheet2.createRow((short) 0);
			HSSFSheet worksheet3 = workbook.createSheet("Total_Comparision");
			HSSFRow row3 = worksheet3.createRow((short) 0);
			
			HSSFCell cell = null;
			
			CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
			worksheet1.addMergedRegion(region);
			
			region = new CellRangeAddress(0, 0, 5, 9);
			worksheet1.addMergedRegion(region);
			
			cell = sheetoneTitleRow.createCell(0);
			style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);
			cell.setCellValue(firstHarName);
			
			cell = sheetoneTitleRow.createCell(5);
			style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);
			cell.setCellValue(secondHarName);
			
			for (int i = 0; i < sheet1cellHeaders.size(); i++) {
				cell = row1.createCell(i);
				style = workbook.createCellStyle();
				style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style);
				cell.setCellValue(sheet1cellHeaders.get(i));
			}

			for (int i = 0; i < sheet2CellHeader.size(); i++) {
				cell = row2.createCell(i+1);
				style = workbook.createCellStyle();
				style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style);
				cell.setCellValue(sheet2CellHeader.get(i));
			}

			cell = row3.createCell(1);
			cell.setCellValue(firstHarName);
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);

			cell = row3.createCell(2);
			cell.setCellValue(secondHarName);
			style = workbook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell.setCellStyle(style);

			for (int i = 1; i < sheet3Cells.size(); i++) {
				HSSFRow newRow = worksheet3.createRow((short) i);
				cell = newRow.createCell(0);
				cell.setCellValue(sheet3Cells.get(i - 1));
			}

			// write here................
			workbook.write(fileOut);

			fileOut.close();
			// xlsReadWriteUpdate(workingFolder, harEntry);

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			System.out.println(
					"XlS File is not created!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void xlsReadWriteUpdate(List<HarEntry> firstEntryList, List<HarEntry> secondEntryList)
			throws IOException, JSONException {
		String baseUrl = firstEntryList.get(0).getRequest().getUrl();
		baseUrl = baseUrl.split("/")[2];
		List<HarEntry> firstAddedList = new ArrayList<HarEntry>();
		List<HarEntry> secondAddedList = new ArrayList<HarEntry>();
		for (HarEntry firstEntry : firstEntryList) {
			String firstEntryUrl = firstEntry.getRequest().getUrl();
			firstEntryUrl = getProperUrl(firstEntryUrl);
			for (HarEntry secondEntry : secondEntryList) {
				String secondEntryUrl = secondEntry.getRequest().getUrl();
				secondEntryUrl = getProperUrl(secondEntryUrl);
				if (firstEntryUrl.equals(secondEntryUrl)) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
				}
			}
		}
		for (HarEntry firstEntry : firstEntryList) {
			String firstEntryUrl = firstEntry.getRequest().getUrl();
			firstEntryUrl = getProperUrl(firstEntryUrl);
			for (HarEntry secondEntry : secondEntryList) {
				String secondEntryUrl = secondEntry.getRequest().getUrl();
				secondEntryUrl = getProperUrl(secondEntryUrl);
				 if (firstEntryUrl.startsWith("https://smetrics.walgreens.com")
						&& secondEntryUrl.startsWith("https://smetrics.walgreens.com")) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
					break;
				} else if (firstEntryUrl.startsWith("https://dpm.demdex.net")
						&& secondEntryUrl.startsWith("https://dpm.demdex.net")) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
					break;
				} else if (firstEntryUrl.startsWith("https://device.4seeresults.com")
						&& secondEntryUrl.startsWith("https://device.4seeresults.com")) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
					break;
				} else if (firstEntryUrl.startsWith("https://pixel.mathtag.com")
						&& secondEntryUrl.startsWith("https://pixel.mathtag.com")) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
					break;
				} else if (firstEntryUrl.startsWith("https://ak1s.abmr.net")
						&& secondEntryUrl.startsWith("https://ak1s.abmr.net")) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
					break;
				} else if (firstEntryUrl.startsWith("https://cm.g.doubleclick.net")
						&& secondEntryUrl.startsWith("https://cm.g.doubleclick.net")) {
					writeXLSheet(firstAddedList, secondAddedList, firstEntry, firstEntryUrl, secondEntry,
							secondEntryUrl, true);
					break;
				}
			}
		}
		removeHarEntry(firstEntryList, secondEntryList, firstAddedList, secondAddedList);

		if (firstEntryList.size() > 0) {
			for (HarEntry harEntry : firstEntryList) {
				writeSecondSheet(harEntry, firstAddedList, firstHarName);
			}
		}
		if (secondEntryList.size() > 0) {
			for (HarEntry harEntry : secondEntryList) {
				writeSecondSheet(harEntry, secondAddedList, secondHarName);
			}
		}
		
		JSONObject jsonObject= new JSONObject();
		jsonObject.put("requestDetails", firstContentArray);
		jsonObject.put("releaseName", firstHarName);
		
		JSONObject jsonObject1= new JSONObject();
		jsonObject1.put("requestDetails", secondContentArray);
		jsonObject1.put("releaseName", secondHarName);
		
		firstReleaseObject.append("details", jsonObject);
		firstReleaseObject.append("details", jsonObject1);
		firstReleaseObject.put("differences", contentDifference);
		
		//jsonObject = firstReleaseObject.append("details", jsonObject);
		
		rootObject.put("matchedURLs", firstReleaseObject);
		
		/*JSONObject secondJsonObject= new JSONObject();
		secondJsonObject = secondJsonObject.put("requestDetails", secondContentArray);
		secondJsonObject = secondJsonObject.put("releaseName", secondHarName);
		secondJsonObject = firstReleaseObject.append("details", secondJsonObject);
		rootObject.put("Matched_Urls", secondJsonObject);*/

		removeHarEntry(firstEntryList, secondEntryList, firstAddedList, secondAddedList);

		writeThirdSheet(firstAddedList, 1);
		writeThirdSheet(secondAddedList, 2);
		
		
		System.out.println("rootObject=========> " + rootObject.toString());

	}

	private static void removeHarEntry(List<HarEntry> firstEntryList, List<HarEntry> secondEntryList,
			List<HarEntry> firstAddedList, List<HarEntry> secondAddedList) {
		for (HarEntry harEntry : firstAddedList) {
			firstEntryList.remove(harEntry);
		}
		for (HarEntry harEntry : secondAddedList) {
			secondEntryList.remove(harEntry);
		}
	}

	private static void writeThirdSheet(List<HarEntry> harEntries, int col) {
		try {
			FileInputStream fsIP = new FileInputStream(
					new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls"));

			HSSFWorkbook wb = new HSSFWorkbook(fsIP);
			HSSFSheet worksheet = wb.getSheetAt(2);

			HSSFRow row1 = worksheet.getRow(1);
			HSSFCell cell = row1.createCell(col);
			cell.setCellValue(harEntries.size());

			int htmlCount = 0;
			long htmlSize = 0;
			int jsCount = 0;
			long jsSize = 0;
			int cssCount = 0;
			long cssSize = 0;
			int imageCount = 0;
			long imageSize = 0;
			int othersCount = 0;

			for (HarEntry harEntry : harEntries) {
				List<HarHeader> headers = harEntry.getResponse().getHeaders();
				for (HarHeader harHeader : headers) {
					if ("Content-Type".equals(harHeader.getName()) && harHeader.getValue().startsWith("text/html")) {
						htmlCount = htmlCount + 1;
						long size = harEntry.getResponse().getContent().getSize();
						htmlSize = htmlSize + size;
						break;
					} else if ("Content-Type".equals(harHeader.getName())
							&& (harHeader.getValue().startsWith("application/javascript")
									|| harHeader.getValue().startsWith("application/x-javascript")
									|| harHeader.getValue().startsWith("text/javascript"))) {
						jsCount = jsCount + 1;
						long size = harEntry.getResponse().getContent().getSize();
						jsSize = jsSize + size;
						break;
					} else if ("Content-Type".equals(harHeader.getName())
							&& harHeader.getValue().startsWith("text/css")) {
						cssCount = cssCount + 1;
						long size = harEntry.getResponse().getContent().getSize();
						cssSize = cssSize + size;
						break;
					} else if ("Content-Type".equals(harHeader.getName())
							&& (harHeader.getValue().startsWith("image/webp")
									|| harHeader.getValue().startsWith("image/gif")
									|| harHeader.getValue().startsWith("image/png"))) {
						imageCount = imageCount + 1;
						long size = harEntry.getResponse().getContent().getSize();
						imageSize = imageSize + size;
						break;
					}
				}
			}

			othersCount = harEntries.size() - htmlCount - jsCount - cssCount - imageCount;

			HSSFRow row2 = worksheet.getRow(2);
			HSSFCell cell2 = row2.createCell(col);
			cell2.setCellValue(htmlCount);

			HSSFRow row3 = worksheet.getRow(3);
			HSSFCell cell3 = row3.createCell(col);
			cell3.setCellValue(htmlSize);

			HSSFRow row4 = worksheet.getRow(4);
			HSSFCell cell4 = row4.createCell(col);
			cell4.setCellValue(jsCount);

			HSSFRow row5 = worksheet.getRow(5);
			HSSFCell cell5 = row5.createCell(col);
			cell5.setCellValue(jsSize);

			HSSFRow row6 = worksheet.getRow(6);
			HSSFCell cell6 = row6.createCell(col);
			cell6.setCellValue(cssCount);

			HSSFRow row7 = worksheet.getRow(7);
			HSSFCell cell7 = row7.createCell(col);
			cell7.setCellValue(cssSize);

			HSSFRow row8 = worksheet.getRow(8);
			HSSFCell cell8 = row8.createCell(col);
			cell8.setCellValue(imageCount);

			HSSFRow row9 = worksheet.getRow(9);
			HSSFCell cell9 = row9.createCell(col);
			cell9.setCellValue(imageSize);

			HSSFRow row10 = worksheet.getRow(10);
			HSSFCell cell10 = row10.createCell(col);
			cell10.setCellValue(othersCount);

			FileOutputStream output_file = new FileOutputStream(
					new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls"));

			wb.write(output_file); // write changes

			fsIP.close();
			output_file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeSecondSheet(HarEntry harEntry, List<HarEntry> addedList, String harName) {
		try {
			FileInputStream fsIP = new FileInputStream(
					new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls"));

			addedList.add(harEntry);

			HSSFWorkbook wb = new HSSFWorkbook(fsIP);
			HSSFSheet worksheet = wb.getSheetAt(1);
			
			int lastRow = worksheet.getLastRowNum();

			HSSFRow row = worksheet.createRow((short) lastRow + 1);

			HSSFCell cell;
			if (harName.equals(firstHarName) && !firstwritten) {
				cell = row.createCell(0);
				cell.setCellValue(harName + " Not Matched Url's");
				firstwritten = true;
			}
			
			if (harName.equals(secondHarName) && !secondwritten) {
				cell = row.createCell(0);
				cell.setCellValue(harName + " Not Matched Url's");
				secondwritten = true;
			}
			
			cell = row.createCell(1);
			cell.setCellValue(harEntry.getRequest().getUrl());

			cell = row.createCell(2);
			cell.setCellValue(harEntry.getResponse().getStatus());

			int time = harEntry.getTime();
			String ConvertedTime = time + "ms";
			if (time >= 1000) {
				double ss = time / 1000.0;
				ConvertedTime = ss + "s";
			}
			cell = row.createCell(3);
			cell.setCellValue(ConvertedTime);

			Long size = harEntry.getResponse().getContent().getSize();
			String ConvertedFileSize = size + "b";
			cell = row.createCell(4);
			if (size > 1024) {
				float converting = (float) (size / 1024.0);
				ConvertedFileSize = converting + "kb";
			}
			cell.setCellValue(ConvertedFileSize);

			cell = row.createCell(5);
			Date startedDateTime = harEntry.getStartedDateTime();
			long onloadtime = startedDateTime.getTime();
			if (firstPageOnload > onloadtime) {
				cell.setCellValue("BEFORE");
			} else {
				cell.setCellValue("AFTER");
			}

			FileOutputStream output_file = new FileOutputStream(
					new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls"));

			wb.write(output_file); // write changes

			fsIP.close();
			output_file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeXLSheet(List<HarEntry> firstAddedList, List<HarEntry> secondAddedList, HarEntry firstEntry,
			String firstEntryUrl, HarEntry secondEntry, String secondEntryUrl, boolean machedUrl)
			throws FileNotFoundException, IOException, JSONException {

		if (firstEntry != null && firstAddedList.contains(firstEntry)) {
			return;
		}

		if (secondEntry != null && secondAddedList.contains(secondEntry)) {
			return;
		}

		firstAddedList.add(firstEntry);
		secondAddedList.add(secondEntry);

		FileInputStream fsIP = new FileInputStream(
				new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls"));

		HSSFWorkbook wb = new HSSFWorkbook(fsIP);
		HSSFSheet worksheet = wb.getSheetAt(0);
		CellStyle style = wb.createCellStyle();

		int lastRow = worksheet.getLastRowNum();
		// System.out.println(lastRow);

		HSSFRow row = worksheet.createRow((short) lastRow + 1);
		Integer firstTime = null;
		Integer secondEntryTime = null;
		Long firstEntryFileSize = null;
		Long secondEntryFileSize = null;
		String firstTimeTaken = "";
		String firstFileSize = "";
		String firstOnload = "";
		String secondTimeTaken = "";
		String secondFileSize = "";
		String secondOnload = "";
		String timeDifference = "";
		String sizeDifference = "";

		if (firstEntry != null) {
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(firstEntryUrl);

			cell = row.createCell(1);
			int responseStatus = firstEntry.getResponse().getStatus();
			cell.setCellValue(responseStatus);

			firstTime = firstEntry.getTime();
			firstTimeTaken = firstTime + "ms";
			if (firstTime >= 1000) {
				double ss = firstTime / 1000.0;
				firstTimeTaken = ss + "s";
			}

			cell = row.createCell(2);
			cell.setCellValue(firstTimeTaken);

			firstEntryFileSize = firstEntry.getResponse().getContent().getSize();
			firstFileSize = firstEntryFileSize + "b";
			cell = row.createCell(3);
			if (firstEntryFileSize > 1024) {
				float converting = (float) (firstEntryFileSize / 1024.0);
				firstFileSize = converting + "kb";
			}
			cell.setCellValue(firstFileSize);

			cell = row.createCell(4);
			Date startedDateTime = firstEntry.getStartedDateTime();
			long time = startedDateTime.getTime();
			if (firstPageOnload > time) {
				firstOnload = "BEFORE";
			} else {
				firstOnload = "AFTER";
			}
			cell.setCellValue(firstOnload);
		}

		if (secondEntry != null) {
			HSSFCell cell = row.createCell(5);
			cell.setCellValue(secondEntryUrl);

			cell = row.createCell(6);
			cell.setCellValue(secondEntry.getResponse().getStatus());

			secondEntryTime = secondEntry.getTime();
			secondTimeTaken = secondEntryTime + "ms";
			if (secondEntryTime >= 1000) {
				double ss = secondEntryTime / 1000.0;
				secondTimeTaken = ss + "s";
			}

			cell = row.createCell(7);
			cell.setCellValue(secondTimeTaken);

			secondEntryFileSize = secondEntry.getResponse().getContent().getSize();
			secondFileSize = secondEntryFileSize + "b";
			cell = row.createCell(8);
			if (secondEntryFileSize > 1024) {
				float converting = (float) (secondEntryFileSize / 1024.0);
				secondFileSize = converting + "kb";
			}
			cell.setCellValue(secondFileSize);

			cell = row.createCell(9);
			Date startedDateTime = secondEntry.getStartedDateTime();
			long time = startedDateTime.getTime();
			if (secondPageOnLoad > time) {
				secondOnload = "BEFORE";
			} else {
				secondOnload = "AFTER";
			}
			cell.setCellValue(secondOnload);
		}
		if (machedUrl) {
			HSSFCell cell = row.createCell(10);
			int timeDiff = secondEntryTime - firstTime;
			timeDifference = timeDiff + "ms";
			if (timeDiff >= 1000) {
				double ss = timeDiff / 1000.0;
				timeDifference = ss + "s";
			}
			if (timeDiff >0) {
				style.setFillForegroundColor(IndexedColors.RED.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style);
			}
			cell.setCellValue(timeDifference);
			
			cell = row.createCell(11);
			long sizeDiff = secondEntryFileSize - firstEntryFileSize;
			long number = (sizeDiff < 0 ? -sizeDiff : sizeDiff);
			sizeDifference = sizeDiff + "b";
			if (number > 1024) {
				float converting = (float) (sizeDiff / 1024.0);
				sizeDifference = converting + "kb";
			}
			if (sizeDiff > 0) {
				style.setFillForegroundColor(IndexedColors.RED.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				cell.setCellStyle(style);
			}
			cell.setCellValue(sizeDifference);
		}
		
		JSONObject firstContent = new JSONObject();
		firstContent.put("url", firstEntryUrl);
		firstContent.put("responseCode", firstEntry.getResponse().getStatus());
		firstContent.put("timeTaken", firstTimeTaken);
		firstContent.put("fileSize", firstFileSize);
		firstContent.put("onload", firstOnload);
		firstContentArray.put(firstContent);
		
		JSONObject secondContent = new JSONObject();
		secondContent.put("url", secondEntryUrl);
		secondContent.put("responseCode", secondEntry.getResponse().getStatus());
		secondContent.put("timeTaken", secondTimeTaken);
		secondContent.put("fileSize", secondFileSize);
		secondContent.put("onload", secondOnload);
		secondContentArray.put(secondContent);
		
		JSONObject difference = new JSONObject();
		difference.put("url", firstEntryUrl);
		difference.put("timeDiff", timeDifference);
		difference.put("sizeDiff", sizeDifference);
		contentDifference.put(difference);

		FileOutputStream output_file = new FileOutputStream(
				new File(workingFolder.getAbsolutePath() + "\\" + "report" + ".xls"));

		wb.write(output_file); // write changes

		fsIP.close();
		output_file.close();
	}

	private static String getProperUrl(String url) {
		if (url.endsWith(".css")) {
			String[] split = url.split("/");
			for (String string : split) {
				if (string.endsWith(".css") && string.length() > 36) {
					int beginIndex = string.length() - 36;
					String substring = string.substring(beginIndex);
					substring = substring.replace(".css", "");
					return url.replace(substring, "");
				}
			}
		} else if (url.endsWith(".js")) {
			String[] split = url.split("/");
			for (String string : split) {
				if (string.endsWith(".js") && string.length() > 36) {
					int beginIndex = string.length() - 35;
					String substring = string.substring(beginIndex);
					substring = substring.replace(".js", "");
					return url.replace(substring, "");
				}
			}
		} else if (url.endsWith(".tmpl")) {
			String[] split = url.split("/");
			for (String string : split) {
				if (string.endsWith(".tmpl") && string.length() > 37) {
					int beginIndex = string.length() - 37;
					String substring = string.substring(beginIndex);
					substring = substring.replace(".tmpl", "");
					return url.replace(substring, "");
				}
			}
		}
		return url;
	}

}
