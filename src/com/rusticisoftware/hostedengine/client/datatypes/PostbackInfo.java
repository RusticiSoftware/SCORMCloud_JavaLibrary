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

import org.w3c.dom.Element;

import com.rusticisoftware.hostedengine.client.Utils;
import com.rusticisoftware.hostedengine.client.XmlUtils;
import com.rusticisoftware.hostedengine.client.datatypes.Enums.RegistrationResultsAuthType;
import com.rusticisoftware.hostedengine.client.datatypes.Enums.RegistrationResultsFormat;

/// <summary>
/// Class to hold registration postback data
/// </summary>
/// <author>Dave@brindlewaye.com</author>

public class PostbackInfo {

    private String regID;
    private String url;
    private String login;
    private String password;
    private RegistrationResultsAuthType authType;
    private RegistrationResultsFormat resultsFormat;
    
    /// <summary>
    /// Inflate postback info object from passed in xml element
    /// </summary>
    /// <param name="postbackInfoElem"></param>
    public PostbackInfo(Element postbackInfoElem) {
        this.setRegID(postbackInfoElem.getAttribute("regid"));
        this.setUrl(XmlUtils.getNamedElemValue(postbackInfoElem, "url"));
        this.setLogin(XmlUtils.getNamedElemValue(postbackInfoElem, "login"));
        this.setPassword(XmlUtils.getNamedElemValue(postbackInfoElem, "password"));
        String authType_str = XmlUtils.getNamedElemValue(postbackInfoElem, "authtype");
        if (!Utils.isNullOrEmpty(authType_str))
        {
            this.setAuthType(RegistrationResultsAuthType.getValue(authType_str));
        }
        else
        {
            this.setAuthType(RegistrationResultsAuthType.FORM);
        }
        String resultsFormat_str = XmlUtils.getNamedElemValue(postbackInfoElem, "resultsformat");
        if (!Utils.isNullOrEmpty(resultsFormat_str)){
            this.setResultsFormat(RegistrationResultsFormat.getValue(resultsFormat_str));
        }
        else
        {
            this.setResultsFormat(RegistrationResultsFormat.COURSE_SUMMARY);
        }
        
    }
    
    @Override
    public boolean equals(Object right)
    {
        int e = 1;
        if (right instanceof PostbackInfo)
        {
            PostbackInfo r = (PostbackInfo)right;
            e = 0;
            e += (this.getAuthType() == r.getAuthType()) ? 0 : 1;
            e += (this.getLogin().equals(r.getLogin())) ? 0 : 1;
            e += (this.getPassword().equals(r.getLogin())) ? 0 : 1;
            e += (this.getRegID().equals(r.getRegID())) ? 0 : 1;
            e += (this.getResultsFormat() == r.getResultsFormat()) ? 0 : 1;
            e += (this.getUrl().equals(r.getUrl())) ? 0 : 1;
        }
        return (e == 0);
    }
    
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public RegistrationResultsAuthType getAuthType() {
        return authType;
    }
    public void setAuthType(RegistrationResultsAuthType authType) {
        this.authType = authType;
    }
    public RegistrationResultsFormat getResultsFormat() {
        return resultsFormat;
    }
    public void setResultsFormat(RegistrationResultsFormat resultsFormat) {
        this.resultsFormat = resultsFormat;
    }
    public String getRegID() {
        return regID;
    }
    public void setRegID(String regID) {
        this.regID = regID;
    }
}
