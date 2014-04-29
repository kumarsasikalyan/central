/**
 * 
 */
package edu.yu.einstein.wasp.dao;

import java.util.Set;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleSource;

/**
 * @author calder
 *
 */
public interface FileGroupDao extends WaspDao<FileGroup> {

	public FileGroup getFileGroupById(Integer id);
	
	public Set<FileGroup> getFilesForCellLibraryByType(SampleSource cellLibrary, FileType fileType);

	public Set<FileGroup> getFilesForCellLibrary(SampleSource cellLibrary);

}
