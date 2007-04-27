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


            <h:outputText value="i18n image with convention support: "/>
            <h:graphicImage value="#{messages.img_earth1}" width="#{messages.img_width_earth1}"
                            height="#{messages.img_height_earth1}" alt="#{messages.img_alt_earth1}"/>

            <h:outputText value="i18n image without convention support: "/>
            <h:graphicImage value="#{messages.earth2}" width="#{messages.earth2_width}"
                            height="#{messages.earth2_height}" alt="#{messages.earth2_alt}"/>

            <h:outputText value="external i18n image without convention support: "/>
            <h:graphicImage value="#{messages.img_external}" width="#{messages.img_width_external}"
                            height="#{messages.img_height_external}" alt="#{messages.img_alt_external}"/>

            <h:outputText value="duplicate el demo: "/>
            <tr:outputText value="#{messages.content1} = #{messages.content1}"/>

            <h:outputText value="unchangeable el demo: "/>
            <tr:outputText value="#{helloWorldBacking.staticContent} and 2 * 7 = #{2*7}"/>


            <h:outputText value="unknown/new conent: "/>
            <tr:outputText value="#{messages.newContent}"/>

            <h:outputText value="input with static default: "/>
            <h:inputText id="hInput1" value="default" required="true"/>

            <h:outputText value="input with new/dynamic content: "/>
            <h:inputText id="hInput2" value="#{messages.newInputContent}"/>

            <h:outputText value="custom component filter demo"/>
            <h:panelGroup/>
            <h:outputText value="filtered component: h:outputText"/>
            <h:outputText value="#{messages.content1}"/>
        </h:panelGrid>

        <h:commandLink value="<< back to the demo overview" action="home" styleClass="demo_back_link"/>
        <%--<h:messages/>--%>
    </t:div>
</h:form>
</f:view>
</body>
</html>
