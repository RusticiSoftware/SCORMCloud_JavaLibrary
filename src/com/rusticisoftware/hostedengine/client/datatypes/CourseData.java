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

public class CourseData {

    private String appId;
    private String courseId;
    private int numberOfVersions;
    private int numberOfRegistrations;
    private String title;
    private long size;
    private List<String> tags;
    private List<VersionInformation> versions;
    private String learningStandard;
    private String tinCanActivityId;

    public CourseData() {
    }

    public static CourseData parseFromXmlElement(Element courseElem) throws Exception {
        final String id = courseElem.getAttribute("id");
        final String title = courseElem.getAttribute("title");
        final String numVersionStr = courseElem.getAttribute("versions");
        final String regs = courseElem.getAttribute("registrations");
        final String sizeStr = courseElem.getAttribute("size");

        int numVersions;
        try {
            numVersions = Integer.parseInt(numVersionStr);
        } catch (Exception e) {
            numVersions = 0;
        }

        int numRegistrations;
        try {
            numRegistrations = Integer.parseInt(regs);
        } catch (Exception e) {
            numRegistrations = 0;
        }

        long size;
        try {
            size = Long.parseLong(sizeStr);
        } catch (Exception e) {
            size = 0;
        }


        CourseData data = new CourseData();
        data.setCourseId(id);
        data.setTitle(title);
        data.setNumberOfVersions(numVersions);
        data.setNumberOfRegistrations(numRegistrations);
        data.setSize(size);

        List<VersionInformation> versions = new ArrayList<VersionInformation>();
        Element versionsElem = XmlUtils.getFirstChildByTagName(courseElem, "versions");
        if (versionsElem != null) {
            List<Element> versionElems = XmlUtils.getChildrenByTagName(versionsElem, "version");
            for (Element versionElem : versionElems) {
                versions.add(VersionInformation.parseFromXmlElement(versionElem));
            }
            data.setNumberOfVersions(versionElems.size());
        }
        data.setVersions(versions);

        List<String> tags = new ArrayList<String>();
        Element tagsElem = XmlUtils.getFirstChildByTagName(courseElem, "tags");
        if (tagsElem != null) {
            List<Element> tagElems = XmlUtils.getChildrenByTagName(tagsElem, "tag");
            for (Element tagElem : tagElems) {
                tags.add(tagElem.getTextContent());
            }
        }
        data.setTags(tags);

        data.setLearningStandard(XmlUtils.getChildElemText(courseElem, "learningStandard"));
        data.setTinCanActivityId(XmlUtils.getChildElemText(courseElem, "tinCanActivityId"));

        return data;
    }

    public static List<CourseData> parseListFromXml(Document courseListXmlDoc) throws Exception {
        Element courseListElem = (Element) courseListXmlDoc.getElementsByTagName("courselist").item(0);

        ArrayList<CourseData> courseList = new ArrayList<CourseData>();
        NodeList courseElems = courseListElem.getChildNodes();
        for (int i = 0; i < courseElems.getLength(); i++) {
            Element courseElem = (Element) courseElems.item(i);
            courseList.add(parseFromXmlElement(courseElem));
        }
        return courseList;
    }

    public static String getXmlString(List<CourseData> courseList) {
        StringBuilder xml = new StringBuilder();
        xml.append("<courselist>");
        for (CourseData data : courseList) {
            xml.append(data.getXmlString());
        }
        xml.append("</courselist>");
        return xml.toString();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getNumberOfVersions() {
        return numberOfVersions;
    }

    public void setNumberOfVersions(int numberOfVersions) {
        this.numberOfVersions = numberOfVersions;
    }

    public int getNumberOfRegistrations() {
        return numberOfRegistrations;
    }

    public void setNumberOfRegistrations(int numberOfRegistrations) {
        this.numberOfRegistrations = numberOfRegistrations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<VersionInformation> getVersions() {
        return versions;
    }

    public void setVersions(List<VersionInformation> versions) {
        this.versions = versions;
    }

    public String getLearningStandard() {
        return learningStandard;
    }

    public void setLearningStandard(String learningStandard) {
        this.learningStandard = learningStandard;
    }

    public String getTinCanActivityId() {
        return tinCanActivityId;
    }

    public void setTinCanActivityId(String tinCanActivityId) {
        this.tinCanActivityId = tinCanActivityId;
    }

    public String getXmlString() {
        CourseData data = this;
        StringBuilder xml = new StringBuilder();
        xml.append("<course id=\"" + XmlUtils.xmlEncode(data.getCourseId()) + "\" ");
        xml.append("title=\"" + XmlUtils.xmlEncode(data.getTitle()) + "\" ");
        xml.append("versions=\"" + data.getNumberOfVersions() + "\" ");
        xml.append("registrations=\"" + data.getNumberOfRegistrations() + "\" ");
        xml.append("size=\"" + data.getSize() + "\" ");
        xml.append(">");
        xml.append(VersionInformation.getXmlString(data.getVersions()));
        xml.append("<tags>");
        if (data.getTags() != null && data.getTags().size() > 0) {
            for (String tag : data.getTags()) {
                xml.append("<tag><![CDATA[" + tag + "]]></tag>");
            }
        }
        xml.append("</tags>");
        if (data.getLearningStandard() != null && data.getLearningStandard().length() > 0) {
            xml.append(XmlUtils.getNamedTextElemXml("learningStandard", data.getLearningStandard()));
        }
        if (data.getTinCanActivityId() != null && data.getTinCanActivityId().length() > 0) {
            xml.append(XmlUtils.getNamedTextElemXml("tinCanActivityId", data.getTinCanActivityId()));
        }
        xml.append("</course>");
        return xml.toString();
    }

    public static class VersionInformation {
        protected int versionId = 0;
        protected Date updateDate = new Date();

        public VersionInformation() {
        }

        public static String getXmlString(List<VersionInformation> versions) {
            if (versions == null) {
                return "";
            }

            StringBuilder sb = new StringBuilder("<versions>");
            for (VersionInformation version : versions) {
                sb.append(version.getXmlString());
            }
            sb.append("</versions>");
            return sb.toString();
        }

        public static VersionInformation parseFromXmlElement(Element versionElem) throws Exception {
            int versionId = Integer.parseInt(XmlUtils.getChildElemText(versionElem, "versionId"));
            Date updateDate = XmlUtils.parseXmlDate(XmlUtils.getChildElemText(versionElem, "updateDate"));
            VersionInformation version = new VersionInformation();
            version.setVersionId(versionId);
            version.setUpdateDate(updateDate);
            return version;
        }

        public String getXmlString() {
            StringBuilder sb = new StringBuilder("<version>");
            sb.append(XmlUtils.getNamedTextElemXml("versionId", String.valueOf(versionId)));
            sb.append(XmlUtils.getNamedTextElemXml("updateDate", XmlUtils.xmlSerialize(updateDate)));
            sb.append("</version>");
            return sb.toString();
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public Date getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
        }

    }
}
