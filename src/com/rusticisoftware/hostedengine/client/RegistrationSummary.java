package com.rusticisoftware.hostedengine.client;

import org.w3c.dom.Element;

public class RegistrationSummary
{
    private String _complete;
    private String _success;
    private String _totaltime;
    private String _score;

    public RegistrationSummary(Element reportElem)
    {
        this._complete = reportElem.getElementsByTagName("complete").item(0).getTextContent();
        this._success = reportElem.getElementsByTagName("success").item(0).getTextContent();
        this._totaltime = reportElem.getElementsByTagName("totaltime").item(0).getTextContent();
        this._score = reportElem.getElementsByTagName("score").item(0).getTextContent();
    }

    public String getComplete()
    {
        return _complete;
    }

    public String getSuccess()
    {
        return _success;
    }

    public String getTotalTime()
    {
        return _totaltime;
    }

    public String getScore()
    {
        return _score;
    }
}
