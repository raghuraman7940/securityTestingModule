package com.auto.testing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import org.zaproxy.clientapi.core.ClientApiMain;
import org.zaproxy.clientapi.gen.Spider;


public class ZapTools {
	
	String ZAP_LOCATION = "C:\\Program Files\\OWASP\\Zed Attack Proxy\\";
	String JAVA_LOCATION = "C:\\Program Files (x86)\\Common Files\\Oracle\\Java\\javapath\\java.exe";
	String SAVE_SESSION_DIRECTORY = "ZAPSessions\\";
		
	public boolean startZAP(String ZAPLOCATION) {
		try {
			this.ZAP_LOCATION=ZAPLOCATION;
			//String[] command = { "CMD", "/C", this.ZAP_LOCATION + "D:\\ZAP.bat" };
			String[] command = { this.JAVA_LOCATION, "-jar", this.ZAP_LOCATION+"zap-2.7.0.jar","-daemon"};
			ProcessBuilder proc = new ProcessBuilder(command);
			proc.directory(new File(this.ZAP_LOCATION));
			Process p = proc.start();
			//p.waitFor();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			OutputStreamWriter oStream = new OutputStreamWriter(
					p.getOutputStream());
			oStream.write("process where name='ZAP.exe'");
			oStream.flush();
			oStream.close();
			String line;
			while ((line = input.readLine()) != null) {
				//kludge to tell when ZAP is started and ready
				//if (line.contains("INFO") && line.contains("org.parosproxy.paros.control.Control") && line.contains("New Session")) {
				if (line.contains("INFO") && line.contains("org.zaproxy.zap.DaemonBootstrap") && line.contains("ZAP is now listening")) {
					input.close();
					break;
				}
			}
			System.out.println("ZAP has started successfully.");
			return true;
		} catch (Exception ex) {
			System.out.println("ZAP was unable to start.");
			ex.printStackTrace();
			return false;
		}
	}
	
	public void stopZAP(String zapaddr, int zapport,String zapapikey) {
		ClientApiMain.main(new String[] { "stop", "zapaddr=" + zapaddr,	"zapport=" + zapport,"apikey="+zapapikey });
	}

	public void startSession(String zapaddr, int zapport,String zapapikey) {
		ClientApiMain.main(new String[] { "newSession", "zapaddr=" + zapaddr, "zapport=" + zapport,"apikey="+zapapikey });
		System.out.println( "session started" );
		System.out.println("Session started successfully.");
	}

	public void saveSession(ClientApi api, String fileName) {
		try {
			String path = this.SAVE_SESSION_DIRECTORY + fileName + ".session";
			api.core.saveSession( path, "true");
			System.out.println( "Session save successful (" + path + ")." );
		} catch (ClientApiException ex) {
			System.out.println( "Error saving session." );
			ex.printStackTrace();
		}
	}


	public boolean CheckIfZAPHasStartedOrNot(String ZAP_ADDRESS, int ZAP_PORT) throws IOException, InterruptedException {
	 String zapApiUrl = "http://"+ZAP_ADDRESS+":"+ZAP_PORT;
	 System.out.println("zapApiUrl :"+zapApiUrl);
	 URL url = new URL(zapApiUrl);
	 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	 connection.setRequestMethod("GET");
	 int MaxRetries=0;
	 int numberOfRetries = 0;
	 boolean result=false;
	 while (numberOfRetries <= MaxRetries) {
		 try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(
			 connection.getInputStream()));
			 String inputLine;
			 StringBuffer response = new StringBuffer();
			 while ((inputLine = in.readLine()) != null) {
				 response.append(inputLine);
			 }
			 in.close();
			// System.out.println(response.toString());
			 System.out.println("ZAP Proxy Server running now");
			 result=true;
			 break;
		 } catch (ConnectException e) {
			 System.out.println("ZAP has not started yet, let's keep polling....");
			 if (MaxRetries>0)
			 {
				 if(numberOfRetries >= MaxRetries){
					 System.out.println("Tried " + numberOfRetries + " of times, couldn't get a response from the ZAP API endpoint");
				 }
			 }
			 continue;
		 } finally {
			 ++numberOfRetries;
			 Thread.sleep(1000);
	 	}
	 }
	 	return result;
	}
	public String ascan(ClientApi api, String ZAP_URI_PORT) throws InterruptedException, FileNotFoundException {
		try {
			 int progress;
			   String scanid;
			System.out.println("Active scan starting...");
			//api.ascan.scan(ZAP_URI_PORT, null, null);
			 ApiResponse resp =api.ascan.scan(ZAP_URI_PORT, "True", "False", null, null, null);
			 //The scan now returns a scan id to support concurrent scanning
	         scanid = ((ApiResponseElement) resp).getValue();
            // Poll the status until it completes
            while (true) {
	                Thread.sleep(5000);
	                progress =
	                        Integer.parseInt(
	                                ((ApiResponseElement) api.ascan.status(scanid)).getValue());
	                System.out.println("Active Scan progress : " + progress + "%");
	                if (progress >= 100) {
	                    break;
	                }
	            }
	            System.out.println("Active Scan complete");

	            
	            String AscanReport=new String(api.core.jsonreport());
//	            System.out.println(AscanReport);
	            this.StoreRespose("Report_AScan",AscanReport);
	            return AscanReport; 
			//return true;
		} catch (ClientApiException ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	public String spider(ClientApi api, String ZAP_URI_PORT) throws InterruptedException, FileNotFoundException {
		try{
			System.out.println("Spider scan starting...");
			
			  
            ApiResponse resp = api.spider.scan(ZAP_URI_PORT, null, null, null, null);
            String scanid;
            int progress;

            // The scan now returns a scan id to support concurrent scanning
            scanid = ((ApiResponseElement) resp).getValue();

            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                progress =
                        Integer.parseInt(
                                ((ApiResponseElement) api.spider.status(scanid)).getValue());
                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Spider complete");
            
            //System.out.println("Spider Alerts:"+resp.toString());
           // System.out.println(new String(api.core.jsonreport()));
            String SpiderReport=new String(api.core.jsonreport());
           // System.out.println(SpiderReport);
            this.StoreRespose("Report_Spider",SpiderReport);
            return SpiderReport;
			//return true;
		} catch(ClientApiException ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}
	
	public String checkErrors(ClientApi api, String ZAP_URI_PORT) {
		String errors = "";
		List<Alert> ignoreAlerts = new ArrayList<>(2);
		//ignoreAlerts.add(new Alert("Cookie set without HttpOnly flag", null, Risk.Low, Reliability.Warning, null, null) {});
		//ignoreAlerts.add(new Alert(null, null, Risk.Low, Reliability.Warning, null, null));
		//ignoreAlerts.add(new Alert(null, null, Risk.Informational, Reliability.Warning, null, null));
		try {
//			System.out.println("Checking Alerts...");
//			api.checkAlerts(ignoreAlerts, null);
//			 ApiResponse resp =api.core.alertsSummary(ZAP_URI_PORT);
//				//System.out.println("Checking Alerts..."+resp.toString());
//			 
//				errors=resp.toString();
			
				//System.out.println("Checking Alerts Summary..."+resp.toString());
				
				
				  // System.out.println(new String(api.core.jsonreport()));
	            errors=new String(api.core.jsonreport());
	            System.out.println("Checking Json report..."+errors);
	        	this.StoreRespose("Report_ZAP",errors);
	          
			 //errors=new String(api.core.alertsSummary(ZAP_URI_PORT));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			errors = ex.getMessage();
		}
		return errors;
	}
	
	/*************************************************************************************************
	 *  Function name 		: StoreRespose
	 *  Reuse Function 		: 
	 *  Description 		: Store the Web services Response to local src/res/Response path
	/**************************************************************************************************/
    public String StoreRespose(String fname,String Value)throws FileNotFoundException
    {
    	
    	String RespPath=null;
    	String Filename;
    	
    	Date d = new Date();
		String date=d.toString().replaceAll(" ", "_");
		date=date.replaceAll(":", "_");
		date=date.replaceAll("\\+", "_");
		Filename="Reports"+"_"+date;
		if (fname.length()>0)
		{
			Filename=fname+"_"+date;;
		}
		
		
    	File file = new File(System.getProperty("user.dir")+"/Reports/"+Filename+".json");
    	//System.out.println("Response stored Path : "+file.getAbsolutePath());	
		try (FileOutputStream fop = new FileOutputStream(file)) 
		{
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = Value.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			//URL myUrl = file.toURI().toURL();
			RespPath=file.getAbsolutePath();
			System.out.println("Response stored Path : "+RespPath);	
			///current_TestCase_xls.setCellData(Constants.TEST_STEPS_SHEET, Constants.Response, rowNum,myUrl.toString());
			return RespPath;
			
		}
     catch (IOException e) {e.printStackTrace();}
		return RespPath;
    }
}