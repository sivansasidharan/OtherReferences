
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RSSReader {
	
	private static RSSReader instance = null;
	
	private RSSReader() {
	}
	
	public static RSSReader getInstance() {
		if (instance == null) {
			instance = new RSSReader();
		}
		return instance;
	}
	
	public void writeNews() {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//URL url = new URL("https://www.sec.gov/cgi-bin/browse-edgar?action=getcurrent&type=&company=&dateb=&owner=include&start=0&count=100&output=atom");
			URL url = new URL("http://feeds.finance.yahoo.com/rss/2.0/headline?s=aapl&region=US&lang=en-US");
			Document doc = builder.parse(url.openStream());
/*			NodeList nodes = doc.getElementsByTagName("entry");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				System.out.println("--------------------------------------"+i);
				System.out.println("Title: " + getElementValue(element, "title"));
				System.out.println("Link: " + getElementValue(element, "link"));
				System.out.println("Summary: " + getElementValue(element, "summary"));
				System.out.println("Updated: " + getElementValue(element, "updated"));
				System.out.println("Category: " + getElementValue(element, "category"));
				System.out.println("id: " + getElementValue(element, "id"));
				System.out.println("--------------------------------------");*/
				
				NodeList nodes = doc.getElementsByTagName("item");
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) nodes.item(i);
					System.out.println("--------------------------------------"+i);
					System.out.println("Title: " + getElementValue(element, "title"));
					System.out.println("Link: " + getElementValue(element, "link"));
					System.out.println("guid : " + getElementValue(element, "guid"));
					System.out.println("pubDate: " + getElementValue(element, "pubDate"));
					System.out.println("--------------------------------------");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private String getCharacterDataFromElement( Element e) {
		try {
			Node child = e.getFirstChild();
			if (child instanceof CharacterData) {
				CharacterData cd = (CharacterData) child;
				return cd.getData();
			}
		}
		catch (Exception ex) {
		}
		return "";
	}
	
	protected float getFloat( String value) {
		if (value != null && !value.equals("")) { return Float.parseFloat(value); }
		return 0;
	}
	
	protected String getElementValue( Element parent, String label) {
		return getCharacterDataFromElement((Element) parent.getElementsByTagName(label).item(0));
	}
	
	public static void main( String[] args) {
		RSSReader reader = RSSReader.getInstance();
		reader.writeNews();
	}
}
