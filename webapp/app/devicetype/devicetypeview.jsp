<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="设备类型" >
<G4Studio:import src="/resource/css/app.css" />
<G4Studio:import src="/app/common/appTools.js" />
<G4Studio:import src="/app/common/gridBbar.js" />
<G4Studio:import src="/app/common/appGrid.js" />
<G4Studio:import src="/app/devicetype/js/deviceType.js" />
<G4Studio:import src="/app/devicetypecheckitem/js/deviceTypeCheckItemPanel.js" />
<G4Studio:ext.codeRender fields="DEVCHKACTION,ENABLED"/>
<G4Studio:ext.codeStore fields="DEVCHKACTION,ENABLED:3"/>
<G4Studio:body>
    <G4Studio:div key="deviceTypeTreeDiv"></G4Studio:div>
</G4Studio:body>
</G4Studio:html>