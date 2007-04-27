<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
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
            <%-- jtracc-usage --%>
            <h:outputText id="output1" value="#{messages.enter}"/>
            <h:inputText id="input1" value="#{helloWorldBacking.name}" required="true"/>

            <%-- jtracc-usage --%>
            <h:commandButton id="button1" value="#{messages.press}" action="#{helloWorldBacking.send}"/>
            <h:message id="message1" for="input1"/>

            <%-- jtracc-usage: new text --%>
            <h:outputText value="message not in bundle:"/>
            <h:outputText value="#{messages.info}"/>
        </h:panelGrid>
    </h:form>
</f:view>
</body>
</html>
