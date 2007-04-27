<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/trinidad" prefix="tr" %>
<%-- jtracc --%>
<%@ taglib uri="http://www.irian.at/jtracc" prefix="jtr" %>

<html>
<head>
    <title>Hello World</title>
</head>
<body>
<f:view>
    <h:form id="form">
        <%-- jtracc-start --%>
        <jtr:translation/>
        <jtr:targetLocale/>
        <jtr:translationPane/>

        <h:commandButton value="translate"/>
        <%-- jtracc-end --%>

        <h:panelGrid id="grid" columns="2">
            <tr:selectBooleanRadio label="#{messages.true_false_label}" text="#{messages.true_false_option}"/>
            <h:panelGroup/>
            <tr:outputLabel value="#{messages.visit}" for="goButton"/>
            <tr:goButton id="goButton" text="#{messages.company}!" destination="http://www.irian.at"/>
        </h:panelGrid>
    </h:form>
</f:view>
</body>
</html>
