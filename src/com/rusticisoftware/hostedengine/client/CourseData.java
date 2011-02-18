package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CourseData {

	private String appId;
	private String courseId;
	private int numberOfVersions;
	private int numberOfRegistrations;
	private String title;
	private long size;


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
		
		return data;
	}
	
	public static List<CourseData> ConvertToCourseDataList (Document courseListXmlDoc) throws Exception {
		Element courseListElem = (Element)courseListXmlDoc.getElementsByTagName("courselist").item(0);
		
		ArrayList<CourseData> courseList = new ArrayList<CourseData>();
		NodeList courseElems = courseListElem.getChildNodes();
		for(int i = 0; i < courseElems.getLength(); i++){
			Element courseElem = (Element)courseElems.item(i);
			courseList.add( parseFromXmlElement(courseElem) );
		}
		return courseList;
	}
	
//	public static String getXmlString (List<CourseData> courseList){
//		StringBuilder xml = new StringBuilder();
//		xml.append("<courselist>");
//		for(CourseData data : courseList){
//			xml.append("<course id=\"" + Utils.xmlEncode(data.getCourseId()) + "\" ");
//			xml.append("title=\"" + Utils.xmlEncode(data.getTitle()) + "\" ");
//			xml.append("versions=\"" + data.getNumberOfVersions() + "\" ");
//			xml.append("registrations=\"" + data.getNumberOfRegistrations() + "\" ");
//			xml.append("size=\"" + data.getSize() + "\" ");
//			xml.append("/>");
//		}
//		xml.append("</courselist>");
//		return xml.toString();
//	}
}
