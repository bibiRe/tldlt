/**
 * 区域管理
 *
 * @author hhe
 * @since 2015-07-13
 */
Ext.onReady(function() {
	function createRootAsyncTreeName() {
		return new Ext.tree.AsyncTreeNode({
			text: '区域树',
			expanded: true,
			id: 0
		});
	}

	var regionTree = new Ext.tree.TreePanel({
		loader: new Ext.tree.TreeLoader({
			baseAttrs: {},
			dataUrl: './region.do?reqCode=regionTreeInit'
		}),
		root: createRootAsyncTreeName(),
		title: '',
		applyTo: 'regionTreeDiv',
		autoScroll: false,
		animate: false,
		useArrows: false,
		border: false
	});
	
	if (regionTree.root) {
		regionTree.root.select();
	}
	regionTree.on('click', function(node) {
		store.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				regionId: node.attributes.id
			}
		});
	});

	var contextMenu = new Ext.menu.Menu({
		id: 'regionTreeContextMenu',
		items: [{
			text: '新增区域',
			iconCls: 'page_addIcon',
			handler: function() {
				addInit();
			}
		}, {
			text: '修改区域',
			iconCls: 'page_edit_1Icon',
			handler: function() {
				editInit();
			}
		}, {
			text: '删除区域',
			iconCls: 'page_delIcon',
			handler: function() {
				var selectModel = regionTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				deleteRegionItems('2', selectNode.attributes.id);
			}
		}, {
			text: '刷新节点',
			iconCls: 'page_refreshIcon',
			handler: function() {
				var selectModel = regionTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				if (selectNode.attributes.leaf) {
					selectNode.parentNode.reload();
				} else {
					selectNode.reload();
				}
			}
		}]
	});
	regionTree.on('contextmenu', function(node, e) {
		e.preventDefault();
		var regionId = node.attributes.id;
		name = node.attributes.text;
		Ext.getCmp('parentRegionname').setValue(name);
		Ext.getCmp('parentId').setValue(regionId);
		store.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				regionId: regionId
			},
			callback: function(r, options, success) {
				for (var i = 0; i < r.length; i++) {
					var record = r[i];
					var regionid_g = record.data.regionId;
					if (regionid_g == regionId) {
						grid.getSelectionModel().selectRow(i);
					}
				}
			}
		});
		node.select();
		contextMenu.showAt(e.getXY());
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), sm, {
		header: '区域名称',
		dataIndex: 'name',
		width: 130
	}, {
		header: '上级区域',
		dataIndex: 'parentRegionname',
		width: 130
	}, {
		header: '节点类型',
		dataIndex: 'type',
		hidden: true,
	}, {
		id: 'parentId',
		header: '父节点编号',
		hidden: true,
		dataIndex: 'parentId'
	}, {
		header: '区域编号',
		dataIndex: 'regionId',
		hidden: false,
		hidden: false,
		width: 130,
		sortable: true
	}, {
		id: 'remark',
		header: '备注',
		dataIndex: 'remark'
	}]);

	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: './region.do?reqCode=query'
		}),
		reader: new Ext.data.JsonReader({
			totalProperty: 'TOTALCOUNT',
			root: 'ROOT'
		}, [{
			name: 'regionId'
		}, {
			name: 'name'
		}, {
			name: 'parentRegionName'
		}, {
			name: 'type'
		}, {
			name: 'remark'
		}, {
			name: 'parentId'
		}, {
			name: 'departmentId'
		}])
	});

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
		this.baseParams = {
			regionName: Ext.getCmp('regionName').getValue()
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
		store: store,
		displayInfo: true,
		displayMsg: '显示{0}条到{1}条,共{2}条',
		emptyMsg: "没有符合条件的记录",
		items: ['-', '&nbsp;&nbsp;', pagesize_combo]
	});
	var grid = new Ext.grid.GridPanel({
		title: '<span class="commoncss">区域信息表</span>',
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
		cm: cm,
		sm: sm,
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
				deleteRegionItems('1', '');
			}
		}, '->', new Ext.form.TextField({
			id: 'regionName',
			name: 'regionName',
			emptyText: '请输入区域名称',
			enableKeyEvents: true,
			listeners: {
				specialkey: function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						queryRegionItem();
					}
				}
			},
			width: 130
		}), {
			text: '查询',
			iconCls: 'previewIcon',
			handler: function() {
				queryRegionItem();
			}
		}, '-', {
			text: '刷新',
			iconCls: 'arrow_refreshIcon',
			handler: function() {
				store.reload();
			}
		}],
		bbar: bbar
	});

	store.load({
		params: {
			start: 0,
			limit: bbar.pageSize,
			firstload: 'true'
		}
	});

	grid.on('rowdblclick', function(grid, rowIndex, event) {
		editInit();
	});

	function createDepartmentCombobox() {
		var addRoot = new Ext.tree.AsyncTreeNode({
			text: '部门树',
			expanded: true,
			id: '001'
		});
		var addDeptTree = new Ext.tree.TreePanel({
			loader: new Ext.tree.TreeLoader({
				baseAttrs: {},
				dataUrl: 'treeDemo.do?reqCode=departmentTreeInit'
			}),
			root: addRoot,
			autoScroll: true,
			animate: false,
			useArrows: false,
			border: false
		});
		// 监听下拉树的节点单击事件
		addDeptTree.on('click', function(node) {
			comboxWithTree.setValue(node.text);
			Ext.getCmp("addRegionFormPanel").findById('parentId').setValue(
				node.attributes.id);
			comboxWithTree.collapse();
		});
		var result = new Ext.form.ComboBox({
			id: 'parentdeptname',
			store: new Ext.data.SimpleStore({
				fields: [],
				data: [
					[]
				]
			}),
			editable: false,
			value: ' ',
			emptyText: '请选择...',
			fieldLabel: '上级部门',
			anchor: '100%',
			mode: 'local',
			triggerAction: 'all',
			maxHeight: 390,
			// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
			tpl: "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv'></div></div></tpl>",
			allowBlank: false,
			onSelect: Ext.emptyFn
		});

		// 监听下拉框的下拉展开事件
		result.on('expand', function() {
			// 将UI树挂到treeDiv容器
			addDeptTree.render('addDeptTreeDiv');
			// addDeptTree.root.expand(); //只是第一次下拉会加载数据
			addDeptTree.root.reload(); // 每次下拉都会加载数据

		});
		return result;
	}

	function createParentRegionCombox() {
		var addRegionTree = new Ext.tree.TreePanel({
			loader: new Ext.tree.TreeLoader({
				baseAttrs: {},
				dataUrl: './region.do?reqCode=regionTreeInit'
			}),
			root: createRootAsyncTreeName(),
			autoScroll: true,
			animate: false,
			useArrows: false,
			border: false
		});
		// 监听下拉树的节点单击事件
		addRegionTree.on('click', function(node) {
			regionComboxWithTree.setValue(node.text);
			Ext.getCmp("addRegionFormPanel").findById('parentId').setValue(
				node.attributes.id);
			regionComboxWithTree.collapse();
		});
		var result = new Ext.form.ComboBox({
			id: 'parentRegionName',
			store: new Ext.data.SimpleStore({
				fields: [],
				data: [
					[]
				]
			}),
			editable: false,
			value: ' ',
			emptyText: '请选择...',
			fieldLabel: '上级区域',
			anchor: '100%',
			mode: 'local',
			triggerAction: 'all',
			labelStyle: micolor,
			maxHeight: 390,
			// 下拉框的显示模板,addRegionTreeDiv作为显示下拉树的容器
			tpl: "<tpl for='.'><div style='height:390px'><div id='addRegionTreeDiv'></div></div></tpl>",
			allowBlank: false,
			onSelect: Ext.emptyFn
		});

		// 监听下拉框的下拉展开事件
		result.on('expand', function() {
			// 将UI树挂到treeDiv容器
			addRegionTree.render('addRegionTreeDiv');
			// addRegionTree.root.expand(); //只是第一次下拉会加载数据
			addRegionTree.root.reload(); // 每次下拉都会加载数据

		});
		return result;
	}

	var regionComboxWithTree = createParentRegionCombox();

	var regionTypeCombo = new Ext.form.ComboBox({
		name: 'type',
		hiddenName: 'type',
		store: REGIONTYPEStore,
		mode: 'local',
		triggerAction: 'all',
		valueField: 'value',
		displayField: 'text',
		value: '1',
		fieldLabel: '区域类型',
		emptyText: '请选择...',
		allowBlank: false,
		labelStyle: micolor,
		forceSelection: true,
		editable: false,
		typeAhead: true,
		readOnly: true,
		anchor: "99%"
	});


	var addRegionFormPanel = new Ext.form.FormPanel({
		id: 'addRegionFormPanel',
		name: 'addRegionFormPanel',
		defaultType: 'textfield',
		labelAlign: 'right',
		labelWidth: 70,
		frame: false,
		bodyStyle: 'padding:5 5 0',
		items: [{
				fieldLabel: '区域名称',
				name: 'name',
				id: 'name',
				allowBlank: false,
				labelStyle: micolor,
				anchor: '99%'
			},
			regionTypeCombo, createDepartmentCombobox(), {
				fieldLabel: '备注',
				name: 'remark',
				allowBlank: true,
				anchor: '99%'
			}, {
				id: 'parentId',
				name: 'parentId',
				hidden: true
			}, {
				id: 'regionId',
				name: 'regionId',
				hidden: true
			}, {
				id: 'parentid_old',
				name: 'parentid_old',
				hidden: true
			}, {
				id: 'departmentId',
				name: 'departmentId',
				hidden: true
			}, {
				id: 'windowmode',
				name: 'windowmode',
				hidden: true
			}
		]
	});

	var addRegionWindow = new Ext.Window({
		layout: 'fit',
		width: 400,
		height: 215,
		resizable: false,
		draggable: true,
		closable: true,
		modal: true,
		closeAction: 'hide',
		title: '<span class="commoncss">新增区域</span>',
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
		items: [addRegionFormPanel],
		buttons: [{
			text: '保存',
			iconCls: 'acceptIcon',
			id: 'btn_id_save_update',
			handler: function() {
				if (runMode == '0') {
					Ext.Msg.alert('提示',
						'系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
					return;
				}
				var mode = Ext.getCmp('windowmode').getValue();
				if (mode == 'add')
					addRegionItem();
				else
					updateRegionItem();
			}
		}, {
			text: '重置',
			id: 'btnReset',
			iconCls: 'tbar_synchronizeIcon',
			handler: function() {
				clearForm(addRegionFormPanel.getForm());
			}
		}, {
			text: '关闭',
			iconCls: 'deleteIcon',
			handler: function() {
				addRegionWindow.hide();
			}
		}]
	});
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			title: '<span class="commoncss">组织机构</span>',
			iconCls: 'chart_organisationIcon',
			tools: [{
				id: 'refresh',
				handler: function() {
					regionTree.root.reload()
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
			items: [regionTree]
		}, {
			region: 'center',
			layout: 'fit',
			border: false,
			margins: '3 3 3 3',
			items: [grid]
		}]
	});

	/**
	 * 根据条件查询区域
	 */
	function queryRegionItem() {
		store.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				queryParam: Ext.getCmp('queryParam').getValue()
			}
		});
	}

	/**
	 * 新增区域初始化
	 */
	function addInit(childFlag) {
		Ext.getCmp('btnReset').hide();
		// clearForm(addRegionFormPanel.getForm());
		var flag = Ext.getCmp('windowmode').getValue();
		if (typeof(flag) != 'undefined') {
			addRegionFormPanel.form.getEl().dom.reset();
		} else {
			clearForm(addRegionFormPanel.getForm());
		}
		var selectModel = regionTree.getSelectionModel();
		var selectNode = selectModel.getSelectedNode();
		if (null != selectNode) {
			if (!childFlag) {
				selectNode = selectNode.parentNode;
			}
		}
		if (null != selectNode) {
			Ext.getCmp('parentRegionName').setValue(selectNode.attributes.text);
			Ext.getCmp('parentId').setValue(selectNode.attributes.id);
		} else {
			Ext.getCmp('parentRegionName').setValue("");
			Ext.getCmp('parentId').setValue("");
		}
		addRegionWindow.show();
		addRegionWindow
			.setTitle('<span class="commoncss">新增区域</span>');
		Ext.getCmp('windowmode').setValue('add');
		regionComboxWithTree.setDisabled(false);
		//Ext.getCmp('btnReset').show();
	}

	/**
	 * 保存区域数据
	 */
	function addRegionItem() {
		if (!addRegionFormPanel.form.isValid()) {
			return;
		}
		addRegionFormPanel.form.submit({
			url: './region.do?reqCode=saveAddInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				addRegionWindow.hide();
				store.reload();
				refreshNode(Ext.getCmp('parentId').getValue());
				form.reset();
				Ext.MessageBox.alert('提示', action.result.msg);
			},
			failure: function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.alert('提示', '区域数据保存失败:<br>' + msg);
			}
		});
	}

	/**
	 * 刷新指定节点
	 */
	function refreshNode(nodeid) {
		var node = regionTree.getNodeById(nodeid);
		/* 异步加载树在没有展开节点之前是获取不到对应节点对象的 */
		if (Ext.isEmpty(node)) {
			regionTree.root.reload();
			return;
		}
		if (node.attributes.leaf) {
			node.parentNode.reload();
		} else {
			node.reload();
		}
	}

	/**
	 * 修改区域初始化
	 */
	function editInit() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.alert('提示', '请先选择要修改的区域!');
			return;
		}
		record = grid.getSelectionModel().getSelected();
		if (record.get('leaf') == '0' || record.get('usercount') != '0' || record.get('rolecount') != '0') {
			regionComboxWithTree.setDisabled(true);
		} else {
			regionComboxWithTree.setDisabled(false);
		}
		if (record.get('regionId') == '001') {
			var a = Ext.getCmp('parentRegionname');
			a.emptyText = '已经是顶级区域';
		} else {}
		addRegionFormPanel.getForm().loadRecord(record);
		addRegionWindow.show();
		addRegionWindow
			.setTitle('<span style="font-weight:normal">修改区域</span>');
		Ext.getCmp('windowmode').setValue('edit');
		Ext.getCmp('parentid_old').setValue(record.get('parentId'));
		Ext.getCmp('btnReset').hide();
	}

	/**
	 * 修改区域数据
	 */
	function updateRegionItem() {
		if (!addRegionFormPanel.form.isValid()) {
			return;
		}
		update();
	}

	/**
	 * 更新
	 */
	function update() {
		var parentId = Ext.getCmp('parentId').getValue();
		var parentid_old = Ext.getCmp('parentid_old').getValue();
		addRegionFormPanel.form.submit({
			url: './region.do?reqCode=saveUpdateInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				addRegionWindow.hide();
				store.reload();
				refreshNode(parentId);
				if (parentId != parentid_old) {
					refreshNode(parentid_old);
				}
				form.reset();
				Ext.MessageBox.alert('提示', action.result.msg);
			},
			failure: function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.alert('提示', '区域数据修改失败:<br>' + msg);
			}
		});
	}

	/**
	 * 删除区域
	 */
	function deleteRegionItems(pType, pRegionid) {
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].get('regionId') == '1') {
				fields = fields + rows[i].get('name') + '<br>';
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
		var strChecked = jsArray2JsString(rows, 'regionId');
		Ext.Msg
			.confirm(
				'请确认',
				'<span style="color:red"><b>提示:</b>删除区域将同时删除区域相关信息,请慎重.</span><br>继续删除吗?',
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
								url: './region.do?reqCode=delete',
								success: function(response) {
									var resultArray = Ext.util.JSON
										.decode(response.responseText);
									store.reload();
									if (pType == '1') {
										regionTree.root.reload();
									} else {
										regionTree.root.reload();
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
									strChecked: strChecked,
									type: pType,
									regionId: pRegionid
								}
							});
					}
				});
	}

});