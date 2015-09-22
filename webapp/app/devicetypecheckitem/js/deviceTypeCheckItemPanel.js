Ext.ux.app.DeviceTypeCheckItemPanel = Ext.extend(Ext.Panel, {
	layout: 'border',
	border: false,
	margins: '3 3 3 3',
	region: 'center',
	deviceTypeId: null,
	gridObj: null,
	checkBoxSM: null,
	editFormPanel: null,
	editWindow: null,
	devicetypeid: null,
	devicetypename: null,
	constructor: function(config) {
		Ext.apply(this, config);
		this.init();
		Ext.ux.app.DeviceTypeCheckItemPanel.superclass.constructor.call(this, Ext.applyIf(config || {}, {
			items: [this.gridObj.grid],
			listeners: {
				beforedestroy: function(panel) {
					var me = panel;
					if (me.editWindow) {
						me.editWindow.close();
						me.editWindow = null;
					}
				}
			}
		}));
	},
	init: function() {
		this.createGridObj();
		this.createEditWindow();
	},

	createGridObj: function() {
		this.checkBoxSM = new Ext.grid.CheckboxSelectionModel();
		this.gridObj = new Ext.ux.app.grid.AppParamGridObj(this.createParamColumnModel, this.createParamStore, this.createParamGrid, this);
	},

	createParamColumnModel: function(me) {
		return me.createColumnModel();
	},

	createParamStore: function(me) {
		return me.createStore();
	},

	createParamGrid: function(store, columnModel, pageToolbar, me) {
		return me.createGrid(store, columnModel, pageToolbar);
	},

	createColumnModel: function() {
		var result = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), this.checkBoxSM, {
			header: '名称',
			dataIndex: 'checkitemname',
			width: 130
		}, {
			header: '动作',
			dataIndex: 'action',
			width: 130,
			renderer: DEVCHKACTIONRender
		}, {
			header: '状态',
			dataIndex: 'state',
			renderer: EXTUXAPP.AppTools.EnabledSpanRender
		}, {
			id: 'content',
			header: '内容',
			dataIndex: 'content'
		}]);

		return result;
	},


	createStore: function() {
		/**
		 * 数据存储
		 */
		var result = new Ext.data.Store({
			proxy: new Ext.data.HttpProxy({
				url: './deviceTypeCheckItem.do?reqCode=queryCheckItemsForManage&devicetypeid=' + this.devicetypeid
			}),
			reader: new Ext.data.JsonReader({
				totalProperty: 'TOTALCOUNT',
				root: 'ROOT'
			}, [{
				name: 'devicecheckcontentid'
			}, {
				name: 'checkitemname'
			}, {
				name: 'devicetypeid'
			}, {
				name: 'action'
			}, {
				name: 'content'
			}, {
				name: 'remark'
			}, {
				name: 'state'
			}])
		});
		result.on('beforeload', function() {
			this.baseParams = {
				checkItemName: Ext.getCmp('checkItemName').getValue(),
			};
		});
		return result;
	},

	createGrid: function(store, columnModel, pageToolbar) {
		var me = this;
		var result = new Ext.grid.GridPanel({
			title: '<span class="commoncss">检查信息表-' + this.devicetypename + '</span>',
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
			autoExpandColumn: 'content',
			cm: columnModel,
			sm: this.checkBoxSM,
			tbar: [{
				text: '新增',
				iconCls: 'page_addIcon',
				handler: function() {
					me.addInit();
				}
			}, '-', {
				text: '修改',
				iconCls: 'page_edit_1Icon',
				handler: function() {
					me.editInit();
				}
			}, '-', {
				text: '删除',
				iconCls: 'page_delIcon',
				handler: function() {
					me.deleteItems();
				}
			}, '->', new Ext.form.TextField({
				id: 'checkItemName',
				name: 'checkItemName',
				emptyText: '请输入检查项名称',
				enableKeyEvents: true,
				listeners: {
					specialkey: function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							me.queryCheckItem();
						}
					}
				},
				width: 130
			}), {
				text: '查询',
				iconCls: 'previewIcon',
				handler: function() {
					me.queryCheckItem();
				}
			}, '-', {
				text: '刷新',
				iconCls: 'arrow_refreshIcon',
				handler: function() {
					me.gridObj.store.reload();
				}
			}],
			bbar: pageToolbar
		});
		result.on('rowdblclick', function(grid, rowIndex, event) {
			me.editInit();
		});
		return result;
	},

	createEditFormPanel: function() {
		var actionCombo = new Ext.form.ComboBox({
			name: 'action',
			hiddenName: 'action',
			store: DEVCHKACTIONStore,
			mode: 'local',
			triggerAction: 'all',
			valueField: 'value',
			displayField: 'text',
			readOnly: false,
			value: '1',
			fieldLabel: '动作',
			emptyText: '请选择...',
			labelStyle: micolor,
			allowBlank: false,
			forceSelection: true,
			editable: false,
			typeAhead: true,
			anchor: "99%"
		});
		var stateCombo = new Ext.form.ComboBox({
			id: 'stateCombo',
			name: 'state',
			hiddenName: 'state',
			store: ENABLEDStore,
			mode: 'local',
			triggerAction: 'all',
			valueField: 'value',
			displayField: 'text',
			readOnly: false,
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
		this.editFormPanel = new Ext.form.FormPanel({
			id: 'editCheckItemFormPanel',
			name: 'editCheckItemFormPanel',
			defaultType: 'textfield',
			labelAlign: 'right',
			labelWidth: 80,
			frame: false,
			bodyStyle: 'padding:5 5 0',
			items: [{
					fieldLabel: '名称',
					name: 'checkitemname',
					// id: 'checkitemname',
					allowBlank: false,
					labelStyle: micolor,
					maxLength: 500,
					maxLengthText: '名称不能超过500个字符',
					anchor: '99%'
				},
				actionCombo, {
					fieldLabel: '检查内容',
					name: 'content',
					allowBlank: true,
					maxLength: 2000,
					xtype: 'textarea',
					maxLengthText: '检查内容不能超过2000个字符',
					anchor: '99%'
				},
				stateCombo, {
					fieldLabel: '备注',
					name: 'remark',
					allowBlank: true,
					maxLength: 1000,
					xtype: 'textarea',
					maxLengthText: '备注不能超过1000个字符',
					anchor: '99%'
				}, {
					id: 'checkitemdevicetypeid',
					name: 'devicetypeid',
					hidden: true
				}, {
					id: 'checkitemwindowmode',
					name: 'checkitemwindowmode',
					hidden: true
				}, {
					// id: 'devicecheckcontentid',
					name: 'devicecheckcontentid',
					hidden: true
				}
			]
		});
	},

	createEditWindow: function() {
		this.createEditFormPanel();
		var me = this;
		this.editWindow = new Ext.Window({
			id: 'editCheckItemWindow',
			layout: 'fit',
			width: 400,
			height: 315,
			resizable: false,
			draggable: true,
			closable: true,
			modal: true,
			closeAction: 'hide',
			title: '<span class="commoncss">新增检查项</span>',
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
			items: [this.editFormPanel],
			buttons: [{
				text: '保存',
				iconCls: 'acceptIcon',
				id: 'btn_id_save_update',
				handler: function() {
					var mode = Ext.getCmp('checkitemwindowmode').getValue();
					if (mode == 'add') {
						me.addInfo();
					} else {
						me.updateInfo();
					}
				}
			}, {
				text: '重置',
				id: 'bthCheckItemReset',
				iconCls: 'tbar_synchronizeIcon',
				handler: function() {
					clearForm(me.editFormPanel.getForm());
				}
			}, {
				text: '关闭',
				iconCls: 'deleteIcon',
				handler: function() {
					me.editWindow.hide();
				}
			}]
		});
	},

	addInit: function() {
		Ext.getCmp('bthCheckItemReset').hide();
		var flag = Ext.getCmp('checkitemwindowmode').getValue();
		if (typeof(flag) != 'undefined') {
			this.editFormPanel.form.getEl().dom.reset();
		} else {
			clearForm(this.editFormPanel.getForm());
		}
		Ext.getCmp('checkitemdevicetypeid').setValue(this.devicetypeid);
		Ext.getCmp('stateCombo').setValue(1);
		this.editWindow.show();
		this.editWindow
			.setTitle('<span class="commoncss">新增检查项</span>');
		Ext.getCmp('checkitemwindowmode').setValue('add');
	},

	addInfo: function() {
		if (!this.editFormPanel.form.isValid()) {
			return;
		}
		var me = this;
		this.editFormPanel.form.submit({
			url: './deviceTypeCheckItem.do?reqCode=saveAddInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				me.editWindow.hide();
				me.gridObj.store.reload();
				form.reset();
				Ext.MessageBox.alert('提示', action.result.msg);
			},
			failure: function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.alert('提示', '检查项数据保存失败:<br>' + msg);
			}
		});
	},

	editInit: function() {
		var record = this.gridObj.grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.MessageBox.alert('提示', '请先选择要修改的检查项!');
			return;
		}
		this.editFormPanel.getForm().loadRecord(record);
		this.editWindow.show();
		this.editWindow.setTitle('<span style="font-weight:normal">修改检查项</span>');
		Ext.getCmp('checkitemwindowmode').setValue('edit');
		// Ext.getCmp('devicecheckcontentid').setValue(record.get('devicecheckcontentid'));
		Ext.getCmp('checkitemdevicetypeid').setValue(this.devicetypeid);
		Ext.getCmp('bthCheckItemReset').hide();
	},

	/**
	 * 更新
	 */
	updateInfo: function() {
		if (!this.editFormPanel.form.isValid()) {
			return;
		}
		var me = this;
		this.editFormPanel.form.submit({
			url: './deviceTypeCheckItem.do?reqCode=saveUpdateInfo',
			waitTitle: '提示',
			method: 'POST',
			waitMsg: '正在处理数据,请稍候...',
			success: function(form, action) {
				me.editWindow.hide();
				me.gridObj.grid.store.reload();
				form.reset();
				Ext.MessageBox.alert('提示', action.result.msg);
			},
			failure: function(form, action) {
				var msg = action.result.msg;
				Ext.MessageBox.alert('提示', '检查项数据修改失败:<br>' + msg);
			}
		});
	},

	queryCheckItem: function() {
		this.gridObj.store.load({
			params: {
				start: 0,
				limit: this.gridObj.gridBottomBar.getPageSize(),
				checkItemName: Ext.getCmp('checkItemName').getValue()
			}
		});
	},

	deleteItems: function() {
		var me = this;
		var rows = this.gridObj.grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			if (pType == '1') {
				Ext.Msg.alert('提示', '请先选中要删除的项目!');
				return;
			}
		}
		var strChecked = jsArray2JsString(rows, 'devicecheckcontentid');
		Ext.Msg.confirm('请确认',
			'<span style="color:red"><b>提示:</b>如果检查项有相关记录，将检查项设置为无效,请慎重.</span><br>继续删除吗?',
			function(btn, text) {
				if (btn == 'yes') {
					showWaitMsg();
					Ext.Ajax.request({
						url: './deviceTypeCheckItem.do?reqCode=delete',
						success: function(response) {
							var resultArray = Ext.util.JSON
								.decode(response.responseText);
							me.gridObj.store.reload();
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
						}
					});
				}
			});
	}

});