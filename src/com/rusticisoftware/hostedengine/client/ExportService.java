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

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rusticisoftware.hostedengine.client.datatypes.Export;

public class ExportService
{
    private Configuration configuration = null;
    private ScormEngineService manager = null;
    
    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    /// <param name="configuration">Application Configuration Data</param>
    public ExportService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }
    
    /// <summary>
    /// Calling this method will start a new data export, and return an id 
    /// that can be used to check on the status of the export using a call to Status
    /// </summary>
    /// <returns>The unique id for the started data export</returns>
    public String Start() throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        Document response = sr.callService("rustici.export.start");
        Element elem = (Element)response.getElementsByTagName("export_id").item(0);
        return elem.getTextContent();
    }
    
    /// <summary>
    /// Calling this method will cancel the export with the passed in export id.
    /// </summary>
    /// <param name="exportId"></param>
    /// <returns>True if successfully canceled</returns>
    public boolean Cancel(String exportId) throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        sr.getParameters().add("exportid", exportId);
        sr.callService("rustici.export.cancel");
        return true;
    }
    
    /// <summary>
    /// A call to this method will return some detailed information about an export, 
    /// including the export status (started, canceled, complete, error), the start date, 
    /// the end date (if complete), the percent complete, and the server from which the 
    /// export should be downloaded.
    /// </summary>
    /// <param name="exportId"></param>
    /// <returns>An Export object containing information about the export</returns>
    public Export Status(String exportId) throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        sr.getParameters().add("exportid", exportId);
        Document response = sr.callService("rustici.export.status");
        Element exportElem = (Element)response.getElementsByTagName("export").item(0);
        return Export.parseFromXmlElement(exportElem);
    }
    
    /// <summary>
    /// Calling this method returns the actual export data for a given export. Note that the export must 
    /// be complete in order to download it's associated data.
    /// </summary>
    /// <param name="toFileName">The file path to write the downloaded export to</param>
    /// <param name="export">An Export object containing information about the export to download</param>
    /// <returns>The filepath where the data was written</returns>
    public String Download(String toFileName, Export export) throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        sr.getParameters().add("exportid", export.getId());
        sr.setServer(export.getServerLocation());
        return sr.getFileFromService(toFileName, "rustici.export.download");
    }
    
    /// <summary>
    /// Calling this method returns a URL to the actual export data for a given export. Note that the export must 
    /// be complete in order to download it's associated data.
    /// </summary>
    /// <param name="export">An Export object containing information about the export to download</param>
    /// <returns>A URL from which the export data can be downloaded</returns>
    public String GetDownloadUrl(Export export) throws Exception
    {
    	ServiceRequest sr = new ServiceRequest(this.configuration);
    	sr.getParameters().add("exportid", export.getId());
    	sr.setServer(export.getServerLocation());
    	return sr.constructUrl("rustici.export.download");
    }
    
    /// <summary>
    /// This method returns a list of export data for all exports current and historical. 
    /// </summary>
    /// <returns></returns>
    public List<Export> List() throws Exception
    {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        Document doc = sr.callService("rustici.export.list");
        Element elem = (Element)doc.getElementsByTagName("exports").item(0);
        return Export.parseExportListFromXml(elem);
    }

}
