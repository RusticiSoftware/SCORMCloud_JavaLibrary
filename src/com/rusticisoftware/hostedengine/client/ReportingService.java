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

import com.rusticisoftware.hostedengine.client.datatypes.Enums;
import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportingService {
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    public ReportingService(Configuration conf, ScormEngineService manager) {
        this.configuration = conf;
        this.manager = manager;
    }

    public String GetReportageAuth(Enums.ReportageNavPermission navPermission, boolean isAdmin) throws Exception {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        sr.getParameters().add("navpermission", navPermission.toString());
        sr.getParameters().add("admin", String.valueOf(isAdmin).toLowerCase());
        Document response = sr.callService("rustici.reporting.getReportageAuth");
        return response.getElementsByTagName("auth").item(0).getTextContent();
    }

    public void UpdateApplicationInReportage() throws Exception {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        sr.callService("rustici.reporting.updateApplicationInReportage");
    }

    /// <summary>
    /// Calling this method returns a URL which will authenticate and launch a Reportage session, starting
    /// at the specified Reportage URL entry point.
    /// </summary>
    /// <returns>A URL from which the export data can be downloaded</returns>
    public String GetReportUrl(String reportageAuth, String reportUrl) throws Exception {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        //Relative path, auto add the right server name
        String fullReportUrl = reportUrl;
        if (reportUrl.startsWith("/Reportage")) {
            fullReportUrl = sr.getProtocol() + "://" + sr.getServer() + reportUrl;
        }
        sr.getParameters().add("auth", reportageAuth);
        sr.getParameters().add("reporturl", fullReportUrl);
        return sr.constructUrl("rustici.reporting.launchReport");
    }

    public Date GetReportDate() throws Exception {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        String url = "http://" + sr.getServer() +
                         "/Reportage/scormreports/api/getReportDate.php?appId=" + this.configuration.getAppId();
        String dateStr = new String(sr.getResponseFromUrl(url), "UTF-8");
        SimpleDateFormat reportageParseDateFormat = new SimpleDateFormat("yyyy-M-d k:mm:ss");
        try {
            return reportageParseDateFormat.parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }
    }


}
