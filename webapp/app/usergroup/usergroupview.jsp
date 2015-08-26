<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="用户组管理"  >
<G4Studio:import src="/app/usergroup/js/usergroupview.js"/>
<G4Studio:ext.codeRender fields="LEAF"/>
<G4Studio:ext.codeRender fields="SEX" />
<G4Studio:body>
<G4Studio:div key="groupTreeDiv"></G4Studio:div>
</G4Studio:body>
<G4Studio:script>
   var root_groupid = '<G4Studio:out key="root_groupid" scope="request"/>';
   var root_groupname = '<G4Studio:out key="root_groupname" scope="request"/>';
   
</G4Studio:script>
</G4Studio:html>