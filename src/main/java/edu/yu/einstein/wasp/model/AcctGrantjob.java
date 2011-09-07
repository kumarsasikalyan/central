
/**
 *
 * AcctGrantjob.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob
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
@Table(name="acct_grantjob")
public class AcctGrantjob extends WaspModel {

	/** 
	 * jobId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int jobId;

	/**
	 * setJobId(int jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (int jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public int getJobId () {
		return this.jobId;
	}




	/** 
	 * grantId
	 *
	 */
	@Column(name="grantid")
	protected int grantId;

	/**
	 * setGrantId(int grantId)
	 *
	 * @param grantId
	 *
	 */
	
	public void setGrantId (int grantId) {
		this.grantId = grantId;
	}

	/**
	 * getGrantId()
	 *
	 * @return grantId
	 *
	 */
	public int getGrantId () {
		return this.grantId;
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
	 * acctLedger
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected AcctLedger acctLedger;

	/**
	 * setAcctLedger (AcctLedger acctLedger)
	 *
	 * @param acctLedger
	 *
	 */
	public void setAcctLedger (AcctLedger acctLedger) {
		this.acctLedger = acctLedger;
		this.jobId = acctLedger.jobId;
	}

	/**
	 * getAcctLedger ()
	 *
	 * @return acctLedger
	 *
	 */
	
	public AcctLedger getAcctLedger () {
		return this.acctLedger;
	}


	/**
	 * acctGrant
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="grantid", insertable=false, updatable=false)
	protected AcctGrant acctGrant;

	/**
	 * setAcctGrant (AcctGrant acctGrant)
	 *
	 * @param acctGrant
	 *
	 */
	public void setAcctGrant (AcctGrant acctGrant) {
		this.acctGrant = acctGrant;
		this.grantId = acctGrant.grantId;
	}

	/**
	 * getAcctGrant ()
	 *
	 * @return acctGrant
	 *
	 */
	
	public AcctGrant getAcctGrant () {
		return this.acctGrant;
	}


}
