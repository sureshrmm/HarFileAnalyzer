package com.photon.har.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.photon.har.rest.util.HarAnalyzerUtil;
import com.photon.har.rest.util.HarReportUtil;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarPage;

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
			File deleteDir = new File(".");
			deleteDir = new File(deleteDir + "/tempDir/");
			if (deleteDir.isDirectory()) {
				deleteTempFile(deleteDir);
			}
			return response;
		} catch (IOException e) {
			bean.setStatus(301);
			bean.setMessage(e.getLocalizedMessage());
			return Response.status(301).entity(bean).build();
		}
	}

	private Response getHarAnalysys(String firstHarfileLocation, String secondHarfileLocation, JSONObject jsonObject) {
		ResponseBean bean = new ResponseBean();
		JSONObject JsonResponse = null;
		try {
			HarReader harReader = new HarReader();
			File tempDir = new File(".");
			tempDir = new File(tempDir + "/tempDir");
			JSONObject request1 = jsonObject.getJSONObject("file1");
			String firstHarName = request1.get("name").toString();
			String firstHarIteration = request1.get("iteration").toString();

			JSONObject request2 = jsonObject.getJSONObject("file2");
			String secondHarName = request2.get("name").toString();
			String secondHarIteration = request2.get("iteration").toString();

			// String firstHarName = getProperty("firstHarName");
			// String secondHarName = getProperty("secondHarName");

			Har firstHar = harReader.readFromFile(new File(tempDir + "/" + firstHarfileLocation));
			List<HarEntry> entries = firstHar.getLog().getEntries();

			Har secondHar = harReader.readFromFile(new File(tempDir + "/" + secondHarfileLocation));
			List<HarEntry> entries1 = secondHar.getLog().getEntries();

			List<HarPage> firstPages = firstHar.getLog().getPages();
			List<HarPage> secondPages = secondHar.getLog().getPages();
			HarPage firstPage = null;
			HarPage secondPage = null;
			// String firstHarIteration = getProperty("firstHarIteration");
			String firstPageName = "page_" + firstHarIteration + "_0";
			// String secondHarIteration = getProperty("secondHarIteration");
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

			HarAnalyzerUtil.createOutputFolderNewXlsFile(firstPage, firstHarName, secondPage, secondHarName);

			List<HarEntry> firstEntry = new ArrayList<HarEntry>();
			List<HarEntry> secondEntry = new ArrayList<HarEntry>();

			for (HarEntry entry : entries) {
				String pageref = entry.getPageref();
				if (firstPageName.equals(pageref)) {
					firstEntry.add(entry);
				}
			}
			for (HarEntry entry : entries1) {
				String pageref = entry.getPageref();
				if (secondPageName.equals(pageref)) {
					secondEntry.add(entry);
				}
			}

			JsonResponse = HarAnalyzerUtil.xlsReadWriteUpdate(firstEntry, secondEntry);
			bean.setStatus(200);
			bean.setMessage("Success");
			bean.setJsonObject(JsonResponse.toString());
			System.out.println("JsonResponseeeeeeeeeeeeeeeeeeee====?"+bean.getJsonObject().toString());
			return Response.status(200).entity(bean).build();
		} catch (HarReaderException e) {
			bean.setStatus(301);
			bean.setMessage(e.getLocalizedMessage());
			return Response.status(301).entity(bean).build();
		} catch (IOException e) {
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
	public Response getHarReport(FormDataMultiPart multipart) {

		try {
			List<FormDataBodyPart> fields = multipart.getFields("files");
			for (int i = 0; i < fields.size(); i++) {
				BodyPartEntity bodyPartEntity = (BodyPartEntity) fields.get(i).getEntity();
				String fileName = fields.get(i).getContentDisposition().getFileName();
				if (fileName != null && fileName != "") {
					String[] split = fileName.split("/");
					fileName = split[split.length - 1];
					InputStream inputStream = bodyPartEntity.getInputStream();
					writeToFile(inputStream, fileName);
				}
			}
			File tempDir = new File(".");
			tempDir = new File(tempDir + "/tempDir");

			String harfileLocation = tempDir.getAbsolutePath();
			File harFiles = new File(harfileLocation);
			File[] listFiles = harFiles.listFiles();
			HarReportUtil harReportUtil = new HarReportUtil("harReport");
			for (File file : listFiles) {
				harReportUtil.createReportHeader(file);
				harReportUtil.writeReportData(file, null);
			}
			deleteTempFile(tempDir);
		} catch (IOException e) {
			return Response.status(301).entity("Har Report Failed...").build();
		}
		return Response.status(200).entity("Har Report Successfully Generated....").build();
	}

	private static String getProperty(String key) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = new FileInputStream("config.properties");
		properties.load(inputStream);
		return properties.getProperty(key);
	}

	private void writeToFile(InputStream uploadedInputStream, String harFileName) throws IOException {
		File tempDir = new File(".");
		tempDir = new File(tempDir + "/tempDir");
		if (!tempDir.isDirectory()) {
			tempDir.mkdirs();
		}
		File file = new File(tempDir + "/" + harFileName);
		OutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
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
	
	@GET
	@Path("/harComparision")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response harComparision() throws IOException {
		String harfileLocation = getProperty("harfileslocation1");
		System.out.println("harfileLocation=====> " + harfileLocation);
		HarReportUtil harReportUtil = new HarReportUtil("ARYA");
		Map<String, HashMap<String,Double>> onloadPageloadValueTimeMap = new HashMap<String,HashMap<String,Double>>();
		//String harfileLocation = "D:/TestData/Harfiles1/";
		File harFiles = new File(harfileLocation);
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
		HarReportUtil.harComparisionReport(onloadPageloadValueTimeMap, "ARYA", "YODA");
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



