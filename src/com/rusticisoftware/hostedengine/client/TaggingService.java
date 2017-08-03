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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TaggingService {
    private static final Logger log = Logger.getLogger(DebugService.class.toString());

    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public TaggingService(Configuration configuration, ScormEngineService manager) {
        this.configuration = configuration;
        this.manager = manager;
    }

    public List<String> getCourseTags(String courseId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        Document response = request.callService("rustici.tagging.getCourseTags");
        return parseTagList(response);
    }

    public void setCourseTags(String courseId, List<String> tags) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("tags", expandTagList(tags));
        request.callService("rustici.tagging.setCourseTags");
    }

    public List<String> getLearnerTags(String learnerId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("learnerid", learnerId);
        Document response = request.callService("rustici.tagging.getLearnerTags");
        return parseTagList(response);
    }

    public void setLearnerTags(String learnerId, List<String> tags) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("learnerid", learnerId);
        request.getParameters().add("tags", expandTagList(tags));
        request.callService("rustici.tagging.setLearnerTags");
    }

    public List<String> parseTagList(Document xmlDoc) {
        NodeList nodeList = xmlDoc.getElementsByTagName("tag");
        List<String> tags = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element child = (Element) nodeList.item(i);
            tags.add(child.getTextContent());
        }
        return tags;
    }

    public String expandTagList(List<String> tags) {
        if (tags.size() == 0) {
            return "";
        }
        StringBuilder tagList = new StringBuilder();
        for (String tag : tags) {
            tagList.append(tag + ",");
        }
        tagList.deleteCharAt(tagList.length() - 1);
        return tagList.toString();
    }

}
