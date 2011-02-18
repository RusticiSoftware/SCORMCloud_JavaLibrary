package com.rusticisoftware.hostedengine.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LaunchInfo
{
    private String id;
    private String completion;
    private String satisfaction;
    private String measureStatus;
    private String normalizedMeasure;
    private String experiencedDurationTracked;
    private String launchTime;
    private String exitTime;
    private String lastUpdated;
    private String log;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCompletion()
    {
        return completion;
    }

    public void setCompletion(String completion)
    {
        this.completion = completion;
    }

    public String getSatisfaction()
    {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction)
    {
        this.satisfaction = satisfaction;
    }

    public String getMeasureStatus()
    {
        return measureStatus;
    }

    public void setMeasureStatus(String measureStatus)
    {
        this.measureStatus = measureStatus;
    }

    public String getNormalizedMeasure()
    {
        return normalizedMeasure;
    }

    public void setNormalizedMeasure(String normalizedMeasure)
    {
        this.normalizedMeasure = normalizedMeasure;
    }

    public String getExperiencedDurationTracked()
    {
        return experiencedDurationTracked;
    }

    public void setExperiencedDurationTracked(String experiencedDurationTracked)
    {
        this.experiencedDurationTracked = experiencedDurationTracked;
    }

    public String getLaunchTime()
    {
        return launchTime;
    }

    public void setLaunchTime(String launchTime)
    {
        this.launchTime = launchTime;
    }

    public String getExitTime()
    {
        return exitTime;
    }

    public void setExitTime(String exitTime)
    {
        this.exitTime = exitTime;
    }

    public String getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public String getLog()
    {
        return log;
    }

    public void setLog(String log)
    {
        this.log = log;
    }

    /// <summary>
    /// Inflate launch info object from passed in xml element
    /// </summary>
    /// <param name="launchInfoElem"></param>
    public LaunchInfo(Element launchInfoElem)
    {
        this.setId(launchInfoElem.getAttribute("id"));
        this.setCompletion(XmlUtils.getNamedElemValue(launchInfoElem, "completion"));
        this.setSatisfaction(XmlUtils.getNamedElemValue(launchInfoElem, "satisfaction"));
        this.setMeasureStatus(XmlUtils.getNamedElemValue(launchInfoElem, "measure_status"));
        this.setNormalizedMeasure(XmlUtils.getNamedElemValue(launchInfoElem, "normalized_measure"));
        this.setExperiencedDurationTracked(XmlUtils.getNamedElemValue(launchInfoElem, "experienced_duration_tracked"));
        this.setLaunchTime(XmlUtils.getNamedElemValue(launchInfoElem, "launch_time"));
        this.setExitTime(XmlUtils.getNamedElemValue(launchInfoElem, "exit_time"));
        this.setLastUpdated(XmlUtils.getNamedElemValue(launchInfoElem, "update_dt"));
        try {
            this.setLog(XmlUtils.getNamedElemXml(launchInfoElem, "log"));
        }
        catch (Exception e) {}
    }

    /// <summary>
    /// Return list of launch info objects from xml element
    /// </summary>
    /// <param name="doc"></param>
    /// <returns></returns>
    public static List<LaunchInfo> ConvertToLaunchInfoList(Element rootElem)
    {
        List<LaunchInfo> launchList = new ArrayList<LaunchInfo>();
        NodeList launches = rootElem.getElementsByTagName("launch");
        for (int i = 0; i < launches.getLength(); i++){
            launchList.add(new LaunchInfo((Element)launches.item(i)));
        }
        return launchList;
    }
}
