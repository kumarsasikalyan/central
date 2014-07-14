/**
 * 
 */
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
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.Panel;

/**
 * @author aj
 *
 */
public class CsvFilePanelRenderer {

	public static Panel getPanelForFileGroup(InputStream is, boolean header) {
		String csvSplitBy = ",";
		
		GridPanel panel = new GridPanel();
		GridContent content = new GridContent();

		if (is == null) {
			panel.setContent(content);
			return panel;
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = br.readLine();
			if (line!=null) {
				String[] fields = line.split(csvSplitBy);
				Integer dataIndex = 1;
				if (header) {	// the first line is the header
					for (String fstr : fields) {
						content.addColumn(new GridColumn(fstr.trim(), "di"+dataIndex, 1));
						content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						dataIndex++;
					}
				} else {	// the first line is the data
					List<String> row = new ArrayList<String>();
					for (String fstr : fields) {
						content.addColumn(new GridColumn("", "di"+dataIndex, 1));
						content.addDataFields(new GridDataField("di"+dataIndex, "string"));
						row.add(fstr.trim());
						dataIndex++;
					}
					content.addDataRow(row);
				}
			}
			while ((line = br.readLine()) != null) {
				List<String> row = new ArrayList<String>();
				String[] fields = line.split(csvSplitBy);
				for (String fstr : fields) {
					row.add(fstr.trim());
				}
				
				content.addDataRow(row);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		panel.setContent(content);

		return panel;
	}

}