Ext.ns("Ext.ux.app");
Ext.ux.app.AppTools = function(){
	
};

Ext.ux.app.AppTools.EnabledSpanRender = function(value,cellmeta, record) {
	var result = "<span class='";
	if (1 == value) {
		result += "stateEnable";
	} else {
		result += "stateDisable";
	}
	result += "'>" + ENABLEDRender(value) + "</span>";
	return result;
}

Ext.ux.app.AppTools.InspectPlanStateRender = function(value,cellmeta, record) {
	var result = "<span class='";
	switch(value) {
	case 1:
		result += "stateColorWriting";
		break;
	case 2:
		result += "stateColorApproving";
		break;
	case 3:
		result += "stateColorApproveFail";
		break;
	case 4:
		result += "stateColorApproveSuccess";
		break;
	case 5:
		result += "stateColorInspecting";
		break;
	case 6:
		result += "stateColorInspectFinished";
		break;
	default:
		result += "";
	}
	result += "'>" + INSPECPLANSTATERender(value) + "</span>";
	return result;
}