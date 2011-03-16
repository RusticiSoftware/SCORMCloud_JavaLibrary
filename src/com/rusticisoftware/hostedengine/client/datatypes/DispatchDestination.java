package com.rusticisoftware.hostedengine.client.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.XmlUtils;

public class DispatchDestination {
	public String id;
	public String name;
	public String notes;
	public String createdBy;
	public Date createDate;
	public Date updateDate;
	public List<String> tags;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public DispatchDestination() {
		
	}
	
	public DispatchDestination(Element destElem) throws Exception {
		setId(XmlUtils.getChildElemText(destElem, "id"));
		setName(XmlUtils.getChildElemText(destElem, "name"));
		setNotes(XmlUtils.getChildElemText(destElem, "notes"));
		setCreatedBy(XmlUtils.getChildElemText(destElem, "createdBy"));
		
		String createDateStr = XmlUtils.getChildElemText(destElem, "createDate");
		setCreateDate(XmlUtils.parseXmlDate(createDateStr));
		
		String updateDateStr = XmlUtils.getChildElemText(destElem, "updateDate");
		setUpdateDate(XmlUtils.parseXmlDate(updateDateStr));
		
		Element tagsElem = XmlUtils.getFirstChildByTagName(destElem, "tags");
		if(tagsElem != null){
			tags = new ArrayList<String>();
			List<Element> tagElems = XmlUtils.getChildrenByTagName(tagsElem, "tag");
			for(Element tag : tagElems){
				tags.add(tag.getTextContent());
			}
		}
	}
	
	public static List<DispatchDestination> parseDestinationList(Element destsElem) throws Exception {
		List<DispatchDestination> destList = new ArrayList<DispatchDestination>();
		List<Element> destElems = XmlUtils.getChildrenByTagName(destsElem, "dispatchDestination");
		for(Element destElem : destElems){
			destList.add(new DispatchDestination(destElem));
		}
		return destList;
	}
	
	public static List<DispatchDestination> parseDestinationList(Document destsDoc) throws Exception {
		NodeList tmpList = destsDoc.getElementsByTagName("dispatchDestinations");
		return parseDestinationList((Element)(tmpList.item(0)));
		
	}
}
