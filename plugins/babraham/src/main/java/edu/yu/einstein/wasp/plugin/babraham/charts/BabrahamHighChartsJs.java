package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.charts.highchartsjs.BasicHighChartsSeries;
import edu.yu.einstein.wasp.charts.highchartsjs.BasicHighChartsSeries.Type;
import edu.yu.einstein.wasp.charts.highchartsjs.HighChartsJsBase;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.web.Tooltip;
import edu.yu.einstein.wasp.web.panel.WebContent;

/**
 * Highcharts JS implementation of Babraham plots that require a custom implementation (http://www.highcharts.com)
 * @author asmclellan
 *
 */
public class BabrahamHighChartsJs extends HighChartsJsBase {

	private static final String HEX_RED = "#F6CECE";
	private static final String HEX_YELLOW = "#F5ECCE";
	private static final String HEX_GREEN = "#CEF6CE";
	private static final String HEX_BLUE = "#0101DF";

	
	public static WebContent getPerBaseSeqQualityPlotHtml(final WaspBoxPlot waspBoxPlot) throws BabrahamDataParseException {
		DataSeries boxPlotDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.BOX_AND_WHISKER);
		DataSeries meanDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.RUNNING_MEAN);
		WebContent content = new WebContent();
		content.setScriptDependencies(getScriptDependencies());
		String containerId = getUniqueContainerId();
		content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", waspBoxPlot.getDescription(), containerId));
		StringBuilder sb = new StringBuilder();
		sb.append(getHCScriptStartCode(ChartType.BOXPLOT, containerId, waspBoxPlot.getTitle(), false));
		sb.append(getBasicXAxisCode(waspBoxPlot.getxAxisLabel(), boxPlotDS.getRowLabels(), 5));
		sb.append("plotOptions: { series: { groupPadding: 0} },\n");
		sb.append("yAxis: { title: { text: '" + waspBoxPlot.getyAxisLabel() + "' },\n ");
		sb.append("plotBands: ["); 
		sb.append("{ color: '" + HEX_RED + "', from: 0, to: 20 },");
		sb.append("{ color: '" + HEX_YELLOW + "', from: 20, to: 28 },");
		sb.append("{ color: '" + HEX_GREEN + "', from: 28, to: 100 }");
		sb.append("]},\n");
		Set<BasicHighChartsSeries> seriesSet = new LinkedHashSet<BasicHighChartsSeries>();
		seriesSet.add(new BasicHighChartsSeries(boxPlotDS));
		seriesSet.add(new BasicHighChartsSeries(meanDS, Type.SPLINE, false, false, Color.RED));
		try {
			sb.append(getBasicSeriesCode(seriesSet));
		} catch (JSONException e) {
			throw new BabrahamDataParseException("Unable to extract data series", e);
		}
		sb.append(getHCScriptEndCode());
		content.setScriptCode(sb.toString());
		return content;
	}
	
	public static WebContent getSplineForBases(final WaspChart2D chart) throws BabrahamDataParseException{
		DataSeries dsA = chart.getDataSeries("% A");
		DataSeries dsC = chart.getDataSeries("% C");
		DataSeries dsT = chart.getDataSeries("% T");
		DataSeries dsG = chart.getDataSeries("% G");
		WebContent content = new WebContent();
		content.setScriptDependencies(getScriptDependencies());
		String containerId = getUniqueContainerId();
		content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", chart.getDescription(), containerId));
		StringBuilder sb = new StringBuilder();
		sb.append(getHCScriptStartCode(ChartType.SPLINE, containerId, chart.getTitle(), true));
		sb.append(getBasicXAxisCode(chart.getxAxisLabel()));
		sb.append(getBasicYAxisCode(chart.getyAxisLabel(), 0, 100));
		Set<BasicHighChartsSeries> seriesSet = new HashSet<BasicHighChartsSeries>();
		seriesSet.add(new BasicHighChartsSeries(dsG, false, false, Color.RED));
		seriesSet.add(new BasicHighChartsSeries(dsA, false, false, Color.BLUE));
		seriesSet.add(new BasicHighChartsSeries(dsT, false, false, Color.GREEN));
		seriesSet.add(new BasicHighChartsSeries(dsC, false, false, Color.BLACK));
		try {
			sb.append(getBasicSeriesCode(seriesSet));
		} catch (JSONException e) {
			throw new BabrahamDataParseException("Unable to extract data series", e);
		}
		sb.append(getHCScriptEndCode());
		content.setScriptCode(sb.toString());
		return content;
	}
	
	public static WebContent getSplineForPerSequenceGC(final WaspChart2D chart) throws BabrahamDataParseException{
		DataSeries dsActual = chart.getDataSeries("GC count per read");
		DataSeries dsTheory = chart.getDataSeries("Theoretical Distribution");
		WebContent content = new WebContent();
		content.setScriptDependencies(getScriptDependencies());
		String containerId = getUniqueContainerId();
		content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", chart.getDescription(), containerId));
		StringBuilder sb = new StringBuilder();
		sb.append(getHCScriptStartCode(ChartType.SPLINE, containerId, chart.getTitle(), true));
		sb.append(getBasicXAxisCode(chart.getxAxisLabel(), 0, 100));
		sb.append(getBasicYAxisCode(chart.getyAxisLabel(), 0, null));
		Set<BasicHighChartsSeries> seriesSet = new HashSet<BasicHighChartsSeries>();
		seriesSet.add(new BasicHighChartsSeries(dsActual, false, false, Color.RED));
		seriesSet.add(new BasicHighChartsSeries(dsTheory, false, false, Color.BLUE));
		try {
			sb.append(getBasicSeriesCode(seriesSet));
		} catch (JSONException e) {
			throw new BabrahamDataParseException("Unable to extract data series", e);
		}
		sb.append(getHCScriptEndCode());
		content.setScriptCode(sb.toString());
		return content;
	}
	
	public static WebContent getQCSummaryTableRepresentation(final WaspChart basicStats) {
		List<List<Object>> data = basicStats.getDataSeries().get(0).getData();
		WebContent content = new WebContent();
		content.setScriptDependencies(getScriptDependencies());
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>" + basicStats.getTitle() + "</h3>\n");
		sb.append("<table class='standardTable' >\n");
		sb.append("<tr>\n");
		sb.append("<th>" +  basicStats.getDataSeries().get(0).getColLabels().get(0) + "</th>\n");
		sb.append("<th>" + basicStats.getDataSeries().get(0).getColLabels().get(1) + "</th>\n");
		sb.append("</tr>\n");
		for (List<Object> row : data){
			sb.append("<tr>\n");
			String moduleName = (String) row.get(0);
			String result = (String) row.get(1);
			String comment = (String)  row.get(2);
			if (result.equals(FastQC.QC_ANALYSIS_RESULT_PASS))
				result = Tooltip.getSuccessHtmlString(comment);
			else if (result.equals(FastQC.QC_ANALYSIS_RESULT_WARN))
				result = Tooltip.getWarningHtmlString(comment);
			else if (result.equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
				result = Tooltip.getFailureHtmlString(comment);
			sb.append("<td class='center'>" + result + "</td>\n");
			sb.append("<td>" + moduleName + "</td>\n");
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		content.setHtmlCode(getSimpleContainerCode(sb.toString(), basicStats.getDescription()));
		return content;
	}

	
}
