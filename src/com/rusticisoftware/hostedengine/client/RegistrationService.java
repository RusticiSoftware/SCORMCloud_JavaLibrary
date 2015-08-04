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

import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rusticisoftware.hostedengine.client.datatypes.Enums.RegistrationResultsAuthType;
import com.rusticisoftware.hostedengine.client.datatypes.LaunchInfo;
import com.rusticisoftware.hostedengine.client.datatypes.PostbackInfo;
import com.rusticisoftware.hostedengine.client.datatypes.RegistrationData;
import com.rusticisoftware.hostedengine.client.datatypes.RegistrationSummary;
import com.rusticisoftware.hostedengine.client.datatypes.Enums.*;

public class RegistrationService
{
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public RegistrationService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }

    /// <summary>
    /// Create a new Registration (Instance of a user taking a course)
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="versionId">Optional versionID, if Int32.MinValue, latest course version is used.</param>
    /// <param name="learnerId">Unique Identifier for the learner</param>
    /// <param name="learnerFirstName">Learner's first name</param>
    /// <param name="learnerLastName">Learner's last name</param>
    /// <param name="resultsPostbackUrl">URL to which the server will post results back to</param>
    /// <param name="authType">Type of Authentication used at results postback time</param>
    /// <param name="postBackLoginName">If postback authentication is used, the logon name</param>
    /// <param name="postBackLoginPassword">If postback authentication is used, the password</param>
    /// <param name="resultsFormat">The Format of the results XML sent to the postback URL</param>
    public void CreateRegistration(String registrationId, String courseId, int versionId, String learnerId, 
        String learnerFirstName, String learnerLastName, String resultsPostbackUrl, 
        RegistrationResultsAuthType authType, String postBackLoginName, String postBackLoginPassword,
        RegistrationResultsFormat resultsFormat) throws Exception
    {
        CreateRegistration(registrationId, courseId, versionId, 
        		learnerId, learnerFirstName, learnerLastName, null, 
        		resultsPostbackUrl, authType, postBackLoginName, 
        		postBackLoginPassword, null, null, null, resultsFormat);
    }
    
    /// <summary>
    /// Create a new Registration (Instance of a user taking a course)
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="versionId">Optional versionID, if Int32.MinValue, latest course version is used.</param>
    /// <param name="learnerId">Unique Identifier for the learner</param>
    /// <param name="learnerFirstName">Learner's first name</param>
    /// <param name="learnerLastName">Learner's last name</param>
    /// <param name="learnerLastName">Learner's email address</param>
    /// <param name="resultsPostbackUrl">URL to which the server will post results back to</param>
    /// <param name="authType">Type of Authentication used at results postback time</param>
    /// <param name="postBackLoginName">If postback authentication is used, the logon name</param>
    /// <param name="postBackLoginPassword">If postback authentication is used, the password</param>
    /// <param name="learnerTags">Comma separated list of learner tags</param>
    /// <param name="courseTags">Comma separated list of course tags</param>
    /// <param name="registrationTags">Comma separated list of registration tags</param>
    /// <param name="resultsFormat">The Format of the results XML sent to the postback URL</param>
    public void CreateRegistration(String registrationId, String courseId, int versionId, String learnerId, 
        String learnerFirstName, String learnerLastName, String email, String resultsPostbackUrl, 
        RegistrationResultsAuthType authType, String postBackLoginName, String postBackLoginPassword,
        String learnerTags, String courseTags, String registrationTags,
        RegistrationResultsFormat resultsFormat) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("fname", learnerFirstName);
        request.getParameters().add("lname", learnerLastName);
        request.getParameters().add("learnerid", learnerId);

        // Required on this signature but not by the actual service
        request.getParameters().add("authtype", authType.toString().toLowerCase());
        request.getParameters().add("resultsformat", resultsFormat.toString().toLowerCase());

        // Optional:
        if(!Utils.isNullOrEmpty(email))
        	request.getParameters().add("email", email);
        if (!Utils.isNullOrEmpty(resultsPostbackUrl))
            request.getParameters().add("postbackurl", resultsPostbackUrl);
        if (!Utils.isNullOrEmpty(postBackLoginName))
            request.getParameters().add("urlname", postBackLoginName);
        if (!Utils.isNullOrEmpty(postBackLoginPassword))
            request.getParameters().add("urlpass", postBackLoginPassword);
        if (versionId != Integer.MIN_VALUE)
            request.getParameters().add("versionid", versionId);

        if(!Utils.isNullOrEmpty(learnerTags)){
        	request.getParameters().add("learnerTags", learnerTags);
        }
        if(!Utils.isNullOrEmpty(courseTags)){
        	request.getParameters().add("courseTags", courseTags);
        }
        if(!Utils.isNullOrEmpty(registrationTags)){
        	request.getParameters().add("registrationTags", registrationTags);
        }

        request.callService("rustici.registration.createRegistration");
    }
    

    //TODO: Other overrides of createRegistration....

    /// <summary>
    /// Create a new Registration (Instance of a user taking a course)
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="learnerId">Unique Identifier for the learner</param>
    /// <param name="learnerFirstName">Learner's first name</param>
    /// <param name="learnerLastName">Learner's last name</param>
    public void CreateRegistration(String registrationId, String courseId, String learnerId,
        String learnerFirstName, String learnerLastName) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("fname", learnerFirstName);
        request.getParameters().add("lname", learnerLastName);
        request.getParameters().add("learnerid", learnerId);
        request.callService("rustici.registration.createRegistration");
    }
    
    /// <summary>
    /// Create a new Registration (Instance of a user taking a course)
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="learnerId">Unique Identifier for the learner</param>
    /// <param name="learnerFirstName">Learner's first name</param>
    /// <param name="learnerLastName">Learner's last name</param>
    /// <param name="email">Learner's email address</param>
    /// <param name="learnerTags">Comma separated list of learner tags</param>
    /// <param name="courseTags">Comma separated list of course tags</param>
    /// <param name="registrationTags">Comma separated list of registration tags</param>
    public void CreateRegistration(String registrationId, String courseId, String learnerId,
        String learnerFirstName, String learnerLastName, String email,
        String learnerTags, String courseTags, String registrationTags) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("fname", learnerFirstName);
        request.getParameters().add("lname", learnerLastName);
        request.getParameters().add("learnerid", learnerId);
        if(!Utils.isNullOrEmpty(email))
        	request.getParameters().add("email", email);
        if(!Utils.isNullOrEmpty(learnerTags)){
        	request.getParameters().add("learnerTags", learnerTags);
        }
        if(!Utils.isNullOrEmpty(courseTags)){
        	request.getParameters().add("courseTags", courseTags);
        }
        if(!Utils.isNullOrEmpty(registrationTags)){
        	request.getParameters().add("registrationTags", registrationTags);
        }
        request.callService("rustici.registration.createRegistration");
    }
       
    /// <summary>
    /// Create a new Registration (Instance of a user taking a course)
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="learnerId">Unique Identifier for the learner</param>
    /// <param name="learnerFirstName">Learner's first name</param>
    /// <param name="learnerLastName">Learner's last name</param>
    /// <param name="email">Learner's email address</param>
    public void CreateRegistration(String registrationId, String courseId, String learnerId,
        String learnerFirstName, String learnerLastName, String email) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("fname", learnerFirstName);
        request.getParameters().add("lname", learnerLastName);
        request.getParameters().add("learnerid", learnerId);
        if(!Utils.isNullOrEmpty(email))
        	request.getParameters().add("email", email);
        request.callService("rustici.registration.createRegistration");
    }

    /// <summary>
    /// Check to see if a registration exists
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <returns>A Boolean indicator of the registration existence </returns>
    public boolean RegistrationExists(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);       
	Document response = request.callService("rustici.registration.exists");
	Element rspElem = (Element)response.getElementsByTagName("rsp").item(0);
	return (Boolean)XmlUtils.getNamedElemValue(rspElem,"result",Boolean.class,false);
    }
       
    /// <summary>
    /// Creates a new instance of an existing registration.  This essentially creates a
    /// fresh take of a course by the user. The latest version of the associated course
    /// will be used.
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <returns>Instance ID of the newly created instance</returns>
//    public int CreateNewInstance (String registrationId) throws Exception
//    {
//        ServiceRequest request = new ServiceRequest(configuration);
//        request.getParameters().add("regid", registrationId);
//        Document response = request.callService("rustici.registration.createNewInstance");
//
//        NodeList successNodes = response.getElementsByTagName("success");
//        return Integer.parse(((Element)successNodes.item(0)).getAttribute("instanceid"));
//    }

    /// <summary>
    /// Return a registration summary object for the given registration
    /// </summary>
    /// <param name="registrationId">The unique identifier of the registration</param>
    /// <returns></returns>
    public RegistrationSummary GetRegistrationSummary(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("resultsformat", "course");
        request.getParameters().add("format", "xml");
        Document response = request.callService("rustici.registration.getRegistrationResult");
        Element reportElem = (Element)response.getElementsByTagName("registrationreport").item(0);
        return new RegistrationSummary(reportElem);
    }

    /// <summary>
    /// Returns the current state of the registration, including completion
    /// and satisfaction type data.  Amount of detail depends on format parameter.
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <returns>Registration data in XML Format</returns>
    public String GetRegistrationResult(String registrationId) throws Exception
    {
        return GetRegistrationResult(registrationId, RegistrationResultsFormat.COURSE_SUMMARY, DataFormat.XML);
    }
    
    /// <summary>
    /// Returns the current state of the registration, including completion
    /// and satisfaction type data.  Amount of detail depends on format parameter.
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="resultsFormat">Degree of detail to return</param>
    /// <returns>Registration data in XML Format</returns>
    public String GetRegistrationResult(String registrationId, RegistrationResultsFormat resultsFormat) throws Exception
    {
        return GetRegistrationResult(registrationId, resultsFormat, DataFormat.XML);
    }

    /// <summary>
    /// Returns the current state of the registration, including completion
    /// and satisfaction type data.  Amount of detail depends on format parameter.
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="resultsFormat">Degree of detail to return</param>
    /// <returns>Registration data in XML Format</returns>
    public String GetRegistrationResult(String registrationId, RegistrationResultsFormat resultsFormat, DataFormat dataFormat) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("resultsformat", resultsFormat.toString().toLowerCase());
        if (dataFormat == DataFormat.JSON)
            request.getParameters().add("format", "json");

        if(dataFormat == DataFormat.XML){
        	Document response = request.callService("rustici.registration.getRegistrationResult");
            // Return the subset of the xml starting with the top <summary>
            Node reportElem = response.getElementsByTagName("registrationreport").item(0);
            return XmlUtils.getXmlString(reportElem);
        } else {
            return request.getStringFromService("rustici.registration.getRegistrationResult");
        }
    }

    /// <summary>
    /// Returns the current state of the listed registrations, including completion
    /// and satisfaction type data.  Amount of detail depends on format parameter.
    /// </summary>
    /// <param name="courseId"> Limit search to only registrations for the course specified by this courseid</param>
    /// <param name="learnerId"> Limit search to only registrations for the learner specified by this learnerid</param>
    /// <param name="filter"> A regular expression that will be used to filter the list of registrations by registrationID.</param>
    /// <param name="courseFilter"> A regular express that will be used to filter the list of registrations by courseID. </param>
    /// <param name="resultsFormat"> One of three values, course, activity, or full.</param>
    /// <param name="after"> Return registrations updated (strictly) after this timestamp.</param>
    /// <param name="until"> Return registrations updated up to and including this timestamp.</param>
    /// <returns>Registration data in XML Format</returns>
    public List<RegistrationData> GetRegistrationListResults(String courseId, String learnerId, String filter, String courseFilter, 
					    RegistrationResultsFormat resultsFormat, Date after, Date until) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
	if (!Utils.isNullOrEmpty(courseId)){
        	request.getParameters().add("courseid", courseId);
	}
	if (!Utils.isNullOrEmpty(learnerId)){
        	request.getParameters().add("learnerid", learnerId);
	}
	if (!Utils.isNullOrEmpty(filter)){
        	request.getParameters().add("filter", filter);
	}
	if (!Utils.isNullOrEmpty(courseFilter)){
        	request.getParameters().add("coursefilter", courseFilter);
	}
	if (resultsFormat != null){
	    request.getParameters().add("resultsformat", resultsFormat.toString().toLowerCase());
	}
	if (after != null){
        	request.getParameters().add("after", after);
	}
	if (until != null){
        	request.getParameters().add("until", until);
	}
	Document response = request.callService("rustici.registration.getRegistrationListResults");
	// Return the subset of the xml starting with the top <summary>
	return RegistrationData.parseListFromXml(response);
    }

    /// <summary>
    /// Returns a list of registration id's along with their associated course
    /// </summary>
    /// <param name="regIdFilterRegex">Optional registration id filter</param>
    /// <param name="courseIdFilterRegex">Option course id filter</param>
    /// <returns></returns>
    public List<RegistrationData> GetRegistrationList(String regIdFilterRegex, String courseIdFilterRegex) throws Exception
    {
    	return GetRegistrationList(regIdFilterRegex, courseIdFilterRegex, null, null);
    }
    
    /// <summary>
    /// Returns a list of registration id's along with their associated course
    /// </summary>
    /// <param name="regIdFilterRegex">Optional registration id filter</param>
    /// <param name="courseIdFilterRegex">Option course id filter</param>
    /// <returns></returns>
    public List<RegistrationData> GetRegistrationList(String regIdFilterRegex, String courseIdFilterRegex, String courseId, String learnerId) throws Exception
    {
    	return GetRegistrationList(regIdFilterRegex, courseIdFilterRegex, courseId, learnerId, null, null);
    }

    /// <summary>
    /// Returns a list of registration id's along with their associated course
    /// </summary>
    /// <param name="regIdFilterRegex">Optional registration id filter</param>
    /// <param name="courseIdFilterRegex">Option course id filter</param>
    /// <
    /// <returns></returns>
    public List<RegistrationData> GetRegistrationList(String regIdFilterRegex, String courseIdFilterRegex, String courseId, String learnerId, Date after, Date until) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        if (!Utils.isNullOrEmpty(regIdFilterRegex))
            request.getParameters().add("filter", regIdFilterRegex);
        
        if (!Utils.isNullOrEmpty(courseId)){
        	request.getParameters().add("courseid", courseId);
        } else if (!Utils.isNullOrEmpty(courseIdFilterRegex)) {
            request.getParameters().add("coursefilter", courseIdFilterRegex);
        }
        
        if (!Utils.isNullOrEmpty(learnerId)){
        	request.getParameters().add("learnerid", learnerId);
        }
        
        if (after != null)
        	request.getParameters().add("after", Utils.getIsoDateString(after));
        if (until != null)
        	request.getParameters().add("until", Utils.getIsoDateString(until));

        Document response = request.callService("rustici.registration.getRegistrationList");

        // Return the subset of the xml starting with the top <summary>
        return RegistrationData.parseListFromXml(response);
    }

    /// <summary>
    /// Returns a list of all registration id's along with their associated course
    /// </summary>
    public List<RegistrationData> GetRegistrationList() throws Exception
    {
        return GetRegistrationList(null, null);
    }

    /// <summary>
    /// Check if a registration already exists
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <returns></returns>
    public String exists(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        Document response = request.callService("rustici.registration.exists");
        Element elem = (Element) response.getElementsByTagName("result").item(0);
        return elem.getTextContent();
    }
    
    /// <summary>
    /// Delete the specified registration
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="deleteLatestInstanceOnly">If false, all instances are deleted</param>
    public void DeleteRegistration(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.callService("rustici.registration.deleteRegistration");
    }


    /// <summary>
    /// Resets all status data regarding the specified registration -- essentially restarts the course
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    public void ResetRegistration(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.callService("rustici.registration.resetRegistration");
    }


    /// <summary>
    /// Clears global objective data for the given registration
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="deleteLatestInstanceOnly">If false, all instances are deleted</param>
    public void ResetGlobalObjectives(String registrationId, boolean deleteLatestInstanceOnly) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        if (deleteLatestInstanceOnly)
            request.getParameters().add("instanceid", "latest");
        request.callService("rustici.registration.resetGlobalObjectives");
    }

    /// <summary>
    /// Delete the specified instance of the registration
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="instanceId">Specific instance of the registration to delete</param>
    public void DeleteRegistrationInstance(String registrationId, int instanceId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("instanceid", instanceId);
        request.callService("rustici.registration.deleteRegistration");
    }

    /// <summary>
    /// Gets the url to directly launch/view the course registration in a browser
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="redirectOnExitUrl">Upon exit, the url that the SCORM player will redirect to</param>
    /// <returns>URL to launch</returns>
    public String GetLaunchUrl(String registrationId, String redirectOnExitUrl) throws Exception
    {
        return GetLaunchUrl(registrationId, redirectOnExitUrl, null, null);

    }
    
    /// <summary>
    /// Gets the url to directly launch/view the course registration in a browser
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="redirectOnExitUrl">Upon exit, the url that the SCORM player will redirect to</param>
    /// <param name="cssUrl">Absolute url that points to a custom player style sheet</param>
    /// <returns>URL to launch</returns>
    public String GetLaunchUrl(String registrationId, String redirectOnExitUrl, String cssUrl) throws Exception
    {
    	return GetLaunchUrl(registrationId, redirectOnExitUrl, cssUrl, null);
    }

    /// <summary>
    /// Gets the url to directly launch/view the course registration in a browser
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="redirectOnExitUrl">Upon exit, the url that the SCORM player will redirect to</param>
    /// <param name="cssUrl">Absolute url that points to a custom player style sheet</param>
    /// <param name="debugLogPointerUrl">Url that the server will postback a "pointer" url regarding
    /// a saved debug log that resides on s3</param>
    /// <returns>URL to launch</returns>
    public String GetLaunchUrl(String registrationId, String redirectOnExitUrl, String cssUrl, String debugLogPointerUrl) throws Exception
    {
    	return GetLaunchUrl(registrationId, redirectOnExitUrl, cssUrl, debugLogPointerUrl, false);
	}

    /// <summary>
    /// Gets the url to directly launch/view the course registration in a browser
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="redirectOnExitUrl">Upon exit, the url that the SCORM player will redirect to</param>
    /// <param name="cssUrl">Absolute url that points to a custom player style sheet</param>
    /// <param name="debugLogPointerUrl">Url that the server will postback a "pointer" url regarding
    /// a saved debug log that resides on s3</param>
    /// <returns>URL to launch</returns>
    public String GetLaunchUrl(String registrationId, String redirectOnExitUrl, String cssUrl, String debugLogPointerUrl, boolean disableTracking) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        if (!Utils.isNullOrEmpty(redirectOnExitUrl))
            request.getParameters().add("redirecturl", redirectOnExitUrl);
        if(!Utils.isNullOrEmpty(cssUrl))
        	request.getParameters().add("cssurl", cssUrl);
        if (!Utils.isNullOrEmpty(debugLogPointerUrl))
            request.getParameters().add("saveDebugLogPointerUrl", debugLogPointerUrl);
        if(disableTracking){
        	request.getParameters().add("disableTracking", "true");
        }

        return request.constructUrl("rustici.registration.launch");
    }
    
  /// <summary>
    /// Gets the url to directly launch/view the course registration in a browser
    /// </summary>
    /// <param name="registrationId">Unique Identifier for the registration</param>
    /// <param name="redirectOnExitUrl">Upon exit, the url that the SCORM player will redirect to</param>
    /// <param name="cssUrl">Absolute url that points to a custom player style sheet</param>
    /// <param name="debugLogPointerUrl">Url that the server will postback a "pointer" url regarding
    /// a saved debug log that resides on s3</param>
    /// <returns>URL to launch</returns>
    public String GetLaunchUrlWithTags(String registrationId, String redirectOnExitUrl, String cssUrl, String debugLogPointerUrl, String learnerTags, String courseTags, String registrationTags) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        if (!Utils.isNullOrEmpty(redirectOnExitUrl))
            request.getParameters().add("redirecturl", redirectOnExitUrl);
        if(!Utils.isNullOrEmpty(cssUrl))
        	request.getParameters().add("cssurl", cssUrl);
        if (!Utils.isNullOrEmpty(debugLogPointerUrl))
            request.getParameters().add("saveDebugLogPointerUrl", debugLogPointerUrl);
        if(!Utils.isNullOrEmpty(learnerTags)){
        	request.getParameters().add("learnerTags", learnerTags);
        }
        if(!Utils.isNullOrEmpty(courseTags)){
        	request.getParameters().add("courseTags", courseTags);
        }
        if(!Utils.isNullOrEmpty(registrationTags)){
        	request.getParameters().add("registrationTags", registrationTags);
        }
        return request.constructUrl("rustici.registration.launch");
    }

    /// <summary>
    /// Returns list of launch info objects, each of which describe a particular launch,
    /// but note, does not include the actual history log for the launch. To get launch
    /// info including the log, use GetLaunchInfo
    /// </summary>
    /// <param name="registrationId"></param>
    /// <returns></returns>
    public List<LaunchInfo> GetLaunchHistory(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        Document response = request.callService("rustici.registration.getLaunchHistory");
        Element launchHistory = ((Element)response.getElementsByTagName("launchhistory").item(0));
        return LaunchInfo.ConvertToLaunchInfoList(launchHistory);
    }

    /// <summary>
    /// Get the full launch information for the launch with the given launch id
    /// </summary>
    /// <param name="launchId"></param>
    /// <returns></returns>
    public LaunchInfo GetLaunchInfo(String launchId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("launchid", launchId);
        Document response = request.callService("rustici.registration.getLaunchInfo");
        Element launchInfoElem = ((Element)response.getElementsByTagName("launch").item(0));
        return new LaunchInfo(launchInfoElem);
    }
    
    public void UpdateLearnerInfo(String learnerId, String learnerFirstName, String learnerLastName) throws Exception {
    	UpdateLearnerInfo(learnerId, learnerFirstName, learnerLastName, null);
    }
    
    public void UpdateLearnerInfo(String learnerId, String learnerFirstName, String learnerLastName, String newLearnerId) throws Exception {
    	ServiceRequest request = new ServiceRequest(configuration);
    	request.getParameters().add("learnerid", learnerId);
    	request.getParameters().add("fname", learnerFirstName);
    	request.getParameters().add("lname", learnerLastName);
    	if(newLearnerId != null){
    		request.getParameters().add("newid", newLearnerId);
    	}
    	request.callService("rustici.registration.updateLearnerInfo");
    }

    /// <summary>
    /// Retrieve current postback info for a registration ID
    /// </summary>
    /// <param name="registrationId">Identifier of registration to be queried</param>
    /// <returns>PostbackInfo XML fragment</returns>
    public PostbackInfo GetPostbackInfo(String registrationId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        Document doc = request.callService("rustici.registration.getPostbackInfo");
        Element postbackInfoElem;
        try
        {
            postbackInfoElem = ((Element)doc.getElementsByTagName("postbackinfo").item(0));
            postbackInfoElem.getChildNodes().getLength();   // Just test that we got a non-null fragment
        }
        catch (NullPointerException e)
        {
            throw new ServiceException("Received malformed response from GetPostbackInfo");
        }

        return new PostbackInfo(postbackInfoElem);
    }
    
    /// <summary>
    /// update the postback URL only for an existing registration
    /// </summary>
    /// <param name="registrationId">Identifier of registration to be updated</param>
    /// <param name="postbackURL">New postback URL to be associated with this registration</param>
    /// <returns></returns>
    public void UpdatePostbackInfo(String registrationId, String postbackURL) throws Exception
    {
        UpdatePostbackInfo(registrationId, postbackURL, null, null, null, null);
    }

    /// <summary>
    /// update the postback URL and student authentication information for an existing registration
    /// </summary>
    /// <param name="registrationId">Identifier of registration to be updated</param>
    /// <param name="postbackURL">New postback URL to be associated with this registration</param>
    /// <param name="name">Login name for student</param>
    /// <param name="password">Password for student</param>
    /// <param name="authType">Authentication type [FORM, HTTPBASIC] for login</param>
    /// <returns></returns>
    public void UpdatePostbackInfo(String registrationId, String postbackURL,
            String name, String password, RegistrationResultsAuthType authType) throws Exception 
    {
        UpdatePostbackInfo(registrationId, postbackURL, name, password, authType, null);
    }

    /// <summary>
    /// update the postback URL, student authentication information, and requested format for results for an existing registration
    /// </summary>
    /// <param name="registrationId">Identifier of registration to be updated</param>
    /// <param name="postbackURL">New postback URL to be associated with this registration</param>
    /// <param name="name">Login name for student</param>
    /// <param name="password">Password for student</param>
    /// <param name="authType">Authentication type [FORM, HTTPBASIC] for login</param>
    /// <param name="resultsFormat">Rreuested format for reported results [COURSE_SUMMARY, ACTIVITY_SUMMARY, FULL_DETAIL]</param>
    /// <returns></returns>
    public void UpdatePostbackInfo(String registrationId, String postbackURL, String name, String password,
            RegistrationResultsAuthType authType, RegistrationResultsFormat resultsFormat) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("regid", registrationId);
        request.getParameters().add("url", postbackURL);
        if (!Utils.isNullOrEmpty(name))
            request.getParameters().add("name", name);
        if (!Utils.isNullOrEmpty(password))
            request.getParameters().add("password", password);
        if (authType != null)
            request.getParameters().add("authtype", authType.toString());
        if (resultsFormat != null)
            request.getParameters().add("resultsformat", resultsFormat.toString());
        
        request.setUsePost(true);
        request.callService("rustici.registration.updatePostbackInfo");
    }

    public void TestRegistrationPostUrl(String resultsPostbackUrl) throws Exception
    {
	TestRegistrationPostUrl(resultsPostbackUrl, null, null, null, null);
    }

    public void TestRegistrationPostUrl(String resultsPostbackUrl, 
					RegistrationResultsAuthType authType, String postBackLoginName, 
					String postBackLoginPassword) throws Exception 
    {
	TestRegistrationPostUrl(resultsPostbackUrl, authType, postBackLoginPassword, postBackLoginPassword, null);
    }

    public void TestRegistrationPostUrl(String resultsPostbackUrl, RegistrationResultsFormat resultsFormat) throws Exception
    {
	TestRegistrationPostUrl(resultsPostbackUrl, null, null, null, resultsFormat);
    }

    
    public void TestRegistrationPostUrl(String resultsPostbackUrl, 
            RegistrationResultsAuthType authType, String postBackLoginName, String postBackLoginPassword,
            RegistrationResultsFormat resultsFormat) throws Exception
        {
            ServiceRequest request = new ServiceRequest(configuration);

            // Required on this signature and by the service
	    request.getParameters().add("postbackurl", resultsPostbackUrl);

            // Optional:
            if (authType!=null)
		request.getParameters().add("authtype", authType.toString().toLowerCase());
            if (!Utils.isNullOrEmpty(postBackLoginName))
                request.getParameters().add("urlname", postBackLoginName);
            if (!Utils.isNullOrEmpty(postBackLoginPassword))
                request.getParameters().add("urlpass", postBackLoginPassword);
            if (resultsFormat!=null)
		request.getParameters().add("resultsformat", resultsFormat.toString().toLowerCase());
            request.callService("rustici.registration.testRegistrationPostUrl");
        }

	public RegistrationData GetRegistrationDetail(String registrationId) throws Exception {
		ServiceRequest sr = new ServiceRequest(configuration);
		sr.getParameters().add("regid", registrationId);
		Document xmlDoc = sr.callService("rustici.registration.getRegistrationDetail");
		return RegistrationData.parseFromXmlElement(
						XmlUtils.getFirstChildByTagName(
								xmlDoc.getDocumentElement(), "registration"));
	}
}
