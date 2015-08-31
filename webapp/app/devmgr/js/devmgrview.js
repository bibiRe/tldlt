/**
 * 设备管理
 * 
 * @author yangcheng
 * @since now
 */


Ext.onReady(function() {
	
	var root = new Ext.tree.AsyncTreeNode( {
		text : root_deptname,
		expanded : true,
		id : root_deptid,
		remark : ''
	});
	var deptTree = new Ext.tree.TreePanel( {
		loader : new Ext.tree.TreeLoader( {
			baseAttrs : {},
			dataUrl : './devMgr.do?reqCode=departmentTreeInit'
		}),
		root : root,
		title : '',
		applyTo : 'deptTreeDiv',
		autoScroll : false,
		animate : false,
		useArrows : false,
		border : false
	});
	deptTree.root.select();
	deptTree.on('click', function(node) {
		var deptid = node.attributes.id;
		store.load( {
			params : {
				start : 0,
				limit : bbar.pageSize,
				deptid : deptid
			}
		});
	});

	var contextMenu = new Ext.menu.Menu( {
		id : 'deptTreeContextMenu',
		items : [ {
			text : '刷新节点',
			iconCls : 'page_refreshIcon',
			handler : function() 
			{
				var selectModel = deptTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				if (selectNode.attributes.leaf) 
				{
					selectNode.parentNode.reload();
				} 
				else 
				{
					selectNode.reload();
				}
			}
		} ]
	});
	
	deptTree.on('contextmenu', function(node, e) 
	{
		e.preventDefault();
		var deptid = node.attributes.id;
		var deptname = node.attributes.text;
		//Ext.getCmp('parentdeptname').setValue(deptname);
		//Ext.getCmp('parentid').setValue(deptid);
		
		store.load( {
			params : {
				start : 0,
				limit : bbar.pageSize,
				deptid : deptid
			},
			callback : function(r, options, success) {
				for ( var i = 0; i < r.length; i++) {
					var record = r[i];
					var deptid_g = record.data.deptid;
					if (deptid_g == deptid) {
						grid.getSelectionModel().selectRow(i,true);
					}
				}
			}
		});
		
		node.select();
		contextMenu.showAt(e.getXY());
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel( [ new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	{
		header : '设备名称',
		dataIndex : 'devname',
		width : 130
	}, {
		header : '区域名称',
		dataIndex : 'regionname',
		width : 100
	}, {
		header : '设备型号名称',
		dataIndex : 'devmodelname',
		width : 130
	},{
		header : '父设备',
		dataIndex : 'parentdevname',
		width : 130
	}, {
		header : '经度',
		dataIndex : 'longtitude',
		width : 150
	}, {
		header : '纬度',
		dataIndex : 'latitude',
		width : 150
	}, {
		header : '高度',
		dataIndex : 'height',
		width : 150
	}, {
		
		header : 'RF编号',
		dataIndex : 'rfid',
		width : 150
	}, {
		header : '检查周期',
		dataIndex : 'checkcycleid',
		renderer: CHECKCYCLERender,
		width : 100
	}, {
		header : '部门编号',
		dataIndex : 'deptid',
		hidden : true
	},{
		header : '设备编号',
		dataIndex : 'devid',
		hidden : true
	},{
		header : '区域编号',
		dataIndex : 'regionid',
		hidden : true,
		sortable : true
	},{
		header : '设备型号编号',
		dataIndex : 'devmodelid',
		hidden : true
	},{
		header : '父设备编号',
		dataIndex : 'parentdevid',
		hidden : true
	},{
		id : 'remark',
		header : '备注',
		dataIndex : 'remark',
		width : 150
	} ]);

	/**
	 * 数据存储
	 */
	var store = new Ext.data.Store( {
		proxy : new Ext.data.HttpProxy( {
			url : './devMgr.do?reqCode=queryDevForDept'
		}),
		reader : new Ext.data.JsonReader( {
			totalProperty : 'TOTALCOUNT',
			root : 'ROOT'
		}, [ {
			name : 'devname'
		}, {
			name : 'regionname'
		}, {
			name : 'devmodelname'
		}, {
			name : 'parentdevname'
		}, {
			name : 'longtitude'
		}, {
			name : 'latitude'
		}, {
			name : 'height'
		}, {
			name : 'rfid'
		}, {
			name : 'checkcycleid'
		}, {
			name : 'deptid'
		} , {
			name : 'devid'
		},{
			name : 'regionid'
		}, {
			name : 'devmodelid'
		}, {
			name : 'parentdevid'
		},{

			name : 'remark'
		}])
	});

	// 翻页排序时带上查询条件
		store.on('beforeload', function() {
			this.baseParams = {
				queryParam : Ext.getCmp('queryParam').getValue()
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
			store : store,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
		});
		var grid = new Ext.grid.GridPanel( {
			title : '<span class="commoncss">设备信息表</span>',
			height : 500,
			// width:600,
			autoScroll : true,
			region : 'center',
			store : store,
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			stripeRows : true,
			frame : true,
			//autoExpandColumn : 'remark',
			cm : cm,
			sm : sm,
			tbar : [ {
				text : '新增',
				iconCls : 'page_addIcon',
				handler : function() {
					addInit();
				}
			}, '-', {
				text : '修改',
				iconCls : 'page_edit_1Icon',
				handler : function() {
					editInit();
				}
			}, '-', {
				text : '删除',
				iconCls : 'page_delIcon',
				handler : function() 
				{
					deleteItems();
				}
			}, '->', new Ext.form.TextField( {
				id : 'queryParam',
				name : 'queryParam',
				emptyText : '区域名称|设备名称|设备型号名称',
				enableKeyEvents : true,
				width : 230,
				listeners : 
				{
					specialkey : function(field, e) 
					{
						if (e.getKey() == Ext.EventObject.ENTER) 
						{
							queryDeptItem();
						}
					}
				},
			}), {
				text : '查询',
				iconCls : 'previewIcon',
				handler : function() 
				{
					queryDeptItem();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'arrow_refreshIcon',
				handler : function() 
				{
					refreshTable();
				}
			} ],
			bbar : bbar
		});

		store.load( {
			params : {
				start : 0,
				limit : bbar.pageSize,
				deptid : 'current_user'
			}
		});

		grid.on('rowdblclick', function(grid, rowIndex, event) 
		{
			editInit();
		});
		
		grid.on('sortchange', function() 
		{
			// grid.getSelectionModel().selectFirstRow();
		});

		bbar.on("change", function() 
		{
			// grid.getSelectionModel().selectFirstRow();
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
			        id: "devMenu",
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
				addInit();
				  
			}

			function fnMenuEdit() 
			  {
			 
				editInit();
				  
			}


			function fnMenuDel() 
			  {
			 
				deleteItems();
				  
			}
		
/////////////////////////////
		
		////////////////////////////

		var  storeRegion = new Ext.data.Store({   
		    proxy: new Ext.data.HttpProxy({   
		       url : './devMgr.do?reqCode=queryregion'
		    }),   
		    reader: new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
		        },[
				     {name : 'id',mapping: 'id'},
				     {name : 'value',mapping: 'value'}
		    ])
		});



		var comboRegionType = new Ext.form.ComboBox({ 
		anchor : '100%', 
		id:'region1', 
		name : "region1",
		hiddenName  : 'regionid',
		typeAhead : true,
		lazyRender : true,
		valueField : 'id', 
		displayField : 'value', 
		value : '0',
		emptyText : '请选择...',
		fieldLabel : '区域', 
		allowBlank : false, 
		editable : false, 
		triggerAction : 'all', 
		store :storeRegion , 
		forceSelection : true, 
		mode : 'remote', //remote ,don't invoke load 
		selectOnFocus : true ,
		editable : false,
		labelStyle : micolor,
		});


		storeRegion.load
		({
			params : 
			{
				deptid : root_deptid
			}
		});

		comboRegionType.on('expand', function()
		{
			

			GetRegionType();

		});
		
		
		function GetRegionType()
		{
			var cur_deptid = Ext.getCmp("addDeviceFormPanel").findById('deptid').getValue()
			


			storeRegion.reload
			({
				params : 
				{
					deptid : cur_deptid
				}
			});
		}

		
////////////////////////////

		var  storeDevmodel = new Ext.data.Store({   
		    proxy: new Ext.data.HttpProxy({   
		       url : './devMgr.do?reqCode=querydevmodel'
		    }),   
		    reader: new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
		        },[
				     {name : 'id',mapping: 'id'},
				     {name : 'value',mapping: 'value'}
		    ])
		});



		var comboDevmodelType = new Ext.form.ComboBox({ 
		anchor : '100%', 
		id:'devmodel1', 
		name : "devmodel1",
		hiddenName  : 'devmodelid',
		typeAhead : true,
		lazyRender : true,
		valueField : 'id', 
		displayField : 'value', 
		value : '0',
		emptyText : '请选择...',
		fieldLabel : '设备型号', 
		allowBlank : false, 
		editable : false, 
		triggerAction : 'all', 
		store :storeDevmodel , 
		forceSelection : true, 
		mode : 'remote', //remote ,don't invoke load 
		selectOnFocus : true ,
		editable : false,
		labelStyle : micolor,
		}); 


		storeDevmodel.load();

		comboDevmodelType.on('expand', function()
		{
					
			storeDevmodel.reload();

		});
		
		
		////////////////////////////

		var  storeParentDev = new Ext.data.Store({   
		    proxy: new Ext.data.HttpProxy({   
		       url : './devMgr.do?reqCode=queryparentdev'
		    }),   
		    reader: new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
		        },[
				     {name : 'id',mapping: 'id'},
				     {name : 'value',mapping: 'value'}
		    ])
		});



		var comboParentDevType = new Ext.form.ComboBox({ 
		anchor : '100%', 
		id:'parentdev1', 
		name : "parentdev1",
		hiddenName  : 'parentdevid',
		typeAhead : true,
		lazyRender : true,
		valueField : 'id', 
		displayField : 'value', 
		value : '0',
		emptyText : '请选择...',
		fieldLabel : '父设备', 
		allowBlank : true, 
		editable : false, 
		triggerAction : 'all', 
		store :storeParentDev , 
		forceSelection : true, 
		mode : 'remote', //remote ,don't invoke load 
		selectOnFocus : true ,
		editable : false,
		labelStyle : micolor,
		
		});


		storeParentDev.load();

		comboParentDevType.on('expand', function()
		{
			//storeParentDev.removeAll();
			//comboParentDevType.setValue('');
			storeParentDev.reload();
			//alert('dd');

		});
		
		
		
		var   storeCheckCycle = new Ext.data.SimpleStore({
			  fields : ['value', 'text'],
			  data : [
			          ['0', '按天']
			          ,
			          ['1', '按周']
			          ,
			          ['2', '按半月']
			          ,
			          ['3', '按一个月']
			          ,
			          ['4', '按季度']
			        ]
			});
		
		
		
		var comboCheckCycleType = new Ext.form.ComboBox({ 
			anchor : '100%', 
			id:'checkcycle1', 
			name : "checkcycle1",
			hiddenName  : 'checkcycleid',
			typeAhead : true,
			lazyRender : true,
			valueField : 'value', 
			displayField : 'text', 
			value : '0',
			emptyText : '请选择...',
			fieldLabel : '检查周期', 
			allowBlank : false, 
			editable : false, 
			triggerAction : 'all', 
			//store :storeCheckCycle , 
			store : CHECKCYCLEStore ,
			forceSelection : true, 
			mode : 'local', 
			selectOnFocus : true ,
			editable : false,
			labelStyle : micolor,
			});
		
		/////////////////////////////////////
	
		
		//////////////////////////////////////
		
		var addDeviceFormPanel = new Ext.form.FormPanel( {
			id : 'addDeviceFormPanel',
			name : 'addDeviceFormPanel',
			defaultType : 'textfield',
			labelAlign : 'right',
			labelWidth : 70,
			frame : false,
			bodyStyle : 'padding:5 5 0',
			items : [ {
				fieldLabel : '设备名称',
				name : 'devname',
				allowBlank : false,
				labelStyle: micolor,
				anchor : '100%'
			},comboDevmodelType,comboParentDevType,comboRegionType,comboCheckCycleType,
			{
				fieldLabel : '经度',
				name : 'longtitude',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			}, {
				fieldLabel : '纬度',
				name : 'latitude',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			}, {
				fieldLabel : '高度',
				name : 'height',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			},{
				fieldLabel : 'RF编号',
				name : 'rfid',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			},
			{
				fieldLabel : '备注',
				name : 'remark',
				allowBlank : true,
				labelStyle: micolor,
				xtype: 'textarea',
				anchor : '100%'
			}, {
				id : 'deptid',
				name : 'deptid',
				hidden : true
			}]
		});
		
		var addDeviceWindow = new Ext.Window( {
			layout : 'fit',
			width : 400,
			height : 380,
			resizable : false,
			draggable : true,
			closable : true,
			modal : true,
			closeAction : 'hide',
			title : '<span class="commoncss">新增设备</span>',
			// iconCls : 'page_addIcon',
			collapsible : true,
			titleCollapse : true,
			maximizable : false,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			pageY : 20,
			pageX : document.body.clientWidth / 2 - 420 / 2,
			animateTarget : Ext.getBody(),
			constrain : true,
			items : [ addDeviceFormPanel ],
			buttons : [
					{
						text : '保存',
						iconCls : 'acceptIcon',
						handler : function() 
						{
							
							
							saveDeviceItem();
							
						}
					},{
						text : '关闭',
						iconCls : 'deleteIcon',
						handler : function() {
							addDeviceWindow.hide();
						}
					} ]
		});
		
		
		
		
/////////////////////////////
		
		////////////////////////////

		var  storeEditRegion = new Ext.data.Store({   
		    proxy: new Ext.data.HttpProxy({   
		       url : './devMgr.do?reqCode=queryregion'
		    }),   
		    reader: new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
		        },[
				     {name : 'id',mapping: 'id'},
				     {name : 'value',mapping: 'value'}
		    ])
		});



		var comboEditRegionType = new Ext.form.ComboBox({ 
		anchor : '100%', 
		id:'region2', 
		name : "region2",
		hiddenName  : 'regionid',
		typeAhead : true,
		lazyRender : true,
		valueField : 'id', 
		displayField : 'value', 
		value : '0',
		emptyText : '请选择...',
		fieldLabel : '区域', 
		allowBlank : false, 
		editable : false, 
		triggerAction : 'all', 
		store :storeEditRegion , 
		forceSelection : true, 
		mode : 'remote', 
		selectOnFocus : true ,
		editable : false,
		labelStyle : micolor,
		});


		storeEditRegion.load
		({
			params : 
			{
				deptid : root_deptid
			}
		});

		comboEditRegionType.on('expand', function()
		{
			

			GetRegionType2();

		});
		
		
		function GetRegionType2()
		{
			var cur_deptid = Ext.getCmp("editDeviceFormPane2").findById('deptid2').getValue();
			


			storeEditRegion.reload
			({
				params : 
				{
					deptid : cur_deptid
				}
			});
		}

		
////////////////////////////

		var  storeEditDevmodel = new Ext.data.Store({   
		    proxy: new Ext.data.HttpProxy({   
		       url : './devMgr.do?reqCode=querydevmodel'
		    }),   
		    reader: new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
		        },[
				     {name : 'id',mapping: 'id'},
				     {name : 'value',mapping: 'value'}
		    ])
		});



		var comboEditDevmodelType = new Ext.form.ComboBox({ 
		anchor : '100%', 
		id:'devmodel2', 
		name : "devmodel2",
		hiddenName  : 'devmodelid',
		typeAhead : true,
		lazyRender : true,
		valueField : 'id', 
		displayField : 'value', 
		value : '0',
		emptyText : '请选择...',
		fieldLabel : '设备型号', 
		allowBlank : false, 
		editable : false, 
		triggerAction : 'all', 
		store :storeEditDevmodel , 
		forceSelection : true, 
		mode : 'remote', //remote ,don't invoke load 
		selectOnFocus : true ,
		editable : false,
		labelStyle : micolor,
		}); 


		storeEditDevmodel.load();

		comboEditDevmodelType.on('expand', function()
		{
					
			storeEditDevmodel.reload();

		});
		
		
		////////////////////////////

		var  storeEditParentDev = new Ext.data.Store({   
		    proxy: new Ext.data.HttpProxy({   
		       url : './devMgr.do?reqCode=queryparentdev'
		    }),   
		    reader: new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
		        },[
				     {name : 'id',mapping: 'id'},
				     {name : 'value',mapping: 'value'}
		    ])
		});



		var comboEditParentDevType = new Ext.form.ComboBox({ 
		anchor : '100%', 
		id:'parentdev2', 
		name : "parentdev2",
		hiddenName  : 'parentdevid',
		typeAhead : true,
		lazyRender : true,
		valueField : 'id', 
		displayField : 'value', 
		value : '0',
		emptyText : '请选择...',
		fieldLabel : '父设备', 
		allowBlank : true, 
		editable : false, 
		triggerAction : 'all', 
		store :storeEditParentDev , 
		forceSelection : true, 
		mode : 'remote', //remote ,don't invoke load 
		selectOnFocus : true ,
		editable : false,
		labelStyle : micolor,
		});



			
		storeEditParentDev.load
		(
				{
					params : 
					{
									devid : ''
					}
		
				}
							
		);

		comboEditParentDevType.on('expand', function()
		{
			//storeEditParentDev.removeAll();
		//	comboEditParentDevType.setValue('');
			
			var devid = Ext.getCmp("editDeviceFormPane2").findById('devid2').getValue();
			
			storeEditParentDev.load
			(
					{
						params : 
						{
										devid : devid
						}
			
					}
								
			);

		});
		
		
		
		var   storeEditCheckCycle = new Ext.data.SimpleStore({
			  fields : ['value', 'text'],
			  data : [
			          ['0', '按天']
			          ,
			          ['1', '按周']
			          ,
			          ['2', '按半月']
			          ,
			          ['3', '按一个月']
			          ,
			          ['4', '按季度']
			        ]
			});
		
		
		
		var comboEditCheckCycleType = new Ext.form.ComboBox({ 
			anchor : '100%', 
			id:'checkcycle2', 
			name : "checkcycle2",
			hiddenName  : 'checkcycleid',
			typeAhead : true,
			lazyRender : true,
			valueField : 'value', 
			displayField : 'text', 
			value : '0',
			emptyText : '请选择...',
			fieldLabel : '检查周期', 
			allowBlank : false, 
			editable : false, 
			triggerAction : 'all', 
			//store :storeEditCheckCycle , 
			store : CHECKCYCLEStore,
			forceSelection : true, 
			mode : 'local', 
			selectOnFocus : true ,
			editable : false,
			labelStyle : micolor,
			});
		
		/////////////////////////////////////
	
		
		//////////////////////////////////////
		
		var editDeviceFormPanel = new Ext.form.FormPanel( {
			id : 'editDeviceFormPane2',
			name : 'editDeviceFormPane2',
			defaultType : 'textfield',
			labelAlign : 'right',
			labelWidth : 70,
			frame : false,
			bodyStyle : 'padding:5 5 0',
			items : [ {
				fieldLabel : '设备名称',
				name : 'devname',
				allowBlank : false,
				labelStyle: micolor,
				anchor : '100%'
			},comboEditDevmodelType,comboEditParentDevType,comboEditRegionType,comboEditCheckCycleType,
			{
				fieldLabel : '经度',
				name : 'longtitude',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			}, {
				fieldLabel : '纬度',
				name : 'latitude',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			}, {
				fieldLabel : '高度',
				name : 'height',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			},{
				fieldLabel : 'RF编号',
				name : 'rfid',
				allowBlank : true,
				labelStyle: micolor,
				anchor : '100%'
			},
			{
				fieldLabel : '备注',
				name : 'remark',
				allowBlank : true,
				labelStyle: micolor,
				xtype: 'textarea',
				anchor : '100%'
			}, {
				name : 'devid',
				id   : 'devid2',
				hidden : true
			},{
				
				name : 'deptid',
				id   : 'deptid2',
				hidden : true
			}]
		});
		
		var editDeviceWindow = new Ext.Window( {
			layout : 'fit',
			width : 400,
			height : 380,
			resizable : false,
			draggable : true,
			closable : true,
			modal : true,
			closeAction : 'hide',
			title : '<span class="commoncss">修改设备</span>',
			// iconCls : 'page_addIcon',
			collapsible : true,
			titleCollapse : true,
			maximizable : false,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			pageY : 20,
			pageX : document.body.clientWidth / 2 - 420 / 2,
			animateTarget : Ext.getBody(),
			constrain : true,
			items : [ editDeviceFormPanel ],
			buttons : [
					{
						text : '保存',
						iconCls : 'acceptIcon',
						handler : function() 
						{
							
							
							UpdateDeviceItem();
							
						}
					},{
						text : '关闭',
						iconCls : 'deleteIcon',
						handler : function() {
							editDeviceWindow.hide();
						}
					} ]
		});
		
		/**
		 * 布局
		 */
		var viewport = new Ext.Viewport( {
			layout : 'border',
			items : [ {
				title : '<span class="commoncss">组织机构</span>',
				iconCls : 'chart_organisationIcon',
				tools : [ {
					id : 'refresh',
					handler : function() {
						deptTree.root.reload()
					}
				} ],
				collapsible : true,
				width : 210,
				minSize : 160,
				maxSize : 280,
				split : true,
				region : 'west',
				autoScroll : true,
				margins : '3 3 3 3',
				// collapseMode:'mini',
				items : [ deptTree ]
			}, {
				region : 'center',
				layout : 'fit',
				border : false,
				margins : '3 3 3 3',
				items : [ grid ]
			} ]
		});

		/**
		 * 根据条件查询部门
		 */
		function queryDeptItem()
		{
			var selectModel = deptTree.getSelectionModel();
			var selectNode = selectModel.getSelectedNode();
			
			var cur_deptid = selectNode.attributes.id;
			
			store.load( {
				params : {
					start : 0,
					limit : bbar.pageSize,
					deptid : cur_deptid,
					queryParam : Ext.getCmp('queryParam').getValue()
				}
			});
		}

		/**
		 * 
		 */
		function addInit() 
		{
			
			clearForm(addDeviceFormPanel.getForm());
			
			var selectModel = deptTree.getSelectionModel();
			var selectNode = selectModel.getSelectedNode();
			
			Ext.getCmp("addDeviceFormPanel").findById('deptid').setValue(selectNode.attributes.id);
			addDeviceWindow.show();
			addDeviceWindow.setTitle('<span class="commoncss">新增设备</span>');
			
			GetRegionType();
			

		}

		/**
		 * 
		 */
		function saveDeviceItem() {
			if (!addDeviceFormPanel.form.isValid()) {
				return;
			}
			addDeviceFormPanel.form.submit( {
				url : './devMgr.do?reqCode=saveDeviceItem',
				waitTitle : '提示',
				method : 'POST',
				waitMsg : '正在处理数据,请稍候...',
				success : function(form, action) {
					addDeviceWindow.hide();
					store.reload();
					
					var nodeid = Ext.getCmp("addDeviceFormPanel").findById('deptid').getValue();
					
					refreshNode(nodeid);
					
				},
				failure : function(form, action) {
					var msg = action.result.msg;
					Ext.MessageBox.alert('提示', '数据保存失败:<br>' + msg);
				}
			});
		}

		/**
		 * 刷新指定节点
		 */
		function refreshNode(nodeid) {
			var node = deptTree.getNodeById(nodeid);
			/* 异步加载树在没有展开节点之前是获取不到对应节点对象的 */
			if (Ext.isEmpty(node)) {
				deptTree.root.reload();
				return;
			}
			if (node.attributes.leaf) {
				node.parentNode.reload();
			} else {
				node.reload();
			}
		}

		/**
		 * 修改部门初始化
		 */
		function editInit() 
		{
			var record = grid.getSelectionModel().getSelected();
			if (Ext.isEmpty(record)) {
				Ext.MessageBox.alert('提示', '请先选择要修改的设备!');
				return;
			}
			
			record = grid.getSelectionModel().getSelected();
			
			
			
			clearForm(editDeviceFormPanel.getForm());
			
			
			//var selectModel = deptTree.getSelectionModel();
		//	var selectNode = selectModel.getSelectedNode();
			
			//Ext.getCmp("editDeviceFormPane2").findById('deptid').setValue(selectNode.attributes.id);
			
			editDeviceFormPanel.getForm().loadRecord(record);
			editDeviceWindow.show();
			editDeviceWindow.setTitle('<span style="font-weight:normal">修改部门</span>');
			
			//var deptid2 = record.get('deptid');
			
			
			//var  devmodeid = record.get('devmodelid');
			//var  devmodelname = record.get('devmodelname');
			
		   // var  devmodelboxrecord =new  Ext.data.Record();
		   
		   // devmodelboxrecord.set('value',devmodelname);
		   // devmodelboxrecord.set('id',devmodeid);
			
		//    storeEditDevmodel.insert(0,devmodelboxrecord);
			
		  //  comboEditDevmodelType.setValue(devmodeid);
		    
		    
			//var  parentdevid = record.get('parentdevid');
			//var  parentdevname = record.get('parentdevname');
			
		   // var  parentdevboxrecord =new  Ext.data.Record();
		   
		   // parentdevboxrecord.set('value',parentdevname);
		   // parentdevboxrecord.set('id',parentdevid);
			
		   // storeEditParentDev.insert(0,parentdevboxrecord);
			
		  //  comboEditParentDevType.setValue(parentdevid);
		    
		    
		    
			//var  regionid = record.get('regionid');
			//var  regionname = record.get('regionname');
			
		   // var  regionboxrecord =new  Ext.data.Record();
		   
		   // regionboxrecord.set('value',regionname);
		   // regionboxrecord.set('id',regionid);
			
		   // storeEditRegion.insert(0,regionboxrecord);
			
		   // comboEditRegionType.setValue(regionid);
		    
		    
			//var  checkcycleid = record.get('checkcycleid');
			
			
			//comboEditCheckCycleType.setValue(checkcycleid);
			
			
		}

		/**
		 * 修改部门数据
		 */
		function UpdateDeviceItem() {
			if (!editDeviceFormPanel.form.isValid()) {
				return;
			}
			update();
		}

		/**
		 * 更新
		 */
		function update() 
		{
			
			editDeviceFormPanel.form.submit( {
				url : './devMgr.do?reqCode=updateDevItem',
				waitTitle : '提示',
				method : 'POST',
				waitMsg : '正在处理数据,请稍候...',
				success : function(form, action) {
					
					editDeviceWindow.hide();
					
					store.reload();
					
					var nodeid = Ext.getCmp("editDeviceFormPane2").findById('deptid2').getValue();
					refreshNode(nodeid);
					
					
					
					form.reset();
					
					//Ext.MessageBox.alert('提示', action.result.msg);
				},
				failure : function(form, action) {
					var msg = action.result.msg;
					Ext.MessageBox.alert('提示', '数据修改失败:<br>' + msg);
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
			var strChecked = jsArray2JsString(rows, 'devid');
			Ext.Msg.confirm('请确认', '你真的要删除吗?', function(btn, text) {
						if (btn == 'yes') {
							showWaitMsg();
								Ext.Ajax.request({
											url : './devMgr.do?reqCode=deldeviceitem',
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

