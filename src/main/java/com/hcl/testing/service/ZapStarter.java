package com.hcl.testing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class ZapStarter {

	String ZAP_LOCATION = "C:\\Program Files\\OWASP\\Zed Attack Proxy\\";
	String SAVE_SESSION_DIRECTORY = "ZAPSessions\\";
	
	 
		public void OpenZAP3() throws Exception
		{
			String zapExecutableLocation = "C:\\Program Files\\OWASP\\Zed Attack Proxy\\ZAP.exe";
			String zapWorkingDirectory = "C:\\Program Files\\OWASP\\Zed Attack Proxy";
			ProcessBuilder pb = new ProcessBuilder(zapExecutableLocation);
			pb.directory(new File(zapWorkingDirectory).getAbsoluteFile());
			pb.redirectErrorStream(true);
			pb.redirectOutput(new File("OWASPZAPOutputStream.txt"));
			System.out.println("Trying to invoke the ZAP executable");
			Process p = pb.start();
			CheckIfZAPHasStartedOrNot();
			System.out.println("Waiting for successful connection to ZAP");
			//waitForSuccessfulConnectionToZap(60000, 1000); // from ClientApi class of java client api
			System.out.println("Seems like we can connect to ZAP APIs now");
		}
		/*public void waitForSuccessfulConnectionToZap(int timeoutInSeconds) {
	       // waitForSuccessfulConnectionToZap(timeoutInSeconds, 1000);
	    }*/
		
		/* public void waitForSuccessfulConnectionToZap(int timeoutInSeconds, int pollingIntervalInMs)
		             {
		        int timeoutInMs = (int) TimeUnit.SECONDS.toMillis(timeoutInSeconds);
		        int connectionTimeoutInMs = timeoutInMs;
		        boolean connectionSuccessful = false;
		        long startTime = System.currentTimeMillis();
		        do {
		            try (Socket socket = new Socket()) {
		                try {
		                    socket.connect(
		                            new InetSocketAddress(zapAddress, zapPort), connectionTimeoutInMs);
		                    connectionSuccessful = true;
		                } catch (SocketTimeoutException ignore) {
		                    throw newTimeoutConnectionToZap(timeoutInSeconds);
		                } catch (IOException ignore) {
		                    // and keep trying but wait some time first...
		                    try {
		                        Thread.sleep(pollingIntervalInMs);
		                    } catch (InterruptedException e) {
		                        Thread.currentThread().interrupt();
		                        throw new ClientApiException(
		                                "The ClientApi was interrupted while sleeping between connection polling.",
		                                e);
		                    }

		                    long ellapsedTime = System.currentTimeMillis() - startTime;
		                    if (ellapsedTime >= timeoutInMs) {
		                        throw newTimeoutConnectionToZap(timeoutInSeconds);
		                    }
		                    connectionTimeoutInMs = (int) (timeoutInMs - ellapsedTime);
		                }
		            } catch (IOException ignore) {
		                // the closing state doesn't matter.
		            }
		        } while (!connectionSuccessful);
		    }*/
		 private static void CheckIfZAPHasStartedOrNot() throws IOException, InterruptedException {
			 String zapApiUrl = "http://localhost:8090";
			 URL url = new URL(zapApiUrl);
			 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			 connection.setRequestMethod("GET");
			 int numberOfRetries = 0;


			 while (numberOfRetries <= 5) {
			 try {
			 BufferedReader in = new BufferedReader(new InputStreamReader(
			 connection.getInputStream()));
			 String inputLine;
			 StringBuffer response = new StringBuffer();


			 while ((inputLine = in.readLine()) != null) {
			 response.append(inputLine);
			 }
			 in.close();
			 System.out.println(response.toString());
			 
			 System.out.println("Response received from the API endpoint. ZAP should be up by now");
			 break;
			 } catch (ConnectException e) {
			 System.out
			 .println("No response received from the API endpoint. Seems like ZAP has not started yet, let's keep polling");
			 if(numberOfRetries >= 30)
			 {
			 System.out.println("Tried " + numberOfRetries + " of times, couldn't get a response from the ZAP API endpoint");
			 }
			 continue;
			 } finally {
			 ++numberOfRetries;
			 Thread.sleep(5000);
			 }
			 }

}
}
