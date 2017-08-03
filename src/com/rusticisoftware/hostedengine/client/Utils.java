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

import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Utils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static Date parseIsoDate(String isoDate) throws Exception {
        return sdf.parse(isoDate);
    }

    public static String getIsoDateString(Date date) {
        return sdf.format(date);
    }

    //Copy inStream to outStream using Java's built in buffering
    public static boolean bufferedCopyStream(InputStream inStream, OutputStream outStream) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(inStream);
        BufferedOutputStream bos = new BufferedOutputStream(outStream);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bos.flush();
        return true;
    }

    //Return String representation of current date, in UTC timezone
    public static String getFormattedTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.getCalendar().setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static Date parseFormattedTime(String formattedTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.parse(formattedTime);
    }

    public static String getEncodedParam(String paramName, String paramVal) throws Exception {
        return URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(paramVal, "UTF-8");
    }

    public static boolean isNullOrEmpty(String val) {
        return (val == null || val.trim().length() == 0);
    }

    public static String getNonXmlPayloadFromResponse(Document response) {
        return response.getElementsByTagName("rsp").item(0).getTextContent();
    }

    public static String cleanRegistrationId(String input) {
        return input.replaceAll("[^-\\w\\s.]+", "");
    }

    public static String join(String[] strings, String separator) {
        if (strings == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String str : strings) {
            result.append(first ? "" : separator);
            result.append(str);
            first = false;
        }
        return result.toString();
    }

    public static String join(List<String> strings, String separator) {
        return join(strings.toArray(new String[strings.size()]), separator);
    }

    public static String xmlEncode(String str) {
        return str.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace("<", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
