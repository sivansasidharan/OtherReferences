/**
 * @author Sivan.Sasidharan
 *
 */

package com.ss.analytics.tools.dataconverter.core;

/**
 * Class To Be Used If Domain Classes are available
 * Generic Converter
 */
public class ConverterTemplate<T> {
	
	public T getObjectFromJSON( String json) {
		return (T) DriverBuilder.getJettisonDriverInstance().fromXML(json);
	}
	
	public String getJSONFromObject( T t) {
		return DriverBuilder.getJettisonDriverInstance().toXML(t);
	}
	
	public T getObjectFromXML( String xml) {
		return (T) DriverBuilder.getStaxDriverInstance().fromXML(xml);
	}
	
	public String getXMLFromObject( T t) {
		return DriverBuilder.getStaxDriverInstance().toXML(t);
	}
	
	public T getObjectFromXMLUsingDomDriver( String xml) {
		return (T) DriverBuilder.getDomDriverInstance().fromXML(xml);
	}
	
	public String getXMLFromObjectUsingDomDriver( T t) {
		return DriverBuilder.getDomDriverInstance().toXML(t);
	}
	
}
