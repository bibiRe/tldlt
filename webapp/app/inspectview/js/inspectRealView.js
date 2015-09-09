/**
 * 实时查看.
 * 
 * @author hhe
 * @since 2015-09-07
 */
Ext.onReady(function() {

	function createUserColumnModel() {
		return new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
			header : '姓名',
			dataIndex : 'username',
			hidden : false,
			hidden : false,
			width : 130,
			sortable : true
		}, {
			header : '性别',
			dataIndex : 'sex',
			width : 60,
			renderer : SEXRender
		}, {
			id : 'deptname',
			header : '所属部门',
			dataIndex : 'deptname',
			width : 130
		} ]);
	}

	function createUserStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : './inspectView.do?reqCode=queryUserForManage'
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'TOTALCOUNT',
				root : 'ROOT'
			}, [ {
				name : 'userid'
			}, {
				name : 'username'
			}, {
				name : 'sex'
			}, {
				name : 'deptid'
			}, {
				name : 'deptname'
			} ])
		});
	}
	
	function createUserGrid(store, columnModel, pageToolbar) {
		var result = new Ext.grid.GridPanel({
			title : '<span class="commoncss">用户信息</span>',
			height : 300,
			// width:600,
			autoScroll : true,
			region : 'center',
			store : store,
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			stripeRows : true,
			frame : true,
			autoExpandColumn : 'deptname',
			cm : columnModel,
			tbar : [ new Ext.form.TextField({
				id : 'userName',
				name : 'userName',
				emptyText : '请输入用户名称',
				enableKeyEvents : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							queryUserName();
						}
					}
				},
				width : 130
			}), {
				text : '查询',
				iconCls : 'previewIcon',
				handler : function() {
					queryUserName();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					userGridObj.store.reload();
				}
			} ],
			bbar : pageToolbar
		});
		return result;
	}
	
	function createInfoColumnModel() {
		return new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
			header : '类型',
			dataIndex : 'type',
			width : 50,
			sortable : true
		}, {
			header : '名称',
			dataIndex : 'name',
			width : 130,
			sortable : true
		}, {
			header : '时间',
			dataIndex : 'time',
			width : 100,
		}, {			
			id :'info',
			header : '信息',
			dataIndex : 'info',
			width : 130
		} ]);
	}
	
	function createInfoStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : './inspectView.do?reqCode=queryInfoForManage'
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'TOTALCOUNT',
				root : 'ROOT'
			}, [ {
				name : 'name'
			}, {
				name : 'type'
			}, {
				name : 'time'
			}, {
				name : 'info'
			} ])
		});
	}

	function createInfoGrid(store, columnModel, pageToolbar) {
		var result = new Ext.grid.GridPanel({
			title : '<span class="commoncss">巡检信息</span>',
			height : 300,
			// width:600,
			autoScroll : true,
			region : 'center',
			store : store,
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			stripeRows : true,
			frame : true,
			autoExpandColumn : 'info',
			cm : columnModel,
			tbar : [ new Ext.form.TextField({
				id : 'infoName',
				name : 'infoName',
				emptyText : '请输入名称',
				enableKeyEvents : true,
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							queryInfoName();
						}
					}
				},
				width : 130
			}), {
				text : '查询',
				iconCls : 'previewIcon',
				handler : function() {
					queryInfoName();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					infoGridObj.store.reload();
				}
			} ],
			bbar : pageToolbar
		});
		return result;
	}
	
	function createPageCombobox() {
		var result = new Ext.form.ComboBox({
			name : 'pagesize',
			hiddenName : 'pagesize',
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			store : new Ext.data.ArrayStore(
					{
						fields : [ 'value', 'text' ],
						data : [ [ 10, '10条/页' ], [ 20, '20条/页' ],
								[ 50, '50条/页' ], [ 100, '100条/页' ],
								[ 250, '250条/页' ], [ 500, '500条/页' ] ]
					}),
			valueField : 'value',
			displayField : 'text',
			value : '50',
			editable : false,
			width : 85
		});
		result.on("select", function(comboBox) {
			comboBox.pageToolbar.pageSize = parseInt(comboBox.getValue());
			store.reload({
				params : {
					start : 0,
					limit : comboBox.pageToolbar.pageSize
				}
			});
		});
		return result;
	}

	function createPageToolbar(store) {
		var combobox = createPageCombobox();
		var result = new Ext.PagingToolbar({
			pageSize : parseInt(combobox.getValue()),
			store : store,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : [ '-', '&nbsp;&nbsp;', combobox ]
		});
		combobox.pageToolbar = result;
		return result;
	}


	function createGridObj(createColumnModelFunc, createStoreFunc,
			createGridFunc) {
		var result = {};
		result.columnModel = createColumnModelFunc();
		result.store = createStoreFunc();
		result.pageToolbar = createPageToolbar(result.store);
		result.grid = createGridFunc(result.store, result.columnModel,
				result.pageToolbar);
		result.store.load({
			params : {
				start : 0,
				limit : result.pageToolbar.pageSize,
				firstload : 'true'
			}
		});
		return result;
	}
	var userGridObj = createGridObj(createUserColumnModel, createUserStore,
			createUserGrid);
	var infoGridObj = createGridObj(createInfoColumnModel, createInfoStore,
			createInfoGrid);
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [ {
			collapsible : true,
			width : 410,
			split : true,
			region : 'west',
			autoScroll : true,
			margins : '3 3 3 3',
			layout : 'border',
			title: '信息',
			border: false,
			items : [{
				region : 'north',
				split : true,
				collapsible : true,
				height:360,
				items : [userGridObj.grid]
			}, {
				region : 'center',
				split : true,
				items : [infoGridObj.grid]
			}]
		}, {
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '3 3 3 3',
			items : []
		} ]
	});

	/**
	 * 根据条件查询实时查看用户
	 */
	function queryUserName() {
		userGridObj.store.load({
			params : {
				start : 0,
				limit : userGridObj.pageToolbar.pageSize,
				userName : Ext.getCmp('userName').getValue()
			}
		});
	}
	/**
	 * 根据条件查询实时查看信息
	 */
	function queryInfoName() {
		infoGridObj.store.load({
			params : {
				start : 0,
				limit : infoGridObj.pageToolbar.pageSize,
				userName : Ext.getCmp('infoName').getValue()
			}
		});
	}
});