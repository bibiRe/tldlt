<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="故障管理" >
<G4Studio:import src="/resource/css/app.css" />
<G4Studio:import src="/app/common/appTools.js" />
<G4Studio:import src="/app/common/gridBbar.js" />
<G4Studio:import src="/app/common/appGrid.js" />
<G4Studio:import src="/app/devicefault/js/deviceFaultView.js" />
<G4Studio:ext.codeRender fields="DEVFAULTSTATE"/>
<G4Studio:ext.codeStore fields="DEVFAULTSTATE:3"/>
<G4Studio:body>
    
</G4Studio:body>
<G4Studio:script>
   var login_userid = '<G4Studio:out key="login_userid" scope="request"/>';
</G4Studio:script>
</G4Studio:html>