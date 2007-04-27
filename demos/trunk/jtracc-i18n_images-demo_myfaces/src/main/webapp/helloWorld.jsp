<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
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

        <t:commandButton value="translate"/>
        <%-- jtracc-end --%>

        <h:panelGrid id="grid" columns="2">
            <%-- jtracc-usage --%>
            <t:graphicImage value="#{messages.img_earth}" width="#{messages.img_width_earth}"
                            height="#{messages.img_height_earth}" alt="#{messages.img_alt_world1}"/>
            <%-- jtracc-usage --%>
            <t:graphicImage value="#{messages.earth2}" width="#{messages.earth2_width}"
                            height="#{messages.earth2_height}" alt="#{messages.world2_alt}"/>


            <%-- jtracc-usage --%>
            <t:outputText id="output1" value="#{messages.name}"/>
            <t:inputText id="input1" value="#{helloWorldBacking.name}" required="true"/>
            <%-- jtracc-usage --%>
            <t:commandButton id="button1" value="#{messages.press}" action="#{helloWorldBacking.send}"/>
            <t:message id="message1" for="input1"/>
        </h:panelGrid>
    </h:form>
</f:view>
</body>
</html>
