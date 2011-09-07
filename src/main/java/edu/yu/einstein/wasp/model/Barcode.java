
/**
 *
 * Barcode.java 
 * @author echeng (table2type.pl)
 *  
 * the Barcode
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="barcode")
public class Barcode extends WaspModel {

	/** 
	 * barcodeId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int barcodeId;

	/**
	 * setBarcodeId(int barcodeId)
	 *
	 * @param barcodeId
	 *
	 */
	
	public void setBarcodeId (int barcodeId) {
		this.barcodeId = barcodeId;
	}

	/**
	 * getBarcodeId()
	 *
	 * @return barcodeId
	 *
	 */
	public int getBarcodeId () {
		return this.barcodeId;
	}




	/** 
	 * barcode
	 *
	 */
	@Column(name="barcode")
	protected String barcode;

	/**
	 * setBarcode(String barcode)
	 *
	 * @param barcode
	 *
	 */
	
	public void setBarcode (String barcode) {
		this.barcode = barcode;
	}

	/**
	 * getBarcode()
	 *
	 * @return barcode
	 *
	 */
	public String getBarcode () {
		return this.barcode;
	}




	/** 
	 * barcodefor
	 *
	 */
	@Column(name="barcodefor")
	protected String barcodefor;

	/**
	 * setBarcodefor(String barcodefor)
	 *
	 * @param barcodefor
	 *
	 */
	
	public void setBarcodefor (String barcodefor) {
		this.barcodefor = barcodefor;
	}

	/**
	 * getBarcodefor()
	 *
	 * @return barcodefor
	 *
	 */
	public String getBarcodefor () {
		return this.barcodefor;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected int isActive;

	/**
	 * setIsActive(int isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (int isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public int getIsActive () {
		return this.isActive;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
		return this.lastUpdUser;
	}




	/** 
	 * resourceBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="barcodeid", insertable=false, updatable=false)
	protected List<ResourceBarcode> resourceBarcode;


	/** 
	 * getResourceBarcode()
	 *
	 * @return resourceBarcode
	 *
	 */
	public List<ResourceBarcode> getResourceBarcode() {
		return this.resourceBarcode;
	}


	/** 
	 * setResourceBarcode
	 *
	 * @param resourceBarcode
	 *
	 */
	public void setResourceBarcode (List<ResourceBarcode> resourceBarcode) {
		this.resourceBarcode = resourceBarcode;
	}



	/** 
	 * sampleBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="barcodeid", insertable=false, updatable=false)
	protected List<SampleBarcode> sampleBarcode;


	/** 
	 * getSampleBarcode()
	 *
	 * @return sampleBarcode
	 *
	 */
	public List<SampleBarcode> getSampleBarcode() {
		return this.sampleBarcode;
	}


	/** 
	 * setSampleBarcode
	 *
	 * @param sampleBarcode
	 *
	 */
	public void setSampleBarcode (List<SampleBarcode> sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}



}
