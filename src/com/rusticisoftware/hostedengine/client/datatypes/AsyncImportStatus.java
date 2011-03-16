package com.rusticisoftware.hostedengine.client.datatypes;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.XmlUtils;

public class AsyncImportStatus
{
    public enum ImportStatus { CREATED, RUNNING, FINISHED, ERROR };
    
    private ImportStatus status = ImportStatus.CREATED;
    private String message = null;
    private Double progress = null;
    private List<ImportResult> importResults;
    private int errorCode = -1;
    private String errorMessage;
    
    public ImportStatus getImportStatus()
    {
        return status;
    }
    public String getMessage(){
    	return message;
    }
    public Double getProgress(){
    	return progress;
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
    
    public AsyncImportStatus(Document asyncImportResultXml)
    {
    	Element root = asyncImportResultXml.getDocumentElement();
        String statusText = XmlUtils.getChildElemText(root, "status");
        
        if (statusText.equals("created")){
            this.status = ImportStatus.CREATED;
        }  else if (statusText.equals("running")) {
            this.status = ImportStatus.RUNNING;
        } else if (statusText.equals("finished")) {
            this.status = ImportStatus.FINISHED;
        } else if (statusText.equals("error")) {
            this.status = ImportStatus.ERROR;
        }

        message = XmlUtils.getChildElemText(root, "message");
        String progressStr = XmlUtils.getChildElemText(root, "progress");
        if(progressStr != null && progressStr.length() > 0){
        	try { progress = Double.parseDouble(progressStr); }
        	catch (Exception e ) {}
        } else {
        	progress = null;
        }
        
        if (this.status == ImportStatus.FINISHED) {
            this.importResults = ImportResult.ConvertToImportResults(asyncImportResultXml);
        }

        if (this.status == ImportStatus.ERROR) {
        	Element errElem = ((Element)(asyncImportResultXml.getElementsByTagName("error")).item(0));
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
    
    public Boolean HasMessage()
    {
    	return message != null && message.length() > 0;
    }
    
    public Boolean HasProgress()
    {
    	return progress != null;
    }
}
