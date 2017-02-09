package com.photon.har.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.photon.har.rest.util.HarAnalyzerUtil;
import com.photon.har.rest.util.HarReportUtil;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarPage;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;

@Path("/")
public class HarFileAnalyzer {

	@POST
	@Path("/getHarAnalysys")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response harFileUpload(@FormDataParam("file1") InputStream inputStream1,
			@FormDataParam("file1") FormDataContentDisposition fileDetail1,
			@FormDataParam("file2") InputStream inputStream2,
			@FormDataParam("file2") FormDataContentDisposition fileDetail2,
			@FormDataParam("requestData") JSONObject jsonObject, FormDataMultiPart form) {
		ResponseBean bean = new ResponseBean();
		try {
			String firstFileName = fileDetail1.getFileName();
			writeToFile(inputStream1, firstFileName);
			String secondFileName = fileDetail2.getFileName();
			writeToFile(inputStream2, secondFileName);
			Response response = getHarAnalysys(firstFileName, secondFileName, jsonObject);
			File deleteDir = getTempDir();
			if (deleteDir.isDirectory()) {
				deleteTempFile(deleteDir);
			}
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			bean.setStatus(301);
			bean.setMessage(e.getLocalizedMessage());
			return Response.status(301).entity(bean).build();
		}
	}

	private Response getHarAnalysys(String firstHarfileLocation, String secondHarfileLocation, JSONObject jsonObject) {
		ResponseBean bean = new ResponseBean();
		try {
			HarFileReader harReader = new HarFileReader();
			File tempDir = getTempDir();
			JSONObject request1 = jsonObject.getJSONObject("file1");
			String firstHarName = request1.get("name").toString();
			String firstHarIteration = request1.get("iteration").toString();

			JSONObject request2 = jsonObject.getJSONObject("file2");
			String secondHarName = request2.get("name").toString();
			String secondHarIteration = request2.get("iteration").toString();

			List<HarWarning> harWarnings = new ArrayList<HarWarning>();
			
			HarLog firstHarLog = harReader.readHarFile(new File(tempDir + "/" + firstHarfileLocation), harWarnings);
			List<HarEntry> entries = firstHarLog.getEntries().getEntries();

			HarLog secondHarLog = harReader.readHarFile(new File(tempDir + "/" + secondHarfileLocation), harWarnings);
			List<HarEntry> entries1 = secondHarLog.getEntries().getEntries();

			List<HarPage> firstPages = firstHarLog.getPages().getPages();
			List<HarPage> secondPages = secondHarLog.getPages().getPages();
			HarPage firstPage = null;
			HarPage secondPage = null;
			String firstPageName = "page_" + firstHarIteration + "_0";
			String secondPageName = "page_" + secondHarIteration + "_0";
			for (HarPage harPage : firstPages) {
				if (firstPageName.equals(harPage.getId())) {
					firstPage = harPage;
				}
			}
			for (HarPage harPage : secondPages) {
				if (secondPageName.equals(harPage.getId())) {
					secondPage = harPage;
				}
			}
			
			if (firstPage == null) {
				
			}

			HarAnalyzerUtil.createOutputFolderNewXlsFile(firstPage, firstHarName, secondPage, secondHarName);

			List<HarEntry> firstEntry = new ArrayList<HarEntry>();
			List<HarEntry> secondEntry = new ArrayList<HarEntry>();

			for (HarEntry entry : entries) {
				String pageref = entry.getPageRef();
				if (firstPageName.equals(pageref)) {
					firstEntry.add(entry);
				}
			}
			for (HarEntry entry : entries1) {
				String pageref = entry.getPageRef();
				if (secondPageName.equals(pageref)) {
					secondEntry.add(entry);
				}
			}

			ResponseBean responseBean = HarAnalyzerUtil.xlsReadWriteUpdate(firstEntry, secondEntry);
			return Response.status(200).entity(responseBean).build();
		}  catch (IOException e) {
			e.printStackTrace();
			bean.setStatus(301);
			bean.setMessage(e.getLocalizedMessage());
			return Response.status(301).entity(bean).build();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@POST
	@Path("/getHarReport")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response getHarReport(FormDataMultiPart multipart, @FormDataParam("requestData") JSONObject jsonObject) {

		JSONArray rootArray = new JSONArray();
		String reportFile = null;
		try {
			File tempDir = getTempDir();
			FileUtils.deleteDirectory(tempDir);
			List<FormDataBodyPart> fields = multipart.getFields("files");
			for (int i = 0; i < fields.size(); i++) {
				BodyPartEntity bodyPartEntity = (BodyPartEntity) fields.get(i).getEntity();
				System.out.println("fields.get(i) : " + fields.get(i).getContentDisposition().getFileName());
				String fileName = fields.get(i).getContentDisposition().getFileName();
				if (fileName != null && fileName != "") {
					String[] split = fileName.split("/");
					fileName = split[split.length - 1];
					InputStream inputStream = bodyPartEntity.getInputStream();
					writeToFile(inputStream, fileName);
				}
				if (jsonObject != null) {
					String from = jsonObject.get("from").toString();
					if ("harViewer".equals(from)) {
						ResponseBean bean = new ResponseBean();
						bean.setStatus(200);
						bean.setMessage("Success");
						bean.setDownloadUrl("/downloads/tempDir" + File.separator + fileName);
						return Response.status(200).entity(bean).build();
					}
				}
			}
			
			String harfileLocation = tempDir.getAbsolutePath();
			File harFiles = new File(harfileLocation);
			File[] listFiles = harFiles.listFiles();
			DateFormat df = new SimpleDateFormat("ddMMYYYYHHmmss");
			Date dateobj = new Date();
			String curentTime = df.format(dateobj);
			reportFile = "harReport" + curentTime + ".xls";
			HarReportUtil harReportUtil = new HarReportUtil(reportFile);
			for (File file : listFiles) {
				harReportUtil.createReportHeader(file);
				JSONObject writeReportData = harReportUtil.writeReportData(file, null);
				rootArray.put(writeReportData);
			}
			deleteTempFile(tempDir);
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(301).entity("Har Report Failed...").build();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseBean bean = new ResponseBean();
		bean.setStatus(200);
		bean.setMessage("Success");
		bean.setJsonObject(rootArray.toString());
		bean.setDownloadUrl("downloads" + File.separator + reportFile);
		return Response.status(200).entity(bean).build();
	}

	private static String getProperty(String key) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream("config.properties");
		properties.load(inputStream);
		return properties.getProperty(key);
	}

	private File writeToFile(InputStream uploadedInputStream, String harFileName) throws IOException {
		File tempDir = getTempDir();
		File file = new File(tempDir + "/" + harFileName);
		OutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
		return file;
	}

	public File getTempDir() {
		File tempDir;
		String temp = System.getProperty("user.dir");
		temp = new File(temp).getParent();
		tempDir = new File(temp + "/webapps/downloads/tempDir");
		if (!tempDir.isDirectory()) {
			tempDir.mkdirs();
		}
		return tempDir;
	}

	private void deleteTempFile(File tempFile) {
		try {
			if (tempFile.isDirectory()) {
				File[] entries = tempFile.listFiles();
				for (File currentFile : entries) {
					deleteTempFile(currentFile);
				}
				tempFile.delete();
			} else {
				tempFile.delete();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	@POST
	@Path("/downloadHARFiles")
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response bulkHArFilesDownload(InputStream inputStream) {
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder builder = new StringBuilder();
			
			File zipDir = HarAnalyzerUtil.getDownoadsDirectory();
			
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			JSONObject jsonObject = new JSONObject(builder.toString());
			String wptURL = jsonObject.get("wptURL").toString();
			String releaseName = jsonObject.get("releaseName").toString();
			String environment = jsonObject.get("environment").toString();
			String browser = jsonObject.get("browser").toString();
			String bandwidth = jsonObject.get("bandwidth").toString();
			String downloadHarFile = DownloadHar.downloadHarFile(wptURL, zipDir.getAbsolutePath(), releaseName, browser, environment, bandwidth);
			ResponseBean bean = new ResponseBean();
			bean.setDownloadUrl(downloadHarFile);
			bean.setStatus(200);
			bean.setMessage("Success");
			return Response.status(200).entity(bean).build();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@POST
	@Path("/compareReleases")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response harComparision(FormDataMultiPart multipart) throws IOException {
		
		List<FormDataBodyPart> release1fields = multipart.getFields("release1HARFiles");
		BodyPartEntity bodyPartEntity = (BodyPartEntity) release1fields.get(0).getEntity();
		//System.out.println("fields.get(i) : " + fields.get(i).getContentDisposition().getFileName());
		String fileName = release1fields.get(0).getContentDisposition().getFileName();
		if (fileName != null && fileName != "") {
			String[] split = fileName.split("/");
			fileName = split[split.length - 1];
			InputStream inputStream = bodyPartEntity.getInputStream();
			File release1Zip = writeToFile(inputStream, fileName);
			File tempDir = getTempDir();
			//HarAnalyzerUtil.unZip(release1Zip, tempDir.getAbsolutePath());
			
		}
		
		
		/*String harfileLocation = getProperty("harfileslocation1");
		HarReportUtil harReportUtil = new HarReportUtil("ARYA");
		Map<String, HashMap<String,Double>> onloadPageloadValueTimeMap = new HashMap<String,HashMap<String,Double>>();*/
		//String harfileLocation = "D:/TestData/Harfiles1/";
		/*File harFiles = new File(harfileLocation);
		File[] listFiles = harFiles.listFiles();
		for (File file : listFiles) {
			harReportUtil.createReportHeader(file);
			harReportUtil.writeReportData(file, onloadPageloadValueTimeMap);
		}
		harReportUtil = new HarReportUtil("YODA");
		harfileLocation = getProperty("harfileslocation2");
		//harfileLocation = "D:/TestData/Harfiles2/";
		harFiles = new File(harfileLocation);
		listFiles = harFiles.listFiles();
		for (File file : listFiles) {
			harReportUtil.createReportHeader(file);
			harReportUtil.writeReportData(file, onloadPageloadValueTimeMap);
		}
		HarReportUtil.harComparisionReport(onloadPageloadValueTimeMap, "ARYA", "YODA");*/
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		HarReportUtil harReportUtil = new HarReportUtil("harCompare1");
		Map<String, HashMap<String,Double>> onloadPageloadValueTimeMap = new HashMap<String,HashMap<String,Double>>();
		String harfileLocation = "D:/TestData/Harfiles1/";
		File harFiles = new File(harfileLocation);
		File[] listFiles = harFiles.listFiles();
		for (File file : listFiles) {
			harReportUtil.createReportHeader(file);
			harReportUtil.writeReportData(file, onloadPageloadValueTimeMap);
		}
		
		harReportUtil = new HarReportUtil("harCompare2");
		harfileLocation = "D:/TestData/Harfiles2/";
		harFiles = new File(harfileLocation);
		listFiles = harFiles.listFiles();
		for (File file : listFiles) {
			harReportUtil.createReportHeader(file);
			harReportUtil.writeReportData(file, onloadPageloadValueTimeMap);
		}
		HarReportUtil.harComparisionReport(onloadPageloadValueTimeMap, "harCompare1", "harCompare2");
	}
	
	private void getSheetColor(String XLSheetLocation) {
		
	}
}



