Ext.ns('Ext.dlt.EditDataWindow');
Ext.dlt.EditDataWindow = Ext.extend(Ext.Window, {
	action: 1,
	actionUrl: "",
	titleInfo: "",
	layout: 'fit',
	width: 300,
	height: 273,
	resizable : false,
    draggable : true,
    closeAction : 'hide',
    addFormPanel: null;
	modal : true,
	collapsible : true,
	titleCollapse : true,
	maximizable : false,
	buttonAlign : 'right',
	border : false,
	animCollapse : true,
	animateTarget : Ext.getBody(),
	constrain : true,
	formPanel: null,
	formItems: {},
	initComponent : function(){
		Ext.dlt.EditDataWindow.superclass.initComponent.call(this);
		this.title = '<span class="commoncss">' + this.getAction() + this.titleInfo + '</span>';
	    this.initFormPanel();
	    this.items = formPanel;
	    this.buttons = createButtons();
	},
	getAction: function() {
		var result = '';
		switch(this.action) {
		case 1:
			result =  '新增';
			break;
		case 2:
			result = '修改';
			break;
		default:
			result = '查看';
		};
		return result;
	},
	
	getActionUrlHead: function() {
		return './'+ this.actionUrl + '.do?reqCode=';
	},
	
	createButtons: function() { return
		[
	        {
	            text : '保存',
	            iconCls : 'acceptIcon',
	            handler : function() {
	                if (runMode == '0') {
	                    Ext.Msg.alert('提示',
	                        '系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
	                    return;
	                }
	                if (this.getComponent('dltform').form
	                        .isValid()) {
	                    this.getComponent('dltform').form
	                            .submit({
	                                url : this.getgetActionUrlHead() + 'saveAddInfo',
	                                waitTitle : '提示',
	                                method : 'POST',
	                                waitMsg : '正在处理数据,请稍候...',
	                                success : function(
	                                        form, action) {
	                                    store.reload();
	                                    Ext.Msg.confirm('请确认',
	                                                    this.getAction() + '成功,您要继续' + this.getAction() + this.titleInfo +'吗?',
	                                                    function(btn,
	                                                            text) {
	                                                        this.getComponent('dltform').form
	                                                        .reset();
	                                                    	if (btn != 'yes') {
	                                                            this.hide();
	                                                        }
	                                                    });
	                                },
	                                failure : function(
	                                        form, action) {
	                                    var msg = action.result.msg;
	                                    Ext.MessageBox
	                                            .alert(
	                                                    '提示',
	                                                    this.titleInfo + '保存失败:<br>'
	                                                            + msg);
	                                    this.getComponent('dltform').form.reset();
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
	                this.hide();
	            }
	        } ];
	},
	
	initFormPanel: function() {
		this.formPanel = new Ext.form.FormPanel({
	        id : 'dltform',
	        name : 'dltform',
	        defaultType : 'textfield',
	        labelAlign : 'right',
	        labelWidth : 60,
	        frame : false,
	        bodyStyle : 'padding:5 5 0',
	        items : [ this.formItems ]
	    });
	}

};
