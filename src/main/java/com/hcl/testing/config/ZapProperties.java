package com.hcl.testing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * This class represents the ZAP Properties.
 * 
 * @author raghruaman-k
 *
 */
@ConfigurationProperties(prefix = "zapconfig")
public class ZapProperties {
	/**
	 * URL for ZAP.
	 */
	public String url;
	/**
	 * ZAP hostname.
	 */
	public String zaphostname;
	/**
	 * ZAP port.
	 */
	public int zapport;
	/**
	 * ZAP User apikey.
	 */
	public String zapapikey;
	/**
	 * ZAP User sessionip.
	 */
	public String sessionip;
	
	/**
	 * ZAP Location.
	 */
	public String ZapLocation;
	/**
	 * ZAP Options.
	 */
	public String zapOptions;

	/**
	 * Get the ZAP URL.
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the ZAP URL.
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Get the ZAP User name
	 * 
	 * @return username
	 */
	public String getzaphostname() {
		return zaphostname;
	}

	/**
	 * Set the ZAP User Password
	 * 
	 * @param username
	 *            username
	 */
	public void setzaphostname(String zaphostname) {
		this.zaphostname = zaphostname;
	}
	
	/**
	 * Get the ZAP User name
	 * 
	 * @return username
	 */
	public int getzapport() {
		return zapport;
	}

	/**
	 * Set the ZAP User Password
	 * 
	 * @param username
	 *            username
	 */
	public void setzapport(int zapport) {
		this.zapport = zapport;
	}

	/**
	 * Get the ZAP User password.
	 * 
	 * @return password
	 */
	public String getzapapikey() {
		return zapapikey;
	}

	/**
	 * Set the ZAP User password.
	 * 
	 * @param password
	 */
	public void setzapapikey(String zapapikey) {
		this.zapapikey = zapapikey;
	}
	
	/**
	 * Get the ZAP ZapLocation.
	 * 
	 * @return ZapLocation
	 */
	public String getZapLocation() {
		return ZapLocation;
	}

	/**
	 * Set the ZAP ZapLocation.
	 * 
	 * @param ZapLocation
	 */
	public void setZapLocation(String ZapLocation) {
		this.ZapLocation = ZapLocation;
	}
	
	/**
	 * Get the ZAP ZapLocation.
	 * 
	 * @return ZapLocation
	 */
	public String getZapOptions() {
		return zapOptions;
	}

	/**
	 * Set the ZAP ZapLocation.
	 * 
	 * @param ZapLocation
	 */
	public void setZapOptions(String zapOptions) {
		this.zapOptions = zapOptions;
	}

	
	/**
	 * Get the ZAP User password.
	 * 
	 * @return password
	 */
	public String getzapsessionip() {
		return sessionip;
	}

	/**
	 * Set the ZAP User password.
	 * 
	 * @param password
	 */
	public void setzapsessionip(String sessionip) {
		this.sessionip = sessionip;
	}
	
}