/**
 * 设备类型
 *
 * @author hhe
 * @since 2015-07-13
 */
Ext.onReady(function() {
	function createRootNode() {
		return new Ext.tree.AsyncTreeNode({
			text: '设备类型树',
			expanded: true,
			id: '0'
		});
	}

	function createTreeLoader() {
		return new Ext.tree.TreeLoader({
			baseAttrs: {},
			dataUrl: './deviceType.do?reqCode=deviceTypeTreeInit'
		});
	}

	var deviceTypeTree = new Ext.tree.TreePanel({
		loader: createTreeLoader(),
		root: createRootNode(),
		title: '',
		applyTo: 'deviceTypeTreeDiv',
		autoScroll: false,
		animate: false,
		useArrows: false,
		border: false
	});
	deviceTypeTree.root.select();
	deviceTypeTree.on('click', function(node) {
		var deviceTypeId = node.attributes.id;
		deviceTypeStore.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				devicetypeid: deviceTypeId
			}
		});
	});

	var contextMenu = new Ext.menu.Menu({
		id: 'deviceTypeTreeContextMenu',
		items: [{
			text: '新增设备类型',
			iconCls: 'page_addIcon',
			handler: function() {
				addInit();
			}
		}, {
			text: '修改设备类型',
			iconCls: 'page_edit_1Icon',
			handler: function() {
				editInit();
			}
		}, {
			text: '删除设备类型',
			iconCls: 'page_delIcon',
			handler: function() {
				deleteItems('2');
			}
		}, {
			text: '刷新节点',
			iconCls: 'page_refreshIcon',
			handler: function() {
				var selectModel = deviceTypeTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				if (selectNode.attributes.leaf) {
					selectNode.parentNode.reload();
				} else {
					selectNode.reload();
				}
			}
		}]
	});
	deviceTypeTree.on('contextmenu', function(node, e) {
		e.preventDefault();
		var devicetypeid = node.attributes.id;
		var deviceTypename = node.attributes.text;
		Ext.getCmp('parentdevicetypename').setValue(deviceTypename);
		Ext.getCmp('parentid').setValue(devicetypeid);
		deviceTypeStore.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				devicetypeid: devicetypeid
			},
			callback: function(r, options, success) {
				for (var i = 0; i < r.length; i++) {
					var record = r[i];
					var deviceTypeid_g = record.data.devicetypeid;
					if (deviceTypeid_g == devicetypeid) {
						deviceTypeInfoGrid.getSelectionModel().selectRow(i);
					}
				}
			}
		});
		node.select();
		contextMenu.showAt(e.getXY());
	});


	var deviceTypeGridSM = new Ext.grid.CheckboxSelectionModel();
	var deviceTypeCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), deviceTypeGridSM, {
		header: '设备类型编号',
		dataIndex: 'devicetypeid',
		hidden: false,
		hidden: false,
		width: 130,
		sortable: true
	}, {
		header: '设备类型名称',
		dataIndex: 'devicetypename',
		width: 130
	}, {
		header: '上级设备类型',
		dataIndex: 'parentdevicetypename',
		width: 130
	}, {
		id: 'parentid',
		header: '父节点编号',
		hidden: true,
		dataIndex: 'parentid'
	}, {
		id: 'remark',
		header: '备注',
		dataIndex: 'remark'
	}]);

	/**
	 * 数据存储
	 */
	var deviceTypeStore = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: './deviceType.do?reqCode=queryDeviceTypesForManage'
		}),
		reader: new Ext.data.JsonReader({
			totalProperty: 'TOTALCOUNT',
			root: 'ROOT'
		}, [{
			name: 'devicetypeid'
		}, {
			name: 'devicetypename'
		}, {
			name: 'parentdevicetypename'
		}, {
			name: 'remark'
		}, {
			name: 'parentid'
		}, {
			name: 'leaf'
		}])
	});

	// 翻页排序时带上查询条件
	deviceTypeStore.on('beforeload', function() {
		this.baseParams = {
			deviceTypeName: Ext.getCmp('deviceTypeName').getValue()
		};
	});

	var pagesize_combo = new Ext.form.ComboBox({
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
	var number = parseInt(pagesize_combo.getValue());
	pagesize_combo.on("select", function(comboBox) {
		bbar.pageSize = parseInt(comboBox.getValue());
		number = parseInt(comboBox.getValue());
		store.reload({
			params: {
				start: 0,
				limit: bbar.pageSize
			}
		});
	});

	var bbar = new Ext.PagingToolbar({
		pageSize: number,
		store: deviceTypeStore,
		displayInfo: true,
		displayMsg: '显示{0}条到{1}条,共{2}条',
		emptyMsg: "没有符合条件的记录",
		items: ['-', '&nbsp;&nbsp;', pagesize_combo]
	});
	var deviceTypeInfoGrid = new Ext.grid.GridPanel({
		title: '<span class="commoncss">设备类型信息表</span>',
		height: 500,
		// width:600,
		autoScroll: true,
		region: 'center',
		store: deviceTypeStore,
		loadMask: {
			msg: '正在加载表格数据,请稍等...'
		},
		stripeRows: true,
		frame: true,
		autoExpandColumn: 'remark',
		cm: deviceTypeCM,
		sm: deviceTypeGridSM,
		tbar: [{
			text: '新增',
			iconCls: 'page_addIcon',
			handler: function() {
				addInit();
			}
		}, '-', {
			text: '修改',
			iconCls: 'page_edit_1Icon',
			handler: function() {
				editInit();
			}
		}, '-', {
			text: '删除',
			iconCls: 'page_delIcon',
			handler: function() {
				deleteItems('1');
			}
		}, '->', new Ext.form.TextField({
			id: 'deviceTypeName',
			name: 'deviceTypeName',
			emptyText: '请输入设备类型名称',
			enableKeyEvents: true,
			listeners: {
				specialkey: function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						queryDeviceTypeItem();
					}
				}
			},
			width: 130
		}), {
			text: '查询',
			iconCls: 'previewIcon',
			handler: function() {
				queryDeviceTypeItem();
			}
		}, '-', {
			text: '刷新',
			iconCls: 'arrow_refreshIcon',
			handler: function() {
				deviceTypeStore.reload();
			}
		}],
		bbar: bbar
	});

	deviceTypeStore.load({
		params: {
			start: 0,
			limit: bbar.pageSize,
			firstload: 'true'
		}
	});

	deviceTypeInfoGrid.on('rowdblclick', function(deviceTypeInfoGrid, rowIndex, event) {
		editInit();
	});
	deviceTypeInfoGrid.on('sortchange', function() {
		// deviceTypeInfoGrid.getSelectionModel().selectFirstRow();
	});

	bbar.on("change", function() {
		// deviceTypeInfoGrid.getSelectionModel().selectFirstRow();
	});

	var addDeviceTypeTreePanel = new Ext.tree.TreePanel({
		loader: createTreeLoader(),
		root: createRootNode(),
		autoScroll: true,
		animate: false,
		useArrows: false,
		border: false
	});
	// 监听下拉树的节点单击事件
	addDeviceTypeTreePanel.on('click', function(node) {
		parentDeviceTypeComboxWithTree.setValue(node.text);
		Ext.getCmp("addDeviceTypeFormPanel").findById('parentid').setValue(
			node.attributes.id);
		parentDeviceTypeComboxWithTree.collapse();
	});
	var parentDeviceTypeComboxWithTree = new Ext.form.ComboBox({
		id: 'parentdevicetypename',
		store: new Ext.data.SimpleStore({
			fields: [],
			data: [
				[]
			]
		}),
		editable: false,
		value: ' ',
		emptyText: '请选择...',
		fieldLabel: '上级设备类型',
		anchor: '100%',
		mode: 'local',
		triggerAction: 'all',
		labelStyle: micolor,
		maxHeight: 390,
		// 下拉框的显示模板,addDeviceTypeTreeDiv作为显示下拉树的容器
		tpl: "<tpl for='.'><div style='height:390px'><div id='addDeviceTypeTreeDiv'></div></div></tpl>",
		allowBlank: false,
		onSelect: Ext.emptyFn
	});
	// 监听下拉框的下拉展开事件
	parentDeviceTypeComboxWithTree.on('expand', function() {
		// 将UI树挂到treeDiv容器
		addDeviceTypeTreePanel.render('addDeviceTypeTreeDiv');
		// addDeviceTypeTreePanel.root.expand(); //只是第一次下拉会加载数据
		addDeviceTypeTreePanel.root.reload(); // 每次下拉都会加载数据

	});


	var addDeviceTypeFormPanel = new Ext.form.FormPanel({
		id: 'addDeviceTypeFormPanel',
		name: 'addDeviceTypeFormPanel',
		defaultType: 'textfield',
		labelAlign: 'right',
		labelWidth: 80,
		frame: false,
		bodyStyle: 'padding:5 5 0',
		items: [{
			    fieldLabel: '编号',
				name: 'devicetypeid',
				id: 'devicetypeid',
				allowBlank: false,
				labelStyle: micolor,
				anchor: '99%'
		    },{
				fieldLabel: '名称',
				name: 'devicetypename',
				id: 'devicetypename',
				allowBlank: false,
				labelStyle: micolor,
				anchor: '99%'
			},
			parentDeviceTypeComboxWithTree, {
				fieldLabel: '备注',
				name: 'remark',
				allowBlank: true,
				anchor: '99%'
			}, {
				id: 'parentid',
				name: 'parentid',
				hidden: true
			}, {
				id: 'windowmode',
				name: 'windowmode',
				hidden: true
			}, {
				id: 'parentid_old',
				name: 'parentid_old',
				hidden: true
			}, {
				id: 'oid',
				name: 'oid',
				hidden: true
			}
		]
	});

	var addDeviceTypeWindow = new Ext.Window({
		layout: 'fit',
		width: 400,
		height: 215,
		resizable: false,
		draggable: true,
		closable: true,
		modal: true,
		closeAction: 'hide',
		title: '<span class="commoncss">新增设备类型</span>',
		// iconCls : 'page_addIcon',
		collapsible: true,
		titleCollapse: true,
		maximizable: false,
		buttonAlign: 'right',
		border: false,
		animCollapse: true,
		pageY: 20,
		pageX: document.body.clientWidth / 2 - 420 / 2,
		animateTarget: Ext.getBody(),
		constrain: true,
		items: [addDeviceTypeFormPanel],
		buttons: [{
			text: '保存',
			iconCls: 'acceptIcon',
			id: 'btn_id_save_update',
			handler: function() {
				var mode = Ext.getCmp('windowmode').getValue();
				if (mode == 'add')
					addDeviceTypeItem();
				else
					updateDeviceTypeItem();
			}
		}, {
			text: '重置',
			id: 'btnReset',
			iconCls: 'tbar_synchronizeIcon',
			handler: function() {
				clearForm(addDeviceTypeFormPanel.getForm());
			}
		}, {
			text: '关闭',
			iconCls: 'deleteIcon',
			handler: function() {
				addDeviceTypeWindow.hide();
			}
		}]
	});
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			title: '<span class="commoncss">设备类型</span>',
			iconCls: 'chart_organisationIcon',
			tools: [{
				id: 'refresh',
				handler: function() {
					deviceTypeTree.root.reload()
				}
			}],
			collapsible: true,
			width: 210,
			minSize: 160,
			maxSize: 280,
			split: true,
			region: 'west',
			autoScroll: true,
			margins: '3 3 3 3',
			// collapseMode:'mini',
			items: [deviceTypeTree]
		}, {
			region: 'center',
			layout: 'fit',
			border: false,
			margins: '3 3 3 3',
			items: [deviceTypeInfoGrid]
		}]
	});

	/**
	 * 根据条件查询设备类型
	 */
	function queryDeviceTypeItem() {
		deviceTypeStore.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				deviceTypeName: Ext.getCmp('deviceTypeName').getValue()
			}
		});
	}

	/**
	 * 新增设备类型初始化
	 */
	function addInit() {
		Ext.getCmp('btnReset').hide();
		// clearForm(addDeviceTypeFormPanel.getForm());
		var flag = Ext.getCmp('windowmode').getValue();
		if (typeof(flag) != 'undefined') {
			addDeviceTypeFormPanel.form.getEl().dom.reset();
		} else {
			clearForm(addDeviceTypeFormPanel.getForm());
		}
		var selectModel = deviceTypeTree.getSelectionModel();
		var selectNode = selectModel.getSelectedNode();
		if (null != selectNode) {
			Ext.getCmp('parentdevicetypename').setValue(selectNode.attributes.text);
			Ext.getCmp('parentid').setValue(selectNode.attributes.id);
		}
		addDeviceTypeWindow.show();
		addDeviceTypeWindow
			.setTitle('<span class="commoncss">新增设备类型</span>');
		Ext.getCmp('windowmode').setValue('add');
		parentDeviceTypeComboxWithTree.setDisabled(false);
		//Ext.getCmp('btnReset').show();
	}

	/**
	 * 保存设备类型数据
	 */
	function addDeviceTypeItem() {
		if (!addDeviceTypeFormPanel.form.isValid()) {
			return;
		}
		addDeviceTypeFormPanel.form.submit({
			url: './deviceType.do?reqCode=saveAddInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				addDeviceTypeWindow.hide();
				deviceTypeStore.reload();
				refreshNode(Ext.getCmp('parentid').getValue());
				form.reset();
				Ext.MessageBox.alert('提示', action.result.msg);
			},
			failure: function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.alert('提示', '设备类型数据保存失败:<br>' + msg);
			}
		});
	}

	/**
	 * 刷新指定节点
	 */
	function refreshNode(nodeid) {
		var node = deviceTypeTree.getNodeById(nodeid);
		/* 异步加载树在没有展开节点之前是获取不到对应节点对象的 */
		if (Ext.isEmpty(node)) {
			deviceTypeTree.root.reload();
			return;
		}
		if (node.attributes.leaf) {
			node.parentNode.reload();
		} else {
			node.reload();
		}
	}

	/**
	 * 修改设备类型初始化
	 */
	function editInit() {
		var record = deviceTypeInfoGrid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.alert('提示', '请先选择要修改的设备类型!');
			return;
		}
		addDeviceTypeFormPanel.getForm().loadRecord(record);
		if (record.get('devicetypeid') == '0') {
			var a = Ext.getCmp('parentdevicetypename');
			a.emptyText = '已经是顶级设备类型';
		}
		if (!record.get('leaf')) {
			parentDeviceTypeComboxWithTree.setDisabled(true);
		} else {
			parentDeviceTypeComboxWithTree.setDisabled(false);
		}
		if (!record.get('parentid')) {
			parentDeviceTypeComboxWithTree.setValue(deviceTypeTree.root.attributes.text);
		}
		addDeviceTypeWindow.show();
		addDeviceTypeWindow
			.setTitle('<span style="font-weight:normal">修改设备类型</span>');
		Ext.getCmp('windowmode').setValue('edit');
		Ext.getCmp('parentid_old').setValue(record.get('parentid'));
		Ext.getCmp('oid').setValue(record.get('devicetypeid'));
		Ext.getCmp('btnReset').hide();
	}

	/**
	 * 修改设备类型数据
	 */
	function updateDeviceTypeItem() {
		if (!addDeviceTypeFormPanel.form.isValid()) {
			return;
		}
		update();
	}

	/**
	 * 更新
	 */
	function update() {
		var parentid = Ext.getCmp('parentid').getValue();
		var parentid_old = Ext.getCmp('parentid_old').getValue();
		addDeviceTypeFormPanel.form.submit({
			url: './deviceType.do?reqCode=saveUpdateInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				addDeviceTypeWindow.hide();
				deviceTypeStore.reload();
				refreshNode(parentid);
				if (parentid != parentid_old) {
					refreshNode(parentid_old);
				}
				form.reset();
				Ext.MessageBox.alert('提示', action.result.msg);
			},
			failure: function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.alert('提示', '设备类型数据修改失败:<br>' + msg);
			}
		});
	}

	/**
	 * 删除设备类型
	 */
	function deleteItems(pType) {
		var selectModel = deviceTypeTree.getSelectionModel();
		var selectNode = selectModel.getSelectedNode();
		var pDeviceTypeid = 0;
		var pDeviceTypePath = null;
		if (null != selectNode) {
			pDeviceTypeid = selectNode.attributes.id;
			pDeviceTypePath = selectNode.getPath('id');
		}
		var rows = deviceTypeInfoGrid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('devicetypeid') == '0') {
				fields = fields + rows[i].get('devicetypename') + '<br>';
			}
			if (rows[i].get('devicetypeid') == pDeviceTypeid) {
				if (null != selectNode) {
					if (null != selectNode.parentNode) {
						pDeviceTypeid = selectNode.parentNode.attributes.id;
						pDeviceTypePath = selectNode.parentNode.getPath('id');
					}
				}
			}
		}
		if (fields != '') {
			Ext.Msg
				.alert(
					'提示',
					'<b>您选中的项目中包含如下系统内置的只读项目</b><br>' + fields + '<font color=red>只读项目不能删除!</font>');
			return;
		}
		if (Ext.isEmpty(rows)) {
			if (pType == '1') {
				Ext.Msg.alert('提示', '请先选中要删除的项目!');
				return;
			}
		}
		var strChecked = jsArray2JsString(rows, 'devicetypeid');
		Ext.Msg
			.confirm(
				'请确认',
				'<span style="color:red"><b>提示:</b>删除设备类型将同时删除设备类型相关信息,请慎重.</span><br>继续删除吗?',
				function(btn, text) {
					if (btn == 'yes') {
						if (runMode == '0') {
							Ext.Msg
								.alert('提示',
									'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
							return;
						}
						showWaitMsg();
						Ext.Ajax
							.request({
								url: './deviceType.do?reqCode=delete',
								success: function(response) {
									var resultArray = Ext.util.JSON
										.decode(response.responseText);
									deviceTypeStore.reload();

									if (!pDeviceTypePath) {
										deviceTypeTree.root.reload();
									} else {
										deviceTypeTree.getLoader().load(deviceTypeTree.getRootNode(),
											function(treeNode) {
												deviceTypeTree.expandPath(pDeviceTypePath, 'id', function(bSucess, oLastNode) {
													deviceTypeTree.getSelectionModel().select(oLastNode);
												});
											}, this);
									}
									Ext.Msg.alert('提示',
										resultArray.msg);
								},
								failure: function(response) {
									var resultArray = Ext.util.JSON
										.decode(response.responseText);
									Ext.Msg.alert('提示',
										resultArray.msg);
								},
								params: {
									ids: strChecked,
									type: pType,
									devicetypeid: pDeviceTypeid
								}
							});
					}
				});
	}

});