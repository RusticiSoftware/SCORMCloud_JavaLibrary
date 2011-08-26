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

package com.rusticisoftware.hostedengine.client.datatypes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.rusticisoftware.hostedengine.client.XmlUtils;

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
