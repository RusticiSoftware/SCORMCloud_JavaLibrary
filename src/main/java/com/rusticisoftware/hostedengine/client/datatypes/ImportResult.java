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
