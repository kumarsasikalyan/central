<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<style>
	
	
	.preloadHidden {
		display:none;
	}
	
	.errorHeader{
		background-color:red;
		background:red;
	}

	.center
	{
		margin-left:auto;
		margin-right:auto;
		display: block;
	}
	
	.selection_dialog
	{
		position: absolute; 
		left:40%; 
		margin-left: -150px; 
		top:0px; 
		width: 550px; 
		height: 630px; 
		padding: 2px;
		z-index:20;
	}
	
	.selection_dialog_shadow
	{
		width: 550px; 
		height: 630px; 
		position: absolute; 
		left:40%; 
		margin-left: -144px; 
		top:12px;
		padding: 2px;
		z-index:10;
	}
	
	.alert_icon {
		float: left; 
		margin-right: .3em;
	}
	
	.dialog_header{
		font-size: 14px; 
		padding:4px;
	}
	
	.qc_title{
		margin-top:10px; 
		padding:4px; 
		font-size: 14px;
	}
	
	.verifyQualityForm{
		padding: 10px;
	}
	
	#displayWindow{
		float:left; 
		margin-left: 20px;
	}
	
	#amount{
		border:0;
		font-weight:bold;
	}
	

	td.formLabel {
		text-align: right;
		color: #191975;
		font-size: 14px;
	}
	
	td.vcenter {
		vertical-align:middle;
		border-bottom: solid 1px;
		
	}
	

	#slider_frame {
		font-family: Arial, Verdana, "Times New Roman", Times, serif;
		font-size: 12px;
		color: #525252;
		margin-left: 15px;
		margin-right: 15px;
		margin-top: 15px;
		
	}
	#slider .ui-slider-range { background: #CCCCCC; }
	#slider .ui-slider-handle { border-color: #525252; }
	
	#slider {
		width: 800px;
		float:left;
		margin-top:10px;
		margin-right:10px;
		
	}
	
	#cycle_number{
		float: left;
		width: 150px;
		margin-top:8px;
		
	}
	
	#intA, #intC, #intG, #intT {
		float: left;
		width: 260px;
		height: 695px;
		margin: 15px;
	}
	
	#intA{
		clear: left;
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_a.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intC{
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_c.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intT{
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_t.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intG{
		background-image:url(<c:out value="${runReportBaseImagePath}/Chart_1_g.png" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#loading_dialog-modal{
		padding:15px;
		font-size: 14px;
	}
	
	#error_dialog-modal
	{
		padding:15px;
		font-size: 14px;
		z-index:100;
	}

	
</style>
<script>
	
	function validate(){
		TOTAL_LANES = 8;
		messagePassFailRoot = "You must choose either 'Pass' or 'Fail' for ALL lanes. You have not yet scored lanes ";
		messagePassFailLanes = "";
		messageCommentsRoot = "You must write a comment for all failed lanes: ";
		messageCommentsLanes = "";
		for (i=1; i <= TOTAL_LANES; i++){
			laneResult = $("input[name='radioL" + i + "']:checked").val();
			laneComment = $("textarea[name='commentsL" + i + "']").val();
			if (laneResult == null){
				if (messagePassFailLanes.length > 0)
					messagePassFailLanes += ", ";
				messagePassFailLanes += i;
			} else if (laneResult == 0 && (laneComment == null || laneComment == "")){
				if (messageCommentsLanes.length > 0)
					messageCommentsLanes += ", ";
				messageCommentsLanes += i;
			}
		}
		message = "";
		if (messagePassFailLanes.length > 0 )
			message += messagePassFailRoot + messagePassFailLanes + ". ";
		if (messageCommentsLanes.length > 0 )
			message += messageCommentsRoot + messageCommentsLanes + ". ";
		if (message.length > 0){
			$( "#warningText" ).html(message);
			$( "#error_dialog-modal" ).dialog("open");
		}
		else{
			$( "#selectionWindow").hide();
			$( "#qualityForm").submit();
		}
	}

	function updateOnSlide( value_update ){
		$( "#amount" ).val( value_update );
		$( "#intA" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_a.png)");
		$( "#intC" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_c.png)");
		$( "#intT" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_t.png)");
		$( "#intG" ).css("background-image", "url(<c:out value="${runReportBaseImagePath}"/>/Chart_" + value_update + "_g.png)");
	}	

	$( window ).load( function(){
		$( "#main" ).show();
		$( '#loading_dialog-modal' ).dialog( 'close' );
	});
		
	$(function() {
		$( "#main" ).hide();
		$( "#selectionWindow" ).hide();
		
		$( "#radioL1" ).buttonset();
		$( "#radioL2" ).buttonset();
		$( "#radioL3" ).buttonset();
		$( "#radioL4" ).buttonset();
		$( "#radioL5" ).buttonset();
		$( "#radioL6" ).buttonset();
		$( "#radioL7" ).buttonset();
		$( "#radioL8" ).buttonset();

		$( "#submitForm" ).button()
					.click(function(){
						validate();
					});
		
		$( "#cancelForm" ).button()
					.click(function(){
						$( "#selectionWindow").hide();
					});
	
		$( "#showForm" ).button()
					.click(function(){
						$( "#selectionWindow").show();
					});

		$( "#selectionWindow" ).draggable({ handle: ".ui-widget-header" });
		
		$( "#slider" ).slider({
			range: "min",
			min: 1,
			max: <c:out value="${numCycles}" />,
			value: 1,
			slide: function( event, ui ) {
				updateOnSlide( ui.value );
			}
		});
		
		$( "#amount" ).val( $( "#slider" ).slider( "value" ) );
		$( "#loading_dialog-modal" ).dialog({
			height: 170,
			modal: true
			});

		$( "#error_dialog-modal" ).dialog({
			modal: true,
			height: 250,
			width: 350,
			autoOpen: false,
			buttons: {
				Ok: function() {
					$( this ).dialog( "close" ); // close modal when 'Ok' button pressed 
				}
			},
			open: function(){
				$(this).parents(".ui-dialog:first").find(".ui-dialog-titlebar").addClass("errorHeader"); // change header bar background color 
			}
		});
				
	});

	
</script>