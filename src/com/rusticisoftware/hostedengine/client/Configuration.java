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

public class Configuration
{
    private String appId = null;
    private String securityKey = null;
    private String scormEngineServiceUrl = null;
    private String origin = "rusticisoftware.javalibrary.1.1.2";

    /// <summary>
    /// Single constructor that contains the required properties
    /// </summary>
    /// <param name="scormEngineServiceUrl">URL to the service, ex: http://services.scorm.com/EngineWebServices</param>
    /// <param name="appId">The Application ID obtained by registering with the SCORM Engine Service</param>
    /// <param name="securityKey">The security key (password) linked to the application ID</param>
    /// <param name="origin">The origin string that defines the organization, application name and version</param>
    public Configuration(String scormEngineServiceUrl, String appId, String securityKey, String origin)
    {
        this.appId = appId;
        this.securityKey = securityKey;
        this.scormEngineServiceUrl = scormEngineServiceUrl;
        this.origin = origin;
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
    
    /// <summary>
    /// The origin string that defines the organization, application name and version
 	/// of the software accessing the Cloud service.
    /// </summary>
    public String getOrigin()
    {
    	return origin;
    }
    public void setOrigin(String origin)
    {
    	this.origin = origin;
    }
}
