<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="巡检记录补录" uxEnabled="true">
<G4Studio:ext.myux uxType="datatimefield"/>
<G4Studio:ext.myux uxType="monthpicker"/>
<G4Studio:import src="/app/inspectionrecordmakeup/js/inspectionrecordmakeupview.js"/>
<G4Studio:ext.codeRender fields="ENABLED,EDITMODE"/>
<G4Studio:ext.codeStore fields="ENABLED,EDITMODE"/>
<G4Studio:ext.codeStore fields="INSPECPLANSTATE"/>
<G4Studio:ext.codeRender fields="INSPECPLANSTATE" />
<G4Studio:body>
</G4Studio:body>
<G4Studio:script>
   var deptid = '<G4Studio:out key="deptid" scope="request"/>';
   var deptname = '<G4Studio:out key="deptname" scope="request"/>';
   
</G4Studio:script>
</G4Studio:html>