package com.rusticisoftware.hostedengine.client.datatypes;

import com.rusticisoftware.hostedengine.client.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class InvitationInfo {
    private String _id;
    private String[] _errors = new String[0];
    private boolean _public;
    private boolean _allowNewRegistrations;
    private boolean _allowLaunch;
    private UserInvitationStatus[] _userInvitations = new UserInvitationStatus[0];
    private String _url;
    private boolean _created;
    private Date _createDate;
    private String _message;
    private String _subject;
    private String _courseId;

    public static List<InvitationInfo> parseListFromXml(Document invListXml) throws Exception {
        Element invListElem = (Element) invListXml.getElementsByTagName("invitationlist").item(0);
        ArrayList<InvitationInfo> invList = new ArrayList<InvitationInfo>();
        NodeList invElems = invListElem.getElementsByTagName("invitationInfo");
        for (int i = 0; i < invElems.getLength(); i++) {
            Element invElem = (Element) invElems.item(i);
            invList.add(parseInvitationInfoElement(invElem));
        }
        return invList;
    }

    public static InvitationInfo parseInvitationInfoElement(Element invitationInfo) throws Exception {
        InvitationInfo result = new InvitationInfo();

        result.set_id(XmlUtils.getChildElemText(invitationInfo, "id"));

        result.set_allowLaunch(Boolean.parseBoolean(XmlUtils.getChildElemText(invitationInfo, "allowLaunch")));
        result.set_allowNewRegistrations(Boolean.parseBoolean(XmlUtils.getChildElemText(invitationInfo, "allowNewRegistrations")));
        result.set_created(Boolean.parseBoolean(XmlUtils.getChildElemText(invitationInfo, "created")));

        result.set_Public(Boolean.parseBoolean(XmlUtils.getChildElemText(invitationInfo, "public")));

        result.set_message(XmlUtils.getChildElemText(invitationInfo, "body"));
        result.set_subject(XmlUtils.getChildElemText(invitationInfo, "subject"));
        result.set_url(XmlUtils.getChildElemText(invitationInfo, "url"));

        result.set_courseId(XmlUtils.getChildElemText(invitationInfo, "courseId"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSSZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        result.set_createDate(sdf.parse(XmlUtils.getChildElemText(invitationInfo, "createdDate")));

        NodeList errElems = invitationInfo.getElementsByTagName("errors").item(0).getChildNodes();
        String[] errors = new String[errElems.getLength()];
        for (int i = 0; i < errElems.getLength(); i++) {
            errors[i] = errElems.item(i).getTextContent();
        }
        result.set_errors(errors);

        result.set_userInvitations(parseUserInvitations((Element) invitationInfo.getElementsByTagName("userInvitations").item(0)));

        return result;
    }

    private static UserInvitationStatus[] parseUserInvitations(Element userInvitations) throws Exception {
        if (userInvitations == null) {
            return new UserInvitationStatus[0];
        }
        NodeList nl = userInvitations.getElementsByTagName("userInvitation");
        UserInvitationStatus[] userInvitationStatuses = new UserInvitationStatus[nl.getLength()];
        for (int ii = 0; ii < nl.getLength(); ii++) {
            Element userInvitation = (Element) nl.item(ii);
            userInvitationStatuses[ii] = new UserInvitationStatus();
            userInvitationStatuses[ii].set_email(XmlUtils.getChildElemText(userInvitation, "email"));
            userInvitationStatuses[ii].set_isStarted("true".equals(XmlUtils.getChildElemText(userInvitation, "isStarted")));
            userInvitationStatuses[ii].set_registrationId(XmlUtils.getChildElemText(userInvitation, "registrationId"));
            userInvitationStatuses[ii].set_url(XmlUtils.getChildElemText(userInvitation, "url"));
            Element regReport = (Element) userInvitation.getElementsByTagName("registrationreport").item(0);
            if (regReport != null) {
                userInvitationStatuses[ii].set_registrationSummary(new RegistrationSummary(regReport));
            }
        }
        return userInvitationStatuses;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String[] get_errors() {
        return _errors;
    }

    public void set_errors(String[] _errors) {
        this._errors = _errors;
    }

    public boolean is_Public() {
        return _public;
    }

    public void set_Public(boolean _isPublic) {
        this._public = _isPublic;
    }

    public boolean is_allowNewRegistrations() {
        return _allowNewRegistrations;
    }

    public void set_allowNewRegistrations(boolean _allowNewRegistrations) {
        this._allowNewRegistrations = _allowNewRegistrations;
    }

    public boolean is_allowLaunch() {
        return _allowLaunch;
    }

    public void set_allowLaunch(boolean _allowLaunch) {
        this._allowLaunch = _allowLaunch;
    }

    public UserInvitationStatus[] get_userInvitations() {
        return _userInvitations;
    }

    public void set_userInvitations(UserInvitationStatus[] _userInvitations) {
        this._userInvitations = _userInvitations;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public boolean is_created() {
        return _created;
    }

    public void set_created(boolean _created) {
        this._created = _created;
    }

    public Date get_createDate() {
        return _createDate;
    }

    public void set_createDate(Date _createDate) {
        this._createDate = _createDate;
    }

    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public String get_subject() {
        return _subject;
    }

    public void set_subject(String _subject) {
        this._subject = _subject;
    }

    public String get_courseId() {
        return _courseId;
    }

    public void set_courseId(String _courseId) {
        this._courseId = _courseId;
    }


}
