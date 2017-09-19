import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import com.ss.analytics.adapt.adapters.mongodb.MongoOperations;


/**
 * @author Sivan.Sasidharan
 *
 */

public class CompanyEdgarDetails {
	
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
	
	private static List<String> readTickerCikcsv() {
		String csvFile = "Test_cik_ticker.csv";
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
		mongoConnector.save("companyEdgarInfo", output);
	}
	
	public static class ClientRunnable implements Runnable {
		
		private final String tickerId;
		
		ClientRunnable( String tickerId) {
			this.tickerId = tickerId;
		}
		
		@Override
		public void run() {
			try {
				CompanyEdgarInfoSchema schema = new CompanyEdgarInfoSchema();
				Document doc = Jsoup.connect(
					"http://www.sec.gov/cgi-bin/browse-edgar?CIK=" + tickerId
								+ "&Find=Search&owner=exclude&action=getcompany").get();
				
				Elements mailerAddress = doc.getElementsByClass("mailer");
				String busniessAddrss = "";
				String mailingaddress = "";
				for (Element span : mailerAddress) {
					if (span.getElementsByClass("mailer").get(0).text().split(" ")[0]
						.equalsIgnoreCase( ( "Business" ))) {
						for (Iterator iterator = span.getElementsByClass("mailerAddress").iterator(); iterator
							.hasNext();) {
							Element element = (Element) iterator.next();
							busniessAddrss = busniessAddrss + element.text() + ",";
						}
					}
					if (span.getElementsByClass("mailer").get(0).text().split(" ")[0]
						.equalsIgnoreCase( ( "Mailing" ))) {
						for (Iterator iterator = span.getElementsByClass("mailerAddress").iterator(); iterator
							.hasNext();) {
							Element element = (Element) iterator.next();
							mailingaddress = mailingaddress + element.text() + ",";
						}
					}
				}
				
				//busniessAddrss = new StringBuilder(busniessAddrss).replace(busniessAddrss.lastIndexOf(","), busniessAddrss.lastIndexOf(",")," - Ph").toString();
				
				schema.setBusinessAddress(busniessAddrss.replaceAll("(,)*$", ""));
				schema.setMailingAddress(mailingaddress.replaceAll("(,)*$", ""));
				
				Elements companyInfo = doc.getElementsByClass("companyInfo");
				for (Element span : companyInfo) {
					//System.out.println("-company name--"+span.getElementsByClass("companyName").text().split(" CIK#")[0]);
					schema.setCompanyName(span.getElementsByClass("companyName").text().split(" CIK#")[0]);
					for (Element cik : span.getElementsByClass("companyName")) {
						//System.out.println("CIK --- " + ( cik.getElementsByTag("a").text().split(" ") )[0]);
						schema.setCompanyCIK( ( cik.getElementsByTag("a").text().split(" ") )[0]);
					}
					
					//get SIC & other details identInfo
					
					List<CompanyEdgarInfoSchema.FormerlyName> formerlyValues = new ArrayList<CompanyEdgarInfoSchema.FormerlyName>();
					CompanyEdgarInfoSchema.FormerlyName values;
					DateFormat formatter;
					Date convertedDate;
					for (Element cik : span.getElementsByClass("identInfo")) {
						schema.setSICCode( ( cik.getElementsByTag("a").text().split(" ") )[0]);
						for (Node element : cik.childNodes()) {
							if (element.outerHtml().startsWith("formerly")) {
								values = schema.new FormerlyName();
								values.setCompanyName(element.toString().split("formerly: ")[1].split(Pattern
									.quote("("))[0]);
								String date = element.toString().split("formerly: ")[1].split(Pattern
									.quote("filings through "))[1];
								formatter = new SimpleDateFormat("yyyy-dd-mm", Locale.ENGLISH);
								formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
								convertedDate = formatter.parse(date.substring(0, date.length() - 1));
								System.out.println("converted date ===" + convertedDate);
								values.setCompanyTillDate(convertedDate);
								formerlyValues.add(values);
							}
							
						}
						
						/*						if (cik.text().split(" formerly:").length > 1) {
													for (int i = 1; i < cik.text().split(" formerly:").length; i++) {
														System.out.println("other details 222--- "
																	+ ( cik.text().split(" formerly:") )[i]);
													}
													
												}*/
						//schema.setCompanyCIK( ( cik.getElementsByTag("a").text().split(" ") )[0]);
						//1288776,GOOG,Google Inc,NASDAQ,7370,CA,DE,770493581
					}
					schema.setFormerly(formerlyValues);
					System.out.println("Final list ---" + formerlyValues.size());
					
					//ends here 
					
				}
				
				if (schema.getCompanyCIK() != null) convertToJsonAndSaveToMongo(schema, tickerId);
			}
			catch (Exception e) {
				System.out.println("Information Not Available For -- " + tickerId + "\n");
				e.printStackTrace();
			}
		}
	}
	
	private static void convertToJsonAndSaveToMongo( CompanyEdgarInfoSchema companyExtract, String tickerId) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> jsonToBeUpdated = new HashMap<String,Object>();
		jsonToBeUpdated.put("CompanyTicker", tickerId);
		jsonToBeUpdated.put("CompanyEdgarInfo", companyExtract);
		try {
			//mapper.writeValue(sample, histQuote);
			jsonString = mapper.writeValueAsString(jsonToBeUpdated);
			System.out.println("Updated" + tickerId + "--->>>json string ---------------" + jsonString);
			
			//updateMongo(jsonString);
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
}
