

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.ss.analytics.adapt.adapters.mongodb.MongoOperations;

public class RestClientHistoricalYF {
	
	private final static long SKIP_LINE = 1;
	
	public static void main( String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(50);
		List<String> tickerList = readTickerCikcsv();
		for (String tickerId : tickerList) {
			try {
				URL url;
				url = new URL("http://real-chart.finance.yahoo.com/table.csv?s=" + tickerId
							+ "&d=3&e=20&f=2015&g=d&a=11&b=12&c=1980&ignore=.csv");
				Runnable worker = new ClientRunnable(url, tickerId);
				executor.execute(worker);
				
			}
			catch (MalformedURLException e) {
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
		
		private final String tickerId;
		
		public ClientRunnable( URL url, String tickerId) {
			this.url = url;
			this.tickerId = tickerId;
		}
		
		@Override
		public void run() {
			HistQuote sampleHist = null;
			List<HistQuote> sampleHistList = null;
			try {
				long counter = SKIP_LINE;
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					System.out.println("Information Not Available For --"+tickerId +" \n");
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				BufferedReader br = new BufferedReader(new InputStreamReader( ( conn.getInputStream() )));
				String output;
				sampleHistList = new ArrayList<HistQuote>();
				while ( ( output = br.readLine() ) != null) {
					if (counter > SKIP_LINE) {
						String dataList[] = output.split(",");
						sampleHist = new HistQuote();
						sampleHist.setCompanyTkr(tickerId);
						sampleHist.setDate(dataList[0]);
						sampleHist.setOpen(dataList[1]);
						sampleHist.setHigh(dataList[2]);
						sampleHist.setLow(dataList[3]);
						sampleHist.setClose(dataList[4]);
						sampleHist.setVolume(dataList[5]);
						sampleHist.setAdj_Close(dataList[6]);
						//sampleHist.setCompanyHistInfo(sampleHist);
						sampleHistList.add(sampleHist);
					}
					counter++;
				}
				convertToJsonAndSaveToMongo(sampleHistList, tickerId);
				conn.disconnect();
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private static void convertToJsonAndSaveToMongo( List<HistQuote> sampleHistList, String tickerId) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> jsonToBeUpdated = new HashMap<String,Object>();
		jsonToBeUpdated.put("CompanyTicker", tickerId);
		jsonToBeUpdated.put("shareInfo", sampleHistList);
		try {
			//mapper.writeValue(sample, histQuote);
			jsonString = mapper.writeValueAsString(jsonToBeUpdated);
			System.out
				.println("Ticker updated" + tickerId + "--->>>json string ---------------" + jsonString);
			updateMongo(jsonString);
		}
		catch (JsonGenerationException e) {
			e.printStackTrace();
			
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static List<String> readTickerCikcsv() {
		String csvFile = "1_cik_ticker.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<String> tickers = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ( ( line = br.readLine() ) != null) {
				String[] cikticker = line.split(cvsSplitBy);
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
		System.out.println("Done hist quote reading !!");
		return tickers;
	}
	
	private static void updateMongo( String output) {
		MongoOperations mongoConnector = new MongoOperations("localhost", "27017", "FinanceYH");
		mongoConnector.save("historicalcompanyshares", output);
	}
	
}
