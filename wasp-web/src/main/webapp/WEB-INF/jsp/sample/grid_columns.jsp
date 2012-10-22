<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="name"  type="text" sortable="true" searchable="false" editable="false" columnWidth="130"/>
<wasp:field name="type"  type="text" sortable="true" searchable="false" editable="false" columnWidth="70"/>
<wasp:field name="subtype"  type="text" sortable="true" searchable="false" editable="false" columnWidth="100"/>
<wasp:field name="jobId"  type="text" sortable="true" searchable="false" editable="false" columnWidth="45"/>
<wasp:field name="submitter"  type="text" sortable="true" searchable="false" editable="false" columnWidth="100"/>
<wasp:field name="pi"  type="text" sortable="true" searchable="false" editable="false" columnWidth="100"/>
<wasp:field name="receivedStatus"  type="text" sortable="true" searchable="false" editable="false" columnWidth="70"/>
<wasp:field name="runs"  type="text" sortable="true" searchable="false" editable="false" columnWidth="70"/>

 _url='/wasp/sample/listJSON.do?selId=${param.selId}';

_navAttr=
	{edit:false,view:false,add:false,del:false,search:false,refresh:true,beforeRefresh: 
		function () { 
			<%--http://stackoverflow.com/questions/5070065/jqgrid-sort-icon-on-columns-are-not-reset-after-triggerreloadgrid-call 
			var myGrid = jQuery("#grid_id");
    		$("span.s-ico",myGrid[0].grid.hDiv).hide();
    		--%>
			<%--http://stackoverflow.com/questions/7089643/programmatically-sorting-the-jqgrid 
			with next line, sortname is set to "date" and with that sidx is also set to "date" 
			so that with reload grid, sidx is date and it is the icon being displayed 
			var myGrid = jQuery("#grid_id");
    		$("span.s-ico",myGrid[0].grid.hDiv).hide();
    		$("#grid_id").setGridParam({sortname:'date'});
    		$("#grid_id").setGridParam({sortorder:'desc'});
			$("span.s-ico",myGrid[0].grid.hDiv).show();--%>
			
			$("#grid_id").setGridParam({sortname:''});
		}
	}

_viewAttr={width:600};
<%-- 
<wasp:field name="name" itemLabel="name" type="text" sortable="true"/>
<wasp:field name="type" type="text" searchable="false"/>
<wasp:field name="subtype" type="text" searchable="false"/>
<wasp:field name="job" type="text" searchable="false"/>
<wasp:field name="submitterUserId"  itemLabel="submitter" type="text" sortable="true" searchable="false"/>
<wasp:field name="isreceived" type="text" />
<wasp:field name="runs" type="text" searchable="false"/>
_navAttr={edit:false,view:true,add:false,del:false};
_viewAttr={width:600};
--%>

  


