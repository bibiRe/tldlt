/**
 * 区域管理
 *
 * @author hhe
 * @since 2015-07-13
 */
Ext.onReady(function() {
	function createRootNode() {
		return new Ext.tree.AsyncTreeNode({
			text: '区域树',
			expanded: true,
			id: '0'
		});
	}
	function createTreeLoader() {
		return new Ext.tree.TreeLoader({
			baseAttrs: {},
			dataUrl: './region.do?reqCode=regionTreeInit'
		});
	}
	var regionTree = new Ext.tree.TreePanel({
		loader: createTreeLoader(),
		root: createRootNode(),
		title: '',
		applyTo: 'regionTreeDiv',
		autoScroll: false,
		animate: false,
		useArrows: false,
		border: false
	});
	regionTree.root.select();
	regionTree.on('click', function(node) {
		var regionId = node.attributes.id;
		regionStore.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				regionid: regionId
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
		regionid = node.attributes.id;
		regionname = node.attributes.text;
		Ext.getCmp('parentregionname').setValue(regionname);
		Ext.getCmp('parentid').setValue(regionid);
		regionStore.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				regionid: regionid
			},
			callback: function(r, options, success) {
				for (var i = 0; i < r.length; i++) {
					var record = r[i];
					var regionid_g = record.data.regionid;
					if (regionid_g == regionid) {
						grid.getSelectionModel().selectRow(i);
					}
				}
			}
		});
		node.select();
		contextMenu.showAt(e.getXY());
	});

	var regionGridSM = new Ext.grid.CheckboxSelectionModel();
	var regionCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), regionGridSM, {
		header: '区域名称',
		dataIndex: 'regionname',
		width: 130
	}, {
		header: '上级区域',
		dataIndex: 'parentregionname',
		width: 130
	}, {
		header: '类型',
		dataIndex: 'regiontype',
		width: 50,
	}, {
		id: 'parentid',
		header: '父节点编号',
		hidden: true,
		dataIndex: 'parentid'
	}, {
		header: '区域编号',
		dataIndex: 'regionid',
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
	var regionStore = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({
			url: './region.do?reqCode=queryRegionsForManage'
		}),
		reader: new Ext.data.JsonReader({
			totalProperty: 'TOTALCOUNT',
			root: 'ROOT'
		}, [{
			name: 'regionid'
		}, {
			name: 'regionname'
		}, {
			name: 'parentregionname'
		}, {
			name: 'remark'
		}, {
			name: 'parentid'
		}])
	});

	// 翻页排序时带上查询条件
	regionStore.on('beforeload', function() {
		this.baseParams = {
			regionName: Ext.getCmp('regionName').getValue()
		};
	});
	
	
	
	var pagesize_combo = new Ext.form.ComboBox( {
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
	var number = parseInt(pagesize_combo.getValue());
	pagesize_combo.on("select", function(comboBox) {
		bbar.pageSize = parseInt(comboBox.getValue());
		number = parseInt(comboBox.getValue());
		store.reload( {
			params : {
				start : 0,
				limit : bbar.pageSize
			}
		});
	});

	var bbar = new Ext.PagingToolbar( {
		pageSize : number,
		store : regionStore,
		displayInfo : true,
		displayMsg : '显示{0}条到{1}条,共{2}条',
		emptyMsg : "没有符合条件的记录",
		items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
	});
	var grid = new Ext.grid.GridPanel({
		title: '<span class="commoncss">区域信息表</span>',
		height: 500,
		// width:600,
		autoScroll: true,
		region: 'center',
		store: regionStore,
		loadMask: {
			msg: '正在加载表格数据,请稍等...'
		},
		stripeRows: true,
		frame: true,
		autoExpandColumn: 'remark',
		cm: regionCM,
		sm: regionGridSM,
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
				regionStore.reload();
			}
		}],
		bbar: bbar
	});

	regionStore.load({
		params: {
			start: 0,
			limit: bbar.pageSize,
			firstload: 'true'
		}
	});

	grid.on('rowdblclick', function(grid, rowIndex, event) {
		editInit();
	});
	grid.on('sortchange', function() {
		// grid.getSelectionModel().selectFirstRow();
	});

	bbar.on("change", function() {
		// grid.getSelectionModel().selectFirstRow();
	});
	
	var addRegionTree = new Ext.tree.TreePanel({
		loader: createTreeLoader(),
		root: createRootNode(),
		autoScroll: true,
		animate: false,
		useArrows: false,
		border: false
	});
	// 监听下拉树的节点单击事件
	addRegionTree.on('click', function(node) {
		comboxWithTree.setValue(node.text);
		Ext.getCmp("addRegionFormPanel").findById('parentid').setValue(
			node.attributes.id);
		comboxWithTree.collapse();
	});
	var comboxWithTree = new Ext.form.ComboBox({
		id: 'parentregionname',
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
	comboxWithTree.on('expand', function() {
		// 将UI树挂到treeDiv容器
		addRegionTree.render('addRegionTreeDiv');
		// addRegionTree.root.expand(); //只是第一次下拉会加载数据
		addRegionTree.root.reload(); // 每次下拉都会加载数据

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
				name: 'regionname',
				id: 'regionname',
				allowBlank: false,
				labelStyle: micolor,
				anchor: '99%'
			},
			comboxWithTree, {
				fieldLabel: '业务对照码',
				name: 'customid',
				allowBlank: true,
				anchor: '99%'
			}, {
				fieldLabel: '排序号',
				name: 'sortno',
				allowBlank: true,
				anchor: '99%'
			}, {
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
				id: 'regionid',
				name: 'regionid',
				hidden: true
			}, {
				id: 'parentid_old',
				name: 'parentid_old',
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
				var mode = Ext.getCmp('windowmode').getValue();
				if (mode == 'add')
					saveRegionItem();
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
		regionStore.load({
			params: {
				start: 0,
				limit: bbar.pageSize,
				regionName: Ext.getCmp('regionName').getValue()
			}
		});
	}

	/**
	 * 新增区域初始化
	 */
	function addInit() {
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
		Ext.getCmp('parentregionname').setValue(selectNode.attributes.text);
		Ext.getCmp('parentid').setValue(selectNode.attributes.id);
		addRegionWindow.show();
		addRegionWindow
			.setTitle('<span class="commoncss">新增区域</span>');
		Ext.getCmp('windowmode').setValue('add');
		comboxWithTree.setDisabled(false);
		//Ext.getCmp('btnReset').show();
	}

	/**
	 * 保存区域数据
	 */
	function saveRegionItem() {
		if (!addRegionFormPanel.form.isValid()) {
			return;
		}
		addRegionFormPanel.form.submit({
			url: './region.do?reqCode=saveRegionItem',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				addRegionWindow.hide();
				regionStore.reload();
				refreshNode(Ext.getCmp('parentid').getValue());
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
			comboxWithTree.setDisabled(true);
		} else {
			comboxWithTree.setDisabled(false);
		}
		if (record.get('regionid') == '001') {
			var a = Ext.getCmp('parentregionname');
			a.emptyText = '已经是顶级区域';
		} else {}
		addRegionFormPanel.getForm().loadRecord(record);
		addRegionWindow.show();
		addRegionWindow
			.setTitle('<span style="font-weight:normal">修改区域</span>');
		Ext.getCmp('windowmode').setValue('edit');
		Ext.getCmp('parentid_old').setValue(record.get('parentid'));
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
		var parentid = Ext.getCmp('parentid').getValue();
		var parentid_old = Ext.getCmp('parentid_old').getValue();
		addRegionFormPanel.form.submit({
			url: './region.do?reqCode=updateRegionItem',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				addRegionWindow.hide();
				regionStore.reload();
				refreshNode(parentid);
				if (parentid != parentid_old) {
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
			if (rows[i].get('regionid') == '001') {
				fields = fields + rows[i].get('regionname') + '<br>';
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
		var strChecked = jsArray2JsString(rows, 'regionid');
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
								url: './region.do?reqCode=deleteRegionItems',
								success: function(response) {
									var resultArray = Ext.util.JSON
										.decode(response.responseText);
									regionStore.reload();
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
									regionid: pRegionid
								}
							});
					}
				});
	}

});