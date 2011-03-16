package com.rusticisoftware.hostedengine.client.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.XmlUtils;

public class Dispatch {

	protected String id = UUID.randomUUID().toString();
	protected String destinationId;
	protected String appId;
	protected String courseAppId;
	protected String courseId;
	protected boolean enabled;
	private List<String> tags = new ArrayList<String>();
	protected String notes;
	protected String createdBy;
	protected Date createDate = new Date();
	protected Date updateDate = new Date();
	
	
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
	
	public String getAppId(){
		return appId;
	}
	
	public void setAppId(String appId){
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
    
    public String getNotes(){
    	return notes;
    }
    public void setNotes(String notes){
    	this.notes = notes;
    }
    
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreateDate(){
		return createDate;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	
	public Date getUpdateDate(){
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	
	public Dispatch (Element dispElem) throws Exception {
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
		if(tagsElem != null){
			tags = new ArrayList<String>();
			List<Element> tagElems = XmlUtils.getChildrenByTagName(tagsElem, "tag");
			for(Element tag : tagElems){
				tags.add(tag.getTextContent());
			}
		}
	}

	public static List<Dispatch> parseDispatchList(Element dispatchesElem) throws Exception {
		List<Dispatch> dispList = new ArrayList<Dispatch>();
		List<Element> dispElems = XmlUtils.getChildrenByTagName(dispatchesElem, "dispatch");
		for(Element dispElem : dispElems){
			dispList.add(new Dispatch(dispElem));
		}
		return dispList;
	}
	
	public static List<Dispatch> parseDispatchList(Document dispsDoc) throws Exception {
		NodeList tmpList = dispsDoc.getElementsByTagName("dispatches");
		return parseDispatchList((Element)(tmpList.item(0)));
		
	}
	
}
