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

import com.rusticisoftware.hostedengine.client.datatypes.AsyncImportStatus;
import com.rusticisoftware.hostedengine.client.datatypes.CourseData;
import com.rusticisoftware.hostedengine.client.datatypes.CourseMetadata;
import com.rusticisoftware.hostedengine.client.datatypes.Enums.MetadataFormat;
import com.rusticisoftware.hostedengine.client.datatypes.Enums.MetadataScope;
import com.rusticisoftware.hostedengine.client.datatypes.ImportResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseService {
    private Configuration configuration = null;
    private ScormEngineService manager = null;

    /// <summary>
    /// Main constructor that provides necessary configuration information
    /// </summary>
    /// <param name="configuration">Application Configuration Data</param>
    public CourseService(Configuration configuration, ScormEngineService manager) {
        this.configuration = configuration;
        this.manager = manager;
    }

    /// <summary>
    /// Check for the existence of a course on SCORM Cloud
    /// </summary>
    public boolean Exists(String courseId) throws Exception {
        ServiceRequest sr = new ServiceRequest(this.configuration);
        sr.getParameters().add("courseid", courseId);
        Document response = sr.callService("rustici.course.exists");
        Element resultElem = (Element) (response.getElementsByTagName("result").item(0));
        return Boolean.parseBoolean(resultElem.getTextContent());
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from the local filesystem.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="absoluteFilePathToZip">Full path to the .zip file</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportCourse(String courseId, String absoluteFilePathToZip) throws Exception {
        return ImportCourse(courseId, absoluteFilePathToZip, null);
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from the local filesystem.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="absoluteFilePathToZip">Full path to the .zip file</param>
    /// <param name="itemIdToImport">ID of manifest item to import</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportCourse(String courseId, String absoluteFilePathToZip, String itemIdToImport) throws Exception {
        return ImportCourse(courseId, absoluteFilePathToZip, itemIdToImport, null, null);
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from the local filesystem.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="absoluteFilePathToZip">Full path to the .zip file</param>
    /// <param name="itemIdToImport">ID of manifest item to import. If null, root organization is imported</param>
    /// <param name="permissionDomain">An permission domain to associate this course with, 
    /// for ftp access service (see ftp service below). 
    /// If the domain specified does not exist, the course will be placed in the default permission domain</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportCourse(String courseId, String absoluteFilePathToZip,
                                           String itemIdToImport, String permissionDomain, String userEmail) throws Exception {
        return this.ImportCourse(courseId, absoluteFilePathToZip, itemIdToImport, permissionDomain, userEmail, true);
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from an existing .zip file on the
    /// Hosted SCORM Engine server.
    /// </summary>
    public List<ImportResult> ImportCourse(String courseId, String absoluteFilePathToZip, String itemIdToImport,
                                           String permissionDomain, String userEmail, boolean useAsync) throws Exception {
        if (useAsync) {
            String importToken = this.ImportCourseAsync(courseId, absoluteFilePathToZip, itemIdToImport, permissionDomain, userEmail, true);
            return GetAsyncImportResults(importToken);
        } else {
            ServiceRequest request = new ServiceRequest(configuration);
            request.getParameters().add("courseid", courseId);
            if (!(itemIdToImport == null || itemIdToImport == "")) {
                request.getParameters().add("itemid", itemIdToImport);
            }
            if (!Utils.isNullOrEmpty(permissionDomain)) {
                request.getParameters().add("pd", permissionDomain);
            }
            if (!Utils.isNullOrEmpty(userEmail)) {
                request.getParameters().add("email", userEmail);
            }
            request.setFileToPost(absoluteFilePathToZip);

            Document response = request.callService("rustici.course.importCourse");
            return ImportResult.ConvertToImportResults(response);
        }
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from an existing .zip file on the
    /// Hosted SCORM Engine server.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="path">The relative path (rooted at your specific appid's upload area) 
    /// where the zip file for importing can be found</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportUploadedCourse(String courseId, String path) throws Exception {
        return ImportUploadedCourse(courseId, path, null, null, null, true);
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from an existing .zip file on the
    /// Hosted SCORM Engine server.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="path">The relative path (rooted at your specific appid's upload area)
    /// where the zip file for importing can be found</param>
    /// <param name="itemIdToImport">ID of manifest item to import</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportUploadedCourse(String courseId, String path, String itemIdToImport) throws Exception {
        return ImportUploadedCourse(courseId, path, itemIdToImport, null, null, true);
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from an existing .zip file on the
    /// Hosted SCORM Engine server.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="path">The relative path (rooted at your specific appid's upload area)
    /// where the zip file for importing can be found</param>
    /// <param name="itemIdToImport">ID of manifest item to import</param>
    /// <param name="permissionDomain">An permission domain to associate this course with, 
    /// for ftp access service (see ftp service below). 
    /// If the domain specified does not exist, the course will be placed in the default permission domain</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportUploadedCourse(String courseId, String path, String itemIdToImport,
                                                   String permissionDomain, String userEmail) throws Exception {
        return ImportUploadedCourse(courseId, path, itemIdToImport, permissionDomain, userEmail, true);
    }

    /// <summary>
    /// Import a SCORM .pif (zip file) from an existing .zip file on the
    /// Hosted SCORM Engine server.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="path">The relative path (rooted at your specific appid's upload area)
    /// where the zip file for importing can be found</param>
    /// <param name="itemIdToImport">ID of manifest item to import</param>
    /// <param name="permissionDomain">An permission domain to associate this course with,
    /// for ftp access service (see ftp service below).
    /// If the domain specified does not exist, the course will be placed in the default permission domain</param>
    /// <param name="useAsync">Use async import</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> ImportUploadedCourse(String courseId, String path, String itemIdToImport,
                                                   String permissionDomain, String userEmail, boolean useAsync) throws Exception {
        if (useAsync) {
            String importToken = this.ImportUploadedCourseAsync(courseId, path, itemIdToImport, permissionDomain, userEmail, false);
            AsyncImportStatus result = null;

            int sleepTime = 2000;
            int maxTries = 60;
            int tryNumber = 0;
            while (tryNumber < maxTries) {
                result = this.GetAsyncImportStatus(importToken);
                if (result.IsComplete()) {
                    break;
                }
                //No result yet, sleep, and retry
                Thread.sleep(sleepTime);
                if (sleepTime < 30000) {
                    sleepTime = (int) (sleepTime * 1.6);
                }
                tryNumber = tryNumber + 1;
            }

            if (result == null) {
                throw new ServiceException("Async import never returned anything!");
            }
            if (result.HasError()) {
                throw new ServiceException(result.getErrorCode(), result.getErrorMessage());
            } else {
                return result.getImportResults();
            }
        } else {
            ServiceRequest request = new ServiceRequest(configuration);
            request.getParameters().add("courseid", courseId);
            request.getParameters().add("path", path);
            if (!(itemIdToImport == null || itemIdToImport == "")) {
                request.getParameters().add("itemid", itemIdToImport);
            }
            if (!Utils.isNullOrEmpty(permissionDomain)) {
                request.getParameters().add("pd", permissionDomain);
            }
            if (!Utils.isNullOrEmpty(userEmail)) {
                request.getParameters().add("email", userEmail);
            }
            Document response = request.callService("rustici.course.importCourse");
            return ImportResult.ConvertToImportResults(response);
        }
    }

    public List<ImportResult> GetAsyncUpdateResults(String updateToken) throws Exception {
        return GetAsyncImportResults(updateToken);
    }

    public List<ImportResult> GetAsyncImportResults(String importToken) throws Exception {
        AsyncImportStatus result = null;

        int sleepTime = 2000;
        int maxTries = 60;
        int tryNumber = 0;
        while (tryNumber < maxTries) {
            result = this.GetAsyncImportStatus(importToken);
            if (result.IsComplete()) {
                break;
            }
            //No result yet, sleep, and retry
            Thread.sleep(sleepTime);
            if (sleepTime < 30000) {
                sleepTime = (int) (sleepTime * 1.6);
            }
            tryNumber = tryNumber + 1;
        }

        if (result == null) {
            throw new ServiceException("Async import never returned anything!");
        }
        if (result.HasError()) {
            throw new ServiceException(result.getErrorCode(), result.getErrorMessage());
        } else {
            return result.getImportResults();
        }
    }

    public String ImportCourseAsync(String courseId, String absoluteFilePath, String itemIdToImport,
                                    String permissionDomain, String userEmail, boolean finalizeSynchronously) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (!Utils.isNullOrEmpty(itemIdToImport)) {
            request.getParameters().add("itemid", itemIdToImport);
        }
        if (!Utils.isNullOrEmpty(permissionDomain)) {
            request.getParameters().add("pd", permissionDomain);
        }
        if (!Utils.isNullOrEmpty(userEmail)) {
            request.getParameters().add("email", userEmail);
        }
        if (finalizeSynchronously) {
            request.getParameters().add("finalize", true);
        }
        request.setFileToPost(absoluteFilePath);


        Document response = request.callService("rustici.course.importCourseAsync");
        String tokenId = response
                             .getElementsByTagName("token").item(0)
                             .getFirstChild().getTextContent();
        return tokenId;
    }


    public String ImportUploadedCourseAsync(String courseId, String path, String itemIdToImport,
                                            String permissionDomain, String userEmail, boolean finalizeSynchronously) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("path", path);
        if (!Utils.isNullOrEmpty(itemIdToImport)) {
            request.getParameters().add("itemid", itemIdToImport);
        }
        if (!Utils.isNullOrEmpty(permissionDomain)) {
            request.getParameters().add("pd", permissionDomain);
        }
        if (!Utils.isNullOrEmpty(userEmail)) {
            request.getParameters().add("email", userEmail);
        }
        if (finalizeSynchronously) {
            request.getParameters().add("finalize", true);
        }
        Document response = request.callService("rustici.course.importCourseAsync");
        String tokenId = response
                             .getElementsByTagName("token").item(0)
                             .getFirstChild().getTextContent();
        return tokenId;
    }


    public AsyncImportStatus GetAsyncUpdateStatus(String tokenId) throws Exception {
        return GetAsyncImportStatus(tokenId);
    }

    public AsyncImportStatus GetAsyncImportStatus(String tokenId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("token", tokenId);
        Document response = request.callService("rustici.course.getAsyncImportResult");
        return new AsyncImportStatus(response);
    }


    /// <summary>
    /// Import new version of an existing course from a SCORM .pif (zip file)
    /// on the local filesystem.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="absoluteFilePathToZip">Full path to the .zip file</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> VersionCourse(String courseId, String absoluteFilePathToZip) throws Exception {
        String location = manager.getUploadService().UploadFile(absoluteFilePathToZip, null);
        List<ImportResult> results = null;
        try {
            results = VersionUploadedCourse(courseId, location);
        } finally {
            manager.getUploadService().DeleteFile(location);
        }
        return results;
    }

    /// <summary>
    /// Import new version of an existing course from a SCORM .pif (zip file) from 
    /// an existing .zip file on the Hosted SCORM Engine server.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="path">Path to file, relative to your upload root</param>
    /// <returns>List of Import Results</returns>
    public List<ImportResult> VersionUploadedCourse(String courseId, String path) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("path", path);
        Document response = request.callService("rustici.course.versionCourse");
        return ImportResult.ConvertToImportResults(response);
    }

    /// <summary>
    /// Delete the specified course
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    public void DeleteCourse(String courseId) throws Exception {
        DeleteCourse(courseId, false, null);
    }

    /// <summary>
    /// Delete the specified course
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="deleteLatestVersionOnly">If false, all versions are deleted</param>
    public void DeleteCourse(String courseId, boolean deleteLatestVersionOnly, String userEmail) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (deleteLatestVersionOnly) {
            request.getParameters().add("versionid", "latest");
        }
        if (userEmail != null) {
            request.getParameters().add("email", userEmail);
        }
        request.callService("rustici.course.deleteCourse");
    }

    /// <summary>
    /// Delete the specified version of a course
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="versionId">Specific version of course to delete</param>
    public void DeleteCourseVersion(String courseId, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        request.getParameters().add("versionid", versionId);
        request.callService("rustici.course.deleteCourse");
    }

    /// <summary>
    /// Retrieve a list of high-level data about all courses owned by the 
    /// configured appId that meet the filter's criteria.
    /// </summary>
    /// <param name="courseIdFilterRegex">Regular expresion to filter the courses by ID</param>
    /// <returns>List of Course Data objects</returns>
    public List<CourseData> GetCourseList(String courseIdFilterRegex, List<String> tagList) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        if (!Utils.isNullOrEmpty(courseIdFilterRegex)) {
            request.getParameters().add("filter", courseIdFilterRegex);
        }
        if (tagList != null && tagList.size() > 0) {
            request.getParameters().add("tags", Utils.join(tagList, ","));
        }
        Document response = request.callService("rustici.course.getCourseList");
        return CourseData.parseListFromXml(response);
    }


    /// <summary>
    /// Retrieve a list of high-level data about all courses owned by the 
    /// configured appId.
    /// </summary>
    /// <returns>List of Course Data objects</returns>
    public List<CourseData> GetCourseList() throws Exception {
        return GetCourseList(null, null);
    }

    /// <summary>
    /// Retrieve the list of course attributes associated with this course.  If
    /// multiple versions of the course exist, the attributes of the latest version
    /// are returned.
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <returns>HashMap of all attributes associated with this course</returns>
    public HashMap<String, String> GetAttributes(String courseId) throws Exception {
        return GetAttributes(courseId, Integer.MIN_VALUE);
    }

    /// <summary>
    /// Retrieve the list of course attributes associated with a specific version
    /// of the specified course.
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="versionId">Specific version the specified course</param>
    /// <returns>HashMap of all attributes associated with this course</returns>
    public HashMap<String, String> GetAttributes(String courseId, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }
        Document response = request.callService("rustici.course.getAttributes");

        // Map the response to a dictionary of name/value pairs
        HashMap<String, String> attributeHashMap = new HashMap<String, String>();
        NodeList attrNodeList = response.getElementsByTagName("attribute");
        for (int i = 0; i < attrNodeList.getLength(); i++) {
            Element attrElem = (Element) attrNodeList.item(i);
            attributeHashMap.put(attrElem.getAttribute("name"), attrElem.getAttribute("value"));
        }
        return attributeHashMap;
    }

    /// <summary>
    /// Update the specified attributes (name/value pairs)
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="versionId">Specific version the specified course</param>
    /// <param name="attributePairs">Map of name/value pairs</param>
    /// <returns>HashMap of changed attributes</returns>
    public HashMap<String, String> UpdateAttributes(String courseId, int versionId,
                                                    Map<String, String> attributePairs) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }

        for (String key : attributePairs.keySet()) {
            if (!Utils.isNullOrEmpty(attributePairs.get(key))) {
                request.getParameters().add(key, attributePairs.get(key));
            }
        }

        Document response = request.callService("rustici.course.updateAttributes");

        // Map the response to a dictionary of name/value pairs.  This list
        // should contain only those values that have changed.  If a param was 
        // specified who's value is the same as the current value, it will not
        // be included in this list.
        HashMap<String, String> attributeHashMap = new HashMap<String, String>();
        NodeList attrNodeList = response.getElementsByTagName("attribute");
        for (int i = 0; i < attrNodeList.getLength(); i++) {
            Element attrElem = (Element) attrNodeList.item(i);
            attributeHashMap.put(attrElem.getAttribute("name"), attrElem.getAttribute("value"));
        }
        return attributeHashMap;
    }

    /// <summary>
    /// Update the specified attributes (name/value pairs) for the specified
    /// course.  If multiple versions of the course exist, only the latest
    /// version's attributes will be updated.
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="attributePairs">Map of name/value pairs</param>
    /// <returns>HashMap of changed attributes</returns>
    public HashMap<String, String> UpdateAttributes(String courseId, Map<String, String> attributePairs) throws Exception {
        return UpdateAttributes(courseId, Integer.MIN_VALUE, attributePairs);
    }

    /// <summary>
    /// Get the Course Metadata in XML Format
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="versionId">Version of the specified course</param>
    /// <param name="scope">Defines the scope of the data to return: Course or Activity level</param>
    /// <param name="format">Defines the amount of data to return:  Summary or Detailed</param>
    /// <returns>XML String representing the Metadata</returns>
    public CourseMetadata GetMetadata(String courseId, int versionId, MetadataScope scope, MetadataFormat format) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }
        request.getParameters().add("scope", scope.toString().toLowerCase());
        request.getParameters().add("mdformat", format.toString().toLowerCase());
        Document response = request.callService("rustici.course.getMetadata");

        // Return the subset of the xml starting with the top <object>
        return CourseMetadata.parseFromXmlElement((Element) (response.getElementsByTagName("package").item(0)));
    }

    /// <summary>
    /// Get the Course Metadata in XML Format.
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="scope">Defines the scope of the data to return: Course or Activity level</param>
    /// <param name="format">Defines the amount of data to return:  Summary or Detailed</param>
    /// <returns>XML String representing the Metadata</returns>

    public CourseMetadata GetMetadata(String courseId, MetadataScope scope, MetadataFormat format) throws Exception {
        return GetMetadata(courseId, Integer.MIN_VALUE, scope, format);
    }

    /// <summary>
    /// Update course files only.  One or more course files can be updating them by
    /// including them in a .zip file and sending updates via this method.  I
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <param name="absoluteFilePathToZip">Full path to the .zip file</param>
    /// <remarks>If multiple versions of a course exist, only the latest version's assets will
    /// be updated.</remarks>
    public List<ImportResult> UpdateAssets(String courseId, String absoluteFilePathToZip, String userEmail, boolean useAsync) throws Exception {
        if (useAsync) {
            String token = this.UpdateAssetsAsync(courseId, absoluteFilePathToZip, userEmail, true);
            return GetAsyncImportResults(token);
        } else {
            ServiceRequest request = new ServiceRequest(configuration);
            request.getParameters().add("courseid", courseId);
            request.setFileToPost(absoluteFilePathToZip);
            Document resp = request.callService("rustici.course.updateAssets");
            return ImportResult.ConvertToImportResults(resp);
        }
    }

    public String UpdateAssetsAsync(String courseId, String absoluteFilePathToZip, String userEmail, boolean finalizeSynchronously) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (!Utils.isNullOrEmpty(userEmail)) {
            request.getParameters().add("email", userEmail);
        }
        if (finalizeSynchronously) {
            request.getParameters().add("finalize", true);
        }
        request.setFileToPost(absoluteFilePathToZip);

        Document response = request.callService("rustici.course.updateAssetsAsync");
        String tokenId = response
                             .getElementsByTagName("token").item(0)
                             .getFirstChild().getTextContent();
        return tokenId;
    }

    /// <summary>
    /// Update course files only.  One or more course files can be updating them by
    /// including them in a .zip file and sending updates via this method.  The
    /// specified file should already exist in the upload domain space.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="versionId">Specific version of the course</param>
    /// <param name="domain">Optional security domain for the file.</param>
    /// <param name="fileName">Name of the file, including extension.</param>
    public List<ImportResult> UpdateAssetsFromUploadedFile(String courseId, int versionId, String path) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }
        request.getParameters().add("path", path);
        Document resp = request.callService("rustici.course.updateAssets");
        return ImportResult.ConvertToImportResults(resp);
    }

    /// <summary>
    /// Update course files only.  One or more course files can be updating them by
    /// including them in a .zip file and sending updates via this method.  The
    /// specified file should already exist in the upload domain space.  
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="domain">Optional security domain for the file.</param>
    /// <param name="fileName">Name of the file, including extension.</param>
    /// <remarks>If multiple versions of a course exist, only the latest version's assets will
    /// be updated.</remarks>
    public List<ImportResult> UpdateAssetsFromUploadedFile(String courseId, String path) throws Exception {
        return UpdateAssetsFromUploadedFile(courseId, Integer.MIN_VALUE, path);
    }

    /// <summary>
    /// Delete one or more files from the specified course directory
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="versionId">Version ID of the specified course</param>
    /// <param name="relativeFilePaths">Path of each file to delete realtive to the course root</param>
    /// <returns>Map of results as a HashMap of booleans</returns>
    public HashMap<String, Boolean> DeleteFiles(String courseId, int versionId, List<String> relativeFilePaths) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }

        for (String fileName : relativeFilePaths) {
            request.getParameters().add("path", fileName);
        }

        Document response = request.callService("rustici.course.deleteFiles");

        HashMap<String, Boolean> attributeHashMap = new HashMap<String, Boolean>();
        NodeList attrNodeList = response.getElementsByTagName("attribute");
        for (int i = 0; i < attrNodeList.getLength(); i++) {
            Element attrElem = (Element) attrNodeList.item(i);
            attributeHashMap.put(attrElem.getAttribute("name"),
                Boolean.parseBoolean(attrElem.getAttribute("value")));
        }
        return attributeHashMap;
    }

    /// <summary>
    /// Delete one or more files from the specified course directory. 
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="relativeFilePaths">Path of each file to delete realtive to the course root</param>
    /// <returns>Map of results as a HashMap of booleans</returns>
    /// <remarks>If  multiple versions of a course exist, only files from the latest version
    /// will be deleted.</remarks>
    public HashMap<String, Boolean> DeleteFiles(String courseId, List<String> relativeFilePaths) throws Exception {
        return DeleteFiles(courseId, Integer.MIN_VALUE, relativeFilePaths);
    }


    /// <summary>
    /// Get the file structure of the given course.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <param name="versionId">Version ID of the specified course</param>
    /// <returns>XML String of the hierarchical file structure of the course</returns>
    public String GetFileStructure(String courseId, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }
        Document response = request.callService("rustici.course.getFileStructure");

        // Return the subset of the xml starting with the top <dir>
        return XmlUtils.getXmlString(response.getElementsByTagName("dir").item(0));
    }

    /// <summary>
    /// Get the file structure of the given course.
    /// </summary>
    /// <param name="courseId">Unique Identifier for this course.</param>
    /// <returns>XML String of the hierarchical file structure of the course</returns>
    /// <remarks>If multiple versions of the course exist, the latest version's
    /// files structure will be retured.</remarks>
    public String GetFileStructure(String courseId) throws Exception {
        return GetFileStructure(courseId, Integer.MIN_VALUE);
    }

    /// <summary>
    /// Gets the url to view/edit the package properties for this course.  Typically
    /// used within an IFRAME
    /// </summary>
    /// <param name="courseId">Unique Identifier for the course</param>
    /// <returns>Signed URL to package property editor</returns>
    public String GetPropertyEditorUrl(String courseId) throws Exception {
        return GetPropertyEditorUrl(courseId, null, -1);
    }

    public String GetPropertyEditorUrl(String courseId, String stylesheetUrl) throws Exception {
        return GetPropertyEditorUrl(courseId, stylesheetUrl, -1);
    }

    public String GetPropertyEditorUrl(String courseId, String stylesheetUrl, int editorVersion) throws Exception {
        String editorVersionStr = "latest";
        if (editorVersion > 0) {
            editorVersionStr = String.valueOf(editorVersion);
        }

        // The local parameter map just contains method methodgetParameters().  We'll
        // now create a complete parameter map that contains the web-service
        // params as well the actual method params.
        ServiceRequest.ParameterMap parameterMap = new ServiceRequest.ParameterMap();
        parameterMap.add("action", "properties.view");
        parameterMap.add("package", "ApiCourseId|" + courseId);
        parameterMap.add("appid", configuration.getAppId());
        parameterMap.add("editor", editorVersionStr);
        parameterMap.add("ts", Utils.getFormattedTime(new Date()));
        if (!Utils.isNullOrEmpty(stylesheetUrl)) {
            parameterMap.add("stylesheet", stylesheetUrl);
        }

        String sig = RequestSigner.getSignatureForRequest(parameterMap, this.configuration.getSecurityKey());
        parameterMap.put("sig", sig);

        StringBuilder paramStr = new StringBuilder();
        for (String paramName : parameterMap.keySet()) {
            String[] vals = parameterMap.get(paramName);
            if (vals == null) {
                continue;
            }
            for (String val : vals) {
                if (val != null) {
                    paramStr.append(Utils.getEncodedParam(paramName, val) + "&");
                }
            }
        }

        //Cut off trailing ampersand
        paramStr.deleteCharAt(paramStr.length() - 1);
        return configuration.getScormEngineServiceUrl() + "/widget?" + paramStr.toString();
    }

    public String GetAssets(String toFileName, String courseId) throws Exception {
        return GetAssets(toFileName, courseId, null, Integer.MIN_VALUE);
    }

    public String GetAssets(String toFileName, String courseId, String path, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (path != null) {
            request.getParameters().add("path", path);
        }
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }

        //Return file path to downloaded file
        return request.getFileFromService(toFileName, "rustici.course.getAssets");
    }

    /// <summary>
    /// Get the url that points directly to a course asset
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <param name="path">Path to asset from root of course</param>
    /// <param name="versionId">Specific Version</param>
    /// <returns>HTTP Url to Asset</returns>
    public String GetAssetUrl(String courseId, String path, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (path != null) {
            request.getParameters().add("path", path);
        }
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }

        return request.constructUrl("rustici.course.getAssets");
    }

    /// <summary>
    /// Get the url that points directly to a course asset
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <param name="path">Path to asset from root of course</param>
    /// <returns>HTTP Url to Asset</returns>
    public String GetAssetUrl(String courseId, String path) throws Exception {
        return GetAssetUrl(courseId, path, Integer.MIN_VALUE);
    }

    public String GetManifest(String toFileName, String courseId) throws Exception {
        return GetManifest(toFileName, courseId, Integer.MIN_VALUE);
    }

    public String GetManifest(String toFileName, String courseId, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }

        //Return file path to downloaded file
        return request.getFileFromService(toFileName, "rustici.course.getManifest");
    }

    /// <summary>
    /// Get the url that points directly to the course imsmanifest.xml
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <returns>HTTP Url to imsmanifest.xml</returns>
    public String GetManifestUrl(String courseId) throws Exception {
        return GetManifestUrl(courseId, Integer.MIN_VALUE);
    }

    /// <summary>
    /// Get the url that points directly to the course imsmanifest.xml
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <param name="versionId">Specific Version</param>
    /// <returns>HTTP Url to imsmanifest.xml</returns>
    public String GetManifestUrl(String courseId, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (versionId != Integer.MIN_VALUE) {
            request.getParameters().add("versionid", versionId);
        }

        return request.constructUrl("rustici.course.getManifest");
    }

    /// <summary>
    /// Get the url that can be opened in a browser and used to preview this course, without
    /// the need for a registration.
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    public String GetPreviewUrl(String courseId) throws Exception {
        return GetPreviewUrl(courseId, null, null, -1);
    }

    /// <summary>
    /// Get the url that can be opened in a browser and used to preview this course, without
    /// the need for a registration.
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <param name="versionId">Version Id</param>
    public String GetPreviewUrl(String courseId, String redirectOnExitUrl) throws Exception {
        return GetPreviewUrl(courseId, redirectOnExitUrl, null, -1);
    }

    /// <summary>
    /// Get the url that can be opened in a browser and used to preview this course, without
    /// the need for a registration.
    /// </summary>
    /// <param name="courseId">Unique Course Identifier</param>
    /// <param name="versionId">Version Id</param>
    public String GetPreviewUrl(String courseId, String redirectOnExitUrl, String cssUrl, int versionId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (!Utils.isNullOrEmpty(redirectOnExitUrl)) {
            request.getParameters().add("redirecturl", redirectOnExitUrl);
        }
        if (!Utils.isNullOrEmpty(cssUrl)) {
            request.getParameters().add("cssurl", cssUrl);
        }
        if (versionId >= 0) {
            request.getParameters().add("versionid", versionId);
        }
        return request.constructUrl("rustici.course.preview");
    }

    public String GetAsyncImportUrl(String courseId, String redirectUrl, String userEmail, boolean finalizeSynchronously) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (!Utils.isNullOrEmpty(userEmail)) {
            request.getParameters().add("email", userEmail);
        }
        if (!Utils.isNullOrEmpty(redirectUrl)) {
            request.getParameters().add("redirecturl", redirectUrl);
        }
        if (finalizeSynchronously) {
            request.getParameters().add("finalize", true);
        }
        return request.constructUrl("rustici.course.importCourseAsync");
    }

    public String GetUpdateUrl(String courseId, String redirectUrl, String userEmail) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (!Utils.isNullOrEmpty(userEmail)) {
            request.getParameters().add("email", userEmail);
        }
        if (!Utils.isNullOrEmpty(redirectUrl)) {
            request.getParameters().add("redirecturl", redirectUrl);
        }
        return request.constructUrl("rustici.course.updateAssets");
    }

    public String GetAsyncUpdateUrl(String courseId, String redirectUrl, String userEmail, boolean finalizeSynchronously) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        if (!Utils.isNullOrEmpty(userEmail)) {
            request.getParameters().add("email", userEmail);
        }
        if (!Utils.isNullOrEmpty(redirectUrl)) {
            request.getParameters().add("redirecturl", redirectUrl);
        }
        if (finalizeSynchronously) {
            request.getParameters().add("finalize", true);
        }

        return request.constructUrl("rustici.course.updateAssetsAsync");
    }

    public CourseData GetCourseDetail(String courseId) throws Exception {
        ServiceRequest request = new ServiceRequest(configuration);
        request.getParameters().add("courseid", courseId);
        Document xmlDoc = request.callService("rustici.course.getCourseDetail");
        return CourseData.parseFromXmlElement(
            XmlUtils.getFirstChildByTagName(xmlDoc.getDocumentElement(), "course"));
    }
}
