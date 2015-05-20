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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.XmlUtils;

public class ActivityProvider
{
    private Boolean accountEnabled;

    private String accountAuthType;
    private String accountKey;
    private String accountLabel;
    private String accountSecret;
    private String id;
    

    public static ActivityProvider parseFromXmlElement(Element exportElem) throws Exception {
      ActivityProvider provider = new ActivityProvider();

      String enabledStr = XmlUtils.getChildElemText(exportElem, "accountEnabled");
      provider.setAccountEnabled("true".equals(enabledStr));

      provider.setAccountAuthType(XmlUtils.getChildElemText(exportElem, "accountAuthType"));
      provider.setAccountKey(XmlUtils.getChildElemText(exportElem, "accountKey"));
      provider.setAccountLabel(XmlUtils.getChildElemText(exportElem, "accountLabel"));
      provider.setAccountSecret(XmlUtils.getChildElemText(exportElem, "accountSecret"));
      provider.setId(XmlUtils.getChildElemText(exportElem, "id"));

      return provider;
    }

    public static List<ActivityProvider> parseListFromXml (Document courseListXmlDoc) throws Exception {
      Element activityProviderListElem = (Element)courseListXmlDoc.getElementsByTagName("activityProviderList").item(0);

      List<ActivityProvider> providerList = new ArrayList<ActivityProvider>();

      NodeList providerElems = activityProviderListElem.getChildNodes();
      for(int i = 0; i < providerElems.getLength(); i++){
        if(providerElems.item(i) instanceof Element) {
        } else {
          continue;
        }
        Element courseElem = (Element)providerElems.item(i);
        providerList.add( parseFromXmlElement(courseElem) );
      }

      return providerList;
    }

    public static String getXmlString (List<ActivityProvider> actiivtyProviderList){
      StringBuilder xml = new StringBuilder();
      xml.append("<activityProviderList>\n");
      for(ActivityProvider data : actiivtyProviderList){
        xml.append(data.getXmlString());
      }
      xml.append("</activityProviderList>");
      return xml.toString();
    }

    public String getXmlString() {
      StringBuilder xml = new StringBuilder();
      xml.append("  <activityProvider>\n");
      xml.append("    <id><![CDATA[" + getId() + "]]></id>\n");
      xml.append("    <accountLabel><![CDATA[" + getAccountLabel() + "]]></accountLabel>\n");
      xml.append("    <accountKey><![CDATA[" + getAccountKey() + "]]></accountKey>\n");
      xml.append("    <accountSecret><![CDATA[" + getAccountSecret() + "]]></accountSecret>\n");
      xml.append("    <accountAuthType><![CDATA[" + getAccountAuthType() + "]]></accountAuthType>\n");
      xml.append("    <accountEnabled>" + getAccountEnabled() + "</accountEnabled>\n");
      xml.append("  </activityProvider>\n");

      return xml.toString();
    }

    public Boolean getAccountEnabled() {
      return accountEnabled;
    }

    public void setAccountEnabled(Boolean accountEnabled) {
      this.accountEnabled = accountEnabled;
    }

    public String getAccountAuthType() {
      return accountAuthType;
    }

    public void setAccountAuthType(String accountAuthType) {
      this.accountAuthType = accountAuthType;
    }

    public String getAccountKey() {
      return accountKey;
    }

    public void setAccountKey(String accountKey) {
      this.accountKey = accountKey;
    }

    public String getAccountLabel() {
      return accountLabel;
    }

    public void setAccountLabel(String accountLabel) {
      this.accountLabel = accountLabel;
    }

    public String getAccountSecret() {
      return accountSecret;
    }

    public void setAccountSecret(String accountSecret) {
      this.accountSecret = accountSecret;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

}
