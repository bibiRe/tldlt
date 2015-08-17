/**
 * 设备型号管理
 * 
 * @author yangcheng
 * @since now
 */
Ext.onReady(function() {
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	        {
				header : '型号名称',
				dataIndex : 'realname'
			},  {
				header : '类型名称',
				dataIndex : 'typename',
				 width : 130
			}, {
				header : '型号备注',
				dataIndex : 'typeremark',
				id : 'typeremark'
			}, 	 {
				header : '厂商全称',
				dataIndex : 'manufrealname'
			}, {
				header : '厂商地址',
				dataIndex : 'manufaddress',
				 width : 130
			}, {
				header : '厂商联系电话',
				dataIndex : 'manufphonenumber'
			}, {
				header : '厂商联系人',
				dataIndex : 'manufcontactperson'
			},{
				header : '型号备注',
				dataIndex : 'remark',
				id : 'remark'
			},{
					header : '型号编号',
					dataIndex : 'id',
					hidden : true,
					sortable : true
			},{
					header : '类型编号',
					dataIndex : 'typeid',
					hidden : true
			},{
					header : '厂商编号',
					dataIndex : 'manufid',
					hidden : true
			}
			
			]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './deviceModel.do?reqCode=query'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [
								{
									name : 'realname'
								}, {
									name : 'remark'
								}, {
									name : 'typename'
								}, {
									name : 'typeremark'
								}, {
									name : 'manufrealname'
								}, {
									name : 'manufaddress'
								}, {
									name : 'manufphonenumber'
								},{
									name : 'manufcontactperson'
								},{
									name : 'id'
								},{
									name : 'typeid'
								},{
									name : 'manufid'
								}
							])
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
				title : '<span class="commoncss">设备型号数据列表</span>',
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
							    addWindow.getComponent('addform').form.reset()
								addWindow.show();
							}
						}, '-', {
							text : '修改',
							iconCls : 'page_edit_1Icon',
							handler : function() {
								ininEditWindow();
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
									emptyText : '型号名称',
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

	grid.addListener('rowdblclick', ininEditWindow);
	grid.on('sortchange', function() {
				
			});

	bbar.on("change", function() {
				
			});
			
  grid.addListener('rowcontextmenu', fnRightClick);
  
  function fnRightClick(goodsGridUi, rowIndex, e) 
  {
        grid.getSelectionModel().selectRow(rowIndex);
		e.preventDefault();
		rightMenu.showAt(e.getXY());
}

var rightMenu = new Ext.menu.Menu(
    {
        id: "popMenu",
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
	addWindow.getComponent('addform').form.reset()
	addWindow.show();
	  
}

function fnMenuEdit() 
  {
 
	  ininEditWindow();
	  
}


function fnMenuDel() 
  {
 
	 deleteItems();
	  
}



////////////////////////////

var  storeDevType = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './deviceModel.do?reqCode=querydevtype'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'id',mapping: 'id'},
		     {name : 'value',mapping: 'value'}
    ])
});



var comboDevType = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'devtypeid1', 
name : "devtypeid1",
hiddenName  : 'devtypeid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '设备类型', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeDevType , 
forceSelection : true, 
mode : 'local', //remote ,don't invoke load 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
}) 




storeDevType.load();

		comboDevType.on('expand', function() {
			
				storeDevType.reload();

			});

	//////////////////////////////////
	
	var  storeDevManuf = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './deviceModel.do?reqCode=querydevmanuf'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
        {name : 'id'},
        {name : 'value'}
    ])
});



var comboDevManuf = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'devmanufid1', 
name : "devmanufid1",
hiddenName  : 'devmanufid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '设备厂商', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeDevManuf , 
forceSelection : true, 
mode : 'local', 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
}) 

storeDevManuf.load();

		comboDevManuf.on('expand', function() {
			
				storeDevManuf.reload();

			});
			
			
	var  addWindow;
	var formPanel;

	formPanel = new Ext.form.FormPanel({
				id : 'addform',
				name : 'addform',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 60,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
							fieldLabel : '型号名称',
							name : 'realname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},comboDevType,comboDevManuf ,{
							fieldLabel : '备注',
							name : 'remark',
							anchor : '100%',
							allowBlank : true,
							labelStyle : micolor,
							xtype: 'textarea'
						}
					]
			});

	addWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 273,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">新增设备型号</span>',
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
				
				if (addWindow.getComponent('addform').form.isValid()) {
					addWindow.getComponent('addform').form.submit({
						url : './deviceModel.do?reqCode=save',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							   store.reload();
							   
							
							  addWindow.getComponent('addform').form.reset();
							  addWindow.hide();
								
							
								
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '设备类型新增失败:<br>' + msg);
						
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
				addWindow.hide();
			}
		}]
	});

	
	
	
	////////////////////////////

var  storeEditDevType = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './deviceModel.do?reqCode=querydevtype'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'id',mapping: 'id'},
		     {name : 'value',mapping: 'value'}
    ])
});



var comboEditDevType = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'devtypeid2', 
name : "devtypeid2",
hiddenName  : 'devtypeid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '设备类型', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeEditDevType , 
forceSelection : true, 
mode : 'local', //remote ,don't invoke load 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
}) 




storeEditDevType.load();

		comboEditDevType.on('expand', function() {
			
				storeEditDevType.reload();

			});

	//////////////////////////////////
	
	var  storeEditDevManuf = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './deviceModel.do?reqCode=querydevmanuf'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
        {name : 'id'},
        {name : 'value'}
    ])
});



var comboEditDevManuf = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'devmanufid2', 
name : "devmanufid2",
hiddenName  : 'devmanufid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '设备厂商', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeEditDevManuf , 
forceSelection : true, 
mode : 'local', 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
}) 

storeEditDevManuf.load();

		comboEditDevManuf.on('expand', function() {
			
				storeEditDevManuf.reload();

			});

	var editWindow, editFormPanel;
	editFormPanel = new Ext.form.FormPanel({
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				frame : false,
				bodyStyle : 'padding:5 5 0',
				id : 'editCodeFormPanel',
				name : 'editFormPanel',
				items : [{
							fieldLabel : '型号名称',
							name : 'realname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},comboEditDevType,comboEditDevManuf ,{
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

	editWindow = new Ext.Window({
				layout : 'fit',
				width : 300,
				height : 273,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span class="commoncss">修改设备型号</span>',
				modal : true,
				collapsible : true,
				titleCollapse : true,
				maximizable : false,
				buttonAlign : 'right',
				border : false,
				animCollapse : true,
				animateTarget : Ext.getBody(),
				constrain : true,
				items : [editFormPanel],
				buttons : [{
					text : '保存',
					iconCls : 'acceptIcon',
					handler : function() {
						
						updateItem();
					}
				}, {
					text : '关闭',
					iconCls : 'deleteIcon',
					handler : function() {
						editWindow.hide();
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
	function ininEditWindow() {
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record)) {
			Ext.Msg.alert('提示', '请先选中要修改的项目');
			return;
		}
		record = grid.getSelectionModel().getSelected();
		editWindow.show();
		editFormPanel.getForm().loadRecord(record);
		
		var  typeid = record.get('typeid');
		var  typename = record.get('typename');
		
	   var  typeboxrecord =new  Ext.data.Record();
	   
		typeboxrecord.set('value',typename);
		typeboxrecord.set('id',typeid);
		
		storeEditDevType.insert(0,typeboxrecord);
		
		comboEditDevType.setValue(typeid);
		
		///////////////////////////////
		
		var  manufid = record.get('manufid');
		var  manufname = record.get('manufrealname');
		
	   var  manufboxrecord =new  Ext.data.Record();
	   
		manufboxrecord.set('value',manufname);
		manufboxrecord.set('id',manufid);
		
		storeEditDevType.insert(0,manufboxrecord);
		
		comboEditDevManuf.setValue(manufid);
	
	}

	/**
	 * 修改
	 */
	function updateItem() {
		if (!editFormPanel.form.isValid()) {
			return;
		}
		editFormPanel.form.submit({
					url : './deviceModel.do?reqCode=update',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editWindow.hide();
						store.reload();
					
					},
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.MessageBox.alert('提示', '修改设备型号失败:<br>' + msg);
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
										url : './deviceModel.do?reqCode=delete',
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