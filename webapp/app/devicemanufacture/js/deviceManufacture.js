/**
 * 设备厂家
 * 
 * @author hhe
 * @since 2015-07-23
 */
Ext.onReady(function() {
    var sm = new Ext.grid.CheckboxSelectionModel();
    var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), sm,
            {
                header : '编号',
                dataIndex : 'id',
                sortable : true,
                width : 50
            }, {
                header : '名称',
                dataIndex : 'name'
            } ]);

    var store = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : './deviceManufacture.do?reqCode=query'
        }),
        reader : new Ext.data.JsonReader({
            totalProperty : 'TOTALCOUNT',
            root : 'ROOT'
        }, [ {
            name : 'id'
        }, {
            name : 'name'
        } ])
    });

    // 翻页排序时带上查询条件
    store.on('beforeload', function() {
        this.baseParams = {
            deviceTypeId : Ext.getCmp('deviceTypeId').getValue(),
            deviceTypeName : Ext.getCmp('deviceTypeName').getValue()
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
            fields : [ 'value', 'text' ],
            data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
                    [ 100, '100条/页' ], [ 250, '250条/页' ],
                    [ 500, '500条/页' ] ]
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
        items : [ '-', '&nbsp;&nbsp;', pagesize_combo ]
    })

    var grid = new Ext.grid.GridPanel({
        title : '<span class="commoncss">设备类型数据列表</span>',
        height : 510,
        store : store,
        region : 'center',
        margins : '3 3 3 3',
        loadMask : {
            msg : '正在加载表格数据,请稍等...'
        },
        stripeRows : true,
        frame : true,
        // autoExpandColumn : 'name',
        cm : cm,
        sm : sm,
        tbar : [ {
            text : '新增',
            iconCls : 'page_addIcon',
            handler : function() {
                addDeviceTypeWindow.show();
            }
        }, '-', {
            text : '修改',
            iconCls : 'page_edit_1Icon',
            handler : function() {
                ininEditCodeWindow();
            }
        }, '-', {
            text : '删除',
            iconCls : 'page_delIcon',
            handler : function() {
                deleteDeviceType();
            }
        }, '->', new Ext.form.TextField({
            id : 'deviceTypeId',
            name : 'deviceTypeId',
            emptyText : '编号',
            width : 100
        }), '-', new Ext.form.TextField({
            id : 'deviceTypeName',
            name : 'deviceTypeName',
            emptyText : '名称',
            enableKeyEvents : true,
            listeners : {
                specialkey : function(field, e) {
                    if (e.getKey() == Ext.EventObject.ENTER) {
                        queryDeviceType();
                    }
                }
            },
            width : 130
        }), {
            text : '查询',
            iconCls : 'previewIcon',
            handler : function() {
                queryDeviceType();
            }
        }, '-', {
            text : '刷新',
            iconCls : 'arrow_refreshIcon',
            handler : function() {
                store.reload();
            }
        } ],
        bbar : bbar
    });
    store.load({
        params : {
            start : 0,
            limit : bbar.pageSize
        }
    });

    grid.addListener('rowdblclick', ininEditCodeWindow);
    grid.on('sortchange', function() {
        // grid.getSelectionModel().selectFirstRow();
    });

    bbar.on("change", function() {
        // grid.getSelectionModel().selectFirstRow();
    });
    /**
     * 新增设备类型对照表
     */
    var addDeviceTypeWindow;
    var addFormPanel;

    addFormPanel = new Ext.form.FormPanel({
        id : 'addDeviceTypeForm',
        name : 'addDeviceTypeForm',
        defaultType : 'textfield',
        labelAlign : 'right',
        labelWidth : 60,
        frame : false,
        bodyStyle : 'padding:5 5 0',
        items : [ {
            fieldLabel : '编号',
            name : 'id',
            anchor : '100%',
            labelStyle : micolor,
            allowBlank : false
        }, {
            fieldLabel : '名称',
            name : 'name',
            anchor : '100%',
            labelStyle : micolor,
            allowBlank : false
        } ]
    });

    addDeviceTypeWindow = new Ext.Window({
        layout : 'fit',
        width : 300,
        height : 273,
        resizable : false,
        draggable : true,
        closeAction : 'hide',
        title : '<span class="commoncss">新增设备类型</span>',
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
        items : [ addFormPanel ],
        buttons : [
            {
                text : '保存',
                iconCls : 'acceptIcon',
                handler : function() {
                    if (runMode == '0') {
                        Ext.Msg.alert('提示',
                            '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
                        return;
                    }
                    if (addDeviceTypeWindow.getComponent('addDeviceTypeForm').form
                            .isValid()) {
                        addDeviceTypeWindow.getComponent('addDeviceTypeForm').form
                                .submit({
                                    url : './deviceManufacture.do?reqCode=saveAddInfo',
                                    waitTitle : '提示',
                                    method : 'POST',
                                    waitMsg : '正在处理数据,请稍候...',
                                    success : function(
                                            form, action) {
                                        store.reload();
                                        Ext.Msg.confirm('请确认',
                                                        '设备类型新增成功,您要继续添加设备类型吗?',
                                                        function(btn,
                                                                text) {
                                                            addDeviceTypeWindow.getComponent('addDeviceTypeForm').form
                                                            .reset();
                                                        	if (btn != 'yes') {
                                                                addDeviceTypeWindow.hide();
                                                            }
                                                        });
                                    },
                                    failure : function(
                                            form, action) {
                                        var msg = action.result.msg;
                                        Ext.MessageBox
                                                .alert(
                                                        '提示',
                                                        '设备类型保存失败:<br>'
                                                                + msg);
                                        addDeviceTypeWindow
                                                .getComponent('addDeviceTypeForm').form
                                                .reset();
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
                    addDeviceTypeWindow.hide();
                }
            } ]
    });

    /** *****************修改设备类型*********************** */
    var editDeviceTypeWindow, editDeviceTypeFormPanel;
    editDeviceTypeFormPanel = new Ext.form.FormPanel({
        labelAlign : 'right',
        labelWidth : 60,
        defaultType : 'textfield',
        frame : false,
        bodyStyle : 'padding:5 5 0',
        id : 'editDeviceTypeFormPanel',
        name : 'editDeviceTypeFormPanel',
        items : [ {
            xtype : 'hidden',
            name : 'oid'
        }, {
            fieldLabel : '编号',
            name : 'id',
            anchor : '100%',
            labelStyle : micolor,
            allowBlank : false
        }, {
            fieldLabel : '名称',
            name : 'name',
            anchor : '100%',
            labelStyle : micolor,
            allowBlank : false
        } ]
    });

    editDeviceTypeWindow = new Ext.Window(
            {
                layout : 'fit',
                width : 300,
                height : 273,
                resizable : false,
                draggable : true,
                closeAction : 'hide',
                title : '<span class="commoncss">修改设备类型</span>',
                modal : true,
                collapsible : true,
                titleCollapse : true,
                maximizable : false,
                buttonAlign : 'right',
                border : false,
                animCollapse : true,
                animateTarget : Ext.getBody(),
                constrain : true,
                items : [ editDeviceTypeFormPanel ],
                buttons : [
                        {
                            text : '保存',
                            iconCls : 'acceptIcon',
                            handler : function() {
                                if (runMode == '0') {
                                    Ext.Msg
                                            .alert('提示',
                                                    '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
                                    return;
                                }
                                updateDeviceType();
                            }
                        }, {
                            text : '关闭',
                            iconCls : 'deleteIcon',
                            handler : function() {
                                editDeviceTypeWindow.hide();
                            }
                        } ]

            });
    /**
     * 布局
     */
    var viewport = new Ext.Viewport({
        layout : 'border',
        items : [ grid ]
    });

    /**
     * 初始化代码修改出口
     */
    function ininEditCodeWindow() {
        var record = grid.getSelectionModel().getSelected();
        if (Ext.isEmpty(record)) {
            Ext.Msg.alert('提示', '请先选中要修改的项目');
            return;
        }
        if (record.get('editmode') == '0') {
            Ext.Msg.alert('提示', '您选中的记录为系统内置的代码对照,不允许修改');
            return;
        }
        var oid = editDeviceTypeFormPanel.getForm().findField("oid");
        oid.readOnly = true;
        oid.setValue(record.id);
        editDeviceTypeWindow.show();
        editDeviceTypeFormPanel.getForm().loadRecord(record);
    }

    /**
     * 修改字典
     */
    function updateDeviceType() {
        if (!editDeviceTypeFormPanel.form.isValid()) {
            return;
        }
        editDeviceTypeFormPanel.form.submit({
            url : './deviceManufacture.do?reqCode=saveUpdateInfo',
            waitTitle : '提示',
            method : 'POST',
            waitMsg : '正在处理数据,请稍候...',
            success : function(form, action) {
                editDeviceTypeWindow.hide();
                store.reload();
            },
            failure : function(form, action) {
                var msg = action.result.msg;
                Ext.MessageBox.alert('提示', '设备类型保存失败:<br>' + msg);
            }
        });
    }

    /**
     * 删除代码对照
     */
    function deleteDeviceType() {
        if (runMode == '0') {
            Ext.Msg.alert('提示', '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
            return;
        }
        var rows = grid.getSelectionModel().getSelections();
        if (Ext.isEmpty(rows)) {
            Ext.Msg.alert('提示', '请先选中要删除的项目!');
            return;
        }
        var strChecked = jsArray2JsString(rows, 'id');
        Ext.Msg.confirm('请确认', '你真的要删除设备类型吗?', function(btn, text) {
            if (btn == 'yes') {
                showWaitMsg();
                Ext.Ajax.request({
                    url : './deviceManufacture.do?reqCode=delete',
                    success : function(response) {
                        store.reload();
                        hideWaitMsg();
                    },
                    failure : function(response) {
                        store.reload();
                        hideWaitMsg();
                        var resultArray = Ext.util.JSON
                                .decode(response.responseText);
                        Ext.Msg.alert('提示', resultArray.msg);
                    },
                    params : {
                        ids : strChecked
                    }
                });
            }
        });
    }

    /**
     * 根据条件查询字典
     */
    function queryDeviceType() {
        store.load({
            params : {
                start : 0,
                limit : bbar.pageSize,
                deviceTypeId : Ext.getCmp('deviceTypeId').getValue(),
                deviceTypeName : Ext.getCmp('deviceTypeName')
                        .getValue()
            }
        });
    }

    /**
     * 刷新字典
     */
    function refreshCodeTable() {
        store.load({
            params : {
                start : 0,
                limit : bbar.pageSize
            }
        });
    }
});