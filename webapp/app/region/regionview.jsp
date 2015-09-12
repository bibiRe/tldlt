<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="区域管理" >
<G4Studio:ext.codeRender fields="REGIONTYPE"  />
<G4Studio:ext.codeStore fields="REGIONTYPE:3"/>
<G4Studio:import src="/app/common/gridBbar.js" />
<G4Studio:import src="/app/region/js/region.js" />
<G4Studio:body>
<G4Studio:div key="regionTreeDiv"></G4Studio:div>
</G4Studio:body>
<G4Studio:script>
   var root_deptid = '<G4Studio:out key="rootDeptid" scope="request"/>';
   var root_deptname = '<G4Studio:out key="rootDeptname" scope="request"/>';
</G4Studio:script>
</G4Studio:html>