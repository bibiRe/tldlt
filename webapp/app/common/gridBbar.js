/*
 * 表格中使用的统一格式的 Bottom bar
 * yening 2015.2.6
 */
Ext.ns('Ext.ux.grid');
Ext.ux.grid.BottomBar = function(store){
	var me = this;
	var pagesizeCombo = new Ext.form.ComboBox({
		name : 'pagesize',
		hiddenName : 'pagesize',
		typeAhead : true,
		triggerAction : 'all',
		lazyRender : true,
		mode : 'local',
		store : new Ext.data.ArrayStore({
					fields : ['value', 'text'],
					data : [[10, '10条/页'], [20, '20条/页'],
							[50, '50条/页'], [100, '100条/页'],
							[250, '250条/页'], [500, '500条/页']]
				}),
		valueField : 'value',
		displayField : 'text',
		value : '50',
		editable : false,
		width : 85
	});

	pagesizeCombo.on("select", function(comboBox) {	
		me.bbar.pageSize = parseInt(comboBox.getValue());
		store.load({
					params : {
						start : 0,
						limit : me.getPageSize()
					}
				});
	});
	
	this.getPageSize = function(){
		return parseInt(pagesizeCombo.getValue());
	};
	this.bbar = new Ext.PagingToolbar({
		pageSize : me.getPageSize(),
		store : store,
		displayInfo : true,
		displayMsg : '显示{0}条到{1}条,共{2}条',
		plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
		emptyMsg : "没有符合条件的记录",
		items : ['-', '&nbsp;&nbsp;', pagesizeCombo]
	});
}