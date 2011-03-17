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
	
	
	private String appId;
	private String courseId;
	private String registrationId;
	private String resultsData;
	private ArrayList<InstanceData> instances = new ArrayList<InstanceData>();
	
	
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
	
	public static RegistrationData parseFromXmlElement (Element elem) {
		String regId = elem.getAttribute("id");
		String courseId = elem.getAttribute("courseid");
		
		
		RegistrationData data = new RegistrationData();
		data.setRegistrationId(regId);
		data.setCourseId(courseId);
		
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
	
	public static List<RegistrationData> ConvertToRegistrationDataList (Document regListXml){
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
			xml.append("<registration id=\"" + XmlUtils.xmlEncode(regData.getRegistrationId()) + "\" ");
			xml.append("courseid=\"" + XmlUtils.xmlEncode(regData.getCourseId()) + "\" ");
			xml.append(">");
			
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
