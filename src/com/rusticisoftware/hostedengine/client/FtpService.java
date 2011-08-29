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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FtpService
{
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    public FtpService(Configuration configuration, ScormEngineService manager)
    {
        this.configuration = configuration;
        this.manager = manager;
    }

    /// <summary>
    /// Creates a new FTP User
    /// </summary>
    /// <param name="userId">User ID</param>
    /// <param name="userPass">User's Password</param>
    /// <param name="permissionDomain">permission domain for the new user</param>
    public void CreateUser(String userId, String userPass, String permissionDomain) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("userid", userId);
        request.getParameters().add("userpass", userPass);
        if (!Utils.isNullOrEmpty(permissionDomain))
            request.getParameters().add("pd", permissionDomain);
        request.callService("rustici.ftp.createUser");
    }

    /// <summary>
    /// Creates a new FTP User with access to the default permission domain
    /// </summary>
    /// <param name="userId">User ID</param>
    /// <param name="userPass">User's Password</param>
    public void CreateUser(String userId, String userPass) throws Exception
    {
        CreateUser(userId, userPass, null);
    }

    /// <summary>
    /// Delete an FTP User
    /// </summary>
    /// <param name="userId">User ID</param>
    public void DeleteUser(String userId) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("userid", userId);
        request.callService("rustici.ftp.deleteUser");
    }

    /// <summary>
    /// Create a new Permission Domain
    /// </summary>
    /// <param name="permissionDomain">Domain Name</param>
    public void CreatePermissionDomain(String permissionDomain) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("pd", permissionDomain);
        request.callService("rustici.ftp.createPermissionDomain");
    }

    /// <summary>
    /// Delete a Permission Domain
    /// </summary>
    /// <param name="permissionDomain">Domain Name</param>
    public void DeletePermissionDomain(String permissionDomain) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("pd", permissionDomain);
        request.callService("rustici.ftp.deletePermissionDomain");
    }

    /// <summary>
    /// Get a list of all of the configured AppId's permission domains
    /// </summary>
    /// <returns></returns>
    public List<String> GetDomainList() throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        Document response = request.callService("rustici.ftp.getDomainList");

        // Map the response to a dictionary of name/value pairs
        ArrayList<String> result = new ArrayList<String>();
        NodeList domainList = response.getElementsByTagName("domain");
        for(int i = 0; i < domainList.getLength(); i++){
            Element domainElem = (Element)domainList.item(0);
            result.add(domainElem.getAttribute("id"));
        }
        return result;
    }

    /// <summary>
    /// Associate a specific course with a particular permission domain
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <param name="permissionDomain">Domain Name</param>
    public void SetCourseDomain(String courseId, String permissionDomain) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("pd", permissionDomain);
        request.callService("rustici.ftp.setCourseDomain");
    }

    /// <summary>
    /// Associate a particular user with a Permission Domain
    /// </summary>
    /// <param name="userId">User ID</param>
    /// <param name="permissionDomain">Permission Domain</param>
    public void SetUserDomain(String userId, String permissionDomain) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("userid", userId);
        request.getParameters().add("pd", permissionDomain);
        request.callService("rustici.ftp.setUserDomain");
    }


    /// <summary>
    /// Set the ftp password for the user with the given userId
    /// </summary>
    /// <param name="userId">User ID</param>
    /// <param name="oldPassword">Current password</param>
    /// <param name="newPassword">New password</param>
    public void SetUserPassword(String userId, String oldPassword, String newPassword) throws Exception
    {
        SetUserPassword(userId, oldPassword, newPassword, false);
    }

    /// <summary>
    /// Set the ftp password for the user with the given userId
    /// </summary>
    /// <param name="userId">User ID</param>
    /// <param name="oldPassword">Current password</param>
    /// <param name="newPassword">New password</param>
    /// /// <param name="newPassword">Force password change</param>
    public void SetUserPassword(String userId, String oldPassword, String newPassword, boolean force) throws Exception
    {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("userid", userId);
        request.getParameters().add("oldpass", oldPassword);
        request.getParameters().add("newpass", newPassword);
        request.getParameters().add("force", String.valueOf(force));
        request.callService("rustici.ftp.setUserPassword");
    }

    // Others....
}
