import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Sivan.Sasidharan
 *
 */

public class CompanyInfoSchema {
	
	Map<String,Collection<String>> companyDetails;
	
	Map<Integer,Collection<String>> keyExecutives;
	
	/**
	 * Gets the <code>companyDetails</code>.<p>
	 * <br>
	 * @return <code>companyDetails</code>
	 * <code>{@link Map<String,Collection<String>>}</code>
	 * <br>
	 * Returns the current value of the <code>companyDetails</code> object.
	 * </p>
	 */
	public Map<String,Collection<String>> getCompanyDetails() {
		return companyDetails;
	}
	
	/**
	 * Sets the <code>companyDetails</code>.<p>
	 * <br>
	 * @param  companyDetails
	 * <code>{@link Map<String,Collection<String>>}</code>
	 * Holds the <code>companyDetails</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setCompanyDetails( Map<String,Collection<String>> companyDetails) {
		this.companyDetails = companyDetails;
	}
	
	/**
	 * Gets the <code>keyExecutives</code>.<p>
	 * <br>
	 * @return <code>keyExecutives</code>
	 * <code>{@link Map<String,Collection<String>>}</code>
	 * <br>
	 * Returns the current value of the <code>keyExecutives</code> object.
	 * </p>
	 */
	public Map<Integer,Collection<String>> getKeyExecutives() {
		return keyExecutives;
	}
	
	/**
	 * Sets the <code>keyExecutives</code>.<p>
	 * <br>
	 * @param  keyExecutives
	 * <code>{@link Map<String,Collection<String>>}</code>
	 * Holds the <code>keyExecutives</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setKeyExecutives( Map<Integer,Collection<String>> keyExecutives) {
		this.keyExecutives = keyExecutives;
	}
	
}
