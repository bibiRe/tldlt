/**
 * 用户组管理
 * 
 * @author yangcheng
 * @since now
 */
Ext.onReady(function() {

var  g_groupid = '';

var root = new Ext.tree.AsyncTreeNode( {
		text : root_groupname,
		expanded : true,
		id : root_groupid,
		remark : ''
	});
	var groupTree = new Ext.tree.TreePanel( {
		loader : new Ext.tree.TreeLoader( {
			baseAttrs : {},
			dataUrl : './userGroup.do?reqCode=groupTreeInit'
		}),
		root : root,
		title : '',
		applyTo : 'groupTreeDiv',
		autoScroll : false,
		animate : false,
		useArrows : false,
		border : false
	});
	
	groupTree.root.select();
	
	groupTree.on('click', function(node) {
		groupid = node.attributes.id;
		if(groupid == '-1')
		{
			groupid='';
		}
		
		g_groupid = groupid;
		
		store.load( {
			params : {
				start : 0,
				limit : bbar.pageSize,
				groupid : g_groupid
			}
		});
	});

	var contextMenu = new Ext.menu.Menu( {
		id : 'groupTreeContextMenu',
		items : [ {
			text : '新增用户组',
			iconCls : 'page_addIcon',
			handler : function() {
				addGroupInit();
			}
		}, {
			text : '修改用户组',
			iconCls : 'page_edit_1Icon',
			handler : function() {
				editGroupInit();
			}
		}, {
			text : '删除用户组',
			iconCls : 'page_delIcon',
			handler : function() {
				var selectModel = groupTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				var id = selectNode.attributes.id;
				
				if(id == -1)
				{
				   id = '';
				}
				deleteGroup(id);
			}
		}, {
			text : '刷新节点',
			iconCls : 'page_refreshIcon',
			handler : function() {
				var selectModel = groupTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				if (selectNode.attributes.leaf) {
					selectNode.parentNode.reload();
				} else {
					selectNode.reload();
				}
			}
		} ]
	});
	
	groupTree.on('contextmenu', function(node, e) {
		e.preventDefault();
		groupid = node.attributes.id;
		groupname = node.attributes.text;
		
		//Ext.getCmp('parentdeptname').setValue(deptname);
	//	Ext.getCmp('parentid').setValue(deptid);
	
		if(groupid == '-1')
		{
			groupid='';
		}
		
		g_groupid = groupid;
		
		store.load( {
			params : {
				start : 0,
				limit : bbar.pageSize,
				groupid : g_groupid
			},
			callback : function(r, options, success) {
				for ( var i = 0; i < r.length; i++) {
					var record = r[i];
					//var deptid_g = record.data.deptid;
					//if (deptid_g == deptid) {
						//grid.getSelectionModel().selectRow(i);
					//}
				}
			}
		});
		node.select();
		contextMenu.showAt(e.getXY());
	});
	
	
	
	var  addGroupWindow;
	var  addGroupformPanel;

	addGroupformPanel = new Ext.form.FormPanel({
				id : 'addgroupform',
				name : 'addgroupform',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 60,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [{
							fieldLabel : '组名',
							name : 'groupname',
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

	addGroupWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 203,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">新增用户组</span>',
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
		items : [addGroupformPanel],
		buttons : [{
			text : '保存',
			iconCls : 'acceptIcon',
			handler : function() {
				
				if (addGroupWindow.getComponent('addgroupform').form.isValid()) {
					addGroupWindow.getComponent('addgroupform').form.submit({
						url : './userGroup.do?reqCode=addgroup',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							  
							  store.reload();
							   
							   groupTree.root.reload();
							   g_groupid = '';
							   	storeGroup.reload();
							   
							 //  Ext.MessageBox.alert('提示', '新增用户组成功');
							 
							   addGroupWindow.getComponent('addgroupform').form.reset();
							   addGroupWindow.hide();
								
							
								
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '新增用户组失败:<br>' + msg);
						//	addGroupWindow.getComponent('manufform').form.reset();
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
				addGroupWindow.hide();
			}
		}]
	});
	
	
			function addGroupInit() 
			{
				addGroupWindow.show();
				//addDeptWindow.setTitle('<span class="commoncss">新增部门</span>');
		}
	
	
	
		var editGroupWindow, editGroupFormPanel;
	
		editGroupFormPanel = new Ext.form.FormPanel({
				labelAlign : 'right',
				labelWidth : 60,
				defaultType : 'textfield',
				frame : false,
				bodyStyle : 'padding:5 5 0',
				id : 'editGroupFormPanel',
				name : 'editGroupFormPanel',
				items : [{
							fieldLabel : '组名',
							name : 'groupname',
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
						},{
							fieldLabel : '编号',
							name : 'id',
							anchor : '100%',
							hidden : true,
							hideLabel : true,
							allowBlank : false
						}]
			});

	editGroupWindow = new Ext.Window({
				layout : 'fit',
				width : 300,
				height : 273,
				resizable : false,
				draggable : true,
				closeAction : 'hide',
				title : '<span class="commoncss">修改用户组</span>',
				modal : true,
				collapsible : true,
				titleCollapse : true,
				maximizable : false,
				buttonAlign : 'right',
				border : false,
				animCollapse : true,
				animateTarget : Ext.getBody(),
				constrain : true,
				items : [editGroupFormPanel],
				buttons : [{
					text : '保存',
					iconCls : 'acceptIcon',
					handler : function() {
						
						updateGroupItem();
					}
				}, {
					text : '关闭',
					iconCls : 'deleteIcon',
					handler : function() {
						editGroupWindow.hide();
					}
				}]

			});
			
			

	function editGroupInit()
	{
			
		
				var selectModel = groupTree.getSelectionModel();
				var selectNode = selectModel.getSelectedNode();
				if (!selectNode.attributes.leaf) 
				{
					Ext.Msg.alert('提示', '请先选中要修改的组');
					return;
				} 

				var  record =new  Ext.data.Record();
	            record.set('groupname',selectNode.attributes.text);
				record.set('remark',selectNode.attributes.remark);
				record.set('id',selectNode.attributes.id);
				
				editGroupWindow.show();
				editGroupFormPanel.getForm().loadRecord(record);
	}


	function updateGroupItem() {
		if (!editGroupFormPanel.form.isValid()) {
			return;
		}
		
		editGroupFormPanel.form.submit({
					url : './userGroup.do?reqCode=updategroup',
					waitTitle : '提示',
					method : 'POST',
					waitMsg : '正在处理数据,请稍候...',
					success : function(form, action) {
						editGroupWindow.hide();
						store.reload();
						groupTree.root.reload();
						g_groupid = '';
						storeGroup.reload();
					
					},
					failure : function(form, action) {
						var msg = action.result.msg;
						Ext.MessageBox.alert('提示', '修改用户组失败:<br>' + msg);
					}
				});
	}
	
	function  deleteGroup(id)
	{
						
		var msg = '你真的要删除吗?';
		
		if (Ext.isEmpty(id)) {
			msg = '你真的要全部删除吗?';
			
		}
		
		Ext.Msg.confirm('请确认', msg, function(btn, text) {
					if (btn == 'yes') {
						showWaitMsg();
							Ext.Ajax.request({
										url : './userGroup.do?reqCode=deletegroup',
										success : function(response) {
											
											hideWaitMsg();
											
											var  resultArray = Ext.util.JSON.decode(response.responseText);
											if(resultArray.success)
											{
												store.reload();
												groupTree.root.reload();
												g_groupid = '';
												storeGroup.reload();
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
											delkeys : id
										}
									});
					}
				});
	}
			
	/////////////////////////////////////////////
	
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header : '序号',width : 40}), sm, 
	        {
				header : '组名',
				dataIndex : 'groupname'
			}, {
				header : '用户名',
				dataIndex : 'username'
			},{
				header : '性别',
				dataIndex : 'usersex',
				renderer : SEXRender
			},{
				header : '备注',
				dataIndex : 'remark',
				id : 'remark'
			},{
					header : '组编号',
					dataIndex : 'idx',
					hidden : true,
					sortable : true

			},{
					header : '用户编号',
					dataIndex : 'userid',
					hidden : true

			}
			
			]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : './userGroup.do?reqCode=querygroupuser'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'TOTALCOUNT',
							root : 'ROOT'
						}, [{
									name : 'groupname'
								}, {
									name : 'username'
								}, {
									name : 'usersex'
								},{
									name : 'remark'
								},{
									name : 'userid'
								}, {
									name : 'idx'
								}])
			});

	// 翻页排序时带上查询条件
	store.on('beforeload', function() {
				this.baseParams = {
					queryParam : Ext.getCmp('queryParam').getValue(),
					groupid : g_groupid
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
				title : '<span class="commoncss">用户组数据列表</span>',
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
							text : '用户管理',
							iconCls : 'page_addIcon',
							handler : function() {

							showUserWindow();
							
							}
						}, '-', '->',
						new Ext.form.TextField({
									id : 'queryParam',
									name : 'queryParam',
									emptyText : '组名|用户名',
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
					limit : bbar.pageSize,
					groupid : g_groupid
				}
			});

	//grid.addListener('rowdblclick', ininEditManufWindow);
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
        id: "UserMenu",
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
                id: 'Del',
               iconCls : 'page_delIcon',
                handler: fnMenuDel,
                text: '删除该行'
            }
        ]
    });
	
	
function fnMenuAdd() 
  {

  
  showUserWindow();
	  
}

function fnMenuDel() 
  {
 
	 deleteItems();
	  
}
	
	
	function  showUserWindow()
	{
		addUserWindow.getComponent('userform').form.reset()
		 addUserWindow.show();
		 
		 
		comboGroup.setValue(g_groupid);
	}
	
	
	//////////////////////////////////////
	
	
	var  storeGroup = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './userGroup.do?reqCode=querygroup'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'id'},
		     {name : 'value'}
    ])
});



var comboGroup = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'group1', 
name : "group1",
hiddenName  : 'id',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '组名', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeGroup , 
forceSelection : true, 
mode : 'local', //remote ,don't invoke load 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
}) 




storeGroup.load();

		comboGroup.on('expand', function() {
			
				storeGroup.reload();
				

			});
			
			
			
			
				var  storeUser = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './userGroup.do?reqCode=queryuser'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'id'},
		     {name : 'value'}
    ])
});



var comboUser = new Ext.form.ComboBox({ 
anchor : '100%', 
id:'user1', 
name : "user1",
hiddenName  : 'userid',
typeAhead : true,
lazyRender : true,
valueField : 'id', 
displayField : 'value', 
value : '0',
emptyText : '请选择...',
fieldLabel : '用户名', 
allowBlank : false, 
editable : false, 
triggerAction : 'all', 
store :storeUser , 
forceSelection : true, 
mode : 'local', //remote ,don't invoke load 
selectOnFocus : true ,
editable : false,
labelStyle : micolor,
}) 




storeUser.load();

		comboUser.on('expand', function() {
			
				storeUser.reload();

			});
			
			
			
   var smuser2 = new Ext.grid.CheckboxSelectionModel();
	var cmuser2  = new Ext.grid.ColumnModel( [ new Ext.grid.RowNumberer({header : '序号',width : 40}), smuser2, 
	{
		header : '用户名',
		dataIndex : 'username',
		id :  'username'
	}, {
		header : '标记',
		dataIndex : 'initialcheck',
		hidden : true,
	},{
		header : '用户编号',
		dataIndex : 'userid',
		hidden : true,
		sortable : true
	} ]);
		
	var  storeuser2 = new Ext.data.Store({   
    proxy: new Ext.data.HttpProxy({   
       url : './userGroup.do?reqCode=queryuser2'
    }),   
    reader: new Ext.data.JsonReader({
					totalProperty : 'TOTALCOUNT',
					root : 'ROOT'
        },[
		     {name : 'username'},
		     {name : 'userid'},
			 {name : 'initialcheck'}
    ])
});




var griduser2 = new Ext.grid.GridPanel( {
			//title : '<span class="commoncss">部门信息表</span>',
			height : 500,
			width:600,
			autoScroll : true,
			region : 'center',
			store : storeuser2,
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			stripeRows : true,
			frame : true,
			cm : cmuser2,
			sm : smuser2,
			autoExpandColumn : 'username'
			
		});

		
		griduser2.on('rowdblclick', function(griduser2, rowIndex, event) 
		{
			
		});
		
		
		
var  storeusertree = new Ext.data.SimpleStore( {
						fields : ['userid','username'],
						data : [ [] ]
					});
					
var comboxuserWithTree = new Ext.form.ComboBox(
				{
					id : 'usertree',
					name: 'usertree',
					anchor : '100%', 
					
					store : storeusertree,
					
					hiddenName  : 'username',

					valueField : 'userid', 
					displayField : 'username', 
					
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
					tpl : "<tpl for='.'><div style='height:390px;'><div id='addDeptTreeDiv'></div></div></tpl>",
					allowBlank : false,
					onSelect : Ext.emptyFn
				});
				
		// 监听下拉框的下拉展开事件
		comboxuserWithTree.on('expand', function() {
			// 将UI树挂到treeDiv容器
				griduser2.render('addDeptTreeDiv');
				
				var selgroupid= comboGroup.getValue();
				
				//alert(comboGroup.getRawValue());//combobox ui text value ,not key value
			
			  storeuser2.reload( {
					params : {
						start : 0,
						limit :9999,
						groupid : selgroupid
					}
				});

			});
			
			
			
		comboxuserWithTree.on('collapse', function() 
		{
			  comboxuserWithTree.setValue('');
			   Ext.getCmp("userform").findById('userid').setValue('');
			   
				var rows = griduser2.getSelectionModel().getSelections();
				var userids = '';
				var usernames = '';
				
				if(rows.length <= 0)
				{
					comboxuserWithTree.setValue(',');
					Ext.getCmp("userform").findById('userid').setValue(',');
					   
				   return;
				}
				
				for ( var i = 0; i < rows.length; i++) 
				{
					userids += rows[i].get('userid');
					userids += ',';
					
					usernames += rows[i].get('username');
					usernames += ',';
			    }

				
			  // var  userrecord =new  Ext.data.Record();
	   
				//userrecord.set('userid',userids);
			//	userrecord.set('username',usernames);
				
			//	storeusertree.removeAll();
			//	storeusertree.insert(0,userrecord);
				//storeusertree.add(userrecord);
		
				comboxuserWithTree.setValue(usernames);
			   Ext.getCmp("userform").findById('userid').setValue(userids);
				
	  });
	  
	  
	  griduser2.store.on("load",function()
	  {  
	  
      //  griduser2.getSelectionModel().selectRow(1,true);  //based on 0
	  
	  
		var total = griduser2.store.getCount();
		for(var i=0;i<total;i++)
		{
		  var userrecord = griduser2.store.getAt(i);
		  
		 // alert(userrecord.get("username"));
		 
		    if(userrecord.get("initialcheck"))
			{
				griduser2.getSelectionModel().selectRow(i,true);
			}
		}
		
    }); 
			

			
			
	var  addUserWindow;
	var  addUserformPanel;

	addUserformPanel = new Ext.form.FormPanel({
				id : 'userform',
				name : 'userform',
				defaultType : 'textfield',
				labelAlign : 'right',
				labelWidth : 60,
				frame : false,
				bodyStyle : 'padding:5 5 0',
				items : [
							comboGroup, 
							
							//comboUser,
							
							comboxuserWithTree,
							
							 {
								id : 'userid',
								name : 'userid',
								hidden : true
							}
						]
			});

	addUserWindow = new Ext.Window({
		layout : 'fit',
		width : 300,
		height : 203,
		resizable : false,
		draggable : true,
		closeAction : 'hide',
		title : '<span class="commoncss">新增用户到组</span>',
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
		items : [addUserformPanel],
		buttons : [{
			text : '保存',
			iconCls : 'acceptIcon',
			handler : function() {
				
				if (addUserWindow.getComponent('userform').form.isValid()) {
				
				  comboxuserWithTree.disable();
				
					addUserWindow.getComponent('userform').form.submit({
						url : './userGroup.do?reqCode=adduserforgroup2',
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							   store.reload();
							   
							 //  Ext.MessageBox.alert('提示', '用户组管理操作成功');
							   addUserWindow.getComponent('userform').form.reset();
							   addUserWindow.hide();
								
							
								
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '用户组管理操作失败:<br>' + msg);
						//	addManufWindow.getComponent('userform').form.reset();
						}
					});
					
					 comboxuserWithTree.enable();
					 
				} else {
					// 表单验证失败
				}
			}
		}, {
			text : '关闭',
			iconCls : 'deleteIcon',
			handler : function() {
				addUserWindow.hide();
			}
		}]
	});

	

	/**
	 * 布局
	 */
		var viewport = new Ext.Viewport( {
			layout : 'border',
			items : [ {
				title : '<span class="commoncss">用户组</span>',
				iconCls : 'chart_organisationIcon',
				tools : [ {
					id : 'refresh',
					handler : function() {
						groupTree.root.reload()
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
				items : [ groupTree ]
			}, {
				region : 'center',
				layout : 'fit',
				border : false,
				margins : '3 3 3 3',
				items : [ grid ]
			} ]
		});

	
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
		var strGroupID = jsArray2JsString(rows, 'idx');
		var strUserID = jsArray2JsString(rows, 'userid');
		
		Ext.Msg.confirm('请确认', '你真的要删除吗?', function(btn, text) {
					if (btn == 'yes') {
						showWaitMsg();
							Ext.Ajax.request({
										url : './userGroup.do?reqCode=deleteuserbygroup',
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
											delkeys1 : strGroupID,
											delkeys2 : strUserID
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