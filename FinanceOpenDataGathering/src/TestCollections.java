import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.ss.analytics.adapt.adapters.mongodb.MongoOperations;



/**
 * @author Sivan.Sasidharan
 *
 */

public class TestCollections {
	
	
	public static void main( String[] args) {
		MongoOperations mongoConnector = new MongoOperations("localhost", "27017", "FinanceYH");
		
		mongoConnector.save("companyInfo", "");
	}
	private static void convertToJsonAndSaveToMongo( List<HistQuote> sampleHistList, String tickerId) {
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> jsonToBeUpdated = new HashMap<String,Object>();
		jsonToBeUpdated.put("CompanyTicker", tickerId);
		jsonToBeUpdated.put("shareInfo", sampleHistList);
		try {
			jsonString = mapper.writeValueAsString(jsonToBeUpdated);
			System.out
				.println("Ticker updated" + tickerId + "--->>>json string ---------------" + jsonString);
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
