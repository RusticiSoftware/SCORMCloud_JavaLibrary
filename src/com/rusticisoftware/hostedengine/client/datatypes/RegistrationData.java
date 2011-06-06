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
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.Utils;
import com.rusticisoftware.hostedengine.client.XmlUtils;

public class RegistrationData {

	public static class InstanceData {
		private int instanceId;
		private int courseVersion;
		
		public InstanceData() {}
		public int getInstanceId(){
			return this.instanceId;
		}
		public void setInstanceId(int instanceId){
			this.instanceId = instanceId;
		}
		public int getCourseVersion(){
			return this.courseVersion;
		}
		public void setCourseVersion(int courseVersion){
			this.courseVersion = courseVersion;
		}
	}
	
	
	private String registrationId;
	private String resultsData;
	private ArrayList<InstanceData> instances = new ArrayList<InstanceData>();
	
	private String appId;
	private String courseId;
	private String courseTitle;
	private String learnerId;
	private String learnerFirstName;
	private String learnerLastName;
	private String email;
	private Date createDate;
	private Date firstAccessDate;
	private Date lastAccessDate;
	private Date completedDate;
	
	
	
	
	public RegistrationData() {}
	
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
	
	public String getCourseTitle(){
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle){
		this.courseTitle = courseTitle;
	}
	
	public String getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(String learnerId) {
		this.learnerId = learnerId;
	}

	public String getLearnerFirstName() {
		return learnerFirstName;
	}

	public void setLearnerFirstName(String learnerFirstName) {
		this.learnerFirstName = learnerFirstName;
	}

	public String getLearnerLastName() {
		return learnerLastName;
	}

	public void setLearnerLastName(String learnerLastName) {
		this.learnerLastName = learnerLastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getFirstAccessDate() {
		return firstAccessDate;
	}

	public void setFirstAccessDate(Date firstAccessDate) {
		this.firstAccessDate = firstAccessDate;
	}

	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public void setInstances(ArrayList<InstanceData> instances) {
		this.instances = instances;
	}

	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public ArrayList<InstanceData> getInstances(){
		return instances;
	}
	public void setResultsData(String resultsData){
		this.resultsData = resultsData;
	}
	public String getResultsData(){
		return this.resultsData;
	}
	
	public static RegistrationData parseFromXmlElement (Element elem) throws Exception {
		String regId = elem.getAttribute("id");
		String courseId = elem.getAttribute("courseid");
		
		
		RegistrationData data = new RegistrationData();
		data.setAppId(XmlUtils.getChildElemText(elem, "appId"));
		data.setRegistrationId(XmlUtils.getChildElemText(elem, "registrationId"));
		data.setCourseId(XmlUtils.getChildElemText(elem, "courseId"));
		data.setCourseTitle(XmlUtils.getChildElemText(elem, "courseTitle"));
		data.setLearnerId(XmlUtils.getChildElemText(elem, "learnerId"));
		data.setLearnerFirstName(XmlUtils.getChildElemText(elem, "learnerFirstName"));
		data.setLearnerLastName(XmlUtils.getChildElemText(elem, "learnerLastName"));
		data.setEmail(XmlUtils.getChildElemText(elem, "email"));
		data.setCreateDate(XmlUtils.parseXmlDate(XmlUtils.getChildElemText(elem, "createDate")));
		data.setFirstAccessDate(XmlUtils.parseXmlDate(XmlUtils.getChildElemText(elem, "getFirstAccessDate")));
		data.setLastAccessDate(XmlUtils.parseXmlDate(XmlUtils.getChildElemText(elem, "getLastAccessDate")));
		data.setCompletedDate(XmlUtils.parseXmlDate(XmlUtils.getChildElemText(elem, "completedDate")));
		
		ArrayList<RegistrationData.InstanceData> instances = data.getInstances();
		NodeList instancesList = elem.getElementsByTagName("instance");
		if(instancesList.getLength() > 0){
			for(int i = 0; i < instancesList.getLength(); i++){
				Element instance = (Element)instancesList.item(i);
				
				int instanceId, courseVersion;
				try { instanceId = Integer.parseInt( instance.getAttribute("id") ); }
				catch (Exception e) { instanceId = -1; }
				try { courseVersion = Integer.parseInt( instance.getAttribute("courseversion") ); }
				catch (Exception e) { courseVersion = -1; }

				RegistrationData.InstanceData instanceData = new RegistrationData.InstanceData();
				instanceData.setInstanceId(instanceId);
				instanceData.setCourseVersion(courseVersion);
				instances.add(instanceData);
			}
		}
		
		NodeList reportList = elem.getElementsByTagName("registrationreport");
		if(reportList.getLength() > 0){
			Element report = (Element)reportList.item(0);
			String regResults = null;
			try { regResults = XmlUtils.getXmlString(report); }
			catch (Exception e) {};
			if(regResults != null){
				data.setResultsData(regResults);
			}
		}		
		
		return data;
	}
	
	public static List<RegistrationData> parseListFromXml (Document regListXml) throws Exception {
		Element regListElem = (Element)regListXml.getElementsByTagName("registrationlist").item(0);
		ArrayList<RegistrationData> regList = new ArrayList<RegistrationData>();
		NodeList regElems = regListElem.getChildNodes();
		for(int i = 0; i < regElems.getLength(); i++){
			Element regElem = (Element)regElems.item(i);
			regList.add( parseFromXmlElement(regElem) );
		}
		return regList;
	}
	
	
	public static String getXmlString (List<RegistrationData> regList) {
		StringBuilder xml = new StringBuilder();
		xml.append("<registrationlist>");
		for(RegistrationData regData : regList){
			xml.append("<registration id=\"" + Utils.xmlEncode(regData.getRegistrationId()) + "\" ");
			xml.append("courseid=\"" + Utils.xmlEncode(regData.getCourseId()) + "\" ");
			xml.append(">");
			
				xml.append(XmlUtils.getNamedTextElemXml("appId", regData.getAppId()));
				xml.append(XmlUtils.getNamedTextElemXml("registrationId", regData.getRegistrationId()));
				xml.append(XmlUtils.getNamedTextElemXml("courseId", regData.getCourseId()));
				xml.append(XmlUtils.getNamedTextElemXml("courseTitle", regData.getCourseTitle()));
				xml.append(XmlUtils.getNamedTextElemXml("learnerId", regData.getLearnerId()));
				xml.append(XmlUtils.getNamedTextElemXml("learnerFirstName", regData.getLearnerFirstName()));
				xml.append(XmlUtils.getNamedTextElemXml("learnerLastName", regData.getLearnerLastName()));
				xml.append(XmlUtils.getNamedTextElemXml("email", regData.getEmail()));
				xml.append(XmlUtils.getNamedTextElemXml("createDate", XmlUtils.xmlSerialize(regData.getCreateDate())));
				xml.append(XmlUtils.getNamedTextElemXml("firstAccessDate", XmlUtils.xmlSerialize(regData.getFirstAccessDate())));
				xml.append(XmlUtils.getNamedTextElemXml("lastAccessDate", XmlUtils.xmlSerialize(regData.getLastAccessDate())));
				xml.append(XmlUtils.getNamedTextElemXml("completedDate", XmlUtils.xmlSerialize(regData.getCompletedDate())));				
			
				ArrayList<RegistrationData.InstanceData> instances = regData.getInstances();
				xml.append("<instances>");
				for(RegistrationData.InstanceData instanceData : instances){
					xml.append("<instance id=\"" + instanceData.getInstanceId() + "\" courseversion=\"" + instanceData.getCourseVersion() + "\" />");
				}
				xml.append("</instances>");
				
				if(regData.getResultsData() != null){
					xml.append(regData.getResultsData());
				}	
			
			xml.append("</registration>");
		}
		xml.append("</registrationlist>");
		return xml.toString();
	}
}