package com.rusticisoftware.hostedengine.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UploadProgress {
	private boolean empty = true;
	private String uploadId;
	private double percentComplete;
	private long bytesRead;
	private long contentLength;
	
	public boolean isEmpty(){
		return empty;
	}
	
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public double getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(double percentComplete) {
		this.percentComplete = percentComplete;
	}
	public long getBytesRead() {
		return bytesRead;
	}
	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

    
    public UploadProgress (Document xmlDoc){
        NodeList upList = xmlDoc.getElementsByTagName("upload_progress");
        if (upList.getLength() > 0) {
        	empty = false;
        	
            Element uploadProg = (Element)upList.item(0);
            Element uploadId = (Element)uploadProg.getElementsByTagName("upload_id").item(0);
            Element percentComplete = (Element)uploadProg.getElementsByTagName("percent_complete").item(0);
            Element bytesRead = (Element)uploadProg.getElementsByTagName("bytes_read").item(0);
            Element contentLength = (Element)uploadProg.getElementsByTagName("content_length").item(0);

            this.uploadId = uploadId.getTextContent();
            this.percentComplete = Double.parseDouble(percentComplete.getTextContent());
            this.bytesRead = Long.parseLong(bytesRead.getTextContent());
            this.contentLength = Long.parseLong(contentLength.getTextContent());
        }
    }
}
