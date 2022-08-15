package com.flatcode.beautytouchadmin.Model;

public class Tools {
    private String imageLogo, oldImageLogo, imageSession, oldImageSession, session, oldSession,
            sessionNumber, oldSessionNumber, year, oldYear, aboutMe, imageMe;

    public Tools() {
    }

    public Tools(String imageLogo, String oldImageLogo, String imageSession, String oldImageSession,
                 String session, String oldSession, String sessionNumber, String oldSessionNumber,
                 String year, String oldYear, String aboutMe, String imageMe) {

        this.imageLogo = imageLogo;
        this.oldImageLogo = oldImageLogo;
        this.imageSession = imageSession;
        this.oldImageSession = oldImageSession;
        this.session = session;
        this.oldSession = oldSession;
        this.sessionNumber = sessionNumber;
        this.oldSessionNumber = oldSessionNumber;
        this.year = year;
        this.oldYear = oldYear;
        this.aboutMe = aboutMe;
        this.imageMe = imageMe;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    public String getOldImageLogo() {
        return oldImageLogo;
    }

    public void setOldImageLogo(String oldImageLogo) {
        this.oldImageLogo = oldImageLogo;
    }

    public String getImageSession() {
        return imageSession;
    }

    public void setImageSession(String imageSession) {
        this.imageSession = imageSession;
    }

    public String getOldImageSession() {
        return oldImageSession;
    }

    public void setOldImageSession(String oldImageSession) {
        this.oldImageSession = oldImageSession;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getOldSession() {
        return oldSession;
    }

    public void setOldSession(String oldSession) {
        this.oldSession = oldSession;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public String getOldSessionNumber() {
        return oldSessionNumber;
    }

    public void setOldSessionNumber(String oldSessionNumber) {
        this.oldSessionNumber = oldSessionNumber;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOldYear() {
        return oldYear;
    }

    public void setOldYear(String oldYear) {
        this.oldYear = oldYear;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getImageMe() {
        return imageMe;
    }

    public void setImageMe(String imageMe) {
        this.imageMe = imageMe;
    }
}