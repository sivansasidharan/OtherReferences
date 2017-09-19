
/**
 * @author Sivan.Sasidharan
 *
 */

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class StockStreamFinance {
	
	
	public static void main( String[] args) throws MalformedURLException {
		ArrayList<String> stocks = Lists.newArrayList();
		stocks.add("FB");
		stocks.add("DATA");
		stocks.add("AAPL");
		stocks.add("GOOG");
		
		// http://cliffngan.net/a/13
		String responseFormat = "sl1d1t1cv";
		
		// "http://finance.yahoo.com/d/quotes.csv?s=DATA&f=snl1d1t1cv&e=.csv"
		String yahooService = "http://finance.yahoo.com/d/quotes.csv?s=%s&f=%s&e=.csv";
		
		for (int i = 0; i < 10; i++) {
			
			int idx = new Double(Math.random() * stocks.size()).intValue(); // generate random index
			String stock = stocks.get(idx);
			
			String query = String.format(yahooService, stock, responseFormat);
			URL url = new URL(query);
			try {
				String response = IOUtils.toString(url.openStream()).replaceAll("\"", "");
				String[] lSplit = response.split(",");
				String stockName = lSplit[0];
				String date = lSplit[2];
				String time = lSplit[3];
				double stockPrice = Double.parseDouble(lSplit[1]);
				System.out.println(String.format("%s - %f - %s %s", stockName, stockPrice, date, time));
				
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
