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

            <%-- trinidad components --%>
            <h:outputText value="trinidad reset button: "/>
            <tr:resetButton text="#{messages.reset}"/>

            <h:outputText id="trLinkDescription" value="trinidad output label: "/>
            <tr:outputLabel value="#{messages.trinidad_label}" for="trLinkDescription"/>

            <h:outputText value="trinidad command button: "/>
            <tr:commandButton id="trButton" text="#{messages.trinidad_pressme}"/>

            <h:outputText value="trinidad command link: "/>
            <tr:commandLink text="#{messages.trinidad_pressme}"/>

            <h:outputText value="trinidad go link: "/>
            <tr:goLink text="#{messages.trinidad_go1}"/>

            <h:outputText value="trinidad go button: "/>
            <tr:goButton text="#{messages.trinidad_go2}"/>

            <h:outputText value="trinidad select boolean checkbox: "/>
            <tr:selectBooleanCheckbox label="#{messages.true_false_label1}" text="#{messages.true_false_option1}"/>

            <h:outputText value="select boolean radio: "/>
            <tr:selectBooleanRadio label="#{messages.true_false_label2}" text="#{messages.true_false_option2}"/>

            <h:outputText value="trinidad input: "/>
            <tr:inputText value="#{messages.newInputContent}"/>

            <h:outputText value="trinidad select one listbox: "/>
            <tr:selectOneListbox size="3" value="3">
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </tr:selectOneListbox>

            <h:outputText value="trinidad select many listbox: "/>
            <tr:selectManyListbox>
                <f:selectItems value="#{helloWorldBacking.options}"/>
                <f:selectItem itemValue="1" itemLabel="#{messages.option_1}"/>
            </tr:selectManyListbox>

            <h:outputText value="trinidad select one radio: "/>
            <tr:selectOneRadio value="2">
                <f:selectItem itemValue="1" itemLabel="#{messages.option_1}"/>
                <f:selectItem itemValue="2" itemLabel="#{messages.option_2}"/>
                <f:selectItem itemValue="3" itemLabel="#{messages.option_3}"/>
            </tr:selectOneRadio>

            <h:outputText value="trinidad select many checkbox: "/>
            <tr:selectManyCheckbox value="3">
                <f:selectItems value="#{helloWorldBacking.options}"/>
            </tr:selectManyCheckbox>

        </h:panelGrid>

        <h:commandLink value="<< back to the demo overview" action="home" styleClass="demo_back_link"/>
        <%--<h:messages/>--%>
    </t:div>
</h:form>
</f:view>
</body>
</html>
