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

import com.rusticisoftware.hostedengine.client.Utils;
import com.rusticisoftware.hostedengine.client.XmlUtils;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Export {
    public static final String STATUS_UNKNOWN = "unknown";
    public static final String STATUS_STARTED = "started";
    public static final String STATUS_CANCELED = "canceled";
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_EXPIRED = "expired";

    private String id;
    private String appId;
    private String status = STATUS_UNKNOWN;
    private Date startDate;
    private Date endDate;
    private double percentComplete = 0.0;
    private String serverLocation = "unavailable";

    public static Export parseFromXmlElement(Element exportElem) throws Exception {
        Export export = new Export();
        export.setId(exportElem.getAttribute("id"));

        export.setAppId(XmlUtils.getChildElemText(exportElem, "appid"));
        export.setStatus(XmlUtils.getChildElemText(exportElem, "status"));
        String startDate = XmlUtils.getChildElemText(exportElem, "start_date");
        export.setStartDate(Utils.parseIsoDate(startDate));
        if (export.getStatus().equals(Export.STATUS_COMPLETE)) {
            String endDate = XmlUtils.getChildElemText(exportElem, "end_date");
            if (endDate != null) {
                export.setEndDate(Utils.parseIsoDate(endDate));
            }
        }

        export.setPercentComplete(
            Double.parseDouble(
                XmlUtils.getChildElemText(exportElem, "percent_complete")));

        export.setServerLocation(XmlUtils.getChildElemText(exportElem, "server_location"));

        return export;
    }

    public static List<Export> parseExportListFromXml(Element exportListElem) throws Exception {
        List<Export> exports = new ArrayList<Export>();
        if (exportListElem != null) {
            List<Element> exportList = XmlUtils.getChildrenByTagName(exportListElem, "export");
            for (Element export : exportList) {
                exports.add(Export.parseFromXmlElement(export));
            }
        }
        return exports;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public String getServerLocation() {
        return serverLocation;
    }

    public void setServerLocation(String serverLocation) {
        this.serverLocation = serverLocation;
    }
}
