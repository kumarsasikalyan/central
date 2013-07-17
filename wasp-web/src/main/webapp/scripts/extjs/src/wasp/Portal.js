/**
 * @class Ext.wasp.Portal
 * @extends Object
 * A sample portal layout application class.
 */

Ext.define('Ext.wasp.Portal', {

    extend: 'Ext.container.Container',
    requires: ['Ext.wasp.PortalPanel', 'Ext.wasp.PortalColumn', 'Ext.wasp.GridPortlet', 'Ext.wasp.ChartPortlet'],

    getTools: function(){
        return [{
        	xtype: 'tool',
        	type: 'maximize',
        	handler: function(e, target, header, tool){
                var portlet = header.ownerCt;
                var portal = portlet.up('portalpanel');
                if(portal===undefined) { // if the portlet is in the max panel
                	var pmax = portlet.ownerCt;
                	pmax.removeAll();
                	var tabpanel = pmax.ownerCt;
                	portal = tabpanel.items.last();
	                tabpanel.getLayout().setActiveItem(portal);
                } else {  // if the portlet is in the normal portal panel
	                var tabpanel = portal.ownerCt;
	                var pmax = tabpanel.items.first();
	                var portletClone = portlet.cloneConfig();
	                portletClone.closable = false;
	                portletClone.collapsible = false;
	                pmax.add(portletClone);
	                pmax.doLayout();
	                tabpanel.getLayout().setActiveItem(pmax);
                }
            }
        }];
    },

    initComponent: function(){
        var content = '<div class="portlet-content">content</div>';

        Ext.apply(this, {
            id: 'wasp-viewport',
            renderTo: 'resultpanel',
            width: 1400,
            height: 800,
            layout: {
                type: 'border',
                padding: '0 5 5 5' // pad the layout from the window edges
            },
            items: [{
                id: 'wasp-header',
                xtype: 'box',
                region: 'north',
                height: 20,
                html: 'Job Result Portal'
            },{
                xtype: 'container',
                region: 'center',
                layout: 'border',
                items: [{
                    id: 'wasp-options',
                    title: 'D3 Treeview',
                    region: 'west',
                    animCollapse: true,
                    width: 400,
                    minWidth: 250,
                    maxWidth: 700,
                    split: true,
                    collapsible: true,
                    layout:{
                        type: 'accordion',
                        animate: true
                    },
                    items: [{
                        html: '<div id="treeview"></div>',
                        title:'Navigation',
                        autoScroll: true,
                        border: false,
                        iconCls: 'nav'
                    },{
                        title:'Settings',
                        html: content,
                        border: false,
                        autoScroll: true,
                        iconCls: 'settings'
                    }]
                },{
                	id: 'wasp-tabpanel',
                    xtype: 'tabpanel',
                    region: 'center',
                    activeTab: 0,
                    items: []
                }]
            }]
        });
        this.callParent(arguments);
    },

    onPortletClose: function(portlet) {
        this.showMsg('"' + portlet.title + '" was removed');
    },
    
    showMsg: function(msg) {
        var el = Ext.get('wasp-msg'),
            msgId = Ext.id();

        this.msgId = msgId;
        el.update(msg).show();

        Ext.defer(this.clearMsg, 3000, this, [msgId]);
    },

    clearMsg: function(msgId) {
        if (msgId === this.msgId) {
            Ext.get('wasp-msg').hide();
        }
    }
});
