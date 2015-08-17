/**
 * 设备厂商管理
 * 
 * @author yangcheng
 * @since now
 */
Ext.onReady(function() {
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	        {
				header : '简称',
				dataIndex : 'nickname'
			}, {
				header : '全称',
				dataIndex : 'realname'
			}, {
				header : '地址',
				dataIndex : 'address',
				 width : 130
			}, {
				header : '联系电话',
				dataIndex : 'phonenumber'
			}, {
				header : '联系人',
				dataIndex : 'contactperson'
			}, {
				header : '备注',
				dataIndex : 'remark',
				id : 'remark'
			},{
					header : '编号',
					dataIndex : 'id',
					hidden : true,
					sortable : true
			}
			
			]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './deviceManufacture.do?reqCode=query'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'nickname'
								}, {
									name : 'realname'
								}, {
									name : 'address'
								}, {
									name : 'phonenumber'
								}, {
									name : 'contactperson'
								}, {
									name : 'remark'
								}, {
									name : 'id'
								}])
			});

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
				this.baseParams = {
					queryParam : Ext.getCmp('queryParam').getValue()
				};
			});

	var pagesize_combo = new Ext.form.ComboBox({
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
	var number = parseInt(pagesize_combo.getValue());
	pagesize_combo.on("select", function(comboBox) {
				bbar.pageSize = parseInt(comboBox.getValue());
				number = parseInt(comboBox.getValue());
				store.reload({
							params : {
								start : 0,
								limit : bbar.pageSize
							}
						});
			});

	var bbar = new Ext.PagingToolbar({
				pageSize : number,
				store : store,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;', pagesize_combo]
			})

	var grid = new Ext.grid.GridPanel({
				title : '<span class="commoncss">设备厂家数据列表</span>',
				height : 510,
				store : store,
				region : 'center',
				margins : '3 3 3 3',
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
				autoExpandColumn : 'remark',
				cm : cm,
				sm : sm,
				tbar : [{
							text : '新增',
							iconCls : 'page_addIcon',
							handler : function() {
							    addManufWindow.getComponent('manufform').form.reset()
								addManufWindow.show();
							}
						}, '-', {
							text : '修改',
							iconCls : 'page_edit_1Icon',
							handler : function() {
								ininEditManufWindow();
							}
						}, '-', {
							text : '删除',
							iconCls : 'page_delIcon',
							handler : function() {
								deleteItems();
							}
						}, '-', '->',
						new Ext.form.TextField({
									id : 'queryParam',
									name : 'queryParam',
									emptyText : '简称|全称',
									enableKeyEvents : true,
									listeners : {
										specialkey : function(field, e) {
											if (e.getKey() == Ext.EventObject.ENTER) {
												queryItem();
											}
										}
									},
									width : 130
								}), {
							text : '查询',
							iconCls : 'previewIcon',
							handler : function() {
								queryItem();
							}
						}, '-', {
							text : '刷新',
							iconCls : 'arrow_refreshIcon',
							handler : function() 
							{
								refreshTable();
								
							}
						}],
				bbar : bbar
			});
	
	store.load({
				params : {
					start : 0,
					limit : bbar.pageSize
				}
			});

	grid.addListener('rowdblclick', ininEditManufWindow);
	grid.on('sortchange', function() {
				
			});

	bbar.on("change", function() {
				
			});
			
  grid.addListener('rowcontextmenu', fnRightClick);
  
  function fnRightClick(goodsGridUi, rowIndex, e) 
  {
      //  alert(e);
        grid.getSelectionModel().selectRow(rowIndex);
		e.preventDefault();
		rightMenu.showAt(e.getXY());
}

var rightMenu = new Ext.menu.Menu(
    {
        id: "ManufMenu",
        items:
        [
		
		    {
                id: 'Create',
              //  icon: 'images/write.gif', //图标文件
			    iconCls : 'page_addIcon',
                handler: fnMenuAdd,
                text: '新增一行'
            },
            {
                id: 'Edit',
              //  icon: 'images/write.gif', //图标文件
			   iconCls : 'page_edit_1Icon',
                handler: fnMenuEdit,
                text: '编辑该行'
            },
            {
                id: 'Del',
               iconCls : 'page_delIcon',
                handler: fnMenuDel,
                text: '删除该行'
            }
        ]
    });
	
	
function fnMenuAdd() 
  {
		addManufWindow.getComponent('manufform').form.reset()
		addManufWindow.show();
	  
}

function fnMenuEdit() 
  {
 
	  ininEditManufWindow();
	  
}


function fnMenuDel() 
  {
 
	 deleteItems();
	  
}
	
	var  addManufWindow;
	var formPanel;

	formPanel = new Ext.form.FormPanel({
				id : 'manufform',
				name : 'manufform',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 60,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
							fieldLabel : '简称',
							name : 'nickname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '全称',
							name : 'realname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '地址',
							name : 'address',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},{
							fieldLabel : '联系电话',
							name : 'phonenumber',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '联系人',
							name : 'contactperson',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},{
							fieldLabel : '备注',
							name : 'remark',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : true,
							xtype: 'textarea'
						}]
			});

	addManufWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 273,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">新增设备厂家</span>',
		// iconCls : 'page_addIcon',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
		buttonAlign : 'right',
		border : false,
		animCollapse : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		items : [formPanel],
		buttons : [{
			text : '保存',
			iconCls : 'acceptIcon',
			handler : function() {
				
				if (addManufWindow.getComponent('manufform').form.isValid()) {
					addManufWindow.getComponent('manufform').form.submit({
						url : './deviceManufacture.do?reqCode=save',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							   store.reload();
							   
							 //  Ext.MessageBox.alert('提示', '设备厂商新增成功');
							   addManufWindow.getComponent('manufform').form.reset();
							   	addManufWindow.hide();
								
							
								
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '设备厂商新增失败:<br>' + msg);
						//	addManufWindow.getComponent('manufform').form.reset();
						}
					});
				} else {
					// 表单验证失败
				}
			}
		}, {
			text : '关闭',
			iconCls : 'deleteIcon',
			handler : function() {
				addManufWindow.hide();
			}
		}]
	});

	

	var editManufWindow, editManufFormPanel;
	editManufFormPanel = new Ext.form.FormPanel({
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				frame : false,
				bodyStyle : 'padding:5 5 0',
				id : 'editCodeFormPanel',
				name : 'editManufFormPanel',
				items : [{
							fieldLabel : '简称',
							name : 'nickname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '全称',
							name : 'realname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '地址',
							name : 'address',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},{
							fieldLabel : '联系电话',
							name : 'phonenumber',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '联系人',
							name : 'contactperson',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},{
							fieldLabel : '备注',
							name : 'remark',
							anchor : '100%',
							allowBlank : true,
							labelStyle : micolor,
							xtype: 'textarea'
						},{
							fieldLabel : '编号',
							name : 'id',
							anchor : '100%',
							hidden : true,
							hideLabel : true,
							allowBlank : false
						}]
			});

	editManufWindow = new Ext.Window({
				layout : 'fit',
				width : 300,
				height : 273,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span class="commoncss">修改设备厂商</span>',
				modal : true,
				collapsible : true,
				titleCollapse : true,
				maximizable : false,
				buttonAlign : 'right',
				border : false,
				animCollapse : true,
				animateTarget : Ext.getBody(),
				constrain : true,
				items : [editManufFormPanel],
				buttons : [{
					text : '保存',
					iconCls : 'acceptIcon',
					handler : function() {
						
						updateManufItem();
					}
				}, {
					text : '关闭',
					iconCls : 'deleteIcon',
					handler : function() {
						editManufWindow.hide();
					}
				}]

			});
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});

	/**
	 * 初始化代码修改出口
	 */
	function ininEditManufWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.Msg.alert('提示', '请先选中要修改的项目');
			return;
		}
		record = grid.getSelectionModel().getSelected();
		editManufWindow.show();
		editManufFormPanel.getForm().loadRecord(record);
	}

	/**
	 * 修改
	 */
	function updateManufItem() {
		if (!editManufFormPanel.form.isValid()) {
			return;
		}
		editManufFormPanel.form.submit({
					url : './deviceManufacture.do?reqCode=update',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editManufWindow.hide();
						store.reload();
					
					},
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.MessageBox.alert('提示', '修改设备厂商失败:<br>' + msg);
					}
				});
	}

	
	/**
	 * 删除
	 */
	function deleteItems() 
	{
	
		var rows = grid.getSelectionModel().getSelections();
		var fields = '';
		
		if (Ext.isEmpty(rows)) {
			Ext.Msg.alert('提示', '请先选中要删除的项目!');
			return;
		}
		var strChecked = jsArray2JsString(rows, 'id');
		Ext.Msg.confirm('请确认', '你真的要删除吗?', function(btn, text) {
					if (btn == 'yes') {
						showWaitMsg();
							Ext.Ajax.request({
										url : './deviceManufacture.do?reqCode=delete',
										success : function(response) {
											
											hideWaitMsg();
											
											var  resultArray = Ext.util.JSON.decode(response.responseText);
											if(resultArray.success)
											{
												store.reload();
											}
											else
											{
												Ext.Msg.alert('提示', resultArray.msg);
											}
										},
										failure : function(response) {
										
										    hideWaitMsg();
											var resultArray = Ext.util.JSON.decode(response.responseText);
											Ext.Msg.alert('提示', resultArray.msg);
										},
										params : {
											delkeys : strChecked
										}
									});
					}
				});
	}

	/**
	 * 根据条件查询
	 */
	function queryItem() {
		store.load({
					params : {
						start : 0,
						limit : bbar.pageSize,
						queryParam : Ext.getCmp('queryParam').getValue()
					}
				});
	}

	/**
	 * 刷新
	 */
	function refreshTable() {
	//	store.load({
			//		params : {
			//			start : 0,
			//			limit : bbar.pageSize
			//		}
			//	});
			
			store.reload();
	}
});