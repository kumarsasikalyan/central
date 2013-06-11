package edu.yu.einstein.wasp.web;

/**
 * 
 * @author asmclellan
 *
 */
public class Tooltip {

	public static String getTooltipHtmlString(String tooltipText){
		return "&nbsp;<img src='/wasp/images/qmark.png' height='15px' border='0' title='" + tooltipText + "' class='tooltip'>&nbsp;";
	}
	
	public static String getCommentHtmlString(String commentText){
		return "&nbsp;<img src='/wasp/images/comment-icon.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
	}
	
	public static String getWarningHtmlString(String commentText){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='/wasp/images/warningAndComment.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='/wasp/images/warning.png' height='20px'  border='0' >&nbsp;";
	}
	
	public static String getSuccessHtmlString(String commentText){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='/wasp/images/passAndComment.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='/wasp/images/pass.png' height='20px'  border='0' >&nbsp;";
	}
	
	public static String getFailureHtmlString(String commentText){
		if (commentText != null && !commentText.isEmpty())
			return "&nbsp;<img src='/wasp/images/failAndComment.png' height='20px'  border='0' title='" + commentText + "' class='tooltip'>&nbsp;";
		else
			return "&nbsp;<img src='/wasp/images/fail.png' height='20px'  border='0' >&nbsp;";
	}

}
