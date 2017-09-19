

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.ss.analytics.adapt.adapters.mongodb.MongoOperations;

public class RestClientYF {
	
	public static void main( String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(50);
		List<String> tickerList = readcsv();
		for (String tickerId : tickerList) {
			try {
				URL url;
				url = new URL(
					"https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22"
								+ tickerId
								+ "%22)&format=json&diagnostics=false&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
				Runnable worker = new ClientRunnable(url);
				executor.execute(worker);
				
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {
			
		}
		System.out.println("\nFinished all threads");
	}
	
	public static class ClientRunnable implements Runnable {
		
		private final URL url;
		
		ClientRunnable( URL url) {
			this.url = url;
		}
		
		@Override
		public void run() {
			try {
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) { throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode()); }
				BufferedReader br = new BufferedReader(new InputStreamReader( ( conn.getInputStream() )));
				String output;
				System.out.println("Output from Server .... \n");
				while ( ( output = br.readLine() ) != null) {
					System.out.println(" - json " + output);
					updateMongo(output);
					//historical 
					/*	String dataList[] = output.split(",");
						sampleHist.setOpen(dataList[0]);
						sampleHist.setHigh(dataList[1]);
						sampleHist.setLow(dataList[2]);
						sampleHist.setClose(dataList[3]);
						sampleHist.setVolume(dataList[4]);
						sampleHist.setAdj_Close(dataList[5]);
						mapper.writeValue(os, sampleHist);*/
				}
				
				conn.disconnect();
			}
			catch (Exception e) {
			}
		}
	}
	
	private static List<String> readcsv() {
		String csvFile = "cik_ticker.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int count = 0;
		List<String> tickers = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ( ( line = br.readLine() ) != null) {
				String[] cikticker = line.split(cvsSplitBy);
				System.out.println("CIK [code= " + cikticker[0] + " Ticker=" + cikticker[1] + "]");
				tickers.add(cikticker[1]);
			}
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("\n ----------------------count  - " + tickers.size());
		System.out.println("Done tickering !!");
		return tickers;
	}
	
	private static void updateMongo( String output) {
		MongoOperations mongoConnector = new MongoOperations("localhost", "27017", "FinanceYH");
		mongoConnector.save("companyshares27Aprl2015", output);
		
	}
}
