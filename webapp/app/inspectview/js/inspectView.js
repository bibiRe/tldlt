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
			header: '性别',
			dataIndex: 'sex',
			width: 60,
			renderer: SEXRender
		}, {
			id: 'deptname',
			header: '所属部门',
			dataIndex: 'deptname',
		}]);
	}

	function createUserStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: './inspectView.do?reqCode=queryUserForManage'
			}),
			reader: new Ext.data.JsonReader({
				totalProperty: 'TOTALCOUNT',
				root: 'ROOT'
			}, [{
				name: 'userid'
			}, {
				name: 'username'
			}, {
				name: 'sex'
			}, {
				name: 'deptid'
			}, {
				name: 'deptname'
			}])
		});
	}

	function createUserGrid(store, columnModel, pageToolbar) {
		var result = new Ext.grid.GridPanel({
			title: '<span class="commoncss">用户信息</span>',
			height: 300,
			// width:600,
			autoScroll: true,
			region: 'center',
			store: store,
			loadMask: {
				msg: '正在加载表格数据,请稍等...'
			},
			stripeRows: true,
			frame: true,
			autoExpandColumn: 'deptname',
			cm: columnModel,
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
		result.on('rowdblclick', function(grid, rowIndex, event) {
			queryUserStatus(grid);
		});
		return result;
	}

	function createInfoColumnModel() {
		return new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
			header: '类型',
			dataIndex: 'type',
			width: 50,
			sortable: true,
			renderer: function(value, meta, record) {
				var result = value;
				switch (value) {
					case 1:
						result = "人员上报GPS";
						break;
					case 2:
						result = "设备上报GPS";
						break;
					case 3:
						result = "巡检上报GPS";
						break;
				}
				return result;
			}
		}, {
			header: '名称',
			dataIndex: 'name',
			width: 130,
			sortable: true
		}, {
			header: '时间',
			dataIndex: 'time',
			width: 100,
			dateFormat: 'Y-m-d H:i:s'
		}, {
			id: 'info',
			header: '信息',
			dataIndex: 'info',
			renderer: function(value, meta, record) {
				if (null != value) {
					var data = record.data;
					if (data.type >= 1 && data.type <= 3) {
						value = "上报GPS,经度：" + value.longtitude + ",纬度：" + value.latitude + ",速度：" + value.speed;
					}
				}
				return value;
			}
		}]);
	}

	function createInfoStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: './inspectView.do?reqCode=queryInfoForManage'
			}),
			reader: new Ext.data.JsonReader({
				totalProperty: 'TOTALCOUNT',
				root: 'ROOT'
			}, [{
				name: 'name'
			}, {
				name: 'type'
			}, {
				name: 'time'
			}, {
				name: 'info'
			}])
		});
	}

	function createInfoGrid(store, columnModel, pageToolbar) {
		var result = new Ext.grid.GridPanel({
			title: '<span class="commoncss">巡检信息</span>',
			height: 300,
			// width:600,
			autoScroll: true,
			region: 'center',
			store: store,
			loadMask: {
				msg: '正在加载表格数据,请稍等...'
			},
			stripeRows: true,
			frame: true,
			autoExpandColumn: 'info',
			cm: columnModel,
			tbar: [new Ext.form.TextField({
				id: 'infoName',
				name: 'infoName',
				emptyText: '请输入名称',
				enableKeyEvents: true,
				listeners: {
					specialkey: function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							queryInfoName();
						}
					}
				},
				width: 130
			}), {
				text: '查询',
				iconCls: 'previewIcon',
				handler: function() {
					queryInfoName();
				}
			}, '-', {
				text: '刷新',
				iconCls: 'arrow_refreshIcon',
				handler: function() {
					infoGridObj.store.reload();
				}
			}],
			bbar: pageToolbar
		});
		result.on('rowdblclick', function(grid, rowIndex, event) {
			queryInfoContent(grid);
		});
		return result;
	}



	function createGridObj(createColumnModelFunc, createStoreFunc,
		createGridFunc) {
		var result = {};
		result.columnModel = createColumnModelFunc();
		result.store = createStoreFunc();
		result.gridBottomBar = new Ext.ux.grid.BottomBar(result.store);
		result.grid = createGridFunc(result.store, result.columnModel,
			result.gridBottomBar.bbar);
		result.store.load({
			params: {
				start: 0,
				limit: result.gridBottomBar.getPageSize(),
				firstload: 'true'
			}
		});
		return result;
	}
	var userGridObj = createGridObj(createUserColumnModel, createUserStore,
		createUserGrid);
	var infoGridObj = createGridObj(createInfoColumnModel, createInfoStore,
		createInfoGrid);
	var mapObj = new Ext.ux.app.InspectViewMap();
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			collapsible: true,
			width: 410,
			split: true,
			region: 'west',
			autoScroll: true,
			margins: '3 3 3 3',
			layout: 'border',
			title: '信息',
			border: false,
			items: [{
				region: 'north',
				split: true,
				collapsible: true,
				height: 360,
				autoScroll: true,
				border: false,
				items: [userGridObj.grid]
			}, {
				region: 'center',
				split: true,
				autoScroll: true,
				border: false,
				items: [infoGridObj.grid]
			}]
		}, {
			region: 'center',
			layout: 'fit',
			border: false,
			margins: '3 3 3 3',
			items: [mapObj]
		}]
	});

	/**
	 * 根据条件查询实时查看用户
	 */
	function queryUserName() {
		userGridObj.store.load({
			params: {
				start: 0,
				limit: userGridObj.gridBottomBar.getPageSize(),
				userName: Ext.getCmp('userName').getValue()
			}
		});
	}

	/**
	 * 根据条件查询实时查看信息
	 */
	function queryInfoName() {
		infoGridObj.store.load({
			params: {
				start: 0,
				limit: infoGridObj.gridBottomBar.getPageSize(),
				userName: Ext.getCmp('infoName').getValue()
			}
		});
	}

	function queryUserStatus(grid) {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			return;
		}
		var userData = record.data;
		Ext.Ajax.request({
			url: './inspectView.do?reqCode=queryUserLastGPSInfo',
			params: {
				userid: userData.userid
			},
			success: function(resp) {
				var respData = Ext.util.JSON.decode(resp.responseText);
				var datas = respData.data;
				if (null != datas) {
					for (var i = 0; i < datas.length; i++) {
						var item = datas[i];
						if (item) {
							item.userid = userData.userid;
							item.deptid = userData.deptid;
							item.deptname = userData.deptname;
						}
					}
					mapObj.showUserGPSInfo(datas);
				}
			},
			failure: function(resp) {
			}
		});
	}
	
	function queryInfoContent(grid) {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			return;
		}
		mapObj.showInspectInfo(record.data);
	}
});