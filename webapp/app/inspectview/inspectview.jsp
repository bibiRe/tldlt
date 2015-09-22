<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<G4Studio:html title="实时查看" >
<G4Studio:import src="/resource/css/app.css" />
<G4Studio:import src="/app/common/appTools.js" />
<G4Studio:import src="/app/common/gridBbar.js" />
<G4Studio:import src="/app/common/appGrid.js" />
<G4Studio:import src="/app/inspectview/js/inspectView.js" />
<G4Studio:import src="/app/inspectview/js/inspectViewMap.js" />
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=1.3"></script>
<G4Studio:ext.codeRender fields="SEX,LOCKED,USERTYPE,DEVFAULTSTATE"/>
<G4Studio:ext.codeStore fields="SEX,LOCKED,USERTYPE,DEVFAULTSTATE:3"/>
<G4Studio:body>
    
</G4Studio:body>
<G4Studio:script>
   var root_deptid = '<G4Studio:out key="rootDeptid" scope="request"/>';
   var root_deptname = '<G4Studio:out key="rootDeptname" scope="request"/>';
   var login_account = '<G4Studio:out key="login_account" scope="request"/>';
</G4Studio:script>
</G4Studio:html>