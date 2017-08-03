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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * <p>Title: Client HTTP Request class</p>
 * <p>Description: this class helps to send POST HTTP requests with various form data,
 * including files. Cookies can be added to be included in the request.</p>
 *
 * @author Vlad Patryshev
 * @version 1.0
 */
public class ClientHttpRequest {
    private static Random random = new Random();
    URLConnection connection;
    OutputStream os = null;
    Map cookies = new HashMap();
    String boundary = "---------------------------" + randomString() + randomString() + randomString();

    /**
     * Creates a new multipart POST HTTP request on a freshly opened URLConnection
     *
     * @param connection an already open URL connection
     * @throws IOException from URLConnection.
     */
    public ClientHttpRequest(URLConnection connection) throws IOException {
        this.connection = connection;
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type",
            "multipart/form-data; boundary=" + boundary);
    }

    /**
     * Creates a new multipart POST HTTP request for a specified URL
     *
     * @param url the URL to send request to
     * @throws IOException from URLConnection.
     */
    public ClientHttpRequest(URL url) throws IOException {
        this(url.openConnection());
    }

    /**
     * Creates a new multipart POST HTTP request for a specified URL string
     *
     * @param urlString the string representation of the URL to send request to
     * @throws IOException from URLConnection.
     */
    public ClientHttpRequest(String urlString) throws IOException {
        this(new URL(urlString));
    }

    protected static String randomString() {
        return Long.toString(random.nextLong(), 36);
    }

    private static void pipe(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[4096];
        int nread;
        int navailable;
        int total = 0;
        synchronized (in) {
            while ((nread = in.read(buf, 0, buf.length)) >= 0) {
                out.write(buf, 0, nread);
                total += nread;
            }
        }
        out.flush();
        buf = null;
    }

    /**
     * posts a new request to specified URL, with parameters that are passed in the argument
     *
     * @param url        The url to post to.
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, Map parameters) throws IOException {
        return new ClientHttpRequest(url).post(parameters);
    }

    /**
     * posts a new request to specified URL, with parameters that are passed in the argument
     *
     * @param url        The url to post to.
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, Object[] parameters) throws IOException {
        return new ClientHttpRequest(url).post(parameters);
    }

    /**
     * posts a new request to specified URL, with cookies and parameters that are passed in the argument
     *
     * @param url        The url to post to.
     * @param cookies    request cookies
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, Map cookies, Map parameters) throws IOException {
        return new ClientHttpRequest(url).post(cookies, parameters);
    }

    /**
     * posts a new request to specified URL, with cookies and parameters that are passed in the argument
     *
     * @param url        The url to post to.
     * @param cookies    request cookies
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, String[] cookies, Object[] parameters) throws IOException {
        return new ClientHttpRequest(url).post(cookies, parameters);
    }

    /**
     * post the POST request specified URL, with the specified parameter
     *
     * @param url    The url to post to.
     * @param name1  parameter name
     * @param value1 parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, String name1, Object value1) throws IOException {
        return new ClientHttpRequest(url).post(name1, value1);
    }

    /**
     * post the POST request to specified URL, with the specified parameters
     *
     * @param url    The url to post to.
     * @param name1  first parameter name
     * @param value1 first parameter value
     * @param name2  second parameter name
     * @param value2 second parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, String name1, Object value1, String name2, Object value2) throws IOException {
        return new ClientHttpRequest(url).post(name1, value1, name2, value2);
    }

    /**
     * post the POST request to specified URL, with the specified parameters
     *
     * @param url    The url to post to.
     * @param name1  first parameter name
     * @param value1 first parameter value
     * @param name2  second parameter name
     * @param value2 second parameter value
     * @param name3  third parameter name
     * @param value3 third parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, String name1, Object value1, String name2,
                                   Object value2, String name3, Object value3) throws IOException {
        return new ClientHttpRequest(url).post(name1, value1, name2, value2, name3, value3);
    }

    /**
     * post the POST request to specified URL, with the specified parameters
     *
     * @param url    The url to post to.
     * @param name1  first parameter name
     * @param value1 first parameter value
     * @param name2  second parameter name
     * @param value2 second parameter value
     * @param name3  third parameter name
     * @param value3 third parameter value
     * @param name4  fourth parameter name
     * @param value4 fourth parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public static InputStream post(URL url, String name1, Object value1, String name2, Object value2,
                                   String name3, Object value3, String name4, Object value4) throws IOException {
        return new ClientHttpRequest(url).post(name1, value1, name2, value2, name3, value3, name4, value4);
    }

    protected void connect() throws IOException {
        if (os == null) {
            os = connection.getOutputStream();
        }
    }

    protected void write(char character) throws IOException {
        connect();
        os.write(character);
    }

    protected void write(String string) throws IOException {
        connect();
        os.write(string.getBytes());
    }

    protected void newline() throws IOException {
        connect();
        write("\r\n");
    }

    protected void writeln(String string) throws IOException {
        connect();
        write(string);
        newline();
    }

    private void boundary() throws IOException {
        write("--");
        write(boundary);
    }

    private void postCookies() {
        StringBuffer cookieList = new StringBuffer();

        for (Iterator i = cookies.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) (i.next());
            cookieList.append(entry.getKey().toString() + "=" + entry.getValue());

            if (i.hasNext()) {
                cookieList.append("; ");
            }
        }
        if (cookieList.length() > 0) {
            connection.setRequestProperty("Cookie", cookieList.toString());
        }
    }

    /**
     * adds a cookie to the requst
     *
     * @param name  cookie name
     * @param value cookie value
     * @throws IOException from URLConnection.
     */
    public void setCookie(String name, String value) throws IOException {
        cookies.put(name, value);
    }

    /**
     * adds cookies to the request
     *
     * @param cookies the cookie "name-to-value" map
     * @throws IOException from URLConnection.
     */
    public void setCookies(Map cookies) throws IOException {
        if (cookies == null) {
            return;
        }
        this.cookies.putAll(cookies);
    }

    /**
     * adds cookies to the request
     *
     * @param cookies array of cookie names and values (cookies[2*i] is a name, cookies[2*i + 1] is a value)
     * @throws IOException from URLConnection.
     */
    public void setCookies(String[] cookies) throws IOException {
        if (cookies == null) {
            return;
        }
        for (int i = 0; i < cookies.length - 1; i += 2) {
            setCookie(cookies[i], cookies[i + 1]);
        }
    }

    private void writeName(String name) throws IOException {
        newline();
        write("Content-Disposition: form-data; name=\"");
        write(name);
        write('"');
    }

    /**
     * adds a string parameter to the request
     *
     * @param name  parameter name
     * @param value parameter value
     * @throws IOException from URLConnection.
     */
    public void setParameter(String name, String value) throws IOException {
        boundary();
        writeName(name);
        newline();
        newline();
        writeln(value);
    }

    /**
     * adds a file parameter to the request
     *
     * @param name     parameter name
     * @param filename the name of the file
     * @param is       input stream to read the contents of the file from
     * @throws IOException from URLConnection.
     */
    public void setParameter(String name, String filename, InputStream is) throws IOException {
        boundary();
        writeName(name);
        write("; filename=\"");
        write(filename);
        write('"');
        newline();
        write("Content-Type: ");
        String type = URLConnection.guessContentTypeFromName(filename);
        if (type == null) {
            type = "application/octet-stream";
        }
        writeln(type);
        newline();
        pipe(is, os);
        newline();
    }

    /**
     * adds a file parameter to the request
     *
     * @param name parameter name
     * @param file the file to upload
     * @throws IOException from URLConnection.
     */
    public void setParameter(String name, File file) throws IOException {
        setParameter(name, file.getName(), new FileInputStream(file));
    }

    /**
     * adds a parameter to the request; if the parameter is a File, the file is uploaded,
     * otherwise the string value of the parameter is passed in the request
     *
     * @param name   parameter name
     * @param object parameter value, a File or anything else that can be stringified
     * @throws IOException from URLConnection.
     */
    public void setParameter(String name, Object object) throws IOException {
        if (object instanceof File) {
            setParameter(name, (File) object);
        } else {
            setParameter(name, object.toString());
        }
    }

    /**
     * adds parameters to the request
     *
     * @param parameters "name-to-value" map of parameters; if a value is a file, the file is uploaded,
     *                   otherwise it is stringified and sent in the request
     * @throws IOException from URLConnection.
     */
    public void setParameters(Map parameters) throws IOException {
        if (parameters == null) {
            return;
        }
        for (Iterator i = parameters.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            setParameter(entry.getKey().toString(), entry.getValue());
        }
    }

    /**
     * adds parameters to the request
     *
     * @param parameters array of parameter names and values (parameters[2*i] is a name, parameters[2*i + 1] is a value);
     *                   if a value is a file, the file is uploaded, otherwise it is stringified and sent in the request
     * @throws IOException from URLConnection.
     */
    public void setParameters(Object[] parameters) throws IOException {
        if (parameters == null) {
            return;
        }
        for (int i = 0; i < parameters.length - 1; i += 2) {
            setParameter(parameters[i].toString(), parameters[i + 1]);
        }
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added
     *
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post() throws IOException {
        boundary();
        writeln("--");
        os.close();
        return connection.getInputStream();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before (if any),
     * and with parameters that are passed in the argument
     *
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(Map parameters) throws IOException {
        setParameters(parameters);
        return post();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before (if any),
     * and with parameters that are passed in the argument
     *
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(Object[] parameters) throws IOException {
        setParameters(parameters);
        return post();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before (if any),
     * and with cookies and parameters that are passed in the arguments
     *
     * @param cookies    request cookies
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(Map cookies, Map parameters) throws IOException {
        setCookies(cookies);
        setParameters(parameters);
        return post();
    }

    /**
     * posts the requests to the server, with all the cookies and parameters that were added before (if any),
     * and with cookies and parameters that are passed in the arguments
     *
     * @param cookies    request cookies
     * @param parameters request parameters
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(String[] cookies, Object[] parameters) throws IOException {
        setCookies(cookies);
        setParameters(parameters);
        return post();
    }

    /**
     * post the POST request to the server, with the specified parameter
     *
     * @param name  parameter name
     * @param value parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(String name, Object value) throws IOException {
        setParameter(name, value);
        return post();
    }

    /**
     * post the POST request to the server, with the specified parameters
     *
     * @param name1  first parameter name
     * @param value1 first parameter value
     * @param name2  second parameter name
     * @param value2 second parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(String name1, Object value1, String name2, Object value2) throws IOException {
        setParameter(name1, value1);
        return post(name2, value2);
    }

    /**
     * post the POST request to the server, with the specified parameters
     *
     * @param name1  first parameter name
     * @param value1 first parameter value
     * @param name2  second parameter name
     * @param value2 second parameter value
     * @param name3  third parameter name
     * @param value3 third parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(String name1, Object value1, String name2, Object value2, String name3, Object value3) throws IOException {
        setParameter(name1, value1);
        return post(name2, value2, name3, value3);
    }

    /**
     * post the POST request to the server, with the specified parameters
     *
     * @param name1  first parameter name
     * @param value1 first parameter value
     * @param name2  second parameter name
     * @param value2 second parameter value
     * @param name3  third parameter name
     * @param value3 third parameter value
     * @param name4  fourth parameter name
     * @param value4 fourth parameter value
     * @return input stream with the server response
     * @throws IOException from URLConnection.
     */
    public InputStream post(String name1, Object value1, String name2, Object value2, String name3,
                            Object value3, String name4, Object value4) throws IOException {
        setParameter(name1, value1);
        return post(name2, value2, name3, value3, name4, value4);
    }
}
