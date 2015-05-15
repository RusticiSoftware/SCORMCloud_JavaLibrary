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

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rusticisoftware.hostedengine.client.datatypes.ActivityProvider;

public class LrsAccountService 
{
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public LrsAccountService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }
    
    /// <summary>
    /// Calling this method will create a new activity provider with a unique, and randomly generated, key and password combination.
    /// </summary>
    /// <returns></returns>
    public ActivityProvider createActivityProvider() throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);

        Document response = sr.callService("rustici.lrsaccount.createActivityProvider");
        Element resultElem = (Element)(response.getElementsByTagName("activityProvider").item(0));
        return ActivityProvider.parseFromXmlElement(resultElem);
    }

    /// <summary>
    /// Calling this method will return a list of all current activity providers created by this service.
    /// </summary>
    /// <returns></returns>
    public List<ActivityProvider> listActivityProviders() throws Exception
    {
      ServiceRequest sr = new ServiceRequest(this.configuration);
      Document response = sr.callService("rustici.lrsaccount.listActivityProviders");

      return ActivityProvider.parseListFromXml(response);
    }

    /// <summary>
    /// A call to this method with the optional parameters passed to it will modify those values for the activity provider 
    /// indicated by the accountKey.
    /// </summary>
    /// <param name="accountKey">Required account key for the activity provider to modify</param>
    /// <param name="authType">Optional 'basic' or 'oauth'</param>
    /// <param name="isActive">Optional true or false indicating whether or not this account should be enabled</param>
    /// <param name="label">Optional name for the activity provider</param>
    public void editActivityProvider(String accountKey, String authType, Boolean isActive, String label) throws Exception 
    {
      ServiceRequest sr = new ServiceRequest(configuration);

      sr.getParameters().add("accountkey", accountKey);

      if(authType != null) {
        sr.getParameters().add("authtype", authType);
      }
      if(isActive != null) {
        sr.getParameters().add("isactive", isActive);
      }
      if(label != null) {
        sr.getParameters().add("label", label);
      }

      sr.callService("rustici.lrsaccount.editActivityProvider");
    }
    
    /// <summary>
    /// Calling this method deletes the activity provider indicated by the accountKey passed in. This cannot be undone. If you 
    /// wish to temporarily disable an Activity provider use the edit API instead.
    /// </summary>
    /// <param name="accountKey">Required account key for the activity provider to delete</param>
    public void deleteActivityProvider(String accountKey) throws Exception
    {
        ServiceRequest sr = new ServiceRequest(configuration);
        
        sr.getParameters().add("accountkey", accountKey);
        
        sr.callService("rustici.lrsaccount.deleteActivityProvider");
    }

}
