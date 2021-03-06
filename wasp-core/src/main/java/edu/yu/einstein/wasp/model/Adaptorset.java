
/**
 *
 * Adaptorset.java 
 * @author echeng (table2type.pl)
 *  
 * the Adaptorset
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="adaptorset")
public class Adaptorset extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3907895623535511740L;

	/**
	 * setAdaptorsetId(Integer adaptorsetId)
	 *
	 * @param adaptorsetId
	 *
	 */
	@Deprecated
	public void setAdaptorsetId (Integer adaptorsetId) {
		setId(adaptorsetId);
	}

	/**
	 * getAdaptorsetId()
	 *
	 * @return adaptorsetId
	 *
	 */
	@Deprecated
	public Integer getAdaptorsetId () {
		return getId();
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/** 
	 * sampleTypeId
	 *
	 */
	@Column(name="sampletypeid")
	protected Integer sampleTypeId;

	/**
	 * setSampleTypeId(Integer sampleTypeId)
	 *
	 * @param sampleTypeId
	 *
	 */
	
	public void setSampleTypeId (Integer sampleTypeId) {
		this.sampleTypeId = sampleTypeId;
	}

	/**
	 * getSampleTypeId()
	 *
	 * @return sampleTypeId
	 *
	 */
	public Integer getSampleTypeId () {
		return this.sampleTypeId;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}




	/**
	 * sampleType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
	protected SampleType sampleType;

	/**
	 * setSampleType (SampleType sampleType)
	 *
	 * @param sampleType
	 *
	 */
	public void setSampleType (SampleType sampleType) {
		this.sampleType = sampleType;
		this.sampleTypeId = sampleType.getId();
	}

	/**
	 * getSampleType ()
	 *
	 * @return sampleType
	 *
	 */
	
	public SampleType getSampleType () {
		return this.sampleType;
	}


	/** 
	 * adaptorsetMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected List<AdaptorsetMeta> adaptorsetMeta;


	/** 
	 * getAdaptorsetMeta()
	 *
	 * @return adaptorsetMeta
	 *
	 */
	@JsonIgnore
	public List<AdaptorsetMeta> getAdaptorsetMeta() {
		return this.adaptorsetMeta;
	}


	/** 
	 * setAdaptorsetMeta
	 *
	 * @param adaptorsetMeta
	 *
	 */
	public void setAdaptorsetMeta (List<AdaptorsetMeta> adaptorsetMeta) {
		this.adaptorsetMeta = adaptorsetMeta;
	}



	/** 
	 * adaptorsetresourcecategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected List<AdaptorsetResourceCategory> adaptorsetresourcecategory;


	/** 
	 * getAdaptorsetResourcecategory()
	 *
	 * @return adaptorsetresourcecategory
	 *
	 */
	@JsonIgnore
	public List<AdaptorsetResourceCategory> getAdaptorsetResourceCategory() {
		return this.adaptorsetresourcecategory;
	}


	/** 
	 * setAdaptorsetResourcecategory
	 *
	 * @param adaptorsetresourcecategory
	 *
	 */
	public void setAdaptorsetResourceCategory (List<AdaptorsetResourceCategory> adaptorsetresourcecategory) {
		this.adaptorsetresourcecategory = adaptorsetresourcecategory;
	}



	/** 
	 * adaptor
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected List<Adaptor> adaptor;


	/** 
	 * getAdaptor()
	 *
	 * @return adaptor
	 *
	 */
	@JsonIgnore
	public List<Adaptor> getAdaptor() {
		return this.adaptor;
	}


	/** 
	 * setAdaptor
	 *
	 * @param adaptor
	 *
	 */
	public void setAdaptor (List<Adaptor> adaptor) {
		this.adaptor = adaptor;
	}



}
