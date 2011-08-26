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

public class ScormCloud
{
    protected static ScormEngineService _singleton;
    protected static ScormEngineService getService() throws Exception
    {
        if (_singleton == null) {
            if (_configuration == null) {
                throw new Exception("Attempted to use ScormCloud object before setting the configuration!");
            }
            _singleton = new ScormEngineService(_configuration);
        }
        return _singleton;
    }
    
    protected static Configuration _configuration;
    public static Configuration getConfiguration()
    {
        return _configuration;
    }
    public static void setConfiguration(Configuration value)
    {
        _configuration = value;
    }


    /// <summary>
    /// Contains all SCORM Engine Package-level (i.e., course) functionality.
    /// </summary>
    public static CourseService getCourseService() throws Exception
    {
        return getService().getCourseService();
    }

    /// <summary>
    /// Contains all SCORM Engine Package-level (i.e., course) functionality.
    /// </summary>
    public static RegistrationService getRegistrationService() throws Exception
    {
        return getService().getRegistrationService();
    }


    /// <summary>
    /// Contains all SCORM Engine Upload/File Management functionality.
    /// </summary>
    public static UploadService getUploadService() throws Exception
    {
        return getService().getUploadService();
    }

    /// <summary>
    /// Contains all SCORM Engine FTP Management functionality.
    /// </summary>
    public static FtpService getFtpService() throws Exception
    {
        return getService().getFtpService();
    }
    
    /// <summary>
    /// Contains all SCORM Engine Data Export functionality
    /// </summary>
    public static ExportService getExportService() throws Exception
    {
        return getService().getExportService();
    }
    
    /// <summary>
    /// Contains SCORM Cloud reporting methods.
    /// </summary>
    public static ReportingService getReportingService() throws Exception
    {
        return getService().getReportingService();
    }
    
    /// <summary>
    /// Contains SCORM Cloud tagging methods.
    /// </summary>
    public static TaggingService getTaggingService() throws Exception
    {
        return getService().getTaggingService();
    }
    
    /// <summary>
    /// Contains SCORM Cloud dispatch methods
    /// </summary>
    public static DispatchService getDispatchService() throws Exception
    {
    	return getService().getDispatchService();
    }
    
    /// <summary>
    /// Contains SCORM Cloud debug methods
    /// </summary>
    public static DebugService getDebugService() throws Exception 
    {
    	return getService().getDebugService();
    }

    /// <summary>
    /// The Application ID obtained by registering with the SCORM Engine Service
    /// </summary>
    public static String getAppId()
    {
        return getConfiguration().getAppId();
    }

    /// <summary>
    /// The security key (password) linked to the Application ID
    /// </summary>
    public static String getSecurityKey()
    {
        return getConfiguration().getSecurityKey();
    }

    /// <summary>
    /// URL to the service, ex: http://cloud.scorm.com/EngineWebServices
    /// </summary>
    public static String getScormEngineServiceUrl()
    {
        return getConfiguration().getScormEngineServiceUrl();
    }

    /// <summary>
    /// Create a new ScormCloud service request
    /// </summary>
    /// <returns></returns>
    public static ServiceRequest createNewRequest()
    {
        return new ServiceRequest(getConfiguration());
    }
}
