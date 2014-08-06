Ext.require([
	'Ext.grid.*',
	'Ext.data.*',
	'Ext.form.field.Number',
	'Ext.form.field.Date',
	'Ext.tip.QuickTipManager',
	'Ext.selection.CheckboxModel',
	'Wasp.RowActions'
]);

function mergeDownloadLinks(records, linkfield) {

	var links = records[0].get(linkfield);

	for (var i = 1; i < records.length; i++) {
		var uuid = records[i].get(linkfield);
		links += "," + uuid.substring(uuid.lastIndexOf('/') + 1);
	}

	return links;
}



var rowHeight = 30, gridHeaderHeight = 30;
var minGridHeight = 200, maxGridHeight = 650;

Ext.define('Wasp.GridPortlet', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.gridportlet',

	fields: [],
	data: [],
	columns: [],

	height: minGridHeight,

	grouping: false,
	groupfield: '',
	groupheader: '{name}',

	dlcol: false,
	dllinkfld: '',
	dlcoltip: "Download",

	dlselect: false,
	dlbtntxt: "Download selected",
	dlbtnalign: 'center',

	grpdl: false,
	grpdltip: "Download all",
	grpdlalign: 'right',

	statusfld: null,

	gbcol: false,
	gblink: '',
	gbtype: '',
	gbttp: '',
	hidegb: '',

	/**
	 * Custom function used for column renderer
	 * @param {Object} val
	 */
	status: function (val) {
		if (val.match(/complete/i) != null) {
			return '<span style="color:green;">' + val + '</span>';
		} else if (val.match(/start/i) != null) {
			return '<span style="color:blue;">' + val + '</span>';
		} else if (val.match(/pending/i) != null) {
			return '<span style="color:pink;">' + val + '</span>';
		} else if (val.match(/unknown/i) != null) {
			return '<span style="color:orange;">' + val + '</span>';
		} else if (val.match(/fail/i) != null) {
			return '<span style="color:red;">' + val + '</span>';
		} else {
			return '<span style="color:gold;">' + val + '</span>';
		}
	},

	listeners: {
		cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
			//Ext.Msg.alert('Selected Record', 'td : ' + td + ' tr: ' + tr);
		},
		celldblclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
			//Ext.Msg.alert('Selected Record', 'td : ' + td + ' tr: ' + tr);
		}
	},

	initComponent: function () {
		var grid = this;

		Ext.tip.QuickTipManager.init();

		var myStore = Ext.create('Ext.data.ArrayStore', {
			fields: this.fields,
			autoLoad: true,
			data: this.data
		});
		
		var rowCnt = myStore.getTotalCount();

		// enable grouping view
		if (this.grouping) {
			myStore.groupField = this.groupfield;
			myStore.group(this.groupfield);

			var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
				groupHeaderTpl: this.groupheader
			});
			Ext.apply(this, {
				features: groupingFeature
			});
			
			rowCnt += myStore.getGroups().length;
		}

		var actioncol = {
			xtype: 'rowactions',
			header: 'Actions',
			minWidth: 80,
			actions: [],
			callbacks: {
				'icon-gb-ucsc': function(grid, record, action, row, col) {
					window.open(record.get(grid.gblink), '_blank');
				},
				'icon-gb-ensembl': function(grid, record, action, row, col) {
					window.open(record.get(grid.gblink), '_blank');
				},
				'icon-gb-igv': function(grid, record, action, row, col) {
					window.open(record.get(grid.gblink), '_blank');
				}
			},
			keepSelection: true
		};
		
		// Add download action buttons to the action column
		if (this.dlcol && this.dllinkfld != '') {
			actioncol.actions.push({
				iconCls: 'icon-download',
				qtip: this.dlcoltip,
				hideIndex: this.hidedl,
				callback: function (grid, record, action, idx, col, e, target) {
					window.location = record.get(grid.dllinkfld);
				}
			});

			// If grouping view is set, add group download button
			if (this.grpdl) {
				actioncol.groupActions = [{
					iconCls: 'icon-group-download',
					qtip: this.grpdltip,
					align: this.grpdlalign,
					callback: function (grid, records, action, groupValue) {
						if (records.length > 0)
							window.location = mergeDownloadLinks(records, grid.dllinkfld);
					}
				}];
			}

		}

		// Add genome browser buttons to the action column
		if (this.gbcol && this.gblink != '') {
			actioncol.actions.push({
				iconIndex: this.gbtype,
				qtipIndex: this.gbttp,
				hideIndex: this.hidegb
			});
		}

		if (actioncol.actions.length > 0)
			this.columns.push(actioncol);

		// Add colorful renderer to the status column
		if (this.statusfld != null) {
			this.columns.forEach(function (element, index, array) {
				if (element.dataIndex == grid.statusfld) {
					Ext.apply(element, {
						renderer: grid.status
					});
				}
			});
		}
		
		// add cell align and header align to columns
		this.columns.forEach(function (element, index, array) {
			if (element.cellAlign != 'undefined') {
				Ext.apply(element, { align: element.cellAlign });
			}
			if (element.headerAlign != 'undefined') {
				Ext.apply(element, { style: 'text-align: '+element.headerAlign });
			}
		});

		// enable selecting multiple files to download
		if (this.dlselect && this.dllinkfld != '') {
			Ext.apply(this, {
				selModel: Ext.create('Ext.selection.CheckboxModel', {
					singleSelect: false,
					sortable: false,
					checkOnly: false,
					mode: 'SIMPLE',
					listeners: {
						selectionchange: function (me, selected, eOpts) {
							var dlbtn = grid.down('button[text="' + grid.dlbtntxt + '"]');
							if (selected.length == 0) {
								dlbtn.disable();
							} else {
								dlbtn.enable();
							}
						}
					}
				}),
				dockedItems: [{
					xtype: 'toolbar',
					dock: 'bottom',
					ui: 'footer',
					layout: {
						pack: this.dlbtnalign
					},
					items: [{
						minWidth: 80,
						disabled: true,
						text: this.dlbtntxt,
						handler: function (me, e) {
							var records = grid.getSelectionModel()
								.getSelection();
							if (records.length > 0) {
								window.location = mergeDownloadLinks(records, grid.dllinkfld);
							}
						}
				}]
			}]
			});
		}

		// adjust grid height by the number of rows in the grid 
		var gridHeight = rowCnt * rowHeight + gridHeaderHeight;
		gridHeight = (gridHeight<minGridHeight) ? minGridHeight : gridHeight;
		gridHeight = (gridHeight>maxGridHeight) ? maxGridHeight : gridHeight;
		Ext.apply(this, {
			store: myStore,
			columns: this.columns,
			height: gridHeight //this.height
		});

		this.callParent(arguments);
	}
});