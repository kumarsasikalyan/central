/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.service;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface BioanalyzerService extends WaspService {

	
	public static final String bioanalyzerChipMeta = "bioanalyzer.chip";
	public static final String bioanalyzerAssayLibrariesAreForMeta = "bioanalyzer.assayLibrariesAreFor";


		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		public void saveOrUpdateJobDraftMeta (JobDraft jobDraft, String metaK, String metaV);
		public String getMeta(JobDraft jobDraft, String metaK);
		public String getMeta(Job job, String metaK);
}
