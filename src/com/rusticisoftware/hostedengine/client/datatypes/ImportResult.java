package com.rusticisoftware.hostedengine.client.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImportResult {
	private String _title = null;
	private String _message = null;
	private boolean _wasSuccessful = false;
	private List<String> _parserWarnings = new ArrayList<String>();
	
	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public boolean getWasSuccessful() {
		return _wasSuccessful;
	}

	public void setWasSuccessful(boolean wasSuccessful) {
		_wasSuccessful = wasSuccessful;
	}

	public List<String> getParserWarnings() {
		return _parserWarnings;
	}

	public void setParserWarnings(List<String> parserWarnings) {
		_parserWarnings = parserWarnings;
	}

	public ImportResult (Element importResultElem)
	{
        boolean wasSuccessful = Boolean.parseBoolean(importResultElem.getAttribute("successful"));
        this.setWasSuccessful(wasSuccessful);
        
        NodeList importResChildren = importResultElem.getChildNodes();
        for(int i = 0; i < importResChildren.getLength(); i++){
            Element elem = (Element)importResChildren.item(i);
            String tagName = elem.getTagName();
            if(tagName.equals("title")){
                this.setTitle(elem.getTextContent());
            }
            else if (tagName.equals("message")){
                this.setMessage(elem.getTextContent());
            }
            else if (tagName.equals("parserwarnings")){
                NodeList warningElems = elem.getElementsByTagName("warning");
                for(int j = 0; j < warningElems.getLength(); j++){
                    Element warningElem = (Element)warningElems.item(j);
                    this.getParserWarnings().add(warningElem.getTextContent());
                }
            }
        }
	}
	
	public static List<ImportResult> ConvertToImportResults (Document importResultXmlDoc)
	{
		ArrayList<ImportResult> results = new ArrayList<ImportResult>();
		NodeList importResultNodes = importResultXmlDoc.getElementsByTagName("importresult");
		for(int i = 0; i < importResultNodes.getLength(); i++){
			Element importResultElem = (Element)importResultNodes.item(i);
			results.add( new ImportResult(importResultElem) );
		}
		return results;
	}
}
