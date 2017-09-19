import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Sivan.Sasidharan
 *
 */

public class CompanyEdgarInfoSchema {
	
	String companyCIK;
	
	String businessAddress;
	
	String mailingAddress;
	
	String companyName;
	
	String tradingExchange;
	
	String SICCode;
	
	List<FormerlyName> formerly;
	
	/**
	 * Gets the <code>formerly</code>.<p>
	 * <br>
	 * @return <code>formerly</code>
	 * <code>{@link List<FormerlyName>}</code>
	 * <br>
	 * Returns the current value of the <code>formerly</code> object.
	 * </p>
	 */
	public List<FormerlyName> getFormerly() {
		return formerly;
	}
	
	/**
	 * Sets the <code>formerly</code>.<p>
	 * <br>
	 * @param  formerly
	 * <code>{@link List<FormerlyName>}</code>
	 * Holds the <code>formerly</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setFormerly( List<FormerlyName> formerly) {
		this.formerly = formerly;
	}
	
	/**
	 * Gets the <code>tradingExchange</code>.<p>
	 * <br>
	 * @return <code>tradingExchange</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>tradingExchange</code> object.
	 * </p>
	 */
	public String getTradingExchange() {
		return tradingExchange;
	}
	
	/**
	 * Sets the <code>tradingExchange</code>.<p>
	 * <br>
	 * @param  tradingExchange
	 * <code>{@link String}</code>
	 * Holds the <code>tradingExchange</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setTradingExchange( String tradingExchange) {
		this.tradingExchange = tradingExchange;
	}
	
	/**
	 * Gets the <code>sICCode</code>.<p>
	 * <br>
	 * @return <code>sICCode</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>sICCode</code> object.
	 * </p>
	 */
	public String getSICCode() {
		return SICCode;
	}
	
	/**
	 * Sets the <code>sICCode</code>.<p>
	 * <br>
	 * @param  sICCode
	 * <code>{@link String}</code>
	 * Holds the <code>sICCode</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setSICCode( String sICCode) {
		SICCode = sICCode;
	}
	
	/**
	 * Gets the <code>companyName</code>.<p>
	 * <br>
	 * @return <code>companyName</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>companyName</code> object.
	 * </p>
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	 * Sets the <code>companyName</code>.<p>
	 * <br>
	 * @param  companyName
	 * <code>{@link String}</code>
	 * Holds the <code>companyName</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setCompanyName( String companyName) {
		this.companyName = companyName;
	}
	
	/**
	 * Gets the <code>companyCIK</code>.<p>
	 * <br>
	 * @return <code>companyCIK</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>companyCIK</code> object.
	 * </p>
	 */
	public String getCompanyCIK() {
		return companyCIK;
	}
	
	/**
	 * Sets the <code>companyCIK</code>.<p>
	 * <br>
	 * @param  companyCIK
	 * <code>{@link String}</code>
	 * Holds the <code>companyCIK</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setCompanyCIK( String companyCIK) {
		this.companyCIK = companyCIK;
	}
	
	/**
	 * Gets the <code>businessAddress</code>.<p>
	 * <br>
	 * @return <code>businessAddress</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>businessAddress</code> object.
	 * </p>
	 */
	public String getBusinessAddress() {
		return businessAddress;
	}
	
	/**
	 * Sets the <code>businessAddress</code>.<p>
	 * <br>
	 * @param  businessAddress
	 * <code>{@link String}</code>
	 * Holds the <code>businessAddress</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setBusinessAddress( String businessAddress) {
		this.businessAddress = businessAddress;
	}
	
	/**
	 * Gets the <code>mailingAddress</code>.<p>
	 * <br>
	 * @return <code>mailingAddress</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>mailingAddress</code> object.
	 * </p>
	 */
	public String getMailingAddress() {
		return mailingAddress;
	}
	
	/**
	 * Sets the <code>mailingAddress</code>.<p>
	 * <br>
	 * @param  mailingAddress
	 * <code>{@link String}</code>
	 * Holds the <code>mailingAddress</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setMailingAddress( String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}
	
	public class FormerlyName {
		
		String companyName;
		
		Date companyTillDate;
		
		/**
		 * Gets the <code>companyName</code>.<p>
		 * <br>
		 * @return <code>companyName</code>
		 * <code>{@link String}</code>
		 * <br>
		 * Returns the current value of the <code>companyName</code> object.
		 * </p>
		 */
		public String getCompanyName() {
			return companyName;
		}
		
		/**
		 * Sets the <code>companyName</code>.<p>
		 * <br>
		 * @param  companyName
		 * <code>{@link String}</code>
		 * Holds the <code>companyName</code>object.
		 * <br>          
		 * </p> 
		 */
		public void setCompanyName( String companyName) {
			this.companyName = companyName;
		}
		
		/**
		 * Gets the <code>companyTillDate</code>.<p>
		 * <br>
		 * @return <code>companyTillDate</code>
		 * <code>{@link Date}</code>
		 * <br>
		 * Returns the current value of the <code>companyTillDate</code> object.
		 * </p>
		 */
		public Date getCompanyTillDate() {
			return companyTillDate;
		}
		
		/**
		 * Sets the <code>companyTillDate</code>.<p>
		 * <br>
		 * @param  companyTillDate
		 * <code>{@link Date}</code>
		 * Holds the <code>companyTillDate</code>object.
		 * <br>          
		 * </p> 
		 */
		public void setCompanyTillDate( Date companyTillDate) {
			this.companyTillDate = companyTillDate;
		}
		
	}
}
