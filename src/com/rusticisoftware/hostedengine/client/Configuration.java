package com.rusticisoftware.hostedengine.client;

public class Configuration
{
    private String appId = null;
    private String securityKey = null;
    private String scormEngineServiceUrl = null;

    /// <summary>
    /// Single constuctor that contains the required properties
    /// </summary>
    /// <param name="scormEngineServiceUrl">URL to the service, ex: http://services.scorm.com/EngineWebServices</param>
    /// <param name="appId">The Application ID obtained by registering with the SCORM Engine Service</param>
    /// <param name="securityKey">The security key (password) linked to the application ID</param>
    public Configuration(String scormEngineServiceUrl, String appId, String securityKey)
    {
        this.appId = appId;
        this.securityKey = securityKey;
        this.scormEngineServiceUrl = scormEngineServiceUrl;
    }
    
    /// <summary>
    /// The Application ID obtained by registering with the SCORM Engine Service
    /// </summary>
    public String getAppId()
    {
        return appId;
    }
    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    /// <summary>
    /// The security key (password) linked to the application ID
    /// </summary>
    public String getSecurityKey()
    {
        return securityKey;
    }
    public void setSecurityKey(String securityKey)
    {
        this.securityKey = securityKey;
    }

    /// <summary>
    /// URL to the service, ex: http://services.scorm.com/EngineWebServices
    /// </summary>
    public String getScormEngineServiceUrl()
    {
        return scormEngineServiceUrl;
    }
    public void setScormEngineServiceUrl(String scormEngineServiceUrl)
    {
        this.scormEngineServiceUrl = scormEngineServiceUrl;
    } 
}
