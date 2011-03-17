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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.Utils;
import com.rusticisoftware.hostedengine.client.XmlUtils;


public class CourseData {

	private String appId;
	private String courseId;
	private int numberOfVersions;
	private int numberOfRegistrations;
	private String title;
	private long size;
	private List<String> tags;


	public CourseData() {
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
	
	public void setNumberOfRegistrations(int numberOfRegistrations){
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
	
	public List<String> getTags(){
		return tags;
	}
	
	public void setTags(List<String> tags){
		this.tags = tags;
	}

	public static CourseData parseFromXmlElement (Element courseElem) throws Exception {
		String id = courseElem.getAttribute("id");
		String title = courseElem.getAttribute("title");
		String versions = courseElem.getAttribute("versions");
		String regs = courseElem.getAttribute("registrations");
		String sizeStr = courseElem.getAttribute("size");
		
		int numVersions = 0;
		try { numVersions = Integer.parseInt(versions); }
		catch (Exception e) {}
		
		int numRegistrations = 0;
        try { numRegistrations = Integer.parseInt(regs); }
        catch (Exception e) {}
        
        long size = 0;
        try { size = Long.parseLong(sizeStr); }
        catch (Exception e) {}
		
		
		CourseData data = new CourseData();
		data.setCourseId(id);
		data.setTitle(title);
		data.setNumberOfVersions(numVersions);
		data.setNumberOfRegistrations(numRegistrations);
		data.setSize(size);
		
		List<String> tags = new ArrayList<String>();
		Element tagsElem = XmlUtils.getFirstChildByTagName(courseElem, "tags");
		if(tagsElem != null){
			List<Element> tagElems = XmlUtils.getChildrenByTagName(tagsElem, "tag");
			for(Element tagElem : tagElems){
				tags.add(tagElem.getTextContent());
			}
		}
		data.setTags(tags);
		
		return data;
	}
	
	public static List<CourseData> parseListFromXml (Document courseListXmlDoc) throws Exception {
		Element courseListElem = (Element)courseListXmlDoc.getElementsByTagName("courselist").item(0);
		
		ArrayList<CourseData> courseList = new ArrayList<CourseData>();
		NodeList courseElems = courseListElem.getChildNodes();
		for(int i = 0; i < courseElems.getLength(); i++){
			Element courseElem = (Element)courseElems.item(i);
			courseList.add( parseFromXmlElement(courseElem) );
		}
		return courseList;
	}
	
	public static String getXmlString (List<CourseData> courseList){
		StringBuilder xml = new StringBuilder();
		xml.append("<courselist>");
		for(CourseData data : courseList){
			xml.append("<course id=\"" + Utils.xmlEncode(data.getCourseId()) + "\" ");
			xml.append("title=\"" + Utils.xmlEncode(data.getTitle()) + "\" ");
			xml.append("versions=\"" + data.getNumberOfVersions() + "\" ");
			xml.append("registrations=\"" + data.getNumberOfRegistrations() + "\" ");
			xml.append("size=\"" + data.getSize() + "\" ");
			xml.append(">");
			xml.append("<tags>");
			if(data.getTags() != null && data.getTags().size() > 0){
				for(String tag : data.getTags()){
					xml.append("<tag><![CDATA[" + tag + "]]></tag>");
				}
			}
			xml.append("</tags>");
			xml.append("</course>");
		}
		xml.append("</courselist>");
		return xml.toString();
	}
}
