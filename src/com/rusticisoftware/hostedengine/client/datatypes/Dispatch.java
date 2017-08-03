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

package com.rusticisoftware.hostedengine.client.datatypes;

import com.rusticisoftware.hostedengine.client.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Dispatch {

    protected String id = UUID.randomUUID().toString();
    protected String destinationId;
    protected String appId;
    protected String courseAppId;
    protected String courseId;
    protected boolean enabled;
    protected String notes;
    protected String createdBy;
    protected Date createDate = new Date();
    protected Date updateDate = new Date();
    private List<String> tags = new ArrayList<String>();


    public Dispatch(Element dispElem) throws Exception {
        setId(XmlUtils.getChildElemText(dispElem, "id"));
        setDestinationId(XmlUtils.getChildElemText(dispElem, "destinationId"));
        setAppId(XmlUtils.getChildElemText(dispElem, "appId"));
        setCourseAppId(XmlUtils.getChildElemText(dispElem, "courseAppId"));
        setCourseId(XmlUtils.getChildElemText(dispElem, "courseId"));

        String enabledStr = XmlUtils.getChildElemText(dispElem, "enabled");
        setEnabled("true".equals(enabledStr));

        setNotes(XmlUtils.getChildElemText(dispElem, "notes"));
        setCreatedBy(XmlUtils.getChildElemText(dispElem, "createdBy"));

        String createDateStr = XmlUtils.getChildElemText(dispElem, "createDate");
        setCreateDate(XmlUtils.parseXmlDate(createDateStr));

        String updateDateStr = XmlUtils.getChildElemText(dispElem, "updateDate");
        setUpdateDate(XmlUtils.parseXmlDate(updateDateStr));

        Element tagsElem = XmlUtils.getFirstChildByTagName(dispElem, "tags");
        if (tagsElem != null) {
            tags = new ArrayList<String>();
            List<Element> tagElems = XmlUtils.getChildrenByTagName(tagsElem, "tag");
            for (Element tag : tagElems) {
                tags.add(tag.getTextContent());
            }
        }
    }

    public static List<Dispatch> parseDispatchList(Element dispatchesElem) throws Exception {
        List<Dispatch> dispList = new ArrayList<Dispatch>();
        List<Element> dispElems = XmlUtils.getChildrenByTagName(dispatchesElem, "dispatch");
        for (Element dispElem : dispElems) {
            dispList.add(new Dispatch(dispElem));
        }
        return dispList;
    }

    public static List<Dispatch> parseDispatchList(Document dispsDoc) throws Exception {
        NodeList tmpList = dispsDoc.getElementsByTagName("dispatches");
        return parseDispatchList((Element) (tmpList.item(0)));

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCourseAppId() {
        return courseAppId;
    }

    public void setCourseAppId(String courseAppId) {
        this.courseAppId = courseAppId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
