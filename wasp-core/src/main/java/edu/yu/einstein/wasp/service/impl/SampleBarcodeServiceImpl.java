
/**
 *
 * SampleBarcodeServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcodeService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.service.SampleBarcodeService;

@Service
public class SampleBarcodeServiceImpl extends WaspServiceImpl<SampleBarcode> implements SampleBarcodeService {

	/**
	 * sampleBarcodeDao;
	 *
	 */
	private SampleBarcodeDao sampleBarcodeDao;

	/**
	 * setSampleBarcodeDao(SampleBarcodeDao sampleBarcodeDao)
	 *
	 * @param sampleBarcodeDao
	 *
	 */
	@Override
	@Autowired
	public void setSampleBarcodeDao(SampleBarcodeDao sampleBarcodeDao) {
		this.sampleBarcodeDao = sampleBarcodeDao;
		this.setWaspDao(sampleBarcodeDao);
	}

	/**
	 * getSampleBarcodeDao();
	 *
	 * @return sampleBarcodeDao
	 *
	 */
	@Override
	public SampleBarcodeDao getSampleBarcodeDao() {
		return this.sampleBarcodeDao;
	}


  @Override
public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode) {
    return this.getSampleBarcodeDao().getSampleBarcodeBySampleBarcode(sampleBarcode);
  }

  @Override
public SampleBarcode getSampleBarcodeBySampleId (final int sampleId) {
    return this.getSampleBarcodeDao().getSampleBarcodeBySampleId(sampleId);
  }

  @Override
public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId) {
    return this.getSampleBarcodeDao().getSampleBarcodeByBarcodeId(barcodeId);
  }

}
