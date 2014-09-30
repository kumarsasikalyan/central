package edu.yu.einstein.wasp.genericfileviewing.panelrenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;

/**
 * 
 * @author aj
 * @author asmclellan
 *
 */
public class AbstractSvFilePanelRender {
	
	
	protected AbstractSvFilePanelRender(){} // makes abstract
	
	/**
	 * Returns grid content parsed from input stream.
	 * @param is
	 * @param header
	 * @param seperator
	 * @param lineLimit (set to -1 for unlimited)
	 * @return
	 */
	protected static GridContent getGridContent(InputStream is, boolean header, String seperator, int lineLimit){
		GridContent content = new GridContent();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			String headerLine = "";
			boolean isFirstNonHeaderLineNotProcessed = true;
			int lineNumber = 1;
			while ((line = br.readLine()) != null && !line.trim().isEmpty() && (lineLimit == -1 || lineNumber <= lineLimit)) {
				line = line.trim();
				if (isFirstNonHeaderLineNotProcessed){
					// If the first line starts with #, it might be a header line There may be more than one so only consider the 
					// last one we find a possible header line and then only if more than one tab delimited item
					if (line.startsWith("#")) {
						header = true;
						headerLine = line.replaceFirst("#+", "");
						continue;
					}
					if (header && headerLine.isEmpty()){
						headerLine = line;
						continue;
					}
					if (header){
						// process header line first
						String[] fields = headerLine.split(seperator);
						if (fields.length > 1){ // unlikely to be a true header if only 1 field
							Integer dataIndex = 1;
							for (String fstr : fields) {
								GridColumn c = new GridColumn(fstr.trim(), "di"+dataIndex, 1);
								c.setWidth(150);
								content.addColumn(c);
								content.addDataFields(new GridDataField("di"+dataIndex, "string"));
								dataIndex++;
							}
						}
					}
					Integer dataIndex = 1;
					List<String> row = new ArrayList<String>();
					for (String fstr : line.split(seperator)) {
						if (!header){
							content.addColumn(new GridColumn("", "di"+dataIndex, 1));
							content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						}
						row.add(fstr.trim());
						dataIndex++;
					}
					content.addDataRow(row);
					isFirstNonHeaderLineNotProcessed = false;
					lineNumber++;
				} else { 
					List<String> row = new ArrayList<String>();
					for (String fstr : line.split(seperator)){
						row.add(fstr.trim());
					}
					content.addDataRow(row);
					lineNumber++;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}