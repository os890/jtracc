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
package at.irian.i18n.jtracc;

import javax.faces.event.ValueChangeEvent;
import java.util.List;
import java.util.Locale;

/**
 * Simple interface to resolve messages using a key to identify them. <br>
 * A typical implementation would be based on a ResourceBundle management
 * againts an external repository (properties file, database, plain text file,
 * etc.).
 */
public interface MessageResolver
{
    public abstract String getMessage(String key, Locale locale);

    public abstract void setMessage(String key, String value, Locale locale);

    public abstract List getSupportedLocales();

    public abstract Locale getDefaultLocale();

    public abstract void changeMessage(ValueChangeEvent event);
}