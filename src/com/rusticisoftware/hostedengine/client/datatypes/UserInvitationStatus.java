package com.rusticisoftware.hostedengine.client.datatypes;

public class UserInvitationStatus {
    private String _email;
    private boolean _isStarted;
    private String _url;
    private String _registrationId;
    private RegistrationSummary _registrationSummary;

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public boolean is_isStarted() {
        return _isStarted;
    }

    public void set_isStarted(boolean _isStarted) {
        this._isStarted = _isStarted;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public RegistrationSummary get_registrationSummary() {
        return _registrationSummary;
    }

    public void set_registrationSummary(RegistrationSummary _registrationSummary) {
        this._registrationSummary = _registrationSummary;
    }

    public String get_registrationId() {
        return _registrationId;
    }

    public void set_registrationId(String _registrationId) {
        this._registrationId = _registrationId;
    }
}
