Ext.ns("Ext.ux.app");
Ext.ux.app.AppTools = function(){
	
};

function getSpanRender(value, spanClass, info) {
	var result = "<span class='";
	result += spanClass;
	result += "'>" + info  + "</span>";
	return result;
};

function getEnableSpanClass(value) {
	return (1 == value)? "stateEnable": "stateDisable";
}

Ext.ux.app.AppTools.EnabledSpanRender = function(value,cellmeta, record) {
	return getSpanRender(value, getEnableSpanClass(value), ENABLEDRender(value));
};

function getDeviceFaultStateSpanClass(value) {
	var result = '';
	switch(value) {
	case 0:
		result = "stateHandling";
		break;
	case 1:
		result = "stateNoHandle";
		break;
	case 2:
		result = "stateHandled";
		break;	
	default:
		result = "stateColorUnknown";
	}
	return result;
}

var EXTUXAPP = Ext.ux.app;

Ext.ux.app.AppTools.DeviceFaultStateRender = function(value,cellmeta, record) {
	return getSpanRender(value, getDeviceFaultStateSpanClass(value), DEVFAULTSTATERender(value));
};

Ext.ux.app.AppTools.enableTextField = function(enabled, id) {
	if (enabled) {
		Ext.getCmp(id).enable();
		Ext.getCmp(id).removeClass('x-custom-field-disabled');		
	} else {
		Ext.getCmp(id).disable();
		Ext.getCmp(id).addClass('x-custom-field-disabled');
	}	
};

Ext.ux.app.AppTools.showTextField = function(visabled, id) {	
	if (visabled) {
		Ext.getCmp(id).show();	
	} else {
		Ext.getCmp(id).hide();
	}
	Ext.getCmp(id).hideLabel = (!visabled);
};

Ext.ux.app.AppTools.showAlert = function(info) {	
	Ext.MessageBox.alert('提示', info);
};

function getInspectPlanStateSpanClass(value) {
	var result = '';
	switch(value) {
	case 1:
		result = "stateColorWriting";
		break;
	case 2:
		result = "stateColorApproving";
		break;
	case 3:
		result = "stateColorApproveFail";
		break;
	case 4:
		result = "stateColorApproveSuccess";
		break;
	case 5:
		result = "stateColorInspecting";
		break;
	case 6:
		result = "stateColorInspectFinished";
		break;
	default:
		result = "stateColorUnknown";
	}
	return result;
}

Ext.ux.app.AppTools.InspectPlanStateRender = function(value,cellmeta, record) {
	return getSpanRender(value, getInspectPlanStateSpanClass(value), INSPECPLANSTATERender(value));
};
