package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FileData
{
    private String name;
    private long size;
    private Date lastModified;
    
    /// <summary>
    /// Purpose of this class is to map the return xml from the course listing
    /// web service into an object.  This is the main constructor.
    /// </summary>
    /// <param name="fileDataElement"></param>
    public FileData(Element fileDataElement)
    {
        this.name = fileDataElement.getAttribute("name");
        this.size = Long.parseLong(fileDataElement.getAttribute("size"));
        try {
            this.lastModified = Utils.parseFormattedTime(fileDataElement.getAttribute("modified"));
        }
        catch (Exception e){}
    }

    /// <summary>
    /// Helper method which takes the full XmlDocument as returned from the course listing
    /// web service and returns a List of CourseData objects.
    /// </summary>
    /// <param name="xmlDoc"></param>
    /// <returns></returns>
    public static ArrayList<FileData> ConvertToFileDataList(Document xmlDoc)
    {
        ArrayList<FileData> allResults = new ArrayList<FileData>();

        NodeList fileDataList = xmlDoc.getElementsByTagName("file");
        for (int i = 0; i < fileDataList.getLength(); i++){
            Element fileData = (Element)fileDataList.item(i);
            allResults.add(new FileData(fileData));
        }
        return allResults;
    }

    /// <summary>
    /// File Name
    /// </summary>
    public String getName()
    {
        return name;
    }

    /// <summary>
    /// File Size
    /// </summary>
    public long getSize()
    {
        return size;
    }

    /// <summary>
    /// File Last Modified Date
    /// </summary>
    public Date getLastModified()
    {
        return lastModified;
    }
}
