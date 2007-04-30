/*
 * jtracc - i18n JSF component library
 * Copyright 2007, IRIAN Solutions GmbH Vienna, Austria
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
var transFormParent = null;
var transFormFields = null;
transPane.domNode.className = transPane.domNode.className + " translationPane";

transPane.closeWindow = function(e)
{
    reparentTransForm();
    this.domNode.style.display = "none";
}
function onMouseOverTRArea(area)
{
    //last child of the translationContainer is the translation image container
    area.firstChild.nextSibling.style.display = "block";
    area.className = "translationContainer translationContainerActive";
}
function onMouseOutTRArea(area)
{
    //last child of the translationContainer is the translation image container
    if (dojo.string.endsWith(area.firstChild.nextSibling.firstChild.src, transImage))
    {
        area.firstChild.nextSibling.style.display = "none";
    }
    area.className = "translationContainer";
}
function onTRChange()
{
    var changed = false;
    dojo.lang.forEach(transFormFields, function(field)
    {
        if (field.value != field.origValue)
        {
            changed = true;
        }
    });
    if (changed)
    {
        transFormParent.firstChild.src = transAltImage;
        transFormParent.style.display = "block";
    }
    else
    {
        transFormParent.firstChild.src = transImage;
        transFormParent.style.display = "none";
    }
}
function showTranslationPane(source)
{
    if (transFormParent !== null)
    {
        reparentTransForm();
    }

    //Put the table into the Floating Pane
    //last child of the translationContainer is the translation image container - it's last child is the translation table
    var table = source.firstChild.nextSibling.lastChild;
    table.parentNode.removeChild(table);
    transFormParent = source.firstChild.nextSibling;
    transFormFields = getInputFields(table);
    dojo.lang.forEach(transFormFields, function(field)
    {
        if (dojo.lang.isUndefined(field.origValue))
        {
            field.origValue = field.value;
        }
        dojo.event.connect(field, "onkeyup", onTRChange);
    }, this);
    transPane.containerNode.appendChild(table);

    //Set up the floating pane
    transPane.domNode.style.display = "block";
    transPane.domNode.style.height = "auto";
    var pos = dojo.html.abs(source, true);
    var size = dojo.html.getContentBox(source);
    transPane.domNode.style.left = pos.x + "px";
    transPane.domNode.style.top = (pos.y + size.height) + "px";
    var mb = dojo.html.getMarginBox(transPane.domNode);
    transPane.shadow.size(400, mb.height);
    transPane.bgIframe.onResized();

}
function reparentTransForm()
{
    var table = transPane.containerNode.firstChild;
    table.parentNode.removeChild(table);
    transFormParent.appendChild(table);
    transFormParent = null;

    dojo.lang.forEach(transFormFields, function(field)
    {
        dojo.event.disconnect(field, "onkeyup", onTRChange);
    }, this);
    transFormFields = null;
}
function getInputFields(node)
{
    var returnArray = [];
    if (node.tagName == "INPUT" && node.type == "text")
    {
        returnArray.push(node);
    }
    node = node.firstChild;
    while (node)
    {
        var results = getInputFields(node);
        returnArray = returnArray.concat(results);
        node = node.nextSibling;
    }
    return returnArray;
}