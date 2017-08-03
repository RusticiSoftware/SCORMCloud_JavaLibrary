/* Software License Agreement (BSD License)
 * 
 * Copyright (c) 2010-2011, Rustici Software, LLC
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Rustici Software, LLC BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.rusticisoftware.hostedengine.client;

import com.rusticisoftware.hostedengine.client.datatypes.Dispatch;
import com.rusticisoftware.hostedengine.client.datatypes.DispatchDestination;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.OutputStream;
import java.util.List;

public class DispatchService {
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public DispatchService(Configuration configuration, ScormEngineService manager) {
        this.configuration = configuration;
        this.manager = manager;
    }

    ///<summary>
    ///</summary>
    ///<param name="page">What page of results to fetch</param>
    ///<param name="tagList">What tags to filter results by</param>
    public List<DispatchDestination> GetDestinationList(int page, List<String> tagList) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("page", page);
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        Document doc = sr.callService("rustici.dispatch.getDestinationList");
        return DispatchDestination.parseDestinationList(doc);
    }

    public String CreateDestination(String name, List<String> tagList, String email) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("name", name);
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        if (email != null) {
            sr.getParameters().add("email", email);
        }
        Document doc = sr.callService("rustici.dispatch.createDestination");
        return XmlUtils.getChildElemText(doc.getDocumentElement(), "destinationId");
    }

    public void DeleteDestination(String destinationId) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("destinationid", destinationId);
        sr.callService("rustici.dispatch.deleteDestination");
    }

    public DispatchDestination GetDestinationInfo(String destinationId) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("destinationid", destinationId);
        Document doc = sr.callService("rustici.dispatch.getDestinationInfo");
        Element destElem = (Element) (doc.getElementsByTagName("dispatchDestination").item(0));
        return new DispatchDestination(destElem);
    }

    public void UpdateDestination(String destinationId, String name, List<String> tagList) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("destinationid", destinationId);
        if (name != null) {
            sr.getParameters().add("name", name);
        }
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        sr.callService("rustici.dispatch.updateDestination");
    }

    public String CreateDispatch(String destinationId, String courseId, List<String> tagList, String email) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("destinationid", destinationId);
        sr.getParameters().add("courseid", courseId);
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        if (email != null) {
            sr.getParameters().add("email", email);
        }
        Document doc = sr.callService("rustici.dispatch.createDispatch");
        return XmlUtils.getChildElemText(doc.getDocumentElement(), "dispatchId");
    }

    public Dispatch GetDispatchInfo(String dispatchId) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("dispatchid", dispatchId);
        Document doc = sr.callService("rustici.dispatch.getDispatchInfo");
        Element dispElem = (Element) (doc.getElementsByTagName("dispatch").item(0));
        return new Dispatch(dispElem);
    }

    public void DeleteDispatches(String destinationId, String courseId, String dispatchId, List<String> tagList) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        if (destinationId != null) {
            sr.getParameters().add("destinationid", destinationId);
        }
        if (courseId != null) {
            sr.getParameters().add("courseid", courseId);
        }
        if (dispatchId != null) {
            sr.getParameters().add("dispatchid", dispatchId);
        }
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        sr.callService("rustici.dispatch.deleteDispatches");
    }

    public List<Dispatch> GetDispatchList(int page, String destinationId, String courseId, List<String> tagList) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        sr.getParameters().add("page", page);
        if (destinationId != null) {
            sr.getParameters().add("destinationid", destinationId);
        }
        if (courseId != null) {
            sr.getParameters().add("courseid", courseId);
        }
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        Document doc = sr.callService("rustici.dispatch.getDispatchList");
        return Dispatch.parseDispatchList(doc);
    }

    public void UpdateDispatches(String destinationId, String courseId, String dispatchId, List<String> tagList,
                                 Boolean enabled, List<String> tagsToAdd, List<String> tagsToRemove) throws Exception {
        UpdateDispatches(destinationId, courseId, dispatchId, tagList, enabled, tagsToAdd, tagsToRemove, null);
    }

    public void UpdateDispatches(String destinationId, String courseId, String dispatchId, List<String> tagList,
                                 Boolean enabled, List<String> tagsToAdd, List<String> tagsToRemove, Boolean instanced) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        if (destinationId != null) {
            sr.getParameters().add("destinationid", destinationId);
        }
        if (courseId != null) {
            sr.getParameters().add("courseid", courseId);
        }
        if (dispatchId != null) {
            sr.getParameters().add("dispatchid", dispatchId);
        }
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        if (enabled != null) {
            sr.getParameters().add("enabled", enabled.toString().toLowerCase());
        }
        if (tagsToAdd != null) {
            sr.getParameters().add("addtags", Utils.join(tagsToAdd, ","));
        }
        if (tagsToRemove != null) {
            sr.getParameters().add("removetags", Utils.join(tagsToRemove, ","));
        }
        if (instanced != null) {
            sr.getParameters().add("instanced", instanced.toString().toLowerCase());
        }
        sr.callService("rustici.dispatch.updateDispatches");
    }

    public String GetDispatchDownloadUrl(String destinationId, String courseId, String dispatchId,
                                         List<String> tagList, String cssUrl) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        if (destinationId != null) {
            sr.getParameters().add("destinationid", destinationId);
        }
        if (courseId != null) {
            sr.getParameters().add("courseid", courseId);
        }
        if (dispatchId != null) {
            sr.getParameters().add("dispatchid", dispatchId);
        }
        if (tagList != null && tagList.size() > 0) {
            sr.getParameters().add("tags", Utils.join(tagList, ","));
        }
        if (cssUrl != null) {
            sr.getParameters().add("cssurl", cssUrl);
        }
        return sr.constructUrl("rustici.dispatch.downloadDispatches");
    }

    public void DownloadDispatchesToStream(OutputStream out, String destinationId, String courseId,
                                           String dispatchId, List<String> tagList, String cssUrl) throws Exception {
        ServiceRequest sr = new ServiceRequest(configuration);
        String dispatchDownloadUrl = GetDispatchDownloadUrl(destinationId, courseId, dispatchId, tagList, cssUrl);
        sr.copyResponseFromUrlToStream(dispatchDownloadUrl, out, false);
    }
}
