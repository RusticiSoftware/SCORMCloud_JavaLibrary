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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class CourseMetadata {

    private String id;
    private String objectTitle;
    private String title;
    private String description;
    private Long duration;
    private Long typicaltime;
    private Double masteryScore;
    private ArrayList<String> keyWords = new ArrayList<String>();
    private ArrayList<CourseMetadata> children = new ArrayList<CourseMetadata>();

    public CourseMetadata() {
    }

    public static CourseMetadata parseFromXmlElement(Element objectElem) throws Exception {
        CourseMetadata data = new CourseMetadata();
        data.setId(objectElem.getAttribute("id"));
        data.setObjectTitle(objectElem.getAttribute("title"));

        Element elem = (Element) objectElem.getElementsByTagName("metadata").item(0);
        NodeList attrs = elem.getChildNodes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Element attr = (Element) attrs.item(i);
            String tagName = attr.getTagName();
            if (tagName.equals("title")) {
                data.setTitle(attr.getTextContent());
            } else if (tagName.equals("description")) {
                data.setDescription(attr.getTextContent());
            } else if (tagName.equals("duration")) {
                Long duration = 0L;
                try {
                    duration = Long.parseLong(attr.getTextContent());
                } catch (Exception e) {
                    duration = 0L;
                }
                data.setDuration(duration);
            } else if (tagName.equals("typicaltime")) {
                Long typicalTime = 0L;
                try {
                    typicalTime = Long.parseLong(attr.getTextContent());
                } catch (Exception e) {
                    typicalTime = 0L;
                }
                data.setTypicaltime(typicalTime);
            } else if (tagName.equals("keywords")) {
                ArrayList<String> kwords = data.getKeyWords();
                NodeList keyWordElems = attr.getElementsByTagName("keyword");
                for (int wordIndex = 0; wordIndex < keyWordElems.getLength(); wordIndex++) {
                    Element keyWordElem = (Element) keyWordElems.item(wordIndex);
                    kwords.add(keyWordElem.getTextContent());
                }
            } else if (tagName.equals("children")) {
                ArrayList<CourseMetadata> children = data.getChildren();
                NodeList childElems = attr.getChildNodes();
                for (int childIndex = 0; childIndex < childElems.getLength(); childIndex++) {
                    Element childElem = (Element) childElems.item(childIndex);
                    children.add(parseFromXmlElement(childElem));
                }
            }
        }
        return data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectTitle() {
        return objectTitle;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTypicaltime() {
        return typicaltime;
    }

    public void setTypicaltime(Long typicaltime) {
        this.typicaltime = typicaltime;
    }

    public Double getMasteryScore() {
        return masteryScore;
    }

    public void setMasteryScore(Double masteryScore) {
        this.masteryScore = masteryScore;
    }

    public ArrayList<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(ArrayList<String> keyWords) {
        this.keyWords = keyWords;
    }

    public ArrayList<CourseMetadata> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<CourseMetadata> children) {
        this.children = children;
    }
}
