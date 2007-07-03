/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.jtracc.renderkit;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Thomas Spiegl
 */
public class ViewSequenceUtils {
    private static final String SEQUENCE_PARAM = "jsf_sequence";

    /**
     * Increments view sequence by 1.
     *
     * @param facescontext
     */
    public static void nextViewSequence(FacesContext facescontext) {
        ExternalContext externalContext = facescontext.getExternalContext();
        Object sessionObj = externalContext.getSession(true);
        synchronized (sessionObj) // synchronized to increase sequence if multiple requests
        // are handled at the same time for the session
        {
            Map map = externalContext.getSessionMap();
            Integer sequence = (Integer) map.get(SEQUENCE_PARAM);
            if (sequence == null || sequence.intValue() == Integer.MAX_VALUE) {
                sequence = new Integer(1);
            }
            else {
                sequence = new Integer(sequence.intValue() + 1);
            }
            map.put(SEQUENCE_PARAM, sequence);
            externalContext.getRequestMap().put(SEQUENCE_PARAM, sequence);
        }
    }

    /**
     * Returns current view sequence and sets current view sequence to Integer(1) if
     * current sequence value is NULL.
     *
     * @param facescontext
     * @return
     */
    public static Integer getViewSequence(FacesContext facescontext) {
        Map map = facescontext.getExternalContext().getRequestMap();
        Integer sequence = (Integer) map.get(SEQUENCE_PARAM);
        if (sequence == null) {
            sequence = new Integer(1);
            map.put(SEQUENCE_PARAM, sequence);

            synchronized (facescontext.getExternalContext().getSession(true)) {
                facescontext.getExternalContext().getSessionMap().put(SEQUENCE_PARAM, sequence);
            }
        }
        return sequence;
    }


    /**
     * Current view sequence, may be NULL!
     *
     * @param context
     * @return
     */
    public static Integer getCurrentSequence(FacesContext context) {
        Map map = context.getExternalContext().getSessionMap();
        return (Integer) map.get(SEQUENCE_PARAM);
    }

}
