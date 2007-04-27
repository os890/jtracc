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


            <%-- tomahawk components --%>
            <h:outputText value="tomahawk command button: "/>
            <t:commandButton value="#{messages.pressme}"/>

            <h:outputText value="tomahawk command link: "/>
            <t:commandLink value="#{messages.followme}"/>

            <h:outputText id="tomahawk_output_label" value="tomahawk output label: "/>
            <t:outputLabel for="tomahawk_output_label" value="#{messages.tomahawk_label}"/>

            <h:outputText value="tomahawk input textarea: "/>
            <t:inputTextarea value="#{messages.company_link}"/>

            <h:outputText value="tomahawk select one listbox: "/>
            <t:selectOneListbox>
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </t:selectOneListbox>

            <h:outputText value="tomahawk select many menu"/>
            <t:selectManyMenu readonly="true">
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </t:selectManyMenu>

            <h:outputText value="tomahawk panel navigation"/>
            <t:panelNavigation2 id="tMenu" layout="list">
                <t:commandNavigation2>
                    <tr:outputText value="#{messages.entry1}"/>
                </t:commandNavigation2>
                <t:commandNavigation2>
                    <tr:outputText value="#{messages.entry2}"/>
                </t:commandNavigation2>
                <t:commandNavigation2>
                    <tr:outputText value="#{messages.entry3}"/>
                </t:commandNavigation2>
            </t:panelNavigation2>

        </h:panelGrid>

        <h:commandLink value="<< back to the demo overview" action="home" styleClass="demo_back_link"/>
        <%--<h:messages/>--%>
    </t:div>
</h:form>
</f:view>
</body>
</html>
