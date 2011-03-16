package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TaggingService {
	private static final Logger log = Logger.getLogger(UploadService.class.toString());
	
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public TaggingService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }
    
    public List<String> getCourseTags(String courseId) throws Exception{
    	ServiceRequest request = new ServiceRequest(configuration);
    	request.getParameters().add("courseid", courseId);
    	Document response = request.callService("rustici.tagging.getCourseTags");
    	return parseTagList(response);
    }
    
    public void setCourseTags(String courseId, List<String> tags) throws Exception {
    	ServiceRequest request = new ServiceRequest(configuration);
    	request.getParameters().add("courseid", courseId);
    	request.getParameters().add("tags", expandTagList(tags));
    	request.callService("rustici.tagging.setCourseTags");
    }
    
    public List<String> getLearnerTags(String learnerId) throws Exception {
    	ServiceRequest request = new ServiceRequest(configuration);
    	request.getParameters().add("learnerid", learnerId);
    	Document response = request.callService("rustici.tagging.getLearnerTags");
    	return parseTagList(response);
    }
    
    public void setLearnerTags(String learnerId, List<String> tags) throws Exception {
    	ServiceRequest request = new ServiceRequest(configuration);
    	request.getParameters().add("learnerid", learnerId);
    	request.getParameters().add("tags", expandTagList(tags));
    	request.callService("rustici.tagging.setLearnerTags");
    }
    
    public List<String> parseTagList(Document xmlDoc){
    	NodeList nodeList = xmlDoc.getElementsByTagName("tag");
    	List<String> tags = new ArrayList<String>();
    	for(int i = 0; i < nodeList.getLength(); i++){
    		Element child = (Element)nodeList.item(i);
    		tags.add(child.getTextContent());
    	}
    	return tags;
    }

    public String expandTagList(List<String> tags){
    	if(tags.size() == 0){
    		return "";
    	}
    	StringBuilder tagList = new StringBuilder();
    	for(String tag : tags){
    		tagList.append(tag+",");
    	}
    	tagList.deleteCharAt(tagList.length()-1);
    	return tagList.toString();
    }
    
}
