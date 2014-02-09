package by.oshmianski.utils;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 01.04.13
 * Time: 16:00
 */
public class AppletParams {
    private static AppletParams ourInstance = new AppletParams();

    private String server = "";
    private String dbIDProc;
    private String dbIDLiK;
    private String viewSuit;
    private String viewDebt;
    private String viewPerson;
    private String viewDebtExt;
    private String viewMove;
    private String viewTemplateWord;

    public static AppletParams getInstance() {
        return ourInstance;
    }

    private AppletParams() {
    }

    public void getParams(JApplet applet){
        server = applet.getParameter("server");
        dbIDProc = applet.getParameter("dbIDProc");
        dbIDLiK = applet.getParameter("dbIDLiK");
        viewSuit = applet.getParameter("viewSuit");
        viewDebt = applet.getParameter("viewDebt");
        viewPerson = applet.getParameter("viewPerson");
        viewDebtExt = applet.getParameter("viewDebtExt");
        viewMove = applet.getParameter("viewMove");
        viewTemplateWord = applet.getParameter("viewTemplateWord");
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDbIDProc() {
        return dbIDProc;
    }

    public void setDbIDProc(String dbIDProc) {
        this.dbIDProc = dbIDProc;
    }

    public String getDbIDLiK() {
        return dbIDLiK;
    }

    public void setDbIDLiK(String dbIDLiK) {
        this.dbIDLiK = dbIDLiK;
    }

    public String getViewSuit() {
        return viewSuit;
    }

    public void setViewSuit(String viewSuit) {
        this.viewSuit = viewSuit;
    }

    public String getViewDebt() {
        return viewDebt;
    }

    public void setViewDebt(String viewDebt) {
        this.viewDebt = viewDebt;
    }

    public String getViewPerson() {
        return viewPerson;
    }

    public void setViewPerson(String viewPerson) {
        this.viewPerson = viewPerson;
    }

    public String getViewDebtExt() {
        return viewDebtExt;
    }

    public void setViewDebtExt(String viewDebtExt) {
        this.viewDebtExt = viewDebtExt;
    }

    public String getViewMove() {
        return viewMove;
    }

    public void setViewMove(String viewMove) {
        this.viewMove = viewMove;
    }

    public String getViewTemplateWord() {
        return viewTemplateWord;
    }

    public void setViewTemplateWord(String viewTemplateWord) {
        this.viewTemplateWord = viewTemplateWord;
    }
}