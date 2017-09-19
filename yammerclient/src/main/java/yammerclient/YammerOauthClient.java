package yammerclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YammerApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class YammerOauthClient {

	private static final String PROTECTED_RESOURCE_URL = "https://www.yammer.com/api/v1/messages/received.json";
	final static String USER = "meera.nair@gmail.com";
	final static String PASSWORD = "Infyuser@123";

	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(
				Level.OFF);
		OAuthService service = new ServiceBuilder().provider(YammerApi.class)
				.apiKey("8vhyHT4Az03ZO4WLAG9XaA")
				.apiSecret("JUBl7uiP0BbsfV8WkMx9pR6azyjlfkIBiZZMF9LK5Q")
				.build();
		System.out.println("=== Yammer OAuth Workflow ===\n");
		System.out.println("----Fetching the Request Token----\n");
		Token requestToken = service.getRequestToken();
		System.out.println("----Request Token Retrieved----\n");
		String verfyURL = service.getAuthorizationUrl(requestToken);
		String vToken = simulateAndRetrieveVToken(verfyURL);
		System.out.println("----Verification Token Retrieved----\n");
		Verifier verifier = new Verifier(vToken);
		System.out.println("----Access Token Processing----\n");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("----Access Token Retrieved----\n");
		
		System.out.println("----Submitting User Request URL----\n");
		OAuthRequest request = new OAuthRequest(Verb.GET,
				PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println(response.getBody());
	}

	private static String simulateAndRetrieveVToken(String verfyURL)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		System.out.println("-- RETRIEVING VERIFICATION TOKEN -- Process will take time -- \n");
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setCssEnabled(true);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setTimeout(5000);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		HtmlPage page = webClient.getPage("https://www.yammer.com/login");
		HtmlForm loginForm = page.getForms().get(0);
		loginForm.getInputByName("login").setValueAttribute(USER);
		loginForm.getInputByName("password").setValueAttribute(PASSWORD);
		HtmlButton submitButton = (HtmlButton) page.createElement("button");
		submitButton.setAttribute("type", "submit");
		loginForm.appendChild(submitButton);
		HtmlPage newPage = submitButton.click();
		newPage = (HtmlPage) webClient.getPage(new URL(verfyURL));
		List<HtmlAnchor> anchors = (List<HtmlAnchor>) newPage.getAnchors();
		String XPath = "string(//a/@href)";
		String verifyToken = null;
		for (HtmlAnchor anch : anchors) {
			if (anch.getOnClickAttribute().indexOf("return false;") != -1) {
				HtmlPage latestPage = (HtmlPage) anch.click();
				String params = latestPage.getByXPath(XPath).get(0).toString();
				try {
					Pattern p = Pattern.compile("oauth_verifier=([^&]+)");
					Matcher m = p.matcher(params);
					while (m.find()) {
						for (String retval : m.group().split("=")) {
							verifyToken = retval;
						}
					}
				} catch (PatternSyntaxException ex) {
					throw ex;
				}

			}
		}
		return verifyToken;
	}

}
