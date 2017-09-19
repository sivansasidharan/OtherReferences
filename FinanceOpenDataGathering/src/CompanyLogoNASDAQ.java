import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.ss.analytics.adapt.adapters.mongodb.MongoOperations;


/**
 * @author Sivan.Sasidharan
 *
 */

public class CompanyLogoNASDAQ {
	
	public static void main( String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(50);
		List<String> tickerList = readcsv();
		for (String tickerId : tickerList) {
			try {
				URL url;
				url = new URL("http://www.nasdaq.com/logos/" + tickerId + ".gif");
				Runnable worker = new ClientRunnable(url, tickerId);
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
		
		private final String tickerId;
		
		private final String filePath;
		
		ClientRunnable( URL url, String tickerId) {
			this.url = url;
			this.tickerId = tickerId;
			filePath = "resources/" + tickerId + "image.gif";
		}
		
		@Override
		public void run() {
			
			try {
				InputStream is = url.openStream();
				OutputStream os = new FileOutputStream("resources/" + tickerId + "image.gif");
				byte[] b = new byte[2048];
				int length;
				while ( ( length = is.read(b) ) != -1) {
					os.write(b, 0, length);
				}
				is.close();
				os.close();
				updateMongo(tickerId,filePath);
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
				System.out.println("cikticker---->>" + cikticker.length);
				if (cikticker.length > 3)
					if (cikticker[3] != null)
						if (cikticker[3].equalsIgnoreCase("NASDAQ")) {
							System.out.println("CIK [code= " + cikticker[0] + " Ticker=" + cikticker[1]
										+ " Exchange - " + cikticker[3] + "]");
							tickers.add(cikticker[1]);
						}
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
	
	private static void updateMongo( String tickerId , String filePath) {
		MongoOperations mongoConnector = new MongoOperations("localhost", "27017", "FinanceYH");
		try {
			mongoConnector.saveImageIntoMongoDB(filePath, tickerId);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
