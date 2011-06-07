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

package com.rusticisoftware.hostedengine.client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.rusticisoftware.hostedengine.client.ServiceException;


public class ServiceRequest {
	private static int INVALID_WEB_SERVICE_RESPONSE = 300; 
	
	public static class ParameterMap extends HashMap<String, String[]> {
        private static final long serialVersionUID = 1L;

        public String getParameter(String name) {
			String[] vals = this.get(name);
			if(vals != null && vals.length > 0){
				return vals[0];
			}
			return null;
		}
		
		public void put(String key, String val){
			if(val != null){
				this.put(key, new String[] {val});
			}
		}
		
		public void add(String key, Object val){
		    this.put(key, val.toString());
		}
	}
	
	private ParameterMap methodParameters = new ParameterMap();
	private String fileToPost = null;
	private Configuration configuration = null;
	private String engineServiceUrl = null;

	
	public ServiceRequest (Configuration configuration) {
		this.configuration = configuration;
		//Keep local copy of services url in case we need to change
		//it (in just this request), like setting a particular server
		this.engineServiceUrl = configuration.getScormEngineServiceUrl();
	}
	
	public ParameterMap getParameters()
	{
	    return methodParameters;
	}
	public void setParameters(ParameterMap params)
	{
	    methodParameters = params;
	}
	
	public String getFileToPost()
	{
	    return fileToPost;
	}
	public void setFileToPost(String fileName)
	{
	    File f = new File(fileName);
	    if (!f.exists()){
	        throw new IllegalArgumentException("Path provided for fileToPost does not point to an existing file. Value = " + fileName);
	    }
	    fileToPost = fileName;
	}
	
	public String getServer()
	{
        int beginIndex = engineServiceUrl.indexOf("://")+3;
        int endIndex = engineServiceUrl.indexOf("/", beginIndex);
        return engineServiceUrl.substring(beginIndex, endIndex);
	}
	
	public void setServer(String serverName)
	{
	    this.engineServiceUrl = this.engineServiceUrl.replaceFirst(getServer(), serverName);
	}
	
	public String getProtocol()
	{
		if(engineServiceUrl == null){
			return null;
		}
		return engineServiceUrl.substring(0, engineServiceUrl.indexOf("://"));
	}
	
	public void setProtocol(String protocol)
	{
		this.engineServiceUrl = this.engineServiceUrl.replaceFirst(getProtocol(), protocol); 
	}
	
	public Document callService(String methodName) throws Exception
	{
	    return getXmlResponseFromUrl(constructUrl(methodName));
	}
	
	public String getStringFromService(String methodName) throws Exception
    {
        return getStringResponseFromUrl(constructUrl(methodName));
    }

	public String getFileFromService(String toFileName, String methodName) throws Exception
    {
        return getFileResponseFromUrl(toFileName, constructUrl(methodName));
    }

    public String getFileResponseFromUrl(String toFileName, String url) throws Exception
    {   
        File f = new File(toFileName);
        FileOutputStream fos = new FileOutputStream(f);
        copyResponseFromUrlToStream(url, fos, true);
        return toFileName;
    }
    
    protected Document getXmlResponseFromUrl(String url) throws Exception
    {
        byte[] responseBytes = getResponseFromUrl(url);
        //System.out.println(url);
        String responseText = new String(responseBytes, "UTF-8");
        //System.out.println(responseText);
        return assertNoErrorAndReturnXmlDoc(responseText);
    }
    
    protected String getStringResponseFromUrl(String url) throws Exception
    {
        byte[] responseBytes = getResponseFromUrl(url);
        String responseText = new String(responseBytes, "UTF-8");
        return responseText;
    }
    
    public byte[] getResponseFromUrl(String urlStr) throws Exception {
    	ByteArrayOutputStream responseBytes = new ByteArrayOutputStream();
    	copyResponseFromUrlToStream(urlStr, responseBytes, true);
    	return responseBytes.toByteArray();
    }
    
    public void copyResponseFromUrlToStream(String urlStr, OutputStream out, boolean closeOutputStream) throws Exception
    {
        URL url = new URL(urlStr);
        int retries = 6;
        int msWait = 200;
        
        while(retries > 0){
        	InputStream responseStream = null;
        	try {
	        	
		        URLConnection connection = url.openConnection();
		        connection.setDoOutput(true);
		        connection.setDoInput(true);
		        connection.setUseCaches(false);

		        if(getFileToPost() == null){
		            responseStream = connection.getInputStream();
		        }
		        else {
		            File f = new File(getFileToPost());
		            responseStream = postFile(connection, "filedata", f);
		        }
		        
		        Utils.bufferedCopyStream(responseStream, out);
		        out.flush();
		        if(closeOutputStream){
		        	out.close();
		        }
		        return;
	        } 
	        catch (IOException ioe) {
	        	System.err.println(ioe.getMessage());
	        	Thread.sleep(msWait);
	        	retries--;
	        	msWait *= 2;
	        	if(retries == 0){
	        		throw ioe;
	        	}
	        }
	        finally {
	        	if(responseStream != null){
	        		responseStream.close();
	        	}
	        }
        }
        throw new Exception("Could not retrieve a response from " + this.getServer());
    }
    
    protected Document assertNoErrorAndReturnXmlDoc(String responseText) throws Exception
    {
        Document xml = parseXmlResponse(responseText);
        raiseServiceExceptionIfPresent(xml);
        return xml;
    }

    /// <summary>
    /// Given the method name and the parameters and configuration associated 
    /// with this object, generate the full URL for the web service invocation.
    /// </summary>
    /// <param name="methodName">Method name for the HOSTED Engine api call</param>
    /// <returns>Fully qualified URL to be used for invocation</returns>
    public String constructUrl(String methodName) throws Exception
    {
        
        ParameterMap allParams = new ParameterMap();
        allParams.put("appid", configuration.getAppId());
        allParams.put("method", methodName);
        allParams.put("ts", Utils.getFormattedTime(new Date()));
        allParams.put("applib", "java");
        allParams.putAll(this.methodParameters);
        
        //Generate signature
        String sig = RequestSigner.getSignatureForRequest(allParams, this.configuration.getSecurityKey());
        allParams.put("sig", sig);
        
        StringBuilder paramStr = new StringBuilder();
        for(String paramName : allParams.keySet()){
            String[] vals = allParams.get(paramName);
            if(vals == null){
                continue;
            }
            for(String val : vals){
                if(val != null){
                    paramStr.append(Utils.getEncodedParam(paramName, val) + "&");
                }
            }
        }
        
        //Cut off trailing ampersand
        paramStr.deleteCharAt(paramStr.length() - 1);
        return engineServiceUrl + "/api?" + paramStr.toString();
    }
	
	protected void raiseServiceExceptionIfPresent(Document xmlResponse) throws ServiceException {
		Element rsp = (Element)xmlResponse.getElementsByTagName("rsp").item(0);
		if(rsp.getAttribute("stat").equals("fail")){
			NodeList errs = rsp.getElementsByTagName("err");	
			ServiceException se = null;
			//Here we loop through looking for inner err elements that may be causes of other err elements
			for(int i = errs.getLength()-1; i >= 0; i--){	
				Element err = (Element)errs.item(i);
				ServiceException cause = getServiceExceptionFromErrElement(err); 
				se = (se == null) ? cause : new ServiceException(se.getErrorCode(), se.getMessage(), cause);
			}
			if(se != null){
				throw se;
			}
		}
	}
	
	protected ServiceException getServiceExceptionFromErrElement(Element err){
		int errCode = -1;
		try { errCode = Integer.parseInt(err.getAttribute("code")); }
		catch (NumberFormatException nfe) {}
		return new ServiceException(errCode, err.getAttribute("msg"));
	}
	
	protected Document parseXmlResponse (String xmlString) throws Exception {
		Document xmlDoc;
		try {
            xmlDoc = XmlUtils.parseXmlString(xmlString);
        }
        catch (SAXParseException e1) {
            throw new ServiceException(INVALID_WEB_SERVICE_RESPONSE, "Error parsing xml", e1);
        }
        catch (SAXException e2) {
            throw new ServiceException(INVALID_WEB_SERVICE_RESPONSE, "Some other error occurred processing xml response", e2);
        }
		
		try {
			NodeList rspElems = xmlDoc.getElementsByTagName("rsp");
			if(rspElems.getLength() < 1){
				throw new ServiceException(INVALID_WEB_SERVICE_RESPONSE, "No response (rsp) element found in web service response.");
			}
			Element rsp = (Element)rspElems.item(0);
			String status = rsp.getAttribute("stat");
			if(status == null){
				throw new ServiceException(INVALID_WEB_SERVICE_RESPONSE, "Expected 'stat' attribute on <rsp> tag.");
			}
			
			if(!status.equals("ok")){
				Element err = (Element)rsp.getFirstChild();
				if(err == null){
					throw new ServiceException(INVALID_WEB_SERVICE_RESPONSE, "Expected <err> element to be first child of <rsp> element");
				}
				
				String errCode = err.getAttribute("code");
				String errMsg = err.getAttribute("msg");
				if(errCode == null || errMsg == null){
					throw new ServiceException(INVALID_WEB_SERVICE_RESPONSE, "Expected 'code' and 'msg' attributes on <err> element");
				}
			}
			return xmlDoc;
		}
		catch (ServiceException se){
			throw new ServiceException(se.getErrorCode(), se.getMessage() + " Response text: " + xmlString);
		}
	}
	
	public InputStream postFile(URLConnection connection, String name, File file) throws Exception 
	{
		String fileName = file.getName();
		Random random = new Random();
		String randomString = Long.toString(random.nextLong(), 36);
		String boundary = "---------------------------" + randomString + randomString + randomString;
		 
		StringBuilder intro = new StringBuilder();	  	  
		intro.append("--");
		intro.append(boundary);
		intro.append("\r\n");
		
		intro.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"");
		intro.append("\r\n");
		
		intro.append("Content-Type: ");
		String type = connection.guessContentTypeFromName(fileName);
		if (type == null) type = "application/octet-stream";
		intro.append(type);
		intro.append("\r\n");
		intro.append("\r\n");
		
		//Then the file data will go here
		
		StringBuilder outro = new StringBuilder();
		outro.append("\r\n");
		outro.append("--");
		outro.append(boundary);
		outro.append("--");
		outro.append("\r\n");
		
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		
		int requestLength = intro.length() + (int)(file.length()) + outro.length();
		((HttpURLConnection)connection).setFixedLengthStreamingMode(requestLength);
		
		OutputStream out = connection.getOutputStream();
		out.write(intro.toString().getBytes());
		Utils.bufferedCopyStream(new FileInputStream(file), out);
		out.write(outro.toString().getBytes());
		out.close();
		
		return connection.getInputStream();
	}
}
