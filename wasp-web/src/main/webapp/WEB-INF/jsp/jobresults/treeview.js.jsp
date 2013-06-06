<script type="text/javascript" src="/wasp/scripts/jquery/jquery.cookie.js"></script>

<script type="text/javascript" src="http://d3js.org/d3.v3.min.js"></script>

<script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/include-ext.js"></script>
<!-- script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/options-toolbar.js"></script -->

<script>
Ext.require('Ext.tab.*');

/* Ext.onReady(function(){
    // basic tabs 1, built from existing content
    var tabs = Ext.widget('tabpanel', {
        renderTo: 'tabs1',
        width: 450,
        activeTab: 0,
        defaults :{
            bodyPadding: 10
        },
        items: [{
            contentEl:'script', 
            title: 'Short Text',
            closable: true
        },{
            contentEl:'markup', 
            title: 'Long Text'
        }]
    });
    
    // second tabs built from JS
    var tabs2 = Ext.widget('tabpanel', {
        renderTo: document.body,
        activeTab: 0,
        width: 600,
        height: 250,
        plain: true,
        defaults :{
            autoScroll: true,
            bodyPadding: 10
        },
        items: [{
                title: 'Normal Tab',
                html: "My content was added during construction."
            },{
                title: 'Ajax Tab 1',
                loader: {
                    url: 'ajax1.htm',
                    contentType: 'html',
                    loadMask: true
                },
                listeners: {
                    activate: function(tab) {
                        tab.loader.load();
                    }
                }
            },{
                title: 'Ajax Tab 2',
                loader: {
                    url: 'ajax2.htm',
                    contentType: 'html',
                    autoLoad: true,
                    params: 'foo=123&bar=abc'
                }
            },{
                title: 'Event Tab',
                listeners: {
                    activate: function(tab){
                        setTimeout(function() {
                            alert(tab.title + ' was activated.');
                        }, 1);
                    }
                },
                html: "I am tab 4's content. I also have an event listener attached."
            },{
                title: 'Disabled Tab',
                disabled: true,
                html: "Can't see me cause I'm disabled"
            }
        ]
    });
});
 */

var state = 'none';

function showhide(btn,layer_ref) {
	if (state == 'block') {
		state = 'none';
		btn.value= 'Show Filters';
	} else {
		state = 'block';
		btn.value='Hide Filters';
	}
	if (document.all) { //IS IE 4 or 5 (or 6 beta)
		eval("document.all." + layer_ref + ".style.display = state");
	}
	if (document.layers) { //IS NETSCAPE 4 or below
		document.layers[layer_ref].display = state;
	}
	if (document.getElementById && !document.all) {
		hza = document.getElementById(layer_ref);
		hza.style.display = state;
	}
}

function showhideTree(btn) {
	if (btn.value == 'Hide Tree') {
		btn.value= 'Show Tree';
		$('#left-container').hide('slow');
		setTimeout(function(){$('#right-container').css('width', '100%')}, 600);
	} else {
		btn.value='Hide Tree';
		$('#left-container').show('slow');
		$('#right-container').css('width', '65%');
	}
}

function toggleview() {
	if (root.jid == undefined || root.jid < 1) {
		root.jid = root.id;
	} else {
		root.jid = -1;
	}
	root.children = '';
	click(root);
	update(root);
}


var margin = {top: 20, right: 80, bottom: 20, left: 20},
    width = 460 - margin.right - margin.left,
    height = 700 - margin.top - margin.bottom,
    i = 0,
    duration = 500,
    branch_length = 100,
    min_branch_int = 40,
    root=new Object();
    
var barHeight = 20,
	barWidth = width * .6;

//var tree = d3.layout.tree().size([height, width]);
var tree = d3.layout.tree().size([height, 100]);

var diagonal = d3.svg.diagonal()
    .projection(function(d) { return [d.y, d.x]; });


var vis;/* = d3.select("#treeview").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .attr("pointer-events", "all")
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
	.call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom))
	.append("g");

vis.append("rect")
  .attr("width", width + margin.right + margin.left)
  .attr("height", height + margin.top + margin.bottom)
  .attr('fill', '#fff');*/

function zoom() {
	  vis.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
	}

function collapse(d) {
  if (d.children) {
    d._children = d.children;
    d._children.forEach(collapse);
    d.children = null;
  }
}

root.myid=${myid};
root.type="${type}";
root.jid=-1;
root.pid=-1;
var seen = [];
var rootstr = JSON.stringify(root, function(key, val) {
   if (typeof val == "object") {
        if (seen.indexOf(val) >= 0)
            return undefined;
        seen.push(val);
    }
    return val; });

var activeNode = {myid: null, type: null};

//d3.json("../data/flare.json", function(json) {
//d3.json("../data/flare100.json", function(json) {
//d3.json("http://localhost:8080/wasp/jobresults/helpTag/getJSTreeJson.do?jobId=${jobId}", function(json) {
//d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?id=${myid}&type=${type}", function(json) {
//d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?id=${myid}&type=${type}&jid=${myid}&pid=-1", function(json) {	
d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?node="+rootstr, function(json) {	
/*   if (height < json.children.length * min_branch_int) {
	  height = json.children.length * min_branch_int;
  }

  tree.size([height, width]);
  vis = d3.select("#treeview").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .attr("pointer-events", "all")
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
  .call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom)).on("dblclick.zoom", null)
  .append("g");
*/
	vis = d3.select("#treeview").append("svg:svg")
	.attr("width", width + margin.right + margin.left)
	.attr("height", height + margin.top + margin.bottom)
	.attr("pointer-events", "all")
	.append("svg:g")
	.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
	//.call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom)).on("dblclick.zoom", null)
	.append("svg:g");


  vis.append("rect")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .attr('fill', '#fff')
    .attr('fill-opacity', '0');

  root = json;
  root.x0 = 0; // height / 2;
  root.y0 = 0;
  //root.children.forEach(collapse);
  update(root);
  click(root);
  toggle(root);
  activeNode.myid = root.myid;
  activeNode.type = root.type;
});

//var g = d3.select("g");
//g.call(drag);

function update(source) {

  // Compute the new tree layout.
  var nodes = tree.nodes(root); //.reverse();

  // Normalize for fixed-depth.
//  nodes.forEach(function(d) { d.y = d.depth * branch_length; });
// Compute the "layout".
  nodes.forEach(function(n, i) {
    n.x = i * barHeight;
  });
  
  // Update the nodes
  var node = vis.selectAll("g.node")
      .data(nodes, function(d) { return d.id || (d.id = ++i); });
  
  var nodeEnter = node.enter().append("svg:g")
  .attr("class", "node")
  .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
  .style("opacity", 1e-6)
  .on("click", click)
  .on("mouseover", onMouseOver)
  .on("mouseout", onMouseOut);

	// Enter any new nodes at the parent's previous position.
	nodeEnter.append("svg:rect")
	  .attr("y", -barHeight / 2)
	  .attr("height", barHeight)
	  .attr("width", barWidth)
	  .style("fill", color)
	  .on("dblclick", toggle);
	
	nodeEnter.append("svg:text")
	  .attr("dy", 3.5)
	  .attr("dx", 13.5)
	  .text(function(d) { return d.name; });
	
	// Add checkbox
	nodeEnter.append("svg:circle")
    .attr("cx", 7)
	.attr("r", 4)
	.attr("fill", "white")
	.on("click", function(d) {
	    if (d.selected) {
	        d.selected = false;
	        d3.select(this).attr("fill", "white");
	    } else {
	        d.selected = true;
	        d3.select(this).attr("fill", "steelblue");
	    }
	    
	    printSelectedNodes();
	});
	
	// Transition nodes to their new position.
	nodeEnter.transition()
	  .duration(duration)
	  .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
	  .style("opacity", 1);
	
	node.transition()
	  .duration(duration)
	  .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
	  .style("opacity", 1)
	.select("rect")
	  .style("fill", color);
	
	// Transition exiting nodes to the parent's new position.
	node.exit().transition()
	  .duration(duration)
	  .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
	  .style("opacity", 1e-6)
	  .remove();
	
	// Update the links
	var link = vis.selectAll("path.link")
	  .data(tree.links(nodes), function(d) { return d.target.id; });
	
	// Enter any new links at the parent's previous position.
	link.enter().insert("svg:path", "g")
	  .attr("class", "link")
	  .attr("d", function(d) {
	    var o = {x: source.x0, y: source.y0};
	    return diagonal({source: o, target: o});
	  })
	.transition()
	  .duration(duration)
	  .attr("d", diagonal);
	
	// Transition links to their new position.
	link.transition()
	  .duration(duration)
	  .attr("d", diagonal);
	
	// Transition exiting nodes to the parent's new position.
	link.exit().transition()
	  .duration(duration)
	  .attr("d", function(d) {
	    var o = {x: source.x, y: source.y};
	    return diagonal({source: o, target: o});
	  })
	  .remove();
	
	// Stash the old positions for transition.
	nodes.forEach(function(d) {
	d.x0 = d.x;
	d.y0 = d.y;
	});

	// store the parent's id in everynode
	nodes.forEach(function(d) {
		if (d!=root)
			d.pid = d.parent.myid;
		else
			d.pid = -1;
	});


}

function printSelectedNodes() {
  var nodes = tree.nodes(root);

  var selected = [];
  nodes.forEach(function(n, i) {
    if (n.selected) {
      selected.push(n.name);
    }
  });
  console.log(selected);
}

//Toggle children
function toggle(d) {
	if (d.children) {
		d._children = d.children;
		d.children = null;
	} else {
		d.children = d._children;
		d._children = null;
	}
	update(d);
}

var tabs, tabCounter=0;

function click(d) {
	if (d.jid==undefined) {
	 d.jid = -1;
	}/*  else {
	 jid = d.jid;
	} */
	
	// parent node's id
/* 	var pid = -1;
	if (d!=root)
		d.pid = d.parent.myid; */

	var seen = [];
	var dstr = JSON.stringify(d, function(key, val) {
	   if (typeof val == "object") {
	        if (seen.indexOf(val) >= 0)
	            return undefined;
	        seen.push(val);
	    }
	    return val; });
	
	//var tabs = $('#mytabs').tabs({closable: true});
  
  $.ajax({
      url: '/wasp/jobresults/getDetailsJson.do?node='+dstr,
      type: 'GET',
      dataType: 'json',
      success: function (result) {
    	if (d.type=='dummy') return;  
      
      	//d3.select('#detailview').select("h3").remove();
      	//d3.select('#detailview').select("tbody").remove();
      	
      	//remove all tabs first
//      	for (var i = $("div#tabs ul li").length  - 1; i >= 0; i--) {
//			$('div#tabs').tabs('remove', i);
//		}
      	
      	if(tabs!=undefined)
      		tabs.close();

      	tabs = Ext.widget('tabpanel', {
      		renderTo: 'mytabs',
      		defaults :{
      			bodyPadding: 10
      		}
      	});
      	
/*      $.each(result, function (index, item) {
			var num_tabs = $("div#tabs ul li").length + 1;

             $("div#tabs ul").append(
                 "<li><a href='#tab" + num_tabs + "'>" + index + "</a></li>"
             );
			$("div#tabs").append(
                 "<div id='tab" + num_tabs + "'>" + item + "</div>"
             );
             $("div#tabs").tabs("refresh");
      	});
*/      
		$.each(result, function (index, item) {
			var tabTitle;
			
			if ((d.type.split('-'))[0]=="filetype") {
				tabTitle = "Download "+(d.type.split('-'))[1].toUpperCase()+" files";
				
				//var tablist = d3.select('#detailview').append("div").attr("id", "mytabs").append("ul");
				//var tabs = $("#mytabs").tabs();
				
	
		      	$.each(result, function (index, item) {
		      		//$("#mytabs").tabs('add',item,index);
		      		//$tabs.tabs('add', '#tabs-'+tabCounter, 'Tab '+tabCounter);
		      		//$tabs.tabs('add', '#tab1', 'Tab '+tabCounter);
		      		//tabCounter++;
		      		//tabtitle=index;
		      		//tabcontent=item;
		      		//addtab();
		      	});
		        return;
			} else {
				tabTitle = d.name+" Details";
			}
			
			//tabTitle = index;
			tabCounter++;
			var tab = tabs.add({
	            // we use the tabs.items property to get the length of current items/tabs
	            title: tabTitle,
	            html : "<div id='tab"+tabCounter+"'></div>"
	        });
	        tabs.setActiveTab(tab);
			
//	        $( "<div id='tab"+tabCounter+"'></div>" ).appendTo( "#mytabs" );
	     	//d3.select('#detailview').append("h3").html(headStr);
	      	//var table = d3.select('#detailview').append("tbody");
	      	var table = d3.select("#tab"+tabCounter).append("tbody");
	        $.each(item, function (index1, item1) {
	         	var row = table.append("tr");
	         	row.append("td").html(index1);
	
	         	if (typeof item1 == 'string' || item1 instanceof String) {
	          		row.append("td").html(item1);
	         	} else if (item1.targetLink != undefined) {
	         		row.append("td").html('<a href="'+item1.targetLink+'">'+item1.label+'</a>');
	          	} else {
	          		var td = row.append("td");
	          		$.each(item1, function (index2, item2) {
	          			var row2 = td.append("tr");
	              		row2.append("td").html(index2);
	              		row2.append("td").html(item2);
	          		});
	          	}
	        });
	        tabs.doComponentLayout();
	        
/*	        $( "<li><a href='#tab"+tabCounter+"'>"+tabTitle+"</a></li>" )
				.appendTo( "#mytabs .ui-tabs-nav" );
		    $('#mytabs').tabs("refresh");
		    $('#mytabs').tabs("option", "active", -1);
*/		});
     }
  });
  
  if (d.children == '') {
	  
	  $.ajax({
	      url: '/wasp/jobresults/getTreeJson.do?node='+dstr,
	      type: 'GET',
	      dataType: 'json',
	      success: function (result) {
      		if (result.children != '') {
	    	  d.children = result.children;
      		  update(d);
      		}
      	}
      });
  }

  if (d.myid !== activeNode.myid && d.type !== activeNode.type) {
  	toggle(d);
    activeNode.myid = d.myid;
    activeNode.type = d.type;
  }
}

function color(d) {
	  return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
	}

function onMouseOver(d)
{
    d3.select(this).select("circle").attr("r", 5);
    d3.select(this).select("text").style("font-weight", "bold");
  if (d.parent)
  {
  }
}

function onMouseOut(d)
{
    d3.select(this).select("circle").attr("r", 4.5);
    d3.select(this).select("text").style("font-weight", "normal");
  if (d.parent)
  {
  }
}

</script>