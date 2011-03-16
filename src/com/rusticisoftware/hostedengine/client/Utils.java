package com.rusticisoftware.hostedengine.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Utils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    public static Date parseIsoDate(String isoDate) throws Exception
    {
        return sdf.parse(isoDate);
    }
    public static String getIsoDateString(Date date)
    {
        return sdf.format(date);
    }
    
    //Copy inStream to outStream using Java's built in buffering
    public static boolean bufferedCopyStream(InputStream inStream, OutputStream outStream) throws Exception {
        BufferedInputStream bis = new BufferedInputStream( inStream );
        BufferedOutputStream bos = new BufferedOutputStream ( outStream );
        while(true){
            int data = bis.read();
            if (data == -1){
                break;
            }
            bos.write(data);
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
    
    public static Date parseFormattedTime(String formattedTime) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.parse(formattedTime);
    }
    
    public static String getEncodedParam(String paramName, String paramVal) throws Exception {
        return URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(paramVal, "UTF-8");
    }
    
    public static boolean isNullOrEmpty(String val){
        return (val == null || val.trim().length() == 0);
    }
    
    public static String getNonXmlPayloadFromResponse(Document response){
        return response.getElementsByTagName("rsp").item(0).getTextContent();
    }
    
    public static String cleanRegistrationId(String input)
    {
		return input.replaceAll("[^-\\w\\s.]+", "");
    }
    
    public static String join(String[] strings, String separator) {
    	if(strings == null){
			return null;
		}
    	
    	StringBuilder result = new StringBuilder();
    	boolean first = true;
		for (String str : strings){
			result.append(first ? "" : separator);
			result.append(str);
			first = false;
		}
		return result.toString();
	}
    public static String join(List<String> strings, String separator){
    	return join(strings.toArray(new String[strings.size()]), separator);
    }
    
    public static String xmlEncode (String str)
    {
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace("<", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
}
