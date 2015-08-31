/**
 * 巡检计划
 * 
 * @author yangcheng
 * @since now
 */





Ext.onReady(function() {
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	        {
				header : '计划名称',
				dataIndex : 'planname',
				width : 90
			}, {
				header : '申请人',
				dataIndex : 'applyusername',
				width : 90
			},{
				header : '申请时间',
				dataIndex : 'applytime',
				width : 150
			}, {
				header : '状态',
				dataIndex : 'state',
				renderer:INSPECPLANSTATERender,	
				width : 50
				
			}, {
				header : '执行人',
				dataIndex : 'executeusername',
				width : 90
			},{
				header : '计划开始时间',
				dataIndex : 'executestartime',
				width : 150
			}, {
				header : '计划结束时间',
				dataIndex : 'executeendtime',
				width : 150
			},{
				header : '审批人',
				dataIndex : 'approveusername',
				width : 90
			}, {
				header : '审批信息',
				dataIndex : 'approvecontent',
				width : 150
			},{
				header : '审批时间',
				dataIndex : 'approvetime',
				width : 150
			},{
				header : '备注',
				dataIndex : 'remark',
				id : 'remark',
				width : 180
			},{
				header : '计划编号',
				dataIndex : 'planid',
				hidden : true,
				sortable : true
			},{
				header : '申请人编号',
				dataIndex : 'applyuserid',
				hidden : true
			},{
				header : '执行人编号',
				dataIndex : 'executeuserid',
				hidden : true
				
		    },{
				header : '审批人编号',
				dataIndex : 'approveuserid',
				hidden : true
			}]);
	
	

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './inspectionPlan.do?reqCode=query'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'planname'
								}, {
									name : 'applyusername'
								}, {
									name : 'applytime'
								}, {
									name : 'state'
								}, {
									name : 'executeusername'
								}, {
									name : 'executestartime'
								}, {
									name : 'executeendtime'
								}, {
									name : 'approveusername'
								}, {
									name : 'approvecontent'
								}, {
									name : 'approvetime'
								}, {
									name : 'remark'
								}, {
									name : 'planid'
								}, {
									name : 'applyuserid'
								}, {
									name : 'executeuserid'
								}, {
									name : 'approveuserid'
								}])
			});
	
	

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
				this.baseParams = {
					queryParam : Ext.getCmp('queryParam').getValue(),
					
					startdatetime : Ext.getCmp('startdatetime').getValue(),
					
					enddatetime : Ext.getCmp('enddatetime').getValue()
	
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
								limit : bbar.pageSize,
								deptid : deptid
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
				title : '<span class="commoncss">巡检计划列表</span>',
				height : 510,
				store : store,
				region : 'center',
				margins : '3 3 3 3',
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
			//	autoExpandColumn : 'remark',
				cm : cm,
				sm : sm,
				tbar : [{
							text : '新增',
							iconCls : 'page_addIcon',
							handler : function() 
							{
								AddInit();
							}
						}, '-', {
							text : '修改',
							iconCls : 'page_edit_1Icon',
							handler : function()
							{
								//ininEditManufWindow();
							}
						}, '-', {
							text : '删除',
							iconCls : 'page_delIcon',
							handler : function() 
							{
								//deleteItems();
							}
						},  '-', {
							text : '变更',
							iconCls : 'page_edit_1Icon',
							handler : function() 
							{
								
							}
						},'-', '->',
						
								'-',
								
								
								'开始时间：'  
			                    , 
			                    
			                    {
							        xtype : 'datetimefield',
							        
									name : 'startdatetime', 
									
									id   : 'startdatetime'
									
								}, 
								
								'-',
								
								'结束时间：'  
			                    , 
			                    
			                    {
							        xtype : 'datetimefield',
							        
									name : 'enddatetime', 
									
									id   : 'enddatetime'
									
								}, 
			                    
								'-',
			                    
								new Ext.form.TextField({
											id : 'queryParam',
											name : 'queryParam',
											emptyText : '申请人名称|审批人名称|执行人名称',
											enableKeyEvents : true,
											listeners : {
												specialkey : function(field, e) {
													if (e.getKey() == Ext.EventObject.ENTER) 
													{
														queryItem();
													}
												}
											},
											width : 230
										}),	
								
								{
							text : '查询',
							iconCls : 'previewIcon',
							handler : function() 
							{
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
					limit : bbar.pageSize,
					deptid : deptid
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

	AddInit();
	  
 }

function fnMenuEdit() 
  {
 
	  //ininEditManufWindow();
	  
}


function fnMenuDel() 
  {
 
	// deleteItems();
	  
}


function AddInit()
{
	addWindow.getComponent('addform').form.reset()
	addWindow.show();
}
	

var comboaddInspecPlanState = new Ext.form.ComboBox({ 
	anchor : '100%', 
	id:'inspecplanstate', 
	name : "inspecplanstate",
	hiddenName  : 'state',
	typeAhead : true,
	lazyRender : true,
	valueField : 'value', 
	displayField : 'text', 
	value : '0',
	emptyText : '请选择...',
	fieldLabel : '计划状态', 
	allowBlank : false, 
	editable : false, 
	triggerAction : 'all', 
	store : INSPECPLANSTATEStore ,
	forceSelection : true, 
	mode : 'local', 
	selectOnFocus : true ,
	editable : false,
	labelStyle : micolor,
	});



///////////////////////////////

var  storeaddUser = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './inspectionPlan.do?reqCode=queryuser'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'id'},
		     {name : 'value'}
    ])
});



var comboaddUser = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'adduser1', 
name : "adduser1",
hiddenName  : 'executeuserid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '执行人', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeaddUser , 
forceSelection : true, 
mode : 'remote', //remote ,don't invoke load 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
});




storeaddUser.load
( 
		{
			params : 
			{
				deptid : deptid
				
			}
		}
);

		comboaddUser.on('expand', function() 
	    {
			
			storeaddUser.reload
			( 
					{
						params : 
						{
							deptid : deptid
							
						}
					}
			);

	    });

////////////////////////////

		
		var smdev = new Ext.grid.CheckboxSelectionModel();
		var cmdev  = new Ext.grid.ColumnModel( [ new Ext.grid.RowNumberer({header : '序号',width : 40}), smdev, 
		{
			header : '设备名称',
			dataIndex : 'devname',
			id :  'devname'
		}, {
			header : '标记',
			dataIndex : 'initialcheck',
			hidden : true,
		},{
			header : '设备编号',
			dataIndex : 'devid',
			hidden : true,
			sortable : true
		} ]);
			
		var  storedev = new Ext.data.Store({   
	    proxy: new Ext.data.HttpProxy({   
	       url : './inspectionPlan.do?reqCode=querydevice'
	    }),   
	    reader: new Ext.data.JsonReader({
						totalProperty : 'TOTALCOUNT',
						root : 'ROOT'
	        },[
			     {name : 'devname'},
			     {name : 'devid'},
				 {name : 'initialcheck'}
	    ])
	});




	var griddev = new Ext.grid.GridPanel( {
				//title : '<span class="commoncss">部门信息表</span>',
				height : 500,
				width:600,
				autoScroll : true,
				region : 'center',
				store : storedev,
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
				cm : cmdev,
				sm : smdev,
				autoExpandColumn : 'devname'
				
			});

			
			griddev.on('rowdblclick', function(griddev, rowIndex, event) 
			{
				
			});
			
			
			
	var  storedevtree = new Ext.data.SimpleStore( {
							fields : ['userid','username'],
							data : [ [] ]
						});
						
	var comboxdevWithTree = new Ext.form.ComboBox(
					{
						id : 'devtree',
						name: 'devtree',
						anchor : '100%', 
						
						store : storedevtree,
						
						hiddenName  : 'devname',

						valueField : 'devid', 
						displayField : 'devname', 
						
						editable : false,
						value : ' ',
						emptyText : '请选择...',
						fieldLabel : '用户',
						anchor : '100%',
						mode : 'local',
						triggerAction : 'all',
						labelStyle: micolor,
						maxHeight : 390,
						// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
						tpl : "<tpl for='.'><div style='height:390px;'><div id='adddevTreeDiv'></div></div></tpl>",
						allowBlank : false,
						onSelect : Ext.emptyFn
					});
					
			// 监听下拉框的下拉展开事件
	comboxdevWithTree.on('expand', function() {
				// 将UI树挂到treeDiv容器
					griddev.render('adddevTreeDiv');
					
		
				
				  storedev.reload( {
						params : {
							start : 0,
							limit :9999,
							planid : ''
						}
					});

				});
				
				
				
			comboxdevWithTree.on('collapse', function() 
			{
				comboxdevWithTree.setValue('');
				   Ext.getCmp("addform").findById('devid').setValue('');
				   
					var rows = griddev.getSelectionModel().getSelections();
					var devids = '';
					var devnames = '';
					
					if(rows.length <= 0)
					{
						comboxdevWithTree.setValue(',');
						Ext.getCmp("devform").findById('devid').setValue(',');
						   
					   return;
					}
					
					for ( var i = 0; i < rows.length; i++) 
					{
						devids += rows[i].get('devid');
						devids += ',';
						
						devnames += rows[i].get('devname');
						devnames += ',';
				    }


					comboxdevWithTree.setValue(devnames);
					Ext.getCmp("addform").findById('devid').setValue(devids);
					
		  });
		  
		  
		  griddev.store.on("load",function()
		  {  
		  
	     
		  
			var total = griddev.store.getCount();
			for(var i=0;i<total;i++)
			{
			  var record = griddev.store.getAt(i);
			  
			 // alert(userrecord.get("username"));
			 
			    if(record.get("initialcheck"))
				{
			    	griddev.getSelectionModel().selectRow(i,true);
				}
			}
			
	    }); 
				
		
		
		
		
		
		
///////////////////////////////////////////////

	var  addWindow;
	var  addformPanel;

	addformPanel = new Ext.form.FormPanel({
				id : 'addform',
				name : 'addform',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 90,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
							fieldLabel : '计划名称',
							name : 'planname',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, 
						
						comboaddInspecPlanState,
						
						comboaddUser,
						
						comboxdevWithTree,
						
						 {
							fieldLabel : '计划开始时间',
					        xtype : 'datetimefield',
							name : 'executestartime', 
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						},{
							fieldLabel : '计划结束时间',
					        xtype : 'datetimefield',
							name : 'executeendtime', 
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : false
						}, {
							fieldLabel : '备注',
							name : 'remark',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : true,
							xtype: 'textarea'
						},{
							id : 'devid',
							name : 'devid',
							hidden : true
						}]
			});

	addWindow = new Ext.Window({
		layout : 'fit',
		width : 380,
		height : 323,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">新增巡检计划</span>',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
		buttonAlign : 'right',
		border : false,
		animCollapse : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		items : [addformPanel],
		buttons : [{
			text : '保存',
			iconCls : 'acceptIcon',
			handler : function() {
				
				if (addWindow.getComponent('addform').form.isValid()) {
					addWindow.getComponent('addform').form.submit({
						url : './inspectionPlan.do?reqCode=addplan',
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
							Ext.MessageBox.alert('提示', '新增失败:<br>' + msg);
				
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
						deptid : deptid ,

						queryParam : Ext.getCmp('queryParam').getValue(),
						
						startdatetime : Ext.getCmp('startdatetime').getValue(),
						
						enddatetime : Ext.getCmp('enddatetime').getValue()
					}
				});
	}

	/**
	 * 刷新
	 */
	function refreshTable()
	{
	
		//	store.load({
			//		params : {
			//			start : 0,
			//			limit : bbar.pageSize
			//		}
			//	});
			
			store.reload();
	}
});