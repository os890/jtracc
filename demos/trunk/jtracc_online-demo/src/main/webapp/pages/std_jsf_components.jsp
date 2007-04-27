<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://myfaces.apache.org/trinidad" prefix="tr" %>
<%@ taglib uri="http://www.irian.at/jtracc" prefix="jtr" %>

<html>
<head>
    <title>jtracc live demo</title>
    <link type="text/css" rel="stylesheet" href="../styles/main.css"/>

    <meta name="keywords"
          content="live, live demo, i18n, jtracc, translation, javaserver faces, jsf, myfaces, tomahawk, trinidad, irian.at, webapp, web-app, demos"/>
    <meta name="description" content="jtracc - Live-Demo"/>
    <meta name="author" content="Gerhard Petracek"/>
    <meta name="publisher" content="IRIAN Solutions Softwareentwicklungs- und Beratungsgesellschaft mbH"/>
    <meta name="copyright" content="IRIAN Solutions Softwareentwicklungs- und Beratungsgesellschaft mbH"/>
</head>
<body>
<f:view>
<h:form id="form">
        <t:div styleClass="demo_translation_panel">
            <jtr:translation styleClass="demo_translation_mode"/>
            <h:outputText value="edit/translate to:" styleClass="demo_language_box_label"/>
            <jtr:targetLocale styleClass="demo_language_box"/>
            <jtr:translationPane/>
            <h:commandButton value="translate"/>
        </t:div>

    <t:div styleClass="demo_content_box">

        <h:commandLink value="<< back to the demo overview" action="home" styleClass="demo_back_link"/>
        
        <h:panelGrid columns="3" styleClass="demo_info_box">
            <h:outputText styleClass="demo_info"
                    value="in this demo version you can change all translatable messages in"/>
            <h:outputText styleClass="demo_info info_bold"
                    value=" your "/>
            <h:outputText styleClass="demo_info"
                    value="session (simulated persistence)"/>
        </h:panelGrid>

        <h:panelGrid id="grid" columns="2" styleClass="demo_component_box">
            <h:outputText value="description" styleClass="demo_headline"/>
            <h:outputText value="demo components" styleClass="demo_headline"/>


            <%-- standard jsf components --%>
            <h:outputText value="command button: "/>
            <h:commandButton id="hButton" value="#{messages.pressme}"/>

            <h:outputText value="command button: "/>
            <h:commandLink id="link" value="#{messages.followme}" target="http://www.irian.at/"/>

            <h:outputText value="output link: "/>
            <h:outputLink id="outputLink" value="#{messages.company_link}">
                <tr:outputText value="#{messages.visit} #{messages.company}"/>
            </h:outputLink>

            <h:outputText value="select one listbox: "/>
            <h:selectOneListbox size="3" value="2">
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </h:selectOneListbox>

            <h:outputText value="select many listbox: "/>
            <h:selectManyListbox value="#{helloWorldBacking.multiSelected}">
                <f:selectItems value="#{helloWorldBacking.options}"/>
                <f:selectItem itemValue="1" itemLabel="#{messages.option_1}"/>
            </h:selectManyListbox>

            <h:outputText value="select many menu: "/>
            <h:selectOneMenu value="3">
                <f:selectItem itemValue="" itemLabel="#{messages.option_1}"/>
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </h:selectOneMenu>

            <h:outputText value="select one radio: "/>
            <h:selectOneRadio value="3">
                <f:selectItem itemValue="1" itemLabel="#{messages.option_1}"/>
                <f:selectItem itemValue="2" itemLabel="#{messages.option_2}"/>
                <f:selectItem itemValue="3" itemLabel="#{messages.option_3}"/>
            </h:selectOneRadio>

            <h:outputText value="select many checkbox: "/>
            <h:selectManyCheckbox value="#{helloWorldBacking.multiSelected}">
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </h:selectManyCheckbox>

            <h:outputText value="complex component (dataTable): "/>
            <h:dataTable id="table" var="entry" value="#{helloWorldBacking.tableContent}">
                <h:column>
                    <f:facet name="header">
                        <tr:outputText value="#{messages.table_head}"/>
                    </f:facet>
                    <tr:outputText value="#{entry.content}"/>
                </h:column>
            </h:dataTable>

        </h:panelGrid>

        <h:commandLink value="<< back to the demo overview" action="home" styleClass="demo_back_link"/>
        <%--<h:messages/>--%>
    </t:div>
</h:form>
</f:view>
</body>
</html>
