package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DebugService
{
	private static final Logger log = Logger.getLogger(UploadService.class.toString());
	
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public DebugService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }

    /// <summary>
    /// Checks for a successful connection to the cloud service
    /// </summary>
    /// <returns>A boolean representing whether the call was successful</returns>
    public boolean CloudPing() throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        Document response = request.callService("rustici.debug.ping");
        return response.getDocumentElement().getAttribute("stat").equalsIgnoreCase("ok");
        
    }
    
  /// <summary>
    /// Checks for a successful authenticated connection to the cloud service
    /// </summary>
    /// <returns>A boolean representing whether the call was successful</returns>
    public boolean CloudAuthPing() throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        Document response = request.callService("rustici.debug.authPing");
        return response.getDocumentElement().getAttribute("stat").equalsIgnoreCase("ok");
        
    }

    
}
