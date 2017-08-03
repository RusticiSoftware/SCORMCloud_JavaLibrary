/* Software License Agreement (BSD License)
 * 
 * Copyright (c) 2010-2012, Rustici Software, LLC
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

import com.rusticisoftware.hostedengine.client.datatypes.InvitationInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class InvitationService {
    private Configuration configuration = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public InvitationService(Configuration configuration, ScormEngineService manager) {
        this.configuration = configuration;
    }

    public String createInvitation(String courseId, boolean publicInvitation, boolean send,
                                   String addresses, String emailSubject, String emailBody, String creatingUserEmail, Integer registrationCap,
                                   String postbackUrl, String authType, String urlName, String urlPass, String resultsFormat,
                                   boolean async, Map<String, String> extendedParameters) throws Exception {

        return createInvitation(courseId, publicInvitation, send, addresses, emailSubject, emailBody, creatingUserEmail,
            registrationCap, postbackUrl, authType, urlName, urlPass, resultsFormat, async, extendedParameters, null);
    }

    public String createInvitation(String courseId, boolean publicInvitation, boolean send,
                                   String addresses, String emailSubject, String emailBody, String creatingUserEmail, Integer registrationCap,
                                   String postbackUrl, String authType, String urlName, String urlPass, String resultsFormat,
                                   boolean async, Map<String, String> extendedParameters, List<String> tagList) throws Exception {

        ServiceRequest request = new ServiceRequest(configuration);
        request.setUsePost(true);

        request.getParameters().add("courseid", courseId);
        request.getParameters().add("public", publicInvitation);

        request.getParameters().add("send", send);

        if (registrationCap != null && registrationCap > 0) {
            request.getParameters().add("registrationCap", registrationCap);
        }
        if (addresses != null) {
            request.getParameters().add("addresses", addresses);
        }
        if (emailSubject != null) {
            request.getParameters().add("emailSubject", emailSubject);
        }
        if (emailBody != null) {
            request.getParameters().add("emailBody", emailBody);
        }
        if (creatingUserEmail != null) {
            request.getParameters().add("creatingUserEmail", creatingUserEmail);
        }

        if (postbackUrl != null) {
            request.getParameters().add("postbackurl", postbackUrl);
        }
        if (authType != null) {
            request.getParameters().add("authtype", authType);
        }
        if (urlName != null) {
            request.getParameters().add("urlname", urlName);
        }
        if (urlPass != null) {
            request.getParameters().add("urlpass", urlPass);
        }
        if (resultsFormat != null) {
            request.getParameters().add("resultsformat", resultsFormat);
        }
        if (tagList != null && tagList.size() > 0) {
            request.getParameters().add("tags", Utils.join(tagList, ","));
        }

        if (extendedParameters != null) {
            for (Entry<String, String> entry : extendedParameters.entrySet()) {
                request.getParameters().add(entry.getKey(), entry.getValue());
            }
        }

        if (async) {
            return Utils.getNonXmlPayloadFromResponse(request.callService("rustici.invitation.createInvitationAsync"));
        } else {
            return Utils.getNonXmlPayloadFromResponse(request.callService("rustici.invitation.createInvitation"));
        }


    }

    public String getInvitationStatus(String invitationId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("invitationId", invitationId);

        Document response = request.callService("rustici.invitation.getInvitationStatus");
        return response.getElementsByTagName("status").item(0).getTextContent();


    }

    public InvitationInfo getInvitationInfo(String invitationId, boolean includeRegistrationSummary) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("invitationId", invitationId);
        request.getParameters().add("detail", includeRegistrationSummary);

        Document response = request.callService("rustici.invitation.getInvitationInfo");
        return InvitationInfo.parseInvitationInfoElement((Element) response.getElementsByTagName("invitationInfo").item(0));
    }


    public List<InvitationInfo> getInvitationList(String invFilter, String coursefilter) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        if (invFilter != null) {
            request.getParameters().add("filter", invFilter);
        }
        if (coursefilter != null) {
            request.getParameters().add("coursefilter", coursefilter);
        }

        Document response = request.callService("rustici.invitation.getInvitationList");
        return InvitationInfo.parseListFromXml(response);
    }

    public void ChangeStatus(String invitationId, boolean enable, boolean open) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("invitationId", invitationId);
        request.getParameters().add("enable", enable);
        request.getParameters().add("open", open);

        request.callService("rustici.invitation.changeStatus");

    }

}
