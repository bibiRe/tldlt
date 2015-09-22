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

	function infoTypeColumnRender(value, meta, record) {
		var result = value;
		switch (value) {
			case 1:
				result = "上报GPS-人员";
				break;
			case 2:
				result = "上报GPS-设备";
				break;
			case 3:
				result = "上报GPS-巡检";
				break;
			case 4:
				result = "巡检设备";
				break;
			case 5:
				result = "上报信息";
				break;
		}
		return result;
	}

	function infoColumnRender(value, meta, record) {
		var result = value;
		if (null != value) {
			var data = record.data;
			switch (data.type) {
				case 1:
				case 2:
				case 3:
					result = "上报GPS,经度：" + value.longtitude + ",纬度：" + value.latitude + ",速度：" + value.speed;
					break;
				case 4:
					result = value.username + "按照" + value.planname + "完成" + value.devicename + "巡检," + (1 == value.inspectstate) ? "设备正常" : "设备异常" + "," + value.remark
					break;
				case 5:
					result = "内容：" + value.remark;
					break;
			}
		}
		return result;
	}

	function createInfoColumnModel() {
		return new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
			header: '类型',
			dataIndex: 'type',
			width: 50,
			sortable: true,
			renderer: infoTypeColumnRender
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
			renderer: infoColumnRender
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
	
	function faultInfoReporterColumnRender(value, meta, record) {
		var data = record.data;
		var result = data.deptname + data.username;		
		return result;
	}
	
	function createFaultInfoColumnModel() {
		return new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
			header: '上报人',			
			width: 130,
			sortable: true,
			renderer: faultInfoReporterColumnRender
		}, {
			header: '时间',
			dataIndex: 'time',
			width: 100,
			dateFormat: 'Y-m-d H:i:s'
		}, {
			header: '设备',
			dataIndex: 'devicename',
			width: 100
		}, {
			header: '状态',
			dataIndex: 'state',
			width: 50,
			renderer: EXTUXAPP.AppTools.DeviceFaultStateRender
		}, {
			id: 'faultinfo',
			header: '信息',
			dataIndex: 'faultinfo'			
		}]);
	}
	
	function createFaultInfoStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: './inspectView.do?reqCode=queryFaultInfoForManage'
			}),
			reader: new Ext.data.JsonReader({
				totalProperty: 'TOTALCOUNT',
				root: 'ROOT'
			}, [{			
				name: 'devicefaultinfoid'
			}, {			
				name: 'reporter'
			}, {			
				name: 'devicename'
			}, {			
				name: 'username'
			}, {			
				name: 'deptname'
			}, {
				name: 'time'
			}, {
				name: 'state'
			}, {
				name: 'longtitude'
			}, {
				name: 'latitude'
			}, {
				name: 'faultinfo'
			}])
		});
	}
	
	function createFaultInfoGrid(store, columnModel, pageToolbar) {
		var result = new Ext.grid.GridPanel({
			title: '<span class="commoncss">故障信息</span>',
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
			autoExpandColumn: 'faultinfo',
			cm: columnModel,
			tbar: [new Ext.form.TextField({
				id: 'faultInfoName',
				name: 'faultInfoName',
				emptyText: '请输入名称',
				enableKeyEvents: true,
				listeners: {
					specialkey: function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							queryFaultInfoName();
						}
					}
				},
				width: 130
			}), {
				text: '查询',
				iconCls: 'previewIcon',
				handler: function() {
					queryFaultInfoName();
				}
			}, '-', {
				text: '刷新',
				iconCls: 'arrow_refreshIcon',
				handler: function() {
					faultInfoGridObj.store.reload();
				}
			}],
			bbar: pageToolbar
		});
		result.on('rowdblclick', function(grid, rowIndex, event) {
			queryFaultInfoContent(grid);
		});
		return result;
	}
	
	function createInfoTabs(infoGrid, faultGrid) {
		var result = new Ext.TabPanel({
			region: 'center',
			margins: '3 3 3 3',
			enableTabScroll: true,
			//autoWidth : true,
			height: 400
			
		});
		// 每一个Tab都可以看作为一个Panel
		result.add({
			id: 'tabInfoBasic',
			title: '<span class="commoncss">信息</span>',
			items:[infoGridObj.grid],
			iconCls: 'book_previousIcon', // 图标
		});
		result.add({
			id: 'tabInfoFault',
			title: '<span class="commoncss">故障</span>',
			iconCls: 'exclamationIcon', // 图标
			items:[faultInfoGridObj.grid]
		});
		result.activate(0);
		return result;
	}
	
	var userGridObj = new Ext.ux.app.grid.AppGridObj(createUserColumnModel, createUserStore,
		createUserGrid);
	var infoGridObj = new Ext.ux.app.grid.AppGridObj(createInfoColumnModel, createInfoStore,
		createInfoGrid);
	var faultInfoGridObj = new Ext.ux.app.grid.AppGridObj(createFaultInfoColumnModel, createFaultInfoStore,
			createFaultInfoGrid);
	var infoTabs = createInfoTabs();
	var mapObj = new Ext.ux.app.InspectViewMap();

	function userCheckInterval() {
		if (!mapObj) {
			return;
		}
		var datas = mapObj.getUserGPSRecord();
		if (datas) {
			for (var i = 0; i < datas.length; i++) {
				queryUserDataStatus(datas[i], false);
			}
		}
	}

	var userTimer = 5000;
	setInterval(userCheckInterval, userTimer);
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
				height: 300,
				autoScroll: true,
				border: false,
				items: [userGridObj.grid]
			}, {
				region: 'center',
				split: true,
				autoScroll: true,
				border: false,
				items: [infoTabs]
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
				infoName: Ext.getCmp('infoName').getValue()
			}
		});
	}

	/**
	 * 根据条件查询实时查看信息
	 */
	function queryFaultInfoName() {
		faultInfoGridObj.store.load({
			params: {
				start: 0,
				limit: faultInfoGridObj.gridBottomBar.getPageSize(),
				infoName: Ext.getCmp('faultInfoName').getValue()
			}
		});
	}
	
	function queryUserDataStatus(userData, create) {
		Ext.Ajax.request({
			url: './inspectView.do?reqCode=queryUserLastGPSInfo',
			params: {
				userid: userData.userid
			},
			success: function(resp) {
				var respData = Ext.util.JSON.decode(resp.responseText);
				var datas = respData.data;
				if (null != datas) {
					var dt1 = new Array();
					for (var i = 0; i < datas.length; i++) {
						var item = datas[i];
						if (item.time == userData.time) {
							continue;
						}
						if (item) {
							item.userid = userData.userid;
							item.deptid = userData.deptid;
							item.deptname = userData.deptname;
							dt1.push(item);
						}
					}
					mapObj.showUserGPSInfo(dt1, create);
				}
			},
			failure: function(resp) {}
		});
	}

	function queryUserStatus(grid) {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			return;
		}
		var userData = record.data;
		queryUserDataStatus(userData, true);
	}

	function queryInfoContent(grid) {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			return;
		}
		mapObj.showInspectInfo(record.data);
	}

	function queryFaultInfoContent(grid) {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			return;
		}
		mapObj.showFaultInfo(record.data);
	}	
});