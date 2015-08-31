<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="设备管理"  >
<G4Studio:import src="/app/devmgr/js/devmgrview.js"/>
<G4Studio:ext.codeRender fields="LEAF"/>
<G4Studio:ext.codeRender fields="SEX" />
<G4Studio:ext.codeStore fields="CHECKCYCLE"/>
<G4Studio:ext.codeRender fields="CHECKCYCLE" />
<G4Studio:body>
<G4Studio:div key="deptTreeDiv"></G4Studio:div>
</G4Studio:body>
<G4Studio:script>
   var root_deptid = '<G4Studio:out key="rootDeptid" scope="request"/>';
   var root_deptname = '<G4Studio:out key="rootDeptname" scope="request"/>';
   
</G4Studio:script>
</G4Studio:html>