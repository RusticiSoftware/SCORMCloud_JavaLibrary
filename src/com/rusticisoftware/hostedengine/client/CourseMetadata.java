package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CourseMetadata {

	private String id;
	private String title;
	private String description;
	private Long duration;
	private Long typicaltime;
	private Double masteryScore;
	private ArrayList<String> keyWords = new ArrayList<String>();
	private ArrayList<CourseMetadata> children = new ArrayList<CourseMetadata>();
	
	public CourseMetadata() {};
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public void setMasteryScore(Double masteryScore){
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
	
	public static CourseMetadata parseFromXmlElement (Element elem) throws Exception {
		CourseMetadata data = new CourseMetadata();
		data.setId(elem.getAttribute("id"));
		NodeList attrs = elem.getChildNodes();
		for(int i = 0; i < attrs.getLength(); i++){
			Element attr = (Element)attrs.item(i);
			String tagName = attr.getTagName();
			if(tagName.equals("title")){
				data.setTitle( attr.getTextContent() );
			}
			else if (tagName.equals("description")){
				data.setDescription( attr.getTextContent() );
			}
			else if (tagName.equals("duration")){
				Long duration = 0L;
				try { duration = Long.parseLong( attr.getTextContent() ); }
				catch (Exception e) {};
				data.setDuration(duration);
			}
			else if (tagName.equals("typicaltime")){
				Long typicalTime = 0L;
				try { typicalTime = Long.parseLong( attr.getTextContent() ); }
				catch (Exception e) {};
				data.setTypicaltime(typicalTime);
			}
			else if (tagName.equals("keywords")){
				ArrayList<String> kwords = data.getKeyWords();
				NodeList keyWordElems = attr.getElementsByTagName("keyword");
				for(int wordIndex = 0; wordIndex < keyWordElems.getLength(); wordIndex++){
					Element keyWordElem = (Element)keyWordElems.item(wordIndex);
					kwords.add( keyWordElem.getTextContent() );
				}
			}
			else if (tagName.equals("children")){
				ArrayList<CourseMetadata> children = data.getChildren();
				NodeList childElems = attr.getChildNodes();
				for(int childIndex = 0; childIndex < childElems.getLength(); childIndex++){
					Element childElem = (Element)childElems.item(childIndex); 
					children.add( parseFromXmlElement(childElem) );
				}
			}
		}
		return data;
	}
}
