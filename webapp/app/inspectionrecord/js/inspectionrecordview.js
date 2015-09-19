/**
 * 巡检记录查看
 * 
 * @author yangcheng
 * @since now
 */





Ext.onReady(function() 
{
	Ext.getDoc().on("contextmenu", function(e)
	{
		e.stopEvent();
	});
	
	function DeviceCheckStateRender(value) 
	{
    	if (value == '1') return '<span style="color:green">正常</span>';
    	if (value == '0') return '<span style="color:red">故障</span>';

    	return '<span style="color:red">未知</span>';
	}
	
	
	function MediaInfoTypeRender(value) 
	{
    	if (value == '0') return '<span style="color:green">图片</span>';
    	if (value == '1') return '<span style="color:green">音频</span>';
    	if (value == '2') return '<span style="color:green">视频</span>';

    	return '<span style="color:red">未知</span>';
	}
	
	var editgridshow = false;
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	        {
				
				header : '巡检记录开始时间',
				
				dataIndex : 'recordexecutestarttime',
				
				width : 150
				
			},{
				header : '巡检记录结束时间',
				dataIndex : 'recordexecuteendtime',
				width : 150
			}, {
				header : '巡检计划名称',
				dataIndex : 'planname',
				width : 150
			},{
				header : '巡检计划申请人',
				dataIndex : 'planapplyusername',
				width : 150
			},{
				header : '巡检计划申请时间',
				dataIndex : 'planapplytime',
				width : 150
			}, {
				header : '巡检计划状态',
				dataIndex : 'planstate',
				renderer:INSPECPLANSTATERender,	
				hidden : true,
				width : 150
				
			}, {
				header : '巡检计划执行人',
				dataIndex : 'planexecuteusername',
				width : 150
			},{
				header : '巡检计划开始时间',
				dataIndex : 'planexecutestartime',
				width : 150
			}, {
				header : '巡检计划结束时间',
				dataIndex : 'planexecuteendtime',
				width : 150
			},{
				header : '巡检计划审批人',
				dataIndex : 'planapproveusername',
				width : 150
			}, {
				header : '巡检计划审批时间',
				dataIndex : 'planapprovetime',
				width : 150
			},{
				header : '巡检计划审批信息',
				dataIndex : 'planapprovecontent',
				width : 150
			},{
				header : '巡检计划备注',
				dataIndex : 'planremark',
				hidden : true,
				id : 'remark',
				width : 180
			},{
				header : '记录编号',
				dataIndex : 'recordid',
				hidden : true
			},{
				header : '巡检计划编号',
				dataIndex : 'planid',
				hidden : true,
				sortable : true
			},{
				header : '巡检计划申请人编号',
				dataIndex : 'planapplyuserid',
				hidden : true
			},{
				header : '巡检计划执行人编号',
				dataIndex : 'planexecuteuserid',
				hidden : true
				
		    },{
				header : '巡检计划审批人编号',
				dataIndex : 'planapproveuserid',
				hidden : true
			}]);
	
	

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './inspectionRecordView.do?reqCode=queryrecord'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [
						    	{
									name : 'recordexecutestarttime'
								},
								{
									name : 'recordexecuteendtime'
								},{
									name : 'planname'
								}, {
									name : 'planapplyusername'
								}, {
									name : 'planapplytime'
								}, {
									name : 'planstate'
								}, {
									name : 'planexecuteusername'
								}, {
									name : 'planexecutestartime'
								}, {
									name : 'planexecuteendtime'
								}, {
									name : 'planapproveusername'
								}, {
									name : 'planapprovecontent'
								}, {
									name : 'planapprovetime'
								}, {
									name : 'planremark'
								}, {
									name : 'recordid'
								},{
									name : 'planid'
								}, {
									name : 'planapplyuserid'
								}, {
									name : 'planexecuteuserid'
								}, {
									name : 'planapproveuserid'
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
				title : '<span class="commoncss">巡检记录列表</span>',
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
							text : '查看详情',
							iconCls : 'previewIcon',
							handler : function()
							{
								
							}
						}, '-', '->',
						
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
											emptyText : '部门名称|执行人名称',
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

	grid.addListener('rowdblclick', showInspecRecord);
	
	grid.on('sortchange', function() 
			{
				
			});

	bbar.on("change", function() 
			{
				
			});
			
  grid.addListener('rowcontextmenu', fnRightClick);
  
  
  
  
	grid.on("cellclick", function(grid, rowIndex, columnIndex, e) 
	{
		
		
		
		
	});
  
	
	
	
	
  function fnRightClick(goodsGridUi, rowIndex, e) 
  {
        grid.getSelectionModel().selectRow(rowIndex);
		e.preventDefault();
		rightMenu.showAt(e.getXY());
}

var rightMenu = new Ext.menu.Menu(
    {
        id: "xMenu1",
        items:
        [
		
            {
                id: 'Edit1',
              //  icon: 'images/write.gif', //图标文件
			   iconCls : 'page_edit_1Icon',
                handler: fnMenuEdit,
                text: '查看详情'
            }
            
        ]
    });
	
	


function fnMenuEdit() 
 {
	
	showInspecRecord();
	  
}


var recordInfoMenu = new Ext.menu.Menu(
	    {
	        id: "xMenu2",
	        items:
	        [
			
	            {
	                id: 'Edit2',
	              //  icon: 'images/write.gif', //图标文件
				   iconCls : 'page_edit_1Icon',
	                handler: fnrecordInfoEdit,
	                text: '查看媒体信息'
	            }
	            
	        ]
	    });


function fnrecordInfoEdit() 
{

    var record = grid2.getSelectionModel().getSelected();
	
	var recordinfoid = record.get('recordinfoid');
	
	Ext.getCmp('whichtbl').setValue('1');
	Ext.getCmp('releateid').setValue(recordinfoid);
	
	var whichtbl = Ext.getCmp('whichtbl').getValue();
	var releateid = Ext.getCmp('releateid').getValue();
	
	store4.load({
		params : {
			start : 0,
			limit : bbar4.pageSize,
			whichtbl : whichtbl ,
			releateid : releateid
		}
	});
	
	
	querymediaWindow.show();
	  
}




var recordInfoItemMenu = new Ext.menu.Menu(
	    {
	        id: "xMenu3",
	        items:
	        [
			
	            {
	                id: 'Edit3',
	              //  icon: 'images/write.gif', //图标文件
				   iconCls : 'page_edit_1Icon',
	                handler: fnrecordInfoItemEdit,
	                text: '查看媒体信息'
	            }
	            
	        ]
	    });


function fnrecordInfoItemEdit() 
{

    var record = grid3.getSelectionModel().getSelected();
	
	var recorditemid = record.get('recorditemid');
	
	Ext.getCmp('whichtbl').setValue('2');
	Ext.getCmp('releateid').setValue(recorditemid);
	
	var whichtbl = Ext.getCmp('whichtbl').getValue();
	var releateid = Ext.getCmp('releateid').getValue();
	
	store4.load({
		params : {
			start : 0,
			limit : bbar4.pageSize,
			whichtbl : whichtbl ,
			releateid : releateid
		}
	});
	
	
	querymediaWindow.show();
	  
}


///////////////////////////////////////

		
function showInspecRecord() 
{

	
	var record = grid.getSelectionModel().getSelected();
	
	if (Ext.isEmpty(record)) {
		
		Ext.MessageBox.alert('提示', '请先选择要查看的记录!');
		return;
	}
	
	var recordid = record.get('recordid');
	
	
	Ext.getCmp('recordid').setValue(recordid);
	
	
	store2.load({
		params : {
			start : 0,
			limit : bbar2.pageSize,
			recordid : recordid
		}
	});
	
	
	grid2.getStore().removeAll();
	grid3.getStore().removeAll();
	
	queryWindow.show();
	
	
	  
}
		
		
		

	
	
	//////////////////////////
	
	
	
	
	/**
	 * 布局
	 */
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});





	
	

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
	
	////////////////////////////
	
	
	
    var sm2 = new Ext.grid.CheckboxSelectionModel();
	var cm2 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm2, 
	        {
				header : '检查设备状态',
				dataIndex : 'devstate',
				renderer:DeviceCheckStateRender,	
				width : 150
			},{
				header : '检查设备名称',
				dataIndex : 'devname',
				width : 150
				
			}, {
				header : '检查信息提交时间',
				dataIndex : 'recordinfotime',
				width : 150
			},{
				header : '检查信息备注',
				dataIndex : 'recordinforemark',
				width : 150
			},{
				header : '检查设备型号名称',
				dataIndex : 'devmodelname',
				width : 150
			},  {
				header : '父设备名称',
				dataIndex : 'parentdevname',
				hidden : true,
				width : 150
			},{
				header : '设备备注',
				dataIndex : 'devremark',
				hidden : true,
				width : 150
			}, {
				header : '设备编号',
				dataIndex : 'devid',
				hidden : true,
				sortable : true
			},{
				header : '父设备编号',
				dataIndex : 'parentdevid',
				hidden : true
			},{
				header : '设备型号编号',
				dataIndex : 'devmodelid',
				hidden : true
			},{
				header : '计划设备编号',
				dataIndex : 'plandevid',
				hidden : true
			},{
				header : '记录编号',
				dataIndex : 'recordid',
				hidden : true
				
		    },{
				header : '记录信息编号',
				dataIndex : 'recordinfoid',
				hidden : true
			}]);
	
	

	var store2 = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './inspectionRecordView.do?reqCode=queryrecordinfo'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [
						    	{
									name : 'devname'
								},
								{
									name : 'devstate'
								},{
									name : 'recordinfotime'
								}, {
									name : 'recordinforemark'
								}, {
									name : 'devmodelname'
								}, {
									name : 'parentdevname'
								}, {
									name : 'devremark'
								}, {
									name : 'recordinfoid'
								}, {
									name : 'recordid'
								}, {
									name : 'plandevid'
								}, {
									name : 'devid'
								}, {
									name : 'parentdevid'
								}, {
									name : 'devmodelid'
								}])
			});
	
	

	// 翻页排序时带上查询条件
	store2.on('beforeload', function()
			{
		
				var recordid = Ext.getCmp('recordid').getValue();
		
	
		
				this.baseParams = 
				{
					queryParam : '',
					recordid : recordid
	
				};
			});

	var pagesize_combo2 = new Ext.form.ComboBox({
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
	
	var number2 = parseInt(pagesize_combo2.getValue());
	
	pagesize_combo2.on("select", function(comboBox) 
			{
				bbar2.pageSize = parseInt(comboBox.getValue());
				number2 = parseInt(comboBox.getValue());
				
				var recordid = Ext.getCmp('recordid').getValue();
				
				store2.reload({
							params : {
								start : 0,
								limit : bbar2.pageSize,
								recordid : recordid
							}
						});
			});

	var bbar2 = new Ext.PagingToolbar({
				pageSize : number2,
				store : store2,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;', pagesize_combo2]
			})

	var grid2 = new Ext.grid.GridPanel({
				title : '<span class="commoncss">各个设备检查列表</span>',
				height : 240,
				store : store2,
				region : 'center',
				margins : '3 3 3 3',
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
			//	autoExpandColumn : 'remark',
				cm : cm2,
				sm : sm2,
				bbar : bbar2
			});
	


	grid2.addListener('rowdblclick',function() 
	{
		
		
//		var record = grid2.getSelectionModel().getSelected();
//		
//		if (Ext.isEmpty(record)) {
//			
//			Ext.MessageBox.alert('提示', '请先选择要查看的详细记录!');
//			return;
//		}
//		
//		var recordinfoid = record.get('recordinfoid');
//		
//		
//		Ext.getCmp('recordinfoid').setValue(recordinfoid);
//		
//		
//		store3.load({
//			params : {
//				start : 0,
//				limit : bbar3.pageSize,
//				recordinfoid : recordinfoid
//			}
//		});
		
		
		    var record = grid2.getSelectionModel().getSelected();
		
			var recordinfoid = record.get('recordinfoid');
			
			Ext.getCmp('whichtbl').setValue('1');
			Ext.getCmp('releateid').setValue(recordinfoid);
			
			var whichtbl = Ext.getCmp('whichtbl').getValue();
			var releateid = Ext.getCmp('releateid').getValue();
			
			store4.load({
				params : {
					start : 0,
					limit : bbar4.pageSize,
					whichtbl : whichtbl ,
					releateid : releateid
				}
			});
			
			
			querymediaWindow.show();

			
		
		
		
	});
	
	
	grid2.addListener('rowclick',function() 
			{
				var record = grid2.getSelectionModel().getSelected();
				
				if (Ext.isEmpty(record)) {
					
					Ext.MessageBox.alert('提示', '请先选择要查看的详细记录!');
					return;
				}
				
				var recordinfoid = record.get('recordinfoid');
				
				
				Ext.getCmp('recordinfoid').setValue(recordinfoid);
				
				
				store3.load({
					params : {
						start : 0,
						limit : bbar3.pageSize,
						recordinfoid : recordinfoid
					}
				});
			});
	
	grid2.on('sortchange', function() 
			{
				
			});

	bbar2.on("change", function() 
			{
				
			});
			

  
  
  
  
	grid2.on("cellclick", function(grid2, rowIndex, columnIndex, e) 
	{
		
		
//		var store = grid2.getStore();
//		var record = store2.getAt(rowIndex);
//		var fieldName = grid2.getColumnModel().getDataIndex(columnIndex);
//		if (fieldName == 'mediainfo') 
//		{
//			var recordinfoid = record.get('recordinfoid');
//			
//			
//			Ext.getCmp('whichtbl').setValue('1');
//			Ext.getCmp('releateid').setValue(recordinfoid);
//			
//			var whichtbl = Ext.getCmp('whichtbl').getValue();
//			var releateid = Ext.getCmp('releateid').getValue();
//			
//			store4.load({
//				params : {
//					start : 0,
//					limit : bbar4.pageSize,
//					whichtbl : whichtbl ,
//					releateid : releateid
//				}
//			});
//			
//			
//			querymediaWindow.show();
//
//			
//		}
		
	});
	
	
	grid2.addListener('rowcontextmenu', fnrecordInfoRightClick);
	
	
	
	  function fnrecordInfoRightClick(goodsGridUi, rowIndex, e) 
	  {
	        grid2.getSelectionModel().selectRow(rowIndex);
			e.preventDefault();
			recordInfoMenu.showAt(e.getXY());
	}
	
	//////////////////////////////////////
	
	
	
	
	
	////////////////////////////
	
	
	
    var sm3 = new Ext.grid.CheckboxSelectionModel();
	var cm3 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm3, 
	        
	        {
				header : '检查状态',
				dataIndex : 'recorditemstate',
				renderer:DeviceCheckStateRender,	
				width : 150
			},{
				header : '设备检查项名称',
				dataIndex : 'devcheckname',
				width : 150
				
			},{
				header : '设备检查项内容',
				dataIndex : 'devcheckcontent',
				width : 150
			}, {
				header : '设备检查项备注',
				dataIndex : 'devcheckremark',
				hidden : true,
				width : 150
			},{
				header : '设备类型名称',
				dataIndex : 'devtypename',
				width : 150
			},{
				header : '设备类型备注',
				dataIndex : 'devtyperemark',
				hidden : true,
				width : 150
			},{
				header : '检查备注',
				dataIndex : 'recorditemremark',
				width : 150
			}, {
				header : '记录项编号',
				dataIndex : 'recorditemid',
				hidden : true,
				sortable : true
			},{
				header : '记录信息编号',
				dataIndex : 'recordinfoid',
				hidden : true
			},{
				header : '设备检查项编号',
				dataIndex : 'devcheckcontentid',
				hidden : true
			}]);
	
	

	var store3 = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './inspectionRecordView.do?reqCode=queryrecordinfoitem'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [
						    	{
									name : 'devcheckname'
								},
								{
									name : 'devcheckcontent'
								},{
									name : 'devcheckremark'
								}, {
									name : 'devtypename'
								}, {
									name : 'devtyperemark'
								}, {
									name : 'recorditemstate'
								}, {
									name : 'recorditemremark'
								}, {
									name : 'recorditemid'
								}, {
									name : 'recordinfoid'
								}, {
									name : 'devcheckcontentid'
								}])
			});
	
	

	// 翻页排序时带上查询条件
	store3.on('beforeload', function() 
			{
		
				var recordinfoid = Ext.getCmp('recordinfoid').getValue();
		
				this.baseParams = 
				{
					queryParam : '',
					recordinfoid : recordinfoid
	
				};
			});

	var pagesize_combo3 = new Ext.form.ComboBox({
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
	
	var number3 = parseInt(pagesize_combo3.getValue());
	
	pagesize_combo3.on("select", function(comboBox) 
			{
				bbar3.pageSize = parseInt(comboBox.getValue());
				number3 = parseInt(comboBox.getValue());
				
				var recordid = Ext.getCmp('recordid').getValue();
				
				store3.reload({
							params : {
								start : 0,
								limit : bbar3.pageSize,
								recordid : recordid
							}
						});
			});

	var bbar3 = new Ext.PagingToolbar({
				pageSize : number3,
				store : store3,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;', pagesize_combo3]
			})

	var grid3 = new Ext.grid.GridPanel({
				title : '<span class="commoncss">当前设备各项检查状况</span>',
				height : 230,
				store : store3,
				region : 'center',
				margins : '3 3 3 3',
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
			//	autoExpandColumn : 'remark',
				cm : cm3,
				sm : sm3,
				bbar : bbar3
			});
	


	grid3.addListener('rowdblclick',function() 
	{
	
	    var record = grid3.getSelectionModel().getSelected();
		
		var recorditemid = record.get('recorditemid');
		
		Ext.getCmp('whichtbl').setValue('2');
		Ext.getCmp('releateid').setValue(recorditemid);
		
		var whichtbl = Ext.getCmp('whichtbl').getValue();
		var releateid = Ext.getCmp('releateid').getValue();
		
		store4.load({
			params : {
				start : 0,
				limit : bbar4.pageSize,
				whichtbl : whichtbl ,
				releateid : releateid
			}
		});
		
		
		querymediaWindow.show();
		
		
	});
	
	grid3.on('sortchange', function() 
			{
				
			});

	bbar3.on("change", function() 
			{
				
			});
			

  
  
  
  
	grid3.on("cellclick", function(grid3, rowIndex, columnIndex, e) 
	{
		
		
		
		
	});
	
	
	grid3.addListener('rowcontextmenu', fnrecordInfoItemRightClick);
	
	
	
	  function fnrecordInfoItemRightClick(goodsGridUi, rowIndex, e) 
	  {
	        grid3.getSelectionModel().selectRow(rowIndex);
			e.preventDefault();
			recordInfoItemMenu.showAt(e.getXY());
	}
	
	//////////////////////////////////////
	
	
	///////////////////////////////////////////////
	var  queryWindow;
	var  queryPanel;

	queryPanel = new Ext.Panel({
				id : 'queryPanel',
				name : 'queryPanel',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 90,
				frame : false,
				bodyStyle : 'padding:0 0 0 0',
				items : [						
						
							grid2,
							
							grid3,
							
							
							{
								id : 'recordid',
								name : 'recordid',
								hidden : true
							},
							
							{
								id : 'recordinfoid',
								name : 'recordinfoid',
								hidden : true
							}
						]
			});

	queryWindow = new Ext.Window({
		layout : 'fit',
		width :  600,
		height : 500,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">巡检记录详细信息</span>',
		modal : true,
		collapsible : true,
		titleCollapse : true,
		maximizable : false,
		buttonAlign : 'right',
		border : false,
		animCollapse : true,
		animateTarget : Ext.getBody(),
		constrain : true,
		items : [queryPanel]
		
	});
	
	
	/////////////////////////////////////////////
	
	
	
	////////////////////////////
	
	
	
    var sm4 = new Ext.grid.CheckboxSelectionModel();
	var cm4 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm4, 
	        {
				header : '媒体类型',
				dataIndex : 'mediatype',
				renderer:MediaInfoTypeRender,	
				width : 50
			},{
				header : '媒体地址',
				dataIndex : 'mediaaddress',
				width : 450
			},{
				header : '媒体信息',
				dataIndex : 'mediaremark',
				width : 150
				
			},{
				header : '媒体创建时间',
				dataIndex : 'mediatime',
				width : 150
			},{
				header : '媒体摘要',
				dataIndex : 'mediafigure',
				hidden : true,
				width : 150
			},  {
				header : '相关备注',
				dataIndex : 'releateremark',
				hidden : true,
				width : 150
			},{
				header : '媒体编号',
				dataIndex : 'mediaid',
				hidden : true,
				sortable : true,
				width : 150
			}, {
				header : '媒体信息编号',
				dataIndex : 'mediainfoid',
				hidden : true
				
			},{
				header : '相关记录编号',
				dataIndex : 'releaterecordid',
				hidden : true
			}]);
	
	

	var store4 = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './inspectionRecordView.do?reqCode=queryrecordmediainfo'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [
						    	{
									name : 'mediaremark'
								},
								{
									name : 'mediaaddress'
								},{
									name : 'mediatype'
								}, {
									name : 'mediatime'
								}, {
									name : 'mediafigure'
								}, {
									name : 'releateremark'
								}, {
									name : 'mediaid'
								}, {
									name : 'mediainfoid'
								}, {
									name : 'releaterecordid'
								}])
			});
	
	

	// 翻页排序时带上查询条件
	store4.on('beforeload', function() 
			{
		
				var whichtbl = Ext.getCmp('whichtbl').getValue();
				var releateid = Ext.getCmp('releateid').getValue();
		
				this.baseParams = 
				{
					queryParam : '',
					whichtbl : whichtbl ,
					releateid : releateid
	
				};
			});

	var pagesize_combo4 = new Ext.form.ComboBox({
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
	
	var number4 = parseInt(pagesize_combo4.getValue());
	
	pagesize_combo4.on("select", function(comboBox) 
			{
				bbar4.pageSize = parseInt(comboBox.getValue());
				number4 = parseInt(comboBox.getValue());
				
				
				var whichtbl = Ext.getCmp('whichtbl').getValue();
				var releateid = Ext.getCmp('releateid').getValue();
				
				store4.reload({
							params : {
								start : 0,
								limit : bbar4.pageSize,
								whichtbl : whichtbl ,
								releateid : releateid
							}
						});
			});

	var bbar4 = new Ext.PagingToolbar({
				pageSize : number4,
				store : store4,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;', pagesize_combo4]
			})

	var grid4 = new Ext.grid.GridPanel({
				title : '<span class="commoncss">媒体列表</span>',
				height : 270,
				store : store4,
				region : 'center',
				margins : '3 3 3 3',
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
			//	autoExpandColumn : 'remark',
				cm : cm4,
				sm : sm4,
				bbar : bbar4
			});
	

//	function openWindow(id,title,url,width,height)
//	{
//	    var win = Ext.get(id)
//	    if (win)
//	    {
//	        win.close();
//	        return;
//	    }
//	    
//	    win = new Ext.Window(
//	    	{
//	        id:id,
//	        title:title,
//	        layout:'fit',
//	        width:width,
//	        height:height,
//	        closeAction:'close',
//	        collapsible:true,
//	        plain: false,
//	        resizable: true,
//	        html : 'http://192.168.128.250:8080/media/2015-09-07/1.jpg'
//	    });
//	    win.show();
//	}
//	
//	function closewin()
//	{
//	    var win = parent.Ext.getCmp('b-win');
//	    if (win) {win.close();}
//	}
	
	//openWindow('b-win','窗口中打开b页面','b.htm',400,300);
	
	
	
	var wnd = null;
	
		
	grid4.addListener('rowdblclick',function() 
	{
	
		var record = grid4.getSelectionModel().getSelected();
		
		if (Ext.isEmpty(record)) {
			
			Ext.MessageBox.alert('提示', '请先选择记录!');
			return;
		}
		
		var mediaaddress = record.get('mediaaddress');
		
		
		var webaddress = mediaaddress;//'http://192.168.128.250:8080/media/2015-09-07/1.jpg';
		
		if(wnd != null)
		{
				wnd.close();
		}
		
		wnd = window.open (webaddress,'mediawindow','height=450,width=450,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');	
		
		
	});
	
	grid4.on('sortchange', function() 
			{
				
			});

	bbar4.on("change", function() 
			{
				
			});
			

  
  
  
  
	grid4.on("cellclick", function(grid4, rowIndex, columnIndex, e) 
	{
		
		

		
	});
	
	
	var  querymediaWindow;
	var  querymediaPanel;

	querymediaPanel = new Ext.Panel({
				id : 'querymediaPanel',
				name : 'querymediaPanel',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 90,
				frame : false,
				bodyStyle : 'padding:0 0 0 0',
				items : [						
						
							
				         	grid4,
							
							{
								id : 'whichtbl',
								name : 'whichtbl',
								hidden : true
							},{
								id : 'releateid',
								name : 'releateid',
								hidden : true
							}
							
							
						]
			});

//	querymediaWindow = new Ext.Window({
//		layout : 'fit',
//		width :  200,
//		height : 200,
//		resizable : false,
//		draggable : true,
//		closeAction : 'hide',
//		title : '<span class="commoncss">巡检记录媒体信息</span>',
//		modal : true,
//		collapsible : true,
//		titleCollapse : true,
//		maximizable : false,
//		buttonAlign : 'right',
//		border : false,
//		animCollapse : true,
//		animateTarget : Ext.getBody(),
//		constrain : true,
//		items : [querymediaPanel]
//		
//	});


	querymediaWindow = new Ext.Window({
		title : '<span class="commoncss">巡检记录媒体信息</span>',
		iconCls : 'imageIcon',
		layout : 'fit', // 设置窗口布局模式
		width :  600, // 窗口宽度
		height : 300, // 窗口高度
		// tbar : tb, // 工具栏
		resizable : false,
		draggable : true,
		titleCollapse : true,
		animateTarget : Ext.getBody(),
		closable : true, // 是否可关闭
		closeAction : 'hide', // 关闭策略
		plain:true,// 将窗口变为半透明状态
		frame : true,
		minimizable:false, // 最大化
	    maximizable:false, // 最小化
		collapsible : true, // 是否可收缩
		animCollapse : true,
		border : true, // 边框线设置
		pageY : 80, // 页面定位Y坐标
		pageX : document.body.clientWidth / 2 - 400 / 2, // 页面定位X坐标
		constrain : true,
		// 设置窗口是否可以溢出父容器
		buttonAlign : 'center',
		
		items : [querymediaPanel]
		
	});
	
	
	querymediaWindow.on('deactivate', function()
	{
		querymediaWindow.hide();
		
		if(wnd != null)
		{
				wnd.close();
		}
		
	});
	


	
//	var interCount = 5000;
//	
//	var delay = new Ext.util.DelayedTask(function()
//	{
//		intervalQuery();
//	});
//	
//	delay.delay( interCount ); 
//	
//	function intervalQuery()
//	{
//		
//		
//		delay.delay( interCount ); 
//	}
	
});