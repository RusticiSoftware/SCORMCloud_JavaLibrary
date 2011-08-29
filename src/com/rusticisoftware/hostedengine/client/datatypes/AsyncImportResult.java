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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class AsyncImportResult
{
    public enum ImportStatus { CREATED, RUNNING, FINISHED, ERROR };
    
    private ImportStatus status = ImportStatus.CREATED;
    private List<ImportResult> importResults;
    private int errorCode = -1;
    private String errorMessage;
    
    public ImportStatus getImportStatus()
    {
        return status;
    }
    public List<ImportResult> getImportResults()
    {
        return importResults;
    }
    public int getErrorCode()
    {
    	return errorCode;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    public AsyncImportResult(Document asyncImportResultXml)
    {
        String statusText = ((Element)(asyncImportResultXml
                                .getElementsByTagName("status")).item(0))
                                .getTextContent();
        
        if (statusText.equals("created")){
            this.status = ImportStatus.CREATED;
        }  else if (statusText.equals("running")) {
            this.status = ImportStatus.RUNNING;
        } else if (statusText.equals("finished")) {
            this.status = ImportStatus.FINISHED;
        } else if (statusText.equals("error")) {
            this.status = ImportStatus.ERROR;
        }

        if (this.status == ImportStatus.FINISHED) {
            this.importResults = ImportResult.ConvertToImportResults(asyncImportResultXml);
        }

        if (this.status == ImportStatus.ERROR) {
        	Element errElem = ((Element)(asyncImportResultXml
                    				.getElementsByTagName("error")).item(0));
        	
        	try { this.errorCode = Integer.parseInt(errElem.getAttribute("code")); }
        	catch (NumberFormatException nfe) {}
        	
            this.errorMessage = errElem.getTextContent();
        }
    }

    public Boolean IsComplete()
    {
        return ((this.status == ImportStatus.FINISHED) || (this.status == ImportStatus.ERROR));
    }

    public Boolean HasError()
    {
        return (this.status == ImportStatus.ERROR);
    }
}
