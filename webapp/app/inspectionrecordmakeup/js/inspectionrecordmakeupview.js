/**
 * 巡检记录补录
 * 
 * @author yangcheng
 * @since now
 */





Ext.onReady(function() {
	
	
	
	function RecordMakeupRender(value) 
	{
		
		
    	if (value == '1') return '<span style="color:rgb(100,0,0)">编写</span>';
    	if (value == '2') return '<span style="color:rgb(180,0,0)">待审批</span>';
    	if (value == '3') return '<span style="color:rgb(255,0,0)">审批失败</span>';
    	if (value == '4') return '<span style="color:green">审批通过</span>';

    	return '<span style="color:rgb(255,0,0)">未知</span>';
}
	
	Ext.getDoc().on("contextmenu", function(e)
			{
				e.stopEvent();
			});
	
	var editgridshow = false;
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	        {
				
				header : '授权',
				
				dataIndex : 'planidkey',
				
				renderer : function(value, cellmeta, record)
				{
					
						if (!record.data['canapproval'])
						{
							return '<img src="../resource/image/ext/edit2.png"/>';
						}
						
						var statev = record.get("recordmakeupstate");
						
						if(statev != 2)
						{
							return '<img src="../resource/image/ext/edit2.png"/>';
						}
					
					
						return '<a href="javascript:void(0);"><img src="../resource/image/ext/edit1.png"/></a>';
				},
				
				width : 35
				
			},{
				header : '巡检记录补录申请人',
				dataIndex : 'recordmakeupapplyusername',
				width : 150
			}, {
				header : '巡检记录补录申请理由',
				dataIndex : 'recordmakeupapplycontent',
				width : 150
			},{
				header : '巡检记录补录申请时间',
				dataIndex : 'recordmakeupapplytime',
				width : 150
			}, {
				header : '巡检记录补录申请状态',
				dataIndex : 'recordmakeupstate',
				renderer:RecordMakeupRender,	
				width : 150
				
			}, {
				header : '待补录巡检计划名称',
				dataIndex : 'planname',
				width : 150
			},{
				header : '待补录巡检计划备注',
				dataIndex : 'planremark',
				hidden : true,
				width : 150
			}, {
				header : '巡检记录补录审批人',
				dataIndex : 'recordmakeupapproveusername',
				width : 150
			}, {
				header : '巡检记录补录审批信息',
				dataIndex : 'recordmakeupapprovecontent',
				width : 150
			},{
				header : '巡检记录补录审批时间',
				dataIndex : 'recordmakeupapprovetime',
				width : 150
			},{
				header : '巡检记录补录编号',
				dataIndex : 'recordmakeupid',
				sortable : true,
				hidden : true
			},{
				header : '巡检计划编号',
				dataIndex : 'inspecplanid',
				hidden : true
				
		    },{
				header : '巡检记录补录申请人编号',
				dataIndex : 'recordmakeupapplyuserid',
				hidden : true
			},{
				header : '巡检记录补录审批人编号',
				dataIndex : 'recordmakeupapproveuserid',
				hidden : true
			}]);
	
	

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './inspectionRecordMakeupView.do?reqCode=queryrecordmakeup'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'recordmakeupapplyusername'
								}, {
									name : 'recordmakeupapplycontent'
								}, {
									name : 'recordmakeupapplytime'
								}, {
									name : 'recordmakeupstate'
								}, {
									name : 'planname'
								}, {
									name : 'planremark'
								}, {
									name : 'recordmakeupapproveusername'
								}, {
									name : 'recordmakeupapprovecontent'
								}, {
									name : 'recordmakeupapprovetime'
								}, {
									name : 'recordmakeupid'
								}, {
									name : 'inspecplanid'
								}, {
									name : 'recordmakeupapplyuserid'
								}, {
									name : 'recordmakeupapproveuserid'
								},{
									name : 'canapproval'
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
				title : '<span class="commoncss">巡检记录补录列表</span>',
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
								ininEditWindow();
							}
						}, '-', {
							text : '删除',
							iconCls : 'page_delIcon',
							handler : function() 
							{
								deleteItems();
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
											emptyText : '申请人名称',
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

	grid.addListener('rowdblclick', ininEditWindow);
	grid.on('sortchange', function() {
				
			});

	bbar.on("change", function() {
				
			});
			
  grid.addListener('rowcontextmenu', fnRightClick);
  
  
  
  
	grid.on("cellclick", function(grid, rowIndex, columnIndex, e) 
	{
		
		
		var store = grid.getStore();
		var record = store.getAt(rowIndex);
		var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == 'planidkey' && columnIndex == 2) 
		{
			
			var canapproval = record.get('canapproval');

		
				if (!canapproval)
				{
					Ext.MessageBox.alert('提示',
							'当前用户没有审批权限<br>'
									+ '审批权限由调度科进行审批');
					return;
				}
				else
				{
					approvalProcess();
				}
			
		}
		
	});
  
	
	
	
	
  function fnRightClick(goodsGridUi, rowIndex, e) 
  {
        grid.getSelectionModel().selectRow(rowIndex);
		e.preventDefault();
		rightMenu.showAt(e.getXY());
}

var rightMenu = new Ext.menu.Menu(
    {
        id: "xMenu",
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
 
	  ininEditWindow();
	  
}


function fnMenuDel() 
  {
 
	 deleteItems();
	  
}


function AddInit()
{
	addWindow.getComponent('addform').form.reset()
	addWindow.show();
}
	


var recordmakeupAddStore = new Ext.data.SimpleStore({
	  fields : ['value', 'text'],
	  data : [
	          ['1', '编写']
	          ,
	          ['2', '待审批']
	        //  ,
	       //   ['5', '巡检进行']
	       //   ,
	       //   ['6', '巡检完成']
	        ]
	});

var comboaddrecordmakeupState = new Ext.form.ComboBox({ 
	anchor : '100%', 
	id:'recordmakeupstateadd', 
	name : "recordmakeupstateadd",
	hiddenName  : 'recordmakeupstate',
	typeAhead : true,
	lazyRender : true,
	valueField : 'value', 
	displayField : 'text', 
	value : '0',
	emptyText : '请选择...',
	fieldLabel : '补录状态', 
	allowBlank : false, 
	editable : false, 
	triggerAction : 'all', 
	store : recordmakeupAddStore ,
	forceSelection : true, 
	mode : 'local', 
	selectOnFocus : true ,
	editable : false,
	labelStyle : micolor,
	});



///////////////////////////////

var  storeaddinspecplan = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './inspectionRecordMakeupView.do?reqCode=queryinspecplan'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'id'},
		     {name : 'value'}
    ])
});



var comboaddInspecPlan = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'addinspecplan1', 
name : "addinspecplan1",
hiddenName  : 'inspecplanid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '巡检计划名称', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeaddinspecplan , 
forceSelection : true, 
mode : 'remote', //remote ,don't invoke load 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
});




storeaddinspecplan.load
( 
		{
			params : 
			{
				param : ''
				
			}
		}
);

comboaddInspecPlan.on('expand', function() 
    {
		
		storeaddinspecplan.reload
		( 
				{
					params : 
					{
						param : ''
						
					}
				}
		);

    });

////////////////////////////

		
		
///////////////////////////////////
		  
		  
		  var recordmakeupEditStore = new Ext.data.SimpleStore({
			  fields : ['value', 'text'],
			  data : [
			          ['1', '编写']
			          ,
			          ['2', '待审批']
			        //  ,
			        //  ['5', '巡检进行']
			        //  ,
			        //  ['6', '巡检完成']
			        ]
			});
		  
		  
		  var comboeditrecordmakeupState = new Ext.form.ComboBox({ 
				anchor : '100%', 
				id:'recordmakeupstateedit', 
				name : "recordmakeupstateedit",
				hiddenName  : 'recordmakeupstate',
				typeAhead : true,
				lazyRender : true,
				valueField : 'value', 
				displayField : 'text', 
				value : '0',
				emptyText : '请选择...',
				fieldLabel : '补录状态', 
				allowBlank : false, 
				editable : false, 
				triggerAction : 'all', 
				store : recordmakeupEditStore ,
				forceSelection : true, 
				mode : 'local', 
				selectOnFocus : true ,
				editable : false,
				labelStyle : micolor,
				});



			///////////////////////////////

		  var  storeeditinspecplan = new Ext.data.Store({   
			    proxy: new Ext.data.HttpProxy({   
			       url : './inspectionRecordMakeupView.do?reqCode=queryinspecplan'
			    }),   
			    reader: new Ext.data.JsonReader({
								totalProperty : 'TOTALCOUNT',
								root : 'ROOT'
			        },[
					     {name : 'id'},
					     {name : 'value'}
			    ])
			});



			var comboeditInspecPlan = new Ext.form.ComboBox({ 
			anchor : '100%', 
			id:'editinspecplan1', 
			name : "editinspecplan1",
			hiddenName  : 'inspecplanid',
			typeAhead : true,
			lazyRender : true,
			valueField : 'id', 
			displayField : 'value', 
			value : '0',
			emptyText : '请选择...',
			fieldLabel : '巡检计划名称', 
			allowBlank : false, 
			editable : false, 
			triggerAction : 'all', 
			store :storeeditinspecplan , 
			forceSelection : true, 
			mode : 'remote', //remote ,don't invoke load 
			selectOnFocus : true ,
			editable : false,
			labelStyle : micolor,
			});




			storeeditinspecplan.load
			( 
					{
						params : 
						{
							param : ''
							
						}
					}
			);

			comboeditInspecPlan.on('expand', function() 
			    {
					
				storeeditinspecplan.reload
					( 
							{
								params : 
								{
									param : ''
									
								}
							}
					);

			    });

////////////////////////////
////////////////////////////		
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
				items : [
				         
				        comboaddInspecPlan, 
						
				        comboaddrecordmakeupState,
						
						{
							fieldLabel : '补录理由',
							name : 'recordmakeupapplycontent',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : true,
							maxLength: 250,
							xtype: 'textarea'
						}]
			});

	addWindow = new Ext.Window({
		layout : 'fit',
		width : 350,
		height : 200,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">新增巡检记录补录申请</span>',
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
						url : './inspectionRecordMakeupView.do?reqCode=addrecordmakeup',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action)
						{
							   
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

	

	var editWindow, editFormPanel;
	editFormPanel = new Ext.form.FormPanel({
				labelAlign : 'right',
				labelWidth : 90,
				defaultType : 'textfield',
				frame : false,
				bodyStyle : 'padding:5 5 0',
				id : 'editform',
				name : 'editform',
				items : [
				        
					    comboeditInspecPlan, 
						
				        comboeditrecordmakeupState,
						
						{
							fieldLabel : '补录理由',
							name : 'recordmakeupapplycontent',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : true,
							maxLength: 250,
							xtype: 'textarea'
						},{
								id : 'recordmakeupid',
								name : 'recordmakeupid',
								hidden : true
						 }]
			});

	editWindow = new Ext.Window({
				layout : 'fit',
				width : 350,
				height : 200,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span class="commoncss">修改巡检计划补录申请</span>',
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
					id   : 'editbutton1',
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
	
	
	editWindow.on('show', function() 
			{
				
//				setTimeout(function()
//						{
//					
//						 
//						  
//				
//						},1);
			
			}, this);
	
	
	//////////////////////////
	
	
///////////////////////////////////////////////
	
	
//	var INSPECPLANSTATEStore = new Ext.data.SimpleStore({
//		  fields : ['value', 'text'],
//		  data : [
//		          ['1', '编写']
//		          ,
//		          ['2', '待审批']
//		          ,
//		          ['3', '审批失败']
//		          ,
//		          ['4', '审批通过']
//		          ,
//		          ['5', '巡检进行']
//		          ,
//		          ['6', '巡检完成']
//		        ]
//		});
	
	
	
	var recordmakeupApproStore = new Ext.data.SimpleStore({
		  fields : ['value', 'text'],
		  data : [
		          ['3', '审批失败']
		          ,
		          ['4', '审批通过']
		        ]
		});
	
	
	 var comboapprorecordmakeupState = new Ext.form.ComboBox({ 
			anchor : '100%', 
			id:'recordmakeupappro', 
			name : "recordmakeupappro",
			hiddenName  : 'recordmakeupstate2',
			typeAhead : true,
			lazyRender : true,
			valueField : 'value', 
			displayField : 'text', 
			value : '0',
			emptyText : '请选择...',
			fieldLabel : '更改状态', 
			allowBlank : false, 
			editable : false, 
			triggerAction : 'all', 
			store : recordmakeupApproStore ,
			forceSelection : true, 
			mode : 'local', 
			selectOnFocus : true ,
			editable : false,
			labelStyle : micolor,
			});

	var  approWindow;
	var  approformPanel;

	approformPanel = new Ext.form.FormPanel({
				id : 'approform',
				name : 'approform',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 90,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [						
						
						comboapprorecordmakeupState,
						
						{
							fieldLabel : '审批意见',
							name : 'approvecontent',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : true,
							xtype: 'textarea'
						},{
							id : 'recordmakeupid2',
							name : 'recordmakeupid',
							hidden : true
						}]
			});

	approWindow = new Ext.Window({
		layout : 'fit',
		width : 350,
		height : 173,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">巡检记录补录审批</span>',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
		buttonAlign : 'right',
		border : false,
		animCollapse : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		items : [approformPanel],
		buttons : [{
			text : '保存',
			iconCls : 'acceptIcon',
			handler : function() {
				
				if (approWindow.getComponent('approform').form.isValid()) {
					approWindow.getComponent('approform').form.submit({
						url : './inspectionRecordMakeupView.do?reqCode=approvalrecordmakeup',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action)
						{
							 
							   

							 store.reload();
							 
							approWindow.getComponent('approform').form.reset();
							approWindow.hide();
								
							
								
						},
						
						failure : function(form, action) 
						{
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '审批失败:<br>' + msg);
				
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
				approWindow.hide();
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
		
		

		Ext.getCmp('editbutton1').enable();
		Ext.getCmp('editbutton1').show();
		//comboeditrecordmakeupState.enable();
		editWindow.getComponent('editform').form.reset()
		
		record = grid.getSelectionModel().getSelected();
		editWindow.show();
		editFormPanel.getForm().loadRecord(record);
		
		var statev = record.get("recordmakeupstate");
		
		if(statev != 1 && statev != 3)
		{
			//comboeditrecordmakeupState.disable();

			Ext.getCmp('editbutton1').disable();
			Ext.getCmp('editbutton1').hide();
		}
		
		if(statev == 3)
		{
			comboeditrecordmakeupState.setRawValue('审批失败');
			comboeditrecordmakeupState.setValue('审批失败');
			
		}
		
		else if(statev == 4)
		{
			comboeditrecordmakeupState.setRawValue('审批通过');
			comboeditrecordmakeupState.setValue('审批通过');
		}
		
		if(statev > 4)
		{
			comboeditrecordmakeupState.setRawValue('未知');
			comboeditrecordmakeupState.setValue('未知');	
		}
		
		

	}

	/**
	 * 修改
	 */
	function updateItem() {
		if (!editFormPanel.form.isValid())
		{
			return;
		}
		
		editFormPanel.form.submit({
					url : './inspectionRecordMakeupView.do?reqCode=updaterecordmakeup',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editWindow.hide();
						store.reload();
					
					},
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.MessageBox.alert('提示', '修改失败:<br>' + msg);
					}
				});
	}

	
	function approvalProcess()
	{
		
		var record = grid.getSelectionModel().getSelected();
		if (Ext.isEmpty(record))
		{
			Ext.Msg.alert('提示', '请先选中要修改的项目');
			return;
		}
		
		record = grid.getSelectionModel().getSelected();
		
		var statev = record.get("recordmakeupstate");
		
		if(statev != 2)
		{
			Ext.Msg.alert('提示', '只有待审批状态的记录可以审批');
			return;
		}
		
		
		approWindow.getComponent('approform').form.reset()
		approWindow.show();
		approformPanel.getForm().loadRecord(record);
		
		
		// Ext.getCmp("approform").findById('approvetime3').disable();
		 

		 
//		 if(statev == 3 || statev == 4)
//		 {
//			 
//			 comboapproInspecPlanState.setValue(statev);
//		 }
		 
		 
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
		var strChecked = jsArray2JsString(rows, 'recordmakeupid');
		Ext.Msg.confirm('请确认', '你真的要删除吗?', function(btn, text) {
					if (btn == 'yes') {
						showWaitMsg();
							Ext.Ajax.request({
										url : './inspectionRecordMakeupView.do?reqCode=deleterecordmakeup',
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