package edu.yu.einstein.wasp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.dao.impl.DBResourceBundle;
import edu.yu.einstein.wasp.model.MetaAttribute.Control;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

/*
 * contains utility methods for manipulating 
 * with *meta and MetaAttribute objects
 * @Author Sasha Levchuk
 */
public final class MetaUtil {
			
		
		 /* 1. populates "control" and "position" property of each object in the list
		  * with values from the messages_en.properties file
		  *
		  * 2. Sorts list on meta.position field 
		  */
		
		public static final <T> void setAttributesAndSort(List<T> list, MetaAttribute.Area area, Locale locale) {
			 
			 for(T t:list) {
				 
				 MetaBase m=(MetaBase)t;
		
				
				String basename=m.getK().substring(area.name().length()+1);
				
				//some old key we dont know about 
				if (getValue(area,basename,"metaposition")==null) continue;
				
				MetaAttribute p=new MetaAttribute();
				
				m.setProperty(p);
				
				int pos=-1;
						    		
				try {	    			
					pos=Integer.parseInt(getValue(area,basename,"metaposition"));
				} catch (Throwable e) {}
				
				p.setMetaposition(pos);
				
				
				p.setControl(getControl(getValue(area,basename,"control"), locale));
				
				
				p.setLabel(getValue(area,basename,"label", locale));
				p.setConstraint(getValue(area,basename,"constraint", locale));
				p.setError(getValue(area,basename,"error",locale));		 		
				
			 }
			 
			 Collections.sort( list, META_POSITION_COMPARATOR);
		 }
		
	public static Control getControl(String key, Locale locale) {
		
		//WaspMessageSourceImpl
		if (
				!
				((WaspMessageSourceImpl)DBResourceBundle.MESSAGE_SOURCE)
				.contains(key, locale)
			) return null;
		
		String controlStr=getMessage(key, locale);
		
		return getControl(controlStr);
		
	}
	
	public static Control getControl(String controlStr) {
		
		if (controlStr==null) return null;
		
		String typeStr=controlStr.substring(0,controlStr.indexOf(":"));
		
		MetaAttribute.Control.Type type=MetaAttribute.Control.Type.valueOf(typeStr);
		
		if (type!=MetaAttribute.Control.Type.select) return null;
		
		MetaAttribute.Control control=new MetaAttribute.Control();
			
		control.setType(MetaAttribute.Control.Type.select); 				
			
			if (controlStr.startsWith("select:${")) {
				String [] els=StringUtils.tokenizeToStringArray(controlStr,":");
				if (els==null || els.length!=4) {
					throw new IllegalStateException(controlStr+" must match 'select:${beanName}:itemValue:itemLabel' pattern");
				}
				
				String beanName=els[1].replace("${","").replace("}", "");
				control.setItems(beanName);
				control.setItemValue(els[2]);
				control.setItemLabel(els[3]);
				 					
			} else {
			 
				List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
				
				String[] pairs=StringUtils.tokenizeToStringArray(controlStr.substring(controlStr.indexOf(":")+1),";");
							
				for(String el:pairs) {
					String [] pair=StringUtils.split(el,":");
					MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
					option.setValue(pair[0]);
					option.setLabel(pair[1]);
					options.add(option);
				}
				control.setOptions(options);	
			}						

			return control;
	}
	
	private static String getValue(MetaAttribute.Area area, String name, String attribute) {
			 
			 return getMessage(area.name()+"."+name+"."+attribute,Locale.US);
				 
	 }
		 
	 private static String getValue(MetaAttribute.Area area, String name, String attribute, Locale locale) {
		 
		 return getMessage(area.name()+"."+name+"."+attribute,locale);
				 
	 }
		 
		private static String getMessage(String key, Locale locale) {
				
				return DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null, locale);
		}
		 
	
		private final static Comparator META_POSITION_COMPARATOR =  new Comparator() {
			public int compare(Object o1, Object o2) {
				
				MetaBase f1=(MetaBase)o1;
				MetaBase f2=(MetaBase)o2;
	
				if (f1==null || f1.getProperty()==null || f1.getProperty().getMetaposition()==-1 ) return -1;
				if (f2==null || f2.getProperty()==null || f2.getProperty().getMetaposition()==-1 ) return 1;
				
				
				Integer p1=f1.getProperty().getMetaposition();
				Integer p2=f2.getProperty().getMetaposition();	    		 
				
				return p1.compareTo(p2);
		
		}
	};
		
	  // returns list of unique "k" values for the given "prefix"
	/*
	  public static final Set<String> getUniqueKeys(MetaAttribute.Area area, Resourcelocale locale) {
		     
		     Set<String> set = new HashSet<String>();
		     
		     Enumeration<String> en=BASE_locale.getKeys();
		     while(en.hasMoreElements()) {
		    	String k = en.nextElement();
		    	
		    	if (!k.startsWith(area.name()+".") || !k.endsWith(".metaposition")) continue;
		    	
		    	String[] path=StringUtils.tokenizeToStringArray(k,".");
		    	
		    	String name=path[1];
		    	
		    	set.add(name);

		    }
		     
		     return set;
		  }
	  
	  */
	 
	  
	  /*
	   * updates "dbList" so it only contains fields found in "properties" file
	   */
	  public static final <T> List<T> syncWithMaster(List<T> dbList, MetaAttribute.Area area, Class clazz) {
		  
		  Set<String> keys=((WaspMessageSourceImpl)DBResourceBundle.MESSAGE_SOURCE).getKeys(Locale.US);
		  		  
		  List<T> resultList = new ArrayList<T>();
				  
		  Set<String> dbKeys = new HashSet<String>();
		  
		  for(T t:dbList) {
			  
			  MetaBase m=(MetaBase)t;
			  
			  if (!m.getK().startsWith(area.name())) continue;
			  
			  String name=m.getK().substring(area.name().length()+1);
			  
			  if (!keys.contains(name)) continue;
			  
			  resultList.add(t);
			  dbKeys.add(m.getK());		  
		  }
		  
		  for(String key:keys) {		  
			  if (!dbKeys.contains(area+"."+key)) {
				  
				  try {
					  T obj=(T)clazz.newInstance();
				   
					  ((MetaBase)obj).setK(area+"."+key);
				  
					  resultList.add(obj);
				  } catch (Throwable e) {
					  throw new IllegalStateException("cant merge attributes ",e);
				  }
			  }
		  }
		  
		  
		  return resultList;
		  
	}


	public static final <T> List<T> getMasterList(Class<T> clazz,MetaAttribute.Area area, Locale locale) {
		List<T> list = new ArrayList<T>();   
		
		//get current list of meta properties to capture
		Set<String> set=((WaspMessageSourceImpl)DBResourceBundle.MESSAGE_SOURCE).getKeys(Locale.US);
		
		for(String name:set) {
			try {
				T obj=(T)clazz.newInstance();

				((MetaBase)obj).setK(area+"."+name);

				list.add(obj);
			} catch (Throwable e) {
				throw new IllegalStateException("cant merge attributes ",e);
			}
		}
		
		//set property attributes and sort them according to "position"
		setAttributesAndSort(list,area,locale);

		return list;
	}


	public static final <T> List<T> getMetaFromJSONForm(HttpServletRequest request, MetaAttribute.Area area, Class clazz, Locale locale) {
		
		List<T> resultList = MetaUtil.getMasterList(clazz, area, locale); 

		Map parms = request.getParameterMap();
		for (Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();)  {  
			Map.Entry entry = (Map.Entry) iterator.next();
			String key=(String)entry.getKey();
			for (T metaObj: resultList) {
				MetaBase meta = (MetaBase) metaObj;
				if (key.equals(meta.getK())) {
					try {
						meta.setV(((String[])entry.getValue())[0]);
					} catch (Throwable e) {
						throw new IllegalStateException("cant merge attributes ",e);
					}
					break;
				}
			}
		}

		return resultList;
	}
	

	public static final <T> List<T> getMetaFromForm(HttpServletRequest request, MetaAttribute.Area area, Class clazz, Locale locale) {
		return getMetaFromForm(request, area, area, clazz, locale);
	}

	public static final <T> List<T> getMetaFromForm(HttpServletRequest request, MetaAttribute.Area area, MetaAttribute.Area parentarea, Class clazz, Locale locale) {
		
		List<T> resultList = MetaUtil.getMasterList(clazz, area, locale); 

		Map parms = request.getParameterMap();
		for (Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();)  {  
			Map.Entry entry = (Map.Entry) iterator.next();
			String key=(String)entry.getKey();
			for (T metaObj: resultList) {
				MetaBase meta = (MetaBase) metaObj;
				if (key.equals(parentarea.name() + "Meta" + "_" + meta.getK())) {
					try {
						meta.setV(((String[])entry.getValue())[0]);
					} catch (Throwable e) {
						throw new IllegalStateException("cant merge attributes ",e);
					}
					break;
				}
			}
		}

		return resultList;
	}
	 
}
