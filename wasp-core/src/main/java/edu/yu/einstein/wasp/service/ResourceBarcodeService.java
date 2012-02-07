
/**
 *
 * ResourceBarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcodeService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.model.ResourceBarcode;

@Service
public interface ResourceBarcodeService extends WaspService<ResourceBarcode> {

	/**
	 * setResourceBarcodeDao(ResourceBarcodeDao resourceBarcodeDao)
	 *
	 * @param resourceBarcodeDao
	 *
	 */
	public void setResourceBarcodeDao(ResourceBarcodeDao resourceBarcodeDao);

	/**
	 * getResourceBarcodeDao();
	 *
	 * @return resourceBarcodeDao
	 *
	 */
	public ResourceBarcodeDao getResourceBarcodeDao();

  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final Integer resourceBarcodeId);

  public ResourceBarcode getResourceBarcodeByResourceId (final Integer resourceId);

  public ResourceBarcode getResourceBarcodeByBarcodeId (final Integer barcodeId);


}
