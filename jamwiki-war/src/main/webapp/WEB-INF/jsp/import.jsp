<%--

  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.

  This program is free software; you can redistribute it and/or modify
  it under the terms of the latest version of the GNU Lesser General
  Public License as published by the Free Software Foundation;

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program (LICENSE.txt); if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

--%>
<%@ page errorPage="/WEB-INF/jsp/error.jsp"
    contentType="text/html; charset=utf-8"
%>

<%@ include file="page-init.jsp" %>

<div style="margin:10px 30px 10px 30px;padding:10px;color:red;text-align:center;border:1px dashed red;"><fmt:message key="common.warning.experimental" /></div>

<c:if test="${!empty error}">
<div class="message red"><fmt:message key="${error.key}"><fmt:param value="${error.params[0]}" /></fmt:message></div>
</c:if>

<fieldset>
<legend><fmt:message key="import.caption.source" /></legend>
<form name="form1" method="post" action="<jamwiki:link value="Special:Import" />" enctype="multipart/form-data">
<input type="file" name="contents" size="50" id="importFile" />
<input type="submit" name="save" value="<fmt:message key="import.button.import" />" />
</form>
</fieldset>
