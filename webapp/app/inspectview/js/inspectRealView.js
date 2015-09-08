/**
 * 实时查看.
 *
 * @author hhe
 * @since 2015-09-07
 */
Ext.onReady(function() {
	
	function createUserColumnModel() {
		return new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
			header: '姓名',
			dataIndex: 'username',
			hidden: false,
			hidden: false,
			width: 130,
			sortable: true
		}, {
			id : 'deptname',
			header : '所属部门',
			dataIndex : 'deptname',
			width : 130
		}, {
			header : '性别',
			dataIndex : 'sex',
			width : 60,
			renderer : SEXRender
		}]);
	}
	
	function createUserStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: './inspect.do?reqCode=queryRealViewUser'
			}),
			reader: new Ext.data.JsonReader({
				totalProperty: 'TOTALCOUNT',
				root: 'ROOT'
			}, [{
				name : 'userid'
			}, {
				name : 'username'
			}, {
				name : 'sex'
			}, {
				name : 'deptid'
			}, {
				name : 'deptname'
			}])
		});
	}

	function createPageCombobox() {		
		var result = new Ext.form.ComboBox({
			name: 'pagesize',
			hiddenName: 'pagesize',
			typeAhead: true,
			triggerAction: 'all',
			lazyRender: true,
			mode: 'local',
			store: new Ext.data.ArrayStore({
				fields: ['value', 'text'],
				data: [
				       [10, '10条/页'],
				       [20, '20条/页'],
				       [50, '50条/页'],
				       [100, '100条/页'],
				       [250, '250条/页'],
				       [500, '500条/页']
				       ]
			}),
			valueField: 'value',
			displayField: 'text',
			value: '50',
			editable: false,
			width: 85
		});
		result.on("select", function(comboBox) {
			comboBox.pageToolbar.pageSize = parseInt(comboBox.getValue());
			store.reload({
				params: {
					start: 0,
					limit: comboBox.pageToolbar.pageSize
				}
			});
		});
		return result;
	}
	
	function createPageToolbar(store) {
		var combobox = createPageCombobox();
		var result =  new Ext.PagingToolbar({
			pageSize: parseInt(combobox.getValue()),
			store: store,
			displayInfo: true,
			displayMsg: '显示{0}条到{1}条,共{2}条',
			emptyMsg: "没有符合条件的记录",
			items: ['-', '&nbsp;&nbsp;', combobox]
		});
		combobox.pageToolbar = result;
		return result;
	}
	
	function createUserGrid(store, pageToolbar) {
		var user_Grid = new Ext.grid.GridPanel({
			title: '<span class="commoncss">用户信息表</span>',
			height: 500,
			// width:600,
			autoScroll: true,
			region: 'center',
			store: store,
			loadMask: {
				msg: '正在加载表格数据,请稍等...'
			},
			stripeRows: true,
			frame: true,
			autoExpandColumn: 'remark',
			cm: result.columnModel,
			tbar: [new Ext.form.TextField({
				id: 'userName',
				name: 'userName',
				emptyText: '请输入用户名称',
				enableKeyEvents: true,
				listeners: {
					specialkey: function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							queryUserName();
						}
					}
				},
				width: 130
			}), {
				text: '查询',
				iconCls: 'previewIcon',
				handler: function() {
					queryUserName();
				}
			}, '-', {
				text: '刷新',
				iconCls: 'arrow_refreshIcon',
				handler: function() {
					userGridObj.store.reload();
				}
			}],
			bbar: pageToolbar
		});
	}
	
	
	function createUserGridObj() {
		var result = {};
		result.columnModel = createColumnModel();
		result.store = createUserStore();
		result.pageToolbar = createPageToolbar(result.store);
		result.grid = createUserGrid(result.store, result.pageToolbar);
		result.store.load({
			params: {
				start: 0,
				limit: result.pageToolbar.pageSize,
				firstload: 'true'
			}
		});
		return result;
	}
	var userGridObj = createUserGridObj();
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			title: '<span class="commoncss">用户</span>',
			collapsible: true,
			width: 210,
			minSize: 160,
			maxSize: 280,
			split: true,
			region: 'west',
			autoScroll: true,
			margins: '3 3 3 3',
			items: [userGridObj.grid]
		}, {
			region: 'center',
			layout: 'fit',
			border: false,
			margins: '3 3 3 3',
			items: [userGridObj.grid]
		}]
	});

	/**
	 * 根据条件查询实时查看
	 */
	function queryUserName() {
		userGridObj.store.load({
			params: {
				start: 0,
				limit: userGridObj.pageToolbar.pageSize,
				userName: Ext.getCmp('userName').getValue()
			}
		});
	}

});