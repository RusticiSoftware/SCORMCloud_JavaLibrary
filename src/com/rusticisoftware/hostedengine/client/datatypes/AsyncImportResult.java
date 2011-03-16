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
