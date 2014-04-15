/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.MetadataRuntimeException;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

/**
 * @author jcai
 * @author asmclellan
 */
@Service
@Transactional("entityManager")
public class GatkServiceImpl extends WaspServiceImpl implements GatkService {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}
	
	@Override
	public Build getGenomeBuild(SampleSource cellLibrary) {
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = genomeService.getBuild(library);
			if (build == null) {
				String mess = "cell library does not have associated genome build metadata annotation";
				logger.error(mess);
				throw new NullResourceException(mess);
			}
			logger.debug("genome build: " + build.getGenome().getName() + "::" + build.getName());
		} catch (ParameterValueRetrievalException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return build;
	}
	
	@Override
	public String getReferenceGenomeFastaFile(Build build) {
		String folder = build.getMetadata("fasta.folder");
		String filename = build.getMetadata("fasta.fileName");
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate reference genome fasta file");
		String index = genomeService.getRemoteBuildPath(build) + "/" + folder + "/" + filename;
		return index;
	}
	
	@Override
	public String getReferenceSnpsVcfFile(Build build) {
		String folder = build.getMetadata("vcf.folder");
		String filename = build.getMetadata("vcf.snps.fileName");
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate snps vcf file");
		String index = genomeService.getRemoteBuildPath(build) + "/" + folder + "/" + filename;
		return index;
	}
	
	@Override
	public String getReferenceIndelsVcfFile(Build build) {
		String folder = build.getMetadata("vcf.folder");
		String filename = build.getMetadata("vcf.indels.fileName");
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate indels vcf file");
		String index = genomeService.getRemoteBuildPath(build) + "/" + folder + "/" + filename;
		return index;
	}


}
