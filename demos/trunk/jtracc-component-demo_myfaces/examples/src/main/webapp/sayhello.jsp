<%@ page session="false" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.myorganitzation.org/mycomponents" prefix="mycomp" %>
<%-- jtracc --%>
<%@ taglib uri="http://www.irian.at/jtracc" prefix="jtr" %>

<html>
<head>
    <title>My JSF Components</title>
</head>

<body>

<f:view>

    <mycomp:sayHello firstName="John" lastName="Smith"/>

    <h:outputText value=" (probably not you, I know)"/>

    <f:verbatim>
        <hr>
    </f:verbatim>

    <h:form>
        <%-- jtracc-start --%>
        <jtr:translation/>
        <jtr:targetLocale/>
        <jtr:translationPane/>

        <h:commandButton value="translate"/>
        <%-- jtracc-end --%>

        <h:panelGrid columns="1">

            <mycomp:sayHello firstName="#{messages.firstName}"
                             lastName="#{messages.lastName}"/>

            <h:commandLink value="[HOME]" action="go_home"/>

        </h:panelGrid>

    </h:form>

</f:view>


</body>

</html>
