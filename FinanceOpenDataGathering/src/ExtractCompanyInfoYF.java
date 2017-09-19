import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.ss.analytics.adapt.adapters.mongodb.MongoOperations;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;


/**
 * @author Sivan.Sasidharan
 *
 */

public class ExtractCompanyInfoYF {
	
	public static void main( String[] args) {
		
		List<String> tickerList = readTickerCikcsv();
		ExecutorService executor = Executors.newFixedThreadPool(50);
		for (String tickerId : tickerList) {
			Runnable worker = new ClientRunnable(tickerId);
			executor.execute(worker);
		}
		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {
			
		}
		System.out.println("\nFinished all threads");
	}
	
	private static void convertToJsonAndSaveToMongo( CompanyInfoSchema companyExtract, String tickerId) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> jsonToBeUpdated = new HashMap<String,Object>();
		jsonToBeUpdated.put("CompanyTicker", tickerId);
		jsonToBeUpdated.put("CompanyExtract", companyExtract);
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
		String csvFile = "cik_ticker.csv";
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
		System.out.println("Done tickering !!");
		return tickers;
	}
	
	private static void updateMongo( String output) {
		MongoOperations mongoConnector = new MongoOperations("localhost", "27017", "FinanceYH");
		mongoConnector.save("companyInfo", output);
	}
	
	public static class ClientRunnable implements Runnable {
		
		private final String tickerId;
		
		ClientRunnable( String tickerId) {
			this.tickerId = tickerId;
		}
		
		@Override
		public void run() {
			
			Multimap<Integer,String> multimap2 = MultimapBuilder.linkedHashKeys().arrayListValues().build();//ArrayListMultimap.create();
			Multimap<String,String> multimap1 = MultimapBuilder.linkedHashKeys().arrayListValues().build();//ArrayListMultimap.create();
			try {
				Document doc = Jsoup.connect("https://in.finance.yahoo.com/q/pr?s=" + tickerId).get();
				Element mainTable = doc.getElementById("yfncsumtab");
				Elements tables = mainTable.getElementsByTag("table");
				for (Element tableId : tables) {
					Elements td1 = tableId.getElementsByClass("yfnc_modtitlew1");
					for (Element element : td1) {
						//	multimap1.put("CompanyName", element.getElementsByTag("b").get(0).text());
						//multimap1.put(element.getElementsByTag("b").get(0).text(), element.getElementsByClass("yfnc_tablehead1").get(0).text());
						multimap1.put(tickerId, element.getElementsByTag("b").get(0).text());
						multimap1.put(tickerId, element.getElementsByClass("yfnc_tabledata1").get(0).text());
						//multimap1.put(element.getElementsByTag("b").get(0).text(), element.getElementsByClass("yfnc_tablehead1").get(1).text());
						multimap1.put(tickerId, element.getElementsByClass("yfnc_tabledata1").get(1).text());
						//multimap1.put(element.getElementsByTag("b").get(0).text(), element.getElementsByClass("yfnc_tablehead1").get(2).text());
						multimap1.put(tickerId, element.getElementsByClass("yfnc_tabledata1").get(2).text());
						//multimap1.put(element.getElementsByTag("b").get(0).text(), element.getElementsByClass("yfnc_tablehead1").get(3).text());
						multimap1.put(tickerId, element.getElementsByClass("yfnc_tabledata1").get(3).text());
						multimap1.put(tickerId, element.getElementsByTag("p").get(0).text());
					}
				}
				//System.out.println("Company Details --> " + multimap1);
				for (Element tds : doc.getElementsByClass("yfnc_modtitlew2")) {//yfnc_datamodoutline1
				
					//System.out.println(" Key executives \n  - Name - " + element.getElementsByClass("yfnc_tabledata1").get(0).text());
					int setSpliiter = 0;
					int noOfExec = 1;
					for (Element tables1 : tds.select("table")) {
						for (Element reqTable : tables1.getElementsByClass("yfnc_datamodoutline1")) {
							if (!reqTable.getElementsByClass("yfnc_tabledata1").text().isEmpty()) {
								for (Element element : reqTable.getElementsByClass("yfnc_tabledata1")) {
									setSpliiter++;
									if (setSpliiter <= 3) {
										//System.out.println("---"+element.getElementsByClass("yfnc_tabledata1").text());
										multimap2.put(noOfExec, element.getElementsByClass("yfnc_tabledata1")
											.text());
										if(setSpliiter==3){
											setSpliiter = 0;
											noOfExec++;
										}
									}
								
								}
							}
						}
					}
					//System.out.println("Key Executives -->" + multimap2);
					
				}
			}
			catch (Exception e) {
				System.out.println("Information Not Available For -- " + tickerId + "\n");
				//e.printStackTrace();
			}
			//System.out.println("converted to map --"+multimap1.asMap() + " && "+multimap2.asMap());
			CompanyInfoSchema companyExtract = new CompanyInfoSchema();
			if (!multimap1.isEmpty() && !multimap2.isEmpty()) {
				companyExtract.setCompanyDetails(multimap1.asMap());
				companyExtract.setKeyExecutives(multimap2.asMap());
				convertToJsonAndSaveToMongo(companyExtract, tickerId);
			}
			
		}
	}
}
