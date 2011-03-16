package com.rusticisoftware.hostedengine.client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rusticisoftware.hostedengine.client.datatypes.Enums;

public class ReportingService
{
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    public ReportingService(Configuration conf, ScormEngineService manager){
    	this.configuration = conf;
    	this.manager = manager;
    }
    
    public String GetReportageAuth(Enums.ReportageNavPermission navPermission, boolean isAdmin) throws Exception
    {
    	ServiceRequest sr = new ServiceRequest(this.configuration);
    	sr.getParameters().add("navpermission", navPermission.toString());
    	sr.getParameters().add("admin", String.valueOf(isAdmin).toLowerCase());
    	Document response = sr.callService("rustici.reporting.getReportageAuth");
    	return response.getElementsByTagName("auth").item(0).getTextContent();
    }
    
    public void UpdateApplicationInReportage() throws Exception
    {
    	ServiceRequest sr = new ServiceRequest(this.configuration);
    	sr.callService("rustici.reporting.updateApplicationInReportage");
    }
    
    /// <summary>
    /// Calling this method returns a URL which will authenticate and launch a Reportage session, starting
    /// at the specified Reportage URL entry point.
    /// </summary>
    /// <returns>A URL from which the export data can be downloaded</returns>
    public String GetReportUrl(String reportageAuth, String reportUrl) throws Exception
    {
    	ServiceRequest sr = new ServiceRequest(this.configuration);
    	//Relative path, auto add the right server name
    	String fullReportUrl = reportUrl;
        if(reportUrl.startsWith("/Reportage")){
            fullReportUrl = "http://" + sr.getServer() + reportUrl;
        }
    	sr.getParameters().add("auth", reportageAuth);
    	sr.getParameters().add("reporturl", fullReportUrl);
    	return sr.constructUrl("rustici.reporting.launchReport");
    }
    
    public Date GetReportDate() throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        String url = "http://" + sr.getServer() + 
            "/Reportage/scormreports/api/getReportDate.php?appId=" + this.configuration.getAppId();
        String dateStr = new String(sr.getResponseFromUrl(url), "UTF-8");
        SimpleDateFormat reportageParseDateFormat = new SimpleDateFormat("yyyy-M-d k:mm:ss");
        try { return reportageParseDateFormat.parse(dateStr); }
        catch (Exception e) { return new Date(); }
    }


}
