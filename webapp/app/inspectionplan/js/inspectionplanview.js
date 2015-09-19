/**
 * 巡检计划
 * 
 * @author yangcheng
 * @since now
 */





Ext.onReady(function() {
	
	
	function INSPECPLANSTATERender2(value) 
	{  	
    	
    	if (value == '1') return '<span style="color:rgb(100,0,0)">编写</span>';
    	if (value == '2') return '<span style="color:rgb(180,0,0)">待审批</span>';
    	if (value == '3') return '<span style="color:rgb(255,0,0)">审批失败</span>';
    	if (value == '4') return '<span style="color:green">审批通过</span>';
    	if (value == '5') return '<span style="color:green">巡检进行</span>';
    	if (value == '6') return '<span style="color:green">巡检完成</span>';

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
						
						var statev = record.get("state");
						
						if(statev != 2)
						{
							return '<img src="../resource/image/ext/edit2.png"/>';
						}
					
					
						return '<a href="javascript:void(0);"><img src="../resource/image/ext/edit1.png"/></a>';
				},
				
				width : 35
				
			},{
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
				renderer:INSPECPLANSTATERender2,	
				width : 100
				
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
			var planid = record.get('planid');
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
	


var INSPECPLANSTATEAddStore = new Ext.data.SimpleStore({
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
	store : INSPECPLANSTATEAddStore ,
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
						fieldLabel : '巡检设备',
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
				   Ext.getCmp("addform").findById('devid1').setValue('');
				   
					var rows = griddev.getSelectionModel().getSelections();
					var devids = '';
					var devnames = '';
					
					if(rows.length <= 0)
					{
						comboxdevWithTree.setValue(',');
						Ext.getCmp("addform").findById('devid1').setValue(',');
						   
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
					Ext.getCmp("addform").findById('devid1').setValue(devids);
					
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
				
		
////////////////////////////////////
///////////////////////////////////
		  
		  
		  var INSPECPLANSTATEEditStore = new Ext.data.SimpleStore({
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
		  
		  
		  var comboeditInspecPlanState = new Ext.form.ComboBox({ 
				anchor : '100%', 
				id:'inspecplanstate2', 
				name : "inspecplanstate2",
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
				store : INSPECPLANSTATEEditStore ,
				forceSelection : true, 
				mode : 'local', 
				selectOnFocus : true ,
				editable : false,
				labelStyle : micolor,
				});



			///////////////////////////////

			var  storeeditUser = new Ext.data.Store({   
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



			var comboeditUser = new Ext.form.ComboBox({ 
			anchor : '100%', 
			id:'adduser2', 
			name : "adduser2",
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




			storeeditUser.load
			( 
					{
						params : 
						{
							deptid : deptid
							
						}
					}
			);

					comboeditUser.on('expand', function() 
				    {
						
						storeeditUser.reload
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

					
					var smdev2 = new Ext.grid.CheckboxSelectionModel();
					var cmdev2  = new Ext.grid.ColumnModel( [ new Ext.grid.RowNumberer({header : '序号',width : 40}), smdev2, 
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
						
					var  storeeditdev = new Ext.data.Store({   
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




				var grideditdev = new Ext.grid.GridPanel( {
							//title : '<span class="commoncss">部门信息表</span>',
							height : 500,
							width:600,
							autoScroll : true,
							region : 'center',
							store : storeeditdev,
							loadMask : {
								msg : '正在加载表格数据,请稍等...'
							},
							stripeRows : true,
							frame : true,
							cm : cmdev2,
							sm : smdev2,
							autoExpandColumn : 'devname'
							
						});

						
						grideditdev.on('rowdblclick', function(grideditdev, rowIndex, event) 
						{
							
						});
						
						
						
				var  storeeditdevtree = new Ext.data.SimpleStore( {
										fields : ['userid','username'],
										data : [ [] ]
									});
									
				var comboxeditdevWithTree = new Ext.form.ComboBox(
								{
									id : 'devtree2',
									name: 'devtree2',
									anchor : '100%', 
									
									store : storeeditdevtree,
									
									hiddenName  : 'devname',

									valueField : 'devid', 
									displayField : 'devname', 
									
									editable : false,
									value : ' ',
									emptyText : '请选择...',
									fieldLabel : '巡检设备',
									anchor : '100%',
									mode : 'local',
									triggerAction : 'all',
									labelStyle: micolor,
									maxHeight : 390,
									// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
									tpl : "<tpl for='.'><div style='height:390px;'><div id='editdevTreeDiv'></div></div></tpl>",
									allowBlank : false,
									onSelect : Ext.emptyFn
								});
								
						// 监听下拉框的下拉展开事件
				comboxeditdevWithTree.on('expand', function()
						{
								// 将UI树挂到treeDiv容器
					
							  grideditdev.render('editdevTreeDiv');
								
							  var planid =  Ext.getCmp("editform").findById('planid').getValue('');
					
							
							  storeeditdev.reload( {
									params : {
										start : 0,
										limit :9999,
										planid : planid
									}
								});

						});
							
							
				
				     function devGridComBoxFill()
				     {
				    	   comboxeditdevWithTree.setValue('');
						   Ext.getCmp("editform").findById('devid2').setValue('');
						   
							var rows = grideditdev.getSelectionModel().getSelections();
							var devids = '';
							var devnames = '';
							
							if(rows.length <= 0)
							{
								//alert('aaa');
								
								comboxeditdevWithTree.setValue(',');
								Ext.getCmp("editform").findById('devid2').setValue(',');
								   
							   return;
							}
							
							for ( var i = 0; i < rows.length; i++) 
							{
								devids += rows[i].get('devid');
								devids += ',';
								
								devnames += rows[i].get('devname');
								devnames += ',';
						    }


							comboxeditdevWithTree.setValue(devnames);
							Ext.getCmp("editform").findById('devid2').setValue(devids);
							
							//alert('xxx');
				     }
							
						comboxeditdevWithTree.on('collapse', function() 
						{
							  
							devGridComBoxFill();
					  });
					  
						
						function firstDevSelCombox()
						{
							
								//  alert('load');
							  
								var devids = '';
								var devnames = ''
					  
								var total = storeeditdev.getCount();
								for(var i=0;i<total;i++)
								{
								  var record = storeeditdev.getAt(i);
								  
								
								    if(record.get("initialcheck"))
									{
								    	
										devids += record.get('devid');
										devids += ',';
										
										devnames += record.get('devname');
										devnames += ',';
									}
								}
								
								if(devids == '')
								{
									devids = ',';
									devnames = ',';
								}
								
								
								comboxeditdevWithTree.setValue(devnames);
								Ext.getCmp("editform").findById('devid2').setValue(devids);
						}
						
					  
					  grideditdev.store.on("load",function()
					  {  
					  
						  
						
						  firstDevSelCombox();
						
					  
						var total = grideditdev.store.getCount();
						for(var i=0;i<total;i++)
						{
						  var record = grideditdev.store.getAt(i);
						  
						 // alert(userrecord.get("username"));
						 
						    if(record.get("initialcheck"))
							{
//						    	if(typeof(grideditdev.getSelectionModel().a) == 'undefined' )
//						    	{
//						    		
//						    	}
						    	
						    	if(editgridshow)
						    	{
						    		
						    	}
						    	else
						    	{
						    		grideditdev.getSelectionModel().selectRow(i,true);
						    	}
							}
						}
						
						editgridshow = false;
						
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
							maxLength: 50,
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
							maxLength: 250,
							xtype: 'textarea'
						},{
							id : 'devid1',
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
				        
							{
								fieldLabel : '计划名称',
								name : 'planname',
								anchor : '100%',
								labelStyle : micolor,
								maxLength: 50,
								allowBlank : false
							}, 
							
							comboeditInspecPlanState,
							
							comboeditUser,
							
							comboxeditdevWithTree,
							
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
								maxLength: 250,
								xtype: 'textarea'
							},{
								id : 'devid2',
								name : 'devid',
								hidden : true
							},{
								id : 'planid',
								name : 'planid',
								hidden : true
							}
						
						]
			});

	editWindow = new Ext.Window({
				layout : 'fit',
				width : 380,
				height : 323,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span class="commoncss">修改巡检计划</span>',
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
				
				setTimeout(function()
						{
					
						  var planid =  Ext.getCmp("editform").findById('planid').getValue('');
							
							
						  storeeditdev.load( {
								params : {
									start : 0,
									limit :9999,
									planid : planid
								}
							});
						  
						  editgridshow = true;
						  
				
						},1);
			
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
	
	
	
	var INSPECPLANSTATEApproStore = new Ext.data.SimpleStore({
		  fields : ['value', 'text'],
		  data : [
		          ['3', '审批失败']
		          ,
		          ['4', '审批通过']
		        ]
		});
	
	
	 var comboapproInspecPlanState = new Ext.form.ComboBox({ 
			anchor : '100%', 
			id:'inspecplanstate3', 
			name : "inspecplanstate3",
			hiddenName  : 'approstate',
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
			store : INSPECPLANSTATEApproStore ,
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
//						 {
//							fieldLabel : '审批时间',
//					        xtype : 'datetimefield',
//							name : 'approvetime', 
//							id   : 'approvetime3', 
//							anchor : '100%',
//							labelStyle : micolor,
//							allowBlank : false
//							
//						}, 
						
						comboapproInspecPlanState,
						
						{
							fieldLabel : '审批意见',
							name : 'approvecontent',
							anchor : '100%',
							labelStyle : micolor,
							allowBlank : true,
							xtype: 'textarea'
						},{
							id : 'planid2',
							name : 'planid',
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
		title : '<span class="commoncss">巡检计划审批</span>',
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
						url : './inspectionPlan.do?reqCode=approvalplan',
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
		//comboeditInspecPlanState.enable();
		editWindow.getComponent('editform').form.reset()
		
		record = grid.getSelectionModel().getSelected();
		editWindow.show();
		editFormPanel.getForm().loadRecord(record);
		
		var statev = record.get("state");
		
		if(statev != 1  && statev != 3)
		{
			//comboeditInspecPlanState.disable();

			Ext.getCmp('editbutton1').disable();
			Ext.getCmp('editbutton1').hide();
		}
		
		if(statev == 3)
		{
			comboeditInspecPlanState.setRawValue('审批失败');
			comboeditInspecPlanState.setValue('审批失败');
		}
		
		else if(statev == 4)
		{
			comboeditInspecPlanState.setRawValue('审批通过');
			comboeditInspecPlanState.setValue('审批通过');
		}
		
		else if(statev == 5)
		{
			comboeditInspecPlanState.setRawValue('巡检进行');
			comboeditInspecPlanState.setValue('巡检进行');
		}
		
		else if(statev == 6)
		{
			comboeditInspecPlanState.setRawValue('巡检完成');
			comboeditInspecPlanState.setValue('巡检完成');
		}

		if(statev > 6)
		{
			comboeditInspecPlanState.setRawValue('未知');
			comboeditInspecPlanState.setValue('未知');
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
					url : './inspectionPlan.do?reqCode=updateplan',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editWindow.hide();
						store.reload();
					
					},
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.MessageBox.alert('提示', '修改设备厂商失败:<br>' + msg);
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
		
		var statev = record.get("state");
		
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
		var strChecked = jsArray2JsString(rows, 'planid');
		Ext.Msg.confirm('请确认', '你真的要删除吗?', function(btn, text) {
					if (btn == 'yes') {
						showWaitMsg();
							Ext.Ajax.request({
										url : './inspectionPlan.do?reqCode=deleteplan',
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