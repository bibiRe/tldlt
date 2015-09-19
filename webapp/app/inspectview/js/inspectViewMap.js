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
	
	getUserGPSRecord: function() {
		var result = new Array();
		var allOverlay = this.mapObj.getOverlays();
		for (var i = 0; i < allOverlay.length; i++) {
			var marker = allOverlay[i];
			if (1 == marker.dataType) {
				result.push(marker.record);
			}
		}
		return result;
	},
	
	getUserGPSItemMarkerByUserId: function(userid) {
		var result = null;
		var allOverlay = this.mapObj.getOverlays();
		for (var i = 0; i < allOverlay.length; i++) {
			var marker = allOverlay[i];
			if (1 == marker.dataType) {
				if (marker.record.userid == userid) {
					result = marker;
				}
			}
		}
		return result;
	},
	
	removeUserGPSMarkerByUserId: function(userid) {
		var marker = this.getUserGPSItemMarkerByUserId(item);
		if (marker) {
			marker.closeInfoWindow();
			this.mapObj.removeOverlay(marker);
		}
	},
	
	getPlanInfoContentBasic: function(data) {
		return data.planname + " 计划开始时间：" +  data.executestarttime + " 计划结束时间：" +  data.executeendtime;
	},
	
	getUserInfoContent: function(data) {
		var sContent = "<h4>位置</h4>";
		sContent += "到达时间：" + data.time;
		sContent += "<br/>";
		
		if (data.currentInfo) {
			var plans = data.currentInfo.soonplan;
			if (plans) {
				if (plans.length > 0) {
					sContent += "<h4>待执行的计划</h4>";
					for(var i = 0; i < plans.length; i++) {
						sContent += this.getPlanInfoContentBasic(plans[i]) + "<br/>";
					}
				}
			}
			plans = data.currentInfo.executeplan;
			if (plans) {
				if (plans.length > 0) {
					sContent += "<h4>正执行的计划</h4>";
					for(var i = 0; i < plans.length; i++) {
						sContent += this.getPlanInfoContentBasic(plans[i]) + " 实际开始时间：" +  plans[i].realstarttime + "<br/>";
					}
				}
			}
		}
		return sContent;
	},
	
	showUserInfoWindow: function(marker) {
		var me = this;
		var datarec = marker.record;			
		Ext.Ajax.request({
			url: './inspectView.do?reqCode=queryUserCurrentInfo',
			params: {
				userid: datarec.userid
			},
			success: function(resp) {
				var respData = Ext.util.JSON.decode(resp.responseText);
				var datas = respData.data;
				datarec.currentInfo = datas;
				var infoWindow = me.createInfoWindow("信息", me.getUserInfoContent(datarec));
				marker.openInfoWindow(infoWindow);
			},
			failure: function(resp) {
			}
		});
	},
	
	checkUserItemChanged: function(olddata, newdata) {
		var checkItems= ["longtitude", "latitude", "deptname", "username", "time"];
		for(var i = 0; i < checkItems.length; i++) {
			if (olddata[checkItems[i]] != newdata[checkItems[i]]) {
				return true;
			}
		}
		return false;
	},
	
	removeMarker: function(marker) {
		marker.closeInfoWindow();
		this.mapObj.removeOverlay(marker);
	},
	
	showUserGPSItem: function(item, createNew) {
		if (!item.longtitude) {
			return null;
		}
		var marker = this.getUserGPSItemMarkerByUserId(item.userid);
		if (null != marker) {
			 if (this.checkUserItemChanged(marker.record, item)) {
				 this.removeMarker(marker);
				 marker = null;
			 }
		} else {
			if (!createNew) {
				return;
			}
		}
		if (null == marker) {
			var myIcon = new BMap.Icon(webContext + "/resource/image/ext/user.png", new BMap.Size(16,16));
			var marker = this.createMarker(item.longtitude, item.latitude, {icon:myIcon}, 1, item);
			var label = new BMap.Label(item.deptname + '-' + item.username, {offset:new BMap.Size(20,-10)});		
			marker.setLabel(label);
			marker.addEventListener("click", function(e){          
				this.closeInfoWindow();
				var me = this.getMap().owner;
				me.showUserInfoWindow(this);
			});
		}
		return marker;
	},

	showUserGPSInfo: function(datas, create) {
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
			this.showUserGPSItem(item, create);
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
	
	removeMarkerByDataType: function(dataType) {
		var allOverlay = this.mapObj.getOverlays();
		for (var i = 0; i < allOverlay.length; i++) {
			var marker = allOverlay[i];
			if (dataType == marker.dataType) {
				this.removeMarker(marker);
				return;
			}
		}
	},
	
	removeInfoMarker: function() {
		return this.removeMarkerByDataType(2);
	},
	
	removeFaultInfoMarker: function() {
		return this.removeMarkerByDataType(3);
	},
	
	getInspectInfoWindowContent: function(record) {
		var result = ''; 
		switch (record.type) {
		case 1:
		case 2:
		case 3:
			result = record.name + "上报GPS";
			result += "<br/>时间：" + record.time;
			result += "<br/>经度：" + record.info.longtitude;
			result += "<br/>纬度：" + record.info.latitude;
			result += "<br/>速度：" + record.info.speed;
			break;
		case 4:
			result = record.name; 
			result += "<br/>巡检计划：" + record.info.planname;
			result += "<br/>时间：" + record.time;
			result += "<br/>设备：" + ((1 == record.info.inspectstate) ? "正常": "异常");
			result += "<br/>情况：" + record.info.remark;
			break;
		case 5:
			result = record.name;
			result +=  "<br/>时间：" + record.time;
			result +=  "<br/>情况：" + record.info.remark;
			break;			
		}
		result += "<br/>";
		return result;
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
		var infoWindow = this.createInfoWindow("信息", this.getInspectInfoWindowContent(record));
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
	},
	
	getFaultInfoWindowContent: function(record) {
		var result = record.deptname + record.username + "上报故障";
		result += "<br/>时间：" + record.time;
		result += "<br/>设备：" + record.devicename;
		result += "<br/>情况：" + ((1 == record.state) ? "已修复": "故障");
		result += "<br/>信息：" + record.faultinfo;
		result += "<br/>";
		return result;
	},
	
	showFaultInfoWindow: function(marker) {
		var infoWindow = this.createInfoWindow("故障信息", this.getFaultInfoWindowContent(marker.record));
		infoWindow.marker = marker;		
		marker.openInfoWindow(infoWindow); //开启信息窗口
	},
	
	showFaultInfo: function(record) {		
		if (!record) {
			return;
		}
		this.removeFaultInfoMarker();
		var myIcon = new BMap.Icon(webContext + "/resource/image/alarm3.png", new BMap.Size(16, 16));
		var marker = this.createMarker(record.longtitude, record.latitude, {icon:myIcon}, 3, record);
		var point = marker.getPosition();
		this.mapObj.setCenter(point);
		marker.addEventListener("click", function(e){          
			this.closeInfoWindow();
			var me = this.getMap().owner;
			me.showFaultInfoWindow(this);
		});
		this.showFaultInfoWindow(marker);
	}
});

Ext.reg('inspectViewMap', Ext.ux.app.InspectViewMap);