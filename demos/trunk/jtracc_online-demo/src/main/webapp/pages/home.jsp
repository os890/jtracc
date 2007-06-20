<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<%@ taglib uri="http://myfaces.apache.org/trinidad" prefix="tr" %>

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
    <meta name="robots" content="index, nofollow"/>
</head>
<body>
<f:view>
<h:form id="form">
    <t:div styleClass="demo_home">
        <h:panelGrid id="head" columns="3" styleClass="demo_home_head">
            <h:panelGroup/>
            <h:graphicImage value="../images/logo-head.png" alt="Welcome @ the jtracc Live-Demo"/>
            <h:panelGroup/>
        </h:panelGrid>

        <h:panelGrid id="grid" columns="3" styleClass="demo_sub_headline">
            <h:panelGroup/>
            <h:outputText value="available demos:"/>
            <h:panelGroup/>

            <h:panelGroup/>
            <h:commandLink value="some jtracc features" action="features"/>
            <h:panelGroup/>

            <h:panelGroup/>
            <h:commandLink value="standard JSF (MyFaces) components" action="std_components"/>
            <h:panelGroup/>

            <h:panelGroup/>
            <h:commandLink value="Tomahawk components" action="tomahawk"/>
            <h:panelGroup/>

            <h:panelGroup/>
            <h:commandLink value="Trinidad components" action="trinidad"/>
            <h:panelGroup/>
        </h:panelGrid>
    </t:div>
    <%--<h:messages/>--%>
</h:form>
</f:view>

<%--see google description--%>
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-2092740-2";
urchinTracker();
</script>

</body>
</html>
