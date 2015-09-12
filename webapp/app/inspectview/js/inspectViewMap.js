Ext.ns("Ext.ux.app");
Ext.ux.app.InspectViewMap = Ext.extend(Ext.Panel, {
	anchor: '100%',
	frame: true,
	collapsible: true,
	draggable: true,
	cls: 'x-portlet',
	mapObj: null,
	mapLocationForm: null,
	constructor: function(config) {
		Ext.apply(this, config);
		this.init();
		Ext.ux.app.InspectViewMap.superclass.constructor.call(this, Ext.applyIf(config || {}, {
			layout: 'border',
			width: 500,
			height: 500,
			minWidth: 450,
			minHeight: 500,
			split: true,
			items: [{
				region: 'center',
				layout: 'fit',
				margins: '0 -1 -1 -1',
				items: [this.mapLocationForm]
			}]
		}));
	},
	init: function() {
		this.createFormPanel();
	},

	createMapObj: function() {
		this.mapObj = new BMap.Map("myFrameMap"); // 创建地图实例
		this.mapObj.centerAndZoom(new BMap.Point(118.775223, 32.062868), 15); // 初始化地图,设置中心点坐标和地图级别
		this.mapObj.addControl(new BMap.MapTypeControl()); //添加地图类型控件
		this.mapObj.addControl(new BMap.NavigationControl());
		// this.mapObj.setCurrentCity("南京");          // 设置地图显示的城市 此项是必须设置的
		this.mapObj.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
		this.mapObj.owner = this;
	},

	createFormPanel: function() {
		this.mapLocationForm = new Ext.form.FormPanel({
			id: Ext.id(),
			width: "100%",
			name: 'uploadForm',
			labelWidth: 60,
			defaultType: 'textfield',
			labelAlign: 'right',
			bodyStyle: 'padding:5 5 5 5',
			html: '<div id="myFrameMap" style="width:100%; height:100%"></div>',
			tbar: ['->', "实时查看"]
		});
		var me = this;
		setTimeout(function() {
			me.createMapObj();
		}, 1000);
	},

	createInfoWindow: function(title, content) {
		var opts = {
			title: title // 信息窗口标题
		};
		var infoWindow = new BMap.InfoWindow(content, opts); // 创建信息窗口对象
		infoWindow.disableCloseOnClick();
		return infoWindow;
	},

	removeUserGPSItemMarker: function(item) {
		var allOverlay = this.mapObj.getOverlays();
		for (var i = 0; i < allOverlay.length; i++) {
			var marker = allOverlay[i];
			if (1 == marker.dataType) {
				if (marker.record.userid == item.userid) {
					this.mapObj.removeOverlay(marker);
					return;
				}
			}
		}
	},
	
	getUserInfoContent: function(data) {
		var sContent =
			"<h4>位置</h4>";
		sContent += "到达时间：" + data.time + "<br/>"		
		if (data.soonExecuteInspectPlan) {
			if (data.soonExecuteInspectPlan.length > 0) {
				sContent += "<h4>待执行的计划</h4>";
				for(var i = 0; i < data.soonExecuteInspectPlan.length; i++) {
					sContent += data.soonExecuteInspectPlan[i].planname + " 计划开始时间：" +  data.soonExecuteInspectPlan[i].executestarttime + " 计划结束时间：" +  data.soonExecuteInspectPlan[i].executeendtime + "<br/>";
				}
			}
		}
		if (data.executingInspectPlan) {
			if (data.executingInspectPlan.length > 0) {
				sContent += "<h4>正在执行的计划</h4>";
				for(var i = 0; i < data.executingInspectPlan.length; i++) {
					sContent += data.executingInspectPlan[i].planname + " 计划开始时间：" +  data.executingInspectPlan[i].executestarttime + " 计划结束时间：" +  data.executingInspectPlan[i].executeendtime + " 实际开始时间：" +  data.executingInspectPlan[i].realstarttime + "<br/>";
				}
			}
		}
		return sContent;
	},
	
	queryUserInfoContent: function(marker) {
		var data = marker.record;		
			
		Ext.Ajax.request({
			url: './inspectView.do?reqCode=queryUserExecutingInspectPlan',
			async :  false,
			params: {
				userid: data.userid
			},
			success: function(resp) {
				var respData = Ext.util.JSON.decode(resp.responseText);
				var datas = respData.data;
				data.executingInspectPlan = datas;
			},
			failure: function(resp) {
			}
		});	

		Ext.Ajax.request({
			url: './inspectView.do?reqCode=queryUserSoonExecuteInspectPlan',
			async :  false,
			params: {
				userid: data.userid
			},
			success: function(resp) {
				var respData = Ext.util.JSON.decode(resp.responseText);
				var datas = respData.data;
				data.soonExecuteInspectPlan = datas;
			},
			failure: function(resp) {
			}
		});		
		return this.getUserInfoContent(data);
	},
	
	showUserGPSItem: function(item) {
		if (!item.longtitude) {
			return null;
		}
		this.removeUserGPSItemMarker(item);
		var myIcon = new BMap.Icon(webContext + "/resource/image/ext/user.png", new BMap.Size(16,16));
		var marker = this.createMarker(item.longtitude, item.latitude, {icon:myIcon}, 1, item);
		var label = new BMap.Label(item.deptname + '-' + item.username, {offset:new BMap.Size(20,-10)});
		marker.setLabel(label);
		marker.addEventListener("click", function(e){          
			this.closeInfoWindow();
			var me = this.getMap().owner;
			var infoWindow = me.createInfoWindow("信息", me.queryUserInfoContent(marker));
			this.openInfoWindow(infoWindow);
		});
		return marker;
	},

	showUserGPSInfo: function(datas) {
		if (!datas) {
			return;
		}
		if (datas.length < 1) {
			return;
		}
		for (var i = 0; i < datas.length; i++) {
			var item = datas[i];
			if (!item) {
				continue;
			}
			this.showUserGPSItem(item);
		}
	},

	createMarker: function(long, lat, opts, dataType, record) {
		var point = new BMap.Point(long, lat);
		var marker = null;
		if (!opts) {
		  marker = new BMap.Marker(point); // 创建标注
		} else {
			marker = new BMap.Marker(point, opts); // 创建标注
		}			
		marker.dataType = dataType;
		marker.record = Ext.applyIf({}, record);
		this.mapObj.addOverlay(marker); // 将标注添加到地图中
		this.mapObj.setCenter(point);
		return marker;
	},
	
	removeInfoMarker: function() {
		var allOverlay = this.mapObj.getOverlays();
		for (var i = 0; i < allOverlay.length; i++) {
			var marker = allOverlay[i];
			if (2 == marker.dataType) {
				this.mapObj.removeOverlay(marker);
				return;
			}
		}
	},
	
	showInspectInfo: function(record) {		
		if (!record) {
			return;
		}
		if (!record.info) {
			return;
		}
		this.removeInfoMarker();
		var marker = this.createMarker(record.info.longtitude, record.info.latitude, null, 2, record);
		var point = marker.getPosition();
		this.mapObj.setCenter(point);
		var infoWindow = this.createInfoWindow("信息", record.name + "上报GPS<br/>时间：" + record.time + "<br/>经度：" + record.info.longtitude + "<br/>纬度：" + record.info.latitude + "<br/>速度：" + record.info.speed + "<br/>");
		infoWindow.marker = marker;
		infoWindow.addEventListener("close", function(type, target, point) {
			var tar = this.marker;
			if (tar) {
				if (tar.map) {
					tar.map.removeOverlay(tar);
				}
			}
		});
		marker.openInfoWindow(infoWindow); //开启信息窗口
	}
});

Ext.reg('inspectViewMap', Ext.ux.app.InspectViewMap);