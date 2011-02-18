package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.w3c.dom.Element;

public class Export
{
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
    
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getAppId()
    {
        return appId;
    }
    public void setAppId(String appId)
    {
        this.appId = appId;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public Date getStartDate()
    {
        return startDate;
    }
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
    public Date getEndDate()
    {
        return endDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
    public double getPercentComplete()
    {
        return percentComplete;
    }
    public void setPercentComplete(double percentComplete)
    {
        this.percentComplete = percentComplete;
    }
    public String getServerLocation()
    {
        return serverLocation;
    }
    public void setServerLocation(String serverLocation)
    {
        this.serverLocation = serverLocation;
    }
    
    public static Export parseFromXmlElement(Element exportElem) throws Exception
    {
        Export export = new Export();
        export.setId(exportElem.getAttribute("id"));
        
        export.setAppId( XmlUtils.getChildElemText(exportElem, "appid") );
        export.setStatus( XmlUtils.getChildElemText(exportElem, "status") );
        String startDate = XmlUtils.getChildElemText(exportElem, "start_date");
        export.setStartDate( Utils.parseIsoDate(startDate) );
        if(export.getStatus().equals(Export.STATUS_COMPLETE)){
            String endDate = XmlUtils.getChildElemText(exportElem, "end_date");
            if(endDate != null){
                export.setEndDate(Utils.parseIsoDate(endDate));
            }
        }
        
        export.setPercentComplete( 
                Double.parseDouble(
                        XmlUtils.getChildElemText(exportElem, "percent_complete")) );
        
        export.setServerLocation(XmlUtils.getChildElemText(exportElem, "server_location"));
        
        return export;
    }
    
    public static List<Export> parseExportListFromXml(Element exportListElem) throws Exception
    {
        List<Export> exports = new ArrayList<Export>();
        if(exportListElem != null){
            List<Element> exportList = XmlUtils.getChildrenByTagName(exportListElem, "export");
            for(Element export : exportList){
                exports.add(Export.parseFromXmlElement(export));
            }
        }
        return exports;
    }
}
