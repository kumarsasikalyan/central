
/**
 *
 * FileType.java 
 * @author asmclellan
 *  
 * the FileType
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="filetype")
public class FileType extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2057479265391988507L;
	
	/**
	 * setFileTypeId(Integer fileTypeId)
	 *
	 * @param fileTypeId
	 *
	 */
	@Deprecated
	public void setFileTypeId (Integer fileTypeId) {
		setId(fileTypeId);
	}

	/**
	 * getFileTypeId()
	 *
	 * @return fileTypeId
	 *
	 */
	@Deprecated
	public Integer getFileTypeId () {
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
	 * description
	 *
	 */
	@Column(name="description")
	protected String description;

	/**
	 * setDescription(String description)
	 *
	 * @param description
	 *
	 */
	
	public void setDescription (String description) {
		this.description = description;
	}

	/**
	 * getDescription()
	 *
	 * @return description
	 *
	 */
	public String getDescription () {
		return this.description;
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
	 * file
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected List<FileHandle> file;


	/** 
	 * getFile()
	 *
	 * @return file
	 *
	 */
	@JsonIgnore
	public List<FileHandle> getFile() {
		return this.file;
	}


	/** 
	 * setFile
	 *
	 * @param file
	 *
	 */
	public void setFile (List<FileHandle> file) {
		this.file = file;
	}
	
	/** 
	 * filetypeMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected List<FileTypeMeta> fileTypeMeta;


	/** 
	 * getFileTypeMeta()
	 *
	 * @return fileTypeMeta
	 *
	 */
	@JsonIgnore
	public List<FileTypeMeta> getFileTypeMeta() {
		return this.fileTypeMeta;
	}


	/** 
	 * setFileTypeMeta
	 *
	 * @param fileTypeMeta
	 *
	 */
	public void setFiletypMeta (List<FileTypeMeta> fileTypeMeta) {
		this.fileTypeMeta = fileTypeMeta;
	}

}
