
public class HistQuote {
	
	String Date;
	
	String Open;
	
	String High;
	
	String Low;
	
	String Close;
	
	String Volume;
	
	String companyTkr;
	
	String Adj_Close;
	
	/**
	 * Gets the <code>date</code>.<p>
	 * <br>
	 * @return <code>date</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>date</code> object.
	 * </p>
	 */
	public String getDate() {
		return Date;
	}
	
	/**
	 * Sets the <code>date</code>.<p>
	 * <br>
	 * @param  date
	 * <code>{@link String}</code>
	 * Holds the <code>date</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setDate( String date) {
		Date = date;
	}
	
	/**
	 * Gets the <code>open</code>.<p>
	 * <br>
	 * @return <code>open</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>open</code> object.
	 * </p>
	 */
	public String getOpen() {
		return Open;
	}
	
	/**
	 * Sets the <code>open</code>.<p>
	 * <br>
	 * @param  open
	 * <code>{@link String}</code>
	 * Holds the <code>open</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setOpen( String open) {
		Open = open;
	}
	
	/**
	 * Gets the <code>high</code>.<p>
	 * <br>
	 * @return <code>high</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>high</code> object.
	 * </p>
	 */
	public String getHigh() {
		return High;
	}
	
	/**
	 * Sets the <code>high</code>.<p>
	 * <br>
	 * @param  high
	 * <code>{@link String}</code>
	 * Holds the <code>high</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setHigh( String high) {
		High = high;
	}
	
	/**
	 * Gets the <code>low</code>.<p>
	 * <br>
	 * @return <code>low</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>low</code> object.
	 * </p>
	 */
	public String getLow() {
		return Low;
	}
	
	/**
	 * Sets the <code>low</code>.<p>
	 * <br>
	 * @param  low
	 * <code>{@link String}</code>
	 * Holds the <code>low</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setLow( String low) {
		Low = low;
	}
	
	/**
	 * Gets the <code>close</code>.<p>
	 * <br>
	 * @return <code>close</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>close</code> object.
	 * </p>
	 */
	public String getClose() {
		return Close;
	}
	
	/**
	 * Sets the <code>close</code>.<p>
	 * <br>
	 * @param  close
	 * <code>{@link String}</code>
	 * Holds the <code>close</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setClose( String close) {
		Close = close;
	}
	
	/**
	 * Gets the <code>volume</code>.<p>
	 * <br>
	 * @return <code>volume</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>volume</code> object.
	 * </p>
	 */
	public String getVolume() {
		return Volume;
	}
	
	/**
	 * Sets the <code>volume</code>.<p>
	 * <br>
	 * @param  volume
	 * <code>{@link String}</code>
	 * Holds the <code>volume</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setVolume( String volume) {
		Volume = volume;
	}
	
	/**
	 * Gets the <code>companyTkr</code>.<p>
	 * <br>
	 * @return <code>companyTkr</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>companyTkr</code> object.
	 * </p>
	 */
	public String getCompanyTkr() {
		return companyTkr;
	}
	
	/**
	 * Sets the <code>companyTkr</code>.<p>
	 * <br>
	 * @param  companyTkr
	 * <code>{@link String}</code>
	 * Holds the <code>companyTkr</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setCompanyTkr( String companyTkr) {
		this.companyTkr = companyTkr;
	}
	
	/**
	 * Gets the <code>adj_Close</code>.<p>
	 * <br>
	 * @return <code>adj_Close</code>
	 * <code>{@link String}</code>
	 * <br>
	 * Returns the current value of the <code>adj_Close</code> object.
	 * </p>
	 */
	public String getAdj_Close() {
		return Adj_Close;
	}
	
	/**
	 * Sets the <code>adj_Close</code>.<p>
	 * <br>
	 * @param  adj_Close
	 * <code>{@link String}</code>
	 * Holds the <code>adj_Close</code>object.
	 * <br>          
	 * </p> 
	 */
	public void setAdj_Close( String adj_Close) {
		Adj_Close = adj_Close;
	}
	
	HistJsonSchema companyHistInfo;
	
	/**
	 * Gets the <code>companyHistInfo</code>.<p>
	 * <br>
	 * @return <code>companyHistInfo</code>
	 * <code>{@link HistJsonSchema}</code>
	 * <br>
	 * Returns the current value of the <code>companyHistInfo</code> object.
	 * </p>
	 */
/*	public HistJsonSchema getCompanyHistInfo() {
		return companyHistInfo;
	}*/
	
	/**
	 * Sets the <code>companyHistInfo</code>.<p>
	 * <br>
	 * @param  companyHistInfo
	 * <code>{@link HistJsonSchema}</code>
	 * Holds the <code>companyHistInfo</code>object.
	 * <br>          
	 * </p> 
	 */
/*	public void setCompanyHistInfo( HistQuote hisInfo) {
		HistJsonSchema histJsonData = new HistJsonSchema();
		histJsonData.companyInfo.setDate_(hisInfo.getDate());
		histJsonData.companyInfo.setOpen_(hisInfo.getOpen());
		histJsonData.companyInfo.setHigh_(hisInfo.getHigh());
		histJsonData.companyInfo.setLow_(hisInfo.getLow());
		histJsonData.companyInfo.setClose_(hisInfo.getClose());
		histJsonData.companyInfo.setVolume_(hisInfo.getVolume());
		histJsonData.companyInfo.setAdj_Close_(hisInfo.getAdj_Close());
		histJsonData.setCompanyTkr_(hisInfo.getCompanyTkr());
		companyHistInfo = histJsonData;
	}*/
	
	
/*		public CompanyData getCompanyInfo() {
		return companyInfo;
	}
	
	public void setCompanyInfo( HistQuote companyHisInfo) {
		CompanyData sample = new CompanyData();
		sample.setDate_(companyHisInfo.getDate());
		sample.setOpen_(companyHisInfo.getOpen());
		sample.setHigh_(companyHisInfo.getHigh());
		sample.setLow_(companyHisInfo.getLow());
		sample.setClose_(companyHisInfo.getClose());
		sample.setVolume_(companyHisInfo.getVolume());
		sample.setCompanyTkr_(companyHisInfo.getCompanyTkr());
		sample.setAdj_Close_(companyHisInfo.getAdj_Close());
		companyInfo = sample;
	}*/
}
