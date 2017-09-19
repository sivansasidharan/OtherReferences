import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TestWebsocketConnection {
	
	public void alert( String pURLString) {
		
		URL url;
		try {
			url = new URL(pURLString);
			url.openConnection().getInputStream();
			
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main( String[] args) {
		//TestWebsocketConnection test = new TestWebsocketConnection();
		//test.alert("http://10.165.176.47:9797/realtimefeed?topoB--a-1041-150-544--b-1732-0-525--c-1248-140-514");
		Object value = "$1,590.77";
		System.out.println(Float.parseFloat(value.toString().replace("$","").replace(",", "")));

		//Double amount = Float.parseFloat(RealTimeRuleVO.getValueIn("trn_amount").toString().replace("$","")) * (Float.parseFloat(transactionRecord.get("weight").toString()) / 100.0);
	}
}
