package edu.yu.einstein.wasp.controller.util;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.controller.validator.MetaValidatorImpl;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.util.MetaHelper;


/**
 * Contains utility methods for manipulating
 * meta and MetaAttribute objects
 * @author asmclellan
 *
 */
public class MetaHelperWebapp extends MetaHelper {

	
	/**
	 * Constructor
	 * @param area
	 * @param parentArea
	 * @param domain
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelperWebapp(String area, String domain, Class<T> clazz, HttpSession session) {
		super(area, domain, clazz, (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelperWebapp(String area, Class<T> clazz, HttpSession session) {
		this(area,"",clazz,session);
	}
	

	/**
	 * Constructor
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelperWebapp(Class<T> clazz, HttpSession session) {
		this(WordUtils.uncapitalize(clazz.getSimpleName().replace("Meta", "")), clazz, session);
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 */
	public <T extends MetaBase> MetaHelperWebapp(String area, Class<T> clazz) {
		super(area, clazz);
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 */
	public <T extends MetaBase> MetaHelperWebapp(String area, String domain, Class<T> clazz) {
		super(area, domain, clazz);
	}
	
	
	
	/**
	 * Constructor
	 * @param clazz
	 */
	public <T extends MetaBase> MetaHelperWebapp(Class<T> clazz) {
		super(clazz);
	}


	/**
	 *  Populates provided list from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, List<T> list, Class<T> clazz) {
		return getFromRequest(request, list, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}
	
	/**
	 *  Generates Master List and pulls in from values from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Class<T> clazz) {
		return getFromRequest(request, getMasterList(clazz), (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param visibility 
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		return getFromRequest(request, getMasterList(visibility, clazz), visibility, clazz);
	}
	
	/**
	 *  Populates provided list from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param visibility 
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, List<T> list, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {

		Map<String, String[]> params = request.getParameterMap();
		for (T obj: list) {
			String requestKey = parentArea + "Meta" + "_" + obj.getK();
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[0]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
			displayDebugInfoMessage(obj, "getFromRequest() syncing");
		}

		this.lastList =  list; 

		return list;
	}
	
	/**
	 * Generates Master List and pulls in from values from request that contains multiple objects, so uses index.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param visibility 
	 * @param clazz
	 * @param int counter (zero-based index)
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz, int counter) {
		return getFromRequest(request, getMasterList(visibility, clazz), visibility, clazz, counter);
	}
	
	/**
	 *  Populates provided list from request that contains multiple objects, so uses index. 
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param visibility 
	 * @param clazz
	 * @param int counter (zero-based index)
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, List<T> list, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz, int counter) {

		Map<String, String[]> params = request.getParameterMap();
		for (T obj: list) {
			String requestKey = parentArea + "Meta" + "_" + obj.getK();
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[counter]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
			displayDebugInfoMessage(obj, "getFromRequest() syncing");
		}

		this.lastList =  list; 

		return list;
	}
	
	
	
	
	
	
	
	/**
	 *  Populates provided list from json.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param Map<String,String> jsonMap (json converted to Map<String,String>)
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJson(Map<String,String> jsonMap, List<T> list, Class<T> clazz) {
		return getFromJson(jsonMap, list, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}
	
	/**
	 *  Generates Master List and pulls in from values from json.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param Map<String,String> jsonMap (json converted to Map<String,String>)
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJson(Map<String,String> jsonMap, Class<T> clazz) {
		return getFromJson(jsonMap, getMasterList(clazz), (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from json.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param Map<String,String> jsonMap (json converted to Map<String,String>)
	 * @param visibility 
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJson(Map<String,String> jsonMap, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		return getFromJson(jsonMap, getMasterList(visibility, clazz), visibility, clazz);
	}
	
	/**
	 *  Populates provided list from json.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param Map<String,String> jsonMap (json converted to Map<String,String>)
	 * @param visibility 
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJson(Map<String,String> jsonMap, List<T> list, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {

		for (T obj: list) {
			String requestKey = parentArea + "Meta" + "_" + obj.getK();
			if (! jsonMap.containsKey(requestKey)) { continue; }
			try {
				obj.setV(jsonMap.get(requestKey));
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
			displayDebugInfoMessage(obj, "getFromRequest() syncing");
		}

		this.lastList =  list; 

		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Generates Master List and pulls in from values from request (Json form)
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Class<T> clazz) {
		return getFromJsonForm(request, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from request (Json form)
	 * @param request
	 * @param visibility
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		List<T> list = getMasterList(visibility, clazz);

		Map<String, String[]> params = request.getParameterMap();

		for (T obj: list) {
			String requestKey = obj.getK();
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[0]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
			displayDebugInfoMessage(obj, "getFromJsonForm() syncing");
		}

		this.lastList =  list; 

		return list;
	}

	

	/**
	 * Validates any metadata with constraints in the last generated metadata list using the supplied validator object
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param validator
	 * @param result
	 */
	public void validate(MetaValidator validator, BindingResult result) {
		validator.validate(this.lastList, result, parentArea);
	}


	/**
	 * Validates any metadata with constraints in the last generated metadata list using {@link MetaValidatorImpl}
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param result
	 */
	public void validate(BindingResult result) {
		getMetaValidator().validate(this.lastList, result, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the last generated metadata list using a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param metaValidatorClazz
	 * @param result
	 */
	public <T extends MetaValidator> void validate(Class<T> metaValidatorClazz, BindingResult result) {
		getMetaValidator().validate(this.lastList, result, parentArea);
	}
	
	/**
	 * Adds an error message to and existing meta field regardless of whether there is normally a constraint on it or not.
	 * @param name
	 * @param errorMessageKey
	 * @param result
	 * @throws MetadataException
	 */
	public void addValidationError(String name, String errorMessageKey, BindingResult result) throws MetadataException{
		Errors errors=new BindException(result.getTarget(), parentArea); 
		
		for(int i=0;i<this.lastList.size();i++) {
			MetaBase meta=this.lastList.get(i);
			if (meta.getProperty().getFormVisibility().equals(MetaAttribute.FormVisibility.ignore)) continue;
			if (!meta.getK().equals(area + "." + name)) continue;
			String errorFieldName = parentArea+"Meta["+i+"].k";
			String defaultMessage = errorMessageKey+" (no message has been defined for this property)";
			errors.rejectValue(errorFieldName, errorMessageKey, defaultMessage);
			result.addAllErrors(errors);
			return;
		}
		throw new MetadataException("Cannot find metadata with name: "+name);
	}
	
	/**
	 * Validates any metadata with constraints in the supplied list using {@link MetaValidatorImpl}
	 * @param area
	 * @param list
	 * @param result
	 */
	public static void validate(String area, List<? extends MetaBase> list, BindingResult result) {
		getMetaValidator().validate(list, result, area);
	}
	
	/**
	 * Get a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * @param metaValidatorClazz
	 * @return a metaValidatorClazz instance
	 */
	public static  <T extends MetaValidator> T getMetaValidator(Class <T>metaValidatorClazz) {
		T validator;
		try {
			validator = metaValidatorClazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		return validator;
	}
	
	/**
	 * Get the default {@link MetaValidatorImpl} instance 
	 * @return a MetaValidator Instance
	 */
	public static MetaValidator getMetaValidator() {
		return getMetaValidator(MetaValidatorImpl.class);
	}
	
	/**
	 * Gets list of metadata messages for locale of supplied HttpSession
	 * @param session
	 * @return Map of name : message pairs
	 */
	public static Map<String, String> getMetadataMessages(HttpSession session){
		Set<String> keys=DBResourceBundle.MESSAGE_SOURCE.getKeys(Locale.US);
		Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		Map<String, String> messageMap = new HashMap<String, String>();
		for(String k: keys) {
			if (!k.startsWith("metadata.")) continue; // get ONLY keys for area we are dealing with
			String currentMessage = DBResourceBundle.MESSAGE_SOURCE.getMessage(k,null,locale);
			String[] path=StringUtils.tokenizeToStringArray(k,"."); // e.g. splits user.login.metaposition
			String name=path[1]; // e.g. 'login' using above example
			messageMap.put(name, currentMessage);
		}
		return messageMap;
	}
	
}
