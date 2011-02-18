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
    /// Contains SCORM Cloud debug methods.
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
