/**
 * 故障管理.
 *
 * @author hhe
 * @since 2015-09-07
 */
Ext.onReady(function() {
	var checkBoxSM = new Ext.grid.CheckboxSelectionModel();

	function faultInfoReporterColumnRender(value, meta, record) {
		var data = record.data;
		var result = data.deptname + data.reportername;
		return result;
	}

	function createColumnModel() {
		return new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), checkBoxSM, {
			header: '设备',
			dataIndex: 'devicename',
			hidden: false,
			hidden: false,
			width: 130,
			sortable: true
		}, {
			header: '上报人',
			dataIndex: 'reportername',
			width: 100,
			renderer: faultInfoReporterColumnRender
		}, {
			header: '时间',
			dataIndex: 'time',
			width: 100,
			dateFormat: 'Y-m-d H:i:s'
		}, {
			header: '处理人员',
			dataIndex: 'handlername',
			width: 100
		}, {
			header: '处理状态',
			dataIndex: 'state',
			renderer: EXTUXAPP.AppTools.DeviceFaultStateRender
		}, {
			header: '处理时间',
			dataIndex: 'handletime',
			width: 100,
			dateFormat: 'Y-m-d H:i:s'
		}, {
			id: 'faultinfo',
			header: '错误信息',
			dataIndex: 'faultinfo'
		}]);
	}

	function createStore() {
		/**
		 * 数据存储
		 */
		return new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: './deviceFault.do?reqCode=queryDeviceFaultForManage'
			}),
			reader: new Ext.data.JsonReader({
				totalProperty: 'TOTALCOUNT',
				root: 'ROOT'
			}, [{
				name: 'devicefaultinfoid'
			}, {
				name: 'devicename'
			}, {
				name: 'reporter'
			}, {
				name: 'reportername'
			}, {
				name: 'deptname'
			}, {
				name: 'time'
			}, {
				name: 'state'
			}, {
				name: 'faultinfo'
			}, {
				name: 'handler'
			}, {
				name: 'handlername'
			}, {
				name: 'handletime'
			}, {
				name: 'remark'
			}])
		});
	}



	function createGrid(store, columnModel, pageToolbar) {
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
			sm: checkBoxSM,
			tbar: [{
				text: '修改',
				iconCls: 'page_edit_1Icon',
				handler: function() {
					editInit();
				}
			}, '->', new Ext.form.TextField({
				id: 'faultInfoName',
				name: 'faultInfoName',
				emptyText: '请输入名称',
				enableKeyEvents: true,
				listeners: {
					specialkey: function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							queryDeviceFaultInfoName();
						}
					}
				},
				width: 130
			}), {
				text: '查询',
				iconCls: 'previewIcon',
				handler: function() {
					queryDeviceFaultInfoName();
				}
			}, '-', {
				text: '刷新',
				iconCls: 'arrow_refreshIcon',
				handler: function() {
					gridObj.store.reload();
				}
			}],
			bbar: pageToolbar
		});
		result.on('rowdblclick', function(grid, rowIndex, event) {
			editInit();
		});
		return result;
	}

	var gridObj = new Ext.ux.app.grid.AppGridObj(createColumnModel, createStore,
		createGrid);

	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			region: 'center',
			layout: 'fit',
			border: false,
			margins: '3 3 3 3',
			items: [gridObj.grid]
		}]
	});

	function createEditFormPanel() {
		var stateCombo = new Ext.form.ComboBox({
			id: 'stateCombo',
			name: 'state',
			hiddenName: 'state',
			store: DEVFAULTSTATEStore,
			mode: 'local',
			triggerAction: 'all',
			valueField: 'value',
			displayField: 'text',
			disabled: true,
			fieldClass: 'x-custom-field-disabled',
			value: '1',
			fieldLabel: '状态',
			emptyText: '请选择...',
			labelStyle: micolor,
			allowBlank: false,
			forceSelection: true,
			editable: false,
			typeAhead: true,
			anchor: "99%"
		});
		var result = new Ext.form.FormPanel({
			collapsible: false,
			id: 'editFormPanel',
			name: 'editFormPanel',
			labelAlign: 'right',
			labelWidth: 65,
			bodyStyle: 'padding:5 5 5 5',
			items: [{
				layout: 'column',
				border: false,
				items: [{
					columnWidth: .5,
					layout: 'form',
					labelWidth: 65, // 标签宽度
					defaultType: 'textfield',
					border: false,
					items: [{
						fieldLabel: '设备',
						name: 'devicename',
						labelStyle: micolor,
						disabled: true,
						fieldClass: 'x-custom-field-disabled',
						anchor: '99%'
					}, {
						fieldLabel: '上报人员',
						name: 'reportername',
						labelStyle: micolor,
						disabled: true,
						fieldClass: 'x-custom-field-disabled',
						anchor: '99%'
					}]
				}, {
					columnWidth: .5,
					layout: 'form',
					labelWidth: 65, // 标签宽度
					defaultType: 'textfield',
					border: false,
					items: [stateCombo, {
						fieldLabel: '上报时间',
						name: 'time',
						labelStyle: micolor,
						disabled: true,
						fieldClass: 'x-custom-field-disabled',
						anchor: '99%'
					}]
				}]
			}, {
				fieldLabel: '故障信息',
				name: 'faultinfo',
				labelStyle: micolor,
				readOnly: true,
				xtype: 'textarea',
				disabled: true,
				fieldClass: 'x-custom-field-disabled',
				anchor: '99%'
			}, {
				layout: 'column',
				border: false,
				items: [{
					columnWidth: .5,
					layout: 'form',
					labelWidth: 65, // 标签宽度
					defaultType: 'textfield',
					border: false,
					items: [{
						fieldLabel: '处理人',
						id: 'handlername',
						name: 'handlername',
						labelStyle: micolor,
						disabled: true,
						fieldClass: 'x-custom-field-disabled',
						anchor: '99%'
					}]
				}, {
					columnWidth: .5,
					layout: 'form',
					labelWidth: 65, // 标签宽度
					defaultType: 'textfield',
					border: false,
					items: [{
						fieldLabel: '处理时间',
						id: 'handletime',
						name: 'handletime',
						labelStyle: micolor,
						disabled: true,
						fieldClass: 'x-custom-field-disabled',
						anchor: '99%'
					}]
				}]
			}, {
				fieldLabel: '备注',
				id: 'remark',
				name: 'remark',
				labelStyle: micolor,
				xtype: 'textarea',
				fieldClass: 'x-custom-field-disabled',
				anchor: '99%'
			}, {
				name: 'devicefaultinfoid',
				xtype: 'hidden',
				hidden: true
			}]
		});
		return result;
	}


	function createEditWindow() {
		var result = new Ext.Window({
			id: 'editWindow',
			layout: 'fit',
			width: 600,
			height: 315,
			resizable: false,
			draggable: true,
			closable: true,
			modal: true,
			closeAction: 'hide',
			title: '<span class="commoncss">故障信息</span>',
			// iconCls : 'page_addIcon',
			// collapsible: true,
			// titleCollapse: true,
			maximizable: false,
			buttonAlign: 'right',
			border: false,
			animCollapse: true,
			pageY: 20,
			pageX: document.body.clientWidth / 2 - 420 / 2,
			animateTarget: Ext.getBody(),
			constrain: true,
			items: [createEditFormPanel()],
			buttons: [{
				text: '保存',
				iconCls: 'acceptIcon',
				id: 'btn_id_save_update',
				handler: function() {
					updateInfo();
				}
			}, {
				text: '关闭',
				iconCls: 'deleteIcon',
				handler: function() {
					editWindow.hide();
				}
			}]
		});
		return result;
	}

	var editWindow = createEditWindow();
	/**
	 * 根据条件查询实时查看信息
	 */
	function queryDeviceFaultInfoName() {
		gridObj.store.load({
			params: {
				start: 0,
				limit: gridObj.gridBottomBar.getPageSize(),
				faultInfoName: Ext.getCmp('faultInfoName').getValue()
			}
		});
	}

	function editInit() {
		var record = gridObj.grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.alert('提示', '请先选择故障信息!');
			return;
		}
		var stateWaitHandling = (0 == record.get('state'));
		EXTUXAPP.AppTools.showTextField(!stateWaitHandling, 'handlername');
		EXTUXAPP.AppTools.showTextField(!stateWaitHandling, 'handletime');
		if (stateWaitHandling) {
			EXTUXAPP.AppTools.enableTextField(true, 'stateCombo');
			EXTUXAPP.AppTools.enableTextField(true, 'remark');
			Ext.getCmp('btn_id_save_update').show();
		} else {
			var isLoginUserid = (login_userid == record.get('handler'));
			EXTUXAPP.AppTools.enableTextField(isLoginUserid, 'stateCombo');
			EXTUXAPP.AppTools.enableTextField(isLoginUserid, 'remark');
			if (isLoginUserid) {
				Ext.getCmp('btn_id_save_update').show();
			} else {
				Ext.getCmp('btn_id_save_update').hide();
			}
		}
		Ext.getCmp('editFormPanel').getForm().loadRecord(record);
		editWindow.show();
		editWindow.setTitle('<span style="font-weight:normal">故障信息</span>');
	}
	
	/**
	 * 更新
	 */
	function updateInfo() {
		if (!Ext.getCmp('editFormPanel').form.isValid()) {
			return;
		}
		if ('0' == Ext.getCmp('stateCombo').getValue()) {
			EXTUXAPP.AppTools.showAlert("请选择正确的处理状态值");
			return;
		}
		Ext.getCmp('editFormPanel').form.submit({
			url: './deviceFault.do?reqCode=saveUpdateStateInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				editWindow.hide();
				gridObj.grid.store.reload();
				form.reset();
				EXTUXAPP.AppTools.showAlert(action.result.msg);
			},
			failure: function(form, action) {
				EXTUXAPP.AppTools.showAlert(action.result.msg);
			}
		});
	}
});