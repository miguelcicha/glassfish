/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009-2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.admin.rest.provider;

import org.codehaus.jettison.json.JSONArray;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.admin.rest.results.GetResultList;
import java.util.List;

import org.jvnet.hk2.config.Dom;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Produces;

import static org.glassfish.admin.rest.provider.ProviderUtil.*;


/**
 *
 * @author Rajeshwar Patil
 * @author Luvdovic Champenois ludo@dev.java.net
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class GetResultListJsonProvider extends BaseProvider<GetResultList> {

    public GetResultListJsonProvider() {
        super(GetResultList.class, MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    protected String getContent(GetResultList proxy) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(KEY_ENTITY, new JSONObject());
            obj.put(KEY_METHODS, getJsonForMethodMetaData(proxy.getMetaData()));
            if (proxy.getDomList().size() > 0) {
                obj.put(KEY_CHILD_RESOURCES, getResourcesLinks(proxy.getDomList()));
            }
            if (proxy.getCommandResourcesPaths().length > 0) {
                obj.put(KEY_COMMANDS, getCommandLinks(proxy.getCommandResourcesPaths()));
            }
        } catch (JSONException ex) {
            Logger.getLogger(GetResultJsonProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

        return obj.toString();
    }

    private JSONArray getResourcesLinks(List<Dom> proxyList) {
        JSONArray array = new JSONArray();
        String elementName;
        for (Dom proxy: proxyList) {
            elementName = proxy.getKey();
             array.put(getElementLink(uriInfo, elementName));
        }
        return array;
    }

    private JSONArray getCommandLinks(String[][] commandResourcesPaths) throws JSONException {
        JSONArray array = new JSONArray();

        //TODO commandResourcePath is two dimensional array. It seems the second e.x. see DomainResource#getCommandResourcesPaths().
        //The second dimension POST/GET etc. does not seem to be used. Discussed with Ludo. Need to be removed in a separate checkin.
        for (String[] commandResourcePath : commandResourcesPaths) {
            array.put(getElementLink(uriInfo, commandResourcePath[0]));
        }
        return array;
    }
}
