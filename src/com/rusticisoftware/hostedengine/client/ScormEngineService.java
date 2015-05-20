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

public class ScormEngineService
{
    private Configuration configuration = null;
    private LrsAccountService lrsAccountService = null;
    private CourseService courseService = null;
    private RegistrationService registrationService = null;
    private UploadService uploadService = null;
    private FtpService ftpService = null;
    private ExportService exportService = null;
    private ReportingService reportingService = null;
    private TaggingService taggingService = null;
    private DispatchService dispatchService = null;
    private DebugService debugService = null;
    private InvitationService invitationService = null;
    private StatementForwardService stmtFrwdService = null;

    /// <summary>
    /// SCORM Engine Service constructor that that takes the three required properties.
    /// </summary>
    /// <param name="scormEngineServiceUrl">URL to the service, ex: http://services.scorm.com/EngineWebServices</param>
    /// <param name="appId">The Application ID obtained by registering with the SCORM Engine Service</param>
    /// <param name="securityKey">The security key (password) linked to the application ID</param>
    /// <param name="origin">The origin string that defines the organization, application name and version</param>
    public ScormEngineService(String scormEngineServiceUrl, String appId, String securityKey, String origin)
    {
        this(new Configuration(scormEngineServiceUrl, appId, securityKey, origin));
    }

    /// <summary>
    /// SCORM Engine Service constructor that takes a single configuration parameter
    /// </summary>
    /// <param name="config">The Configuration object to be used to configure the Scorm Engine Service client</param>
    public ScormEngineService(Configuration config)
    {
        configuration = config;
        lrsAccountService = new LrsAccountService(configuration, this);
        courseService = new CourseService(configuration, this);
        registrationService = new RegistrationService(configuration, this);
        invitationService = new InvitationService(configuration, this);
        uploadService = new UploadService(configuration, this);
        ftpService = new FtpService(configuration, this);
        exportService = new ExportService(configuration, this);
        reportingService = new ReportingService(configuration, this);
        taggingService = new TaggingService(configuration, this);
        dispatchService = new DispatchService(configuration, this);
        debugService = new DebugService(configuration, this);
        stmtFrwdService = new StatementForwardService(configuration, this);
    }
    
    /// <summary>
    /// Contains SCORM Engine LRS account functionality.
    /// </summary>
    public LrsAccountService getAccountService()
    {
        return lrsAccountService;
    }

    /// <summary>
    /// Contains all SCORM Engine Package-level (i.e., course) functionality.
    /// </summary>
    public CourseService getCourseService()
    {
        return courseService;
    }

    /// <summary>
    /// Contains all SCORM Engine Package-level (i.e., course) functionality.
    /// </summary>
    public RegistrationService getRegistrationService()
    {
        return registrationService;
    }


    /// <summary>
    /// SCORM Cloud invitation functionality.
    /// </summary>
    public InvitationService getInvitationService()
    {
        return invitationService;
    }

    /// <summary>
    /// Contains all SCORM Engine Upload/File Management functionality.
    /// </summary>
    public UploadService getUploadService()
    {
        return uploadService;
    }

    /// <summary>
    /// Contains all SCORM Engine FTP Management functionality.
    /// </summary>
    public FtpService getFtpService()
    {
        return ftpService;
    }
    
    /// <summary>
    /// Contains all SCORM Engine Data Export functionality.
    /// </summary>
    public ExportService getExportService()
    {
        return exportService;
    }
    
    /// <summary>
    /// Contains SCORM Cloud reporting methods.
    /// </summary>
    public ReportingService getReportingService()
    {
        return reportingService;
    }
    
    /// <summary>
    /// Contains SCORM Cloud tagging methods.
    /// </summary>
    public TaggingService getTaggingService()
    {
        return taggingService;
    }
    
    /// <summary>
    /// Contains methods for accessing SCORM dispatch
    /// </summary>
    public DispatchService getDispatchService() {
		return dispatchService;
	}
    
    /// <summary>
    /// Contains methods for accessing Tin Can statement forwarding
    /// </summary>
    public StatementForwardService getStatementForwardService() {
        return stmtFrwdService;
    }

    /// <summary>
    /// Contains SCORM Cloud debug methods.
    /// </summary>
    public DebugService getDebugService(){
    	return debugService;
    }
    
    /// <summary>
    /// The Application ID obtained by registering with the SCORM Engine Service
    /// </summary>
    public String getAppId()
    {
        return configuration.getAppId();
    }

    /// <summary>
    /// set a new Application Id for service calls
    /// </summary>
    public ScormEngineService setAppId(String newAppId)
    {
        configuration.setAppId(newAppId);
        return this;
    }

    /// <summary>
    /// The security key (password) linked to the Application ID
    /// </summary>
    public String getSecurityKey()
    {
            return configuration.getSecurityKey();
    }

    /// <summary>
    /// The security key (password) linked to the Application ID
    /// </summary>
    public ScormEngineService setSecurityKey(String newSecurityKey)
    {
            configuration.setSecurityKey(newSecurityKey);
            return this;
    }

    /// <summary>
    /// URL to the service, ex: http://cloud.scorm.com/EngineWebServices
    /// </summary>
    public String getScormEngineServiceUrl()
    {
            return configuration.getScormEngineServiceUrl();
    }
    
    public String getOrigin()
    {
    	return configuration.getOrigin();
    }

    public ServiceRequest CreateNewRequest()
    {
        return new ServiceRequest(this.configuration);
    }

	
}