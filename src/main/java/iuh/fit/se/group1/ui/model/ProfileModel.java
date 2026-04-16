/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.model;

/**
 *
 * @author THIS PC
 */
public class ProfileModel {
    private String urlAvt;
    private String roleName;
    private String fullName;


    public ProfileModel() {
    }

    public String getUrlAvt() {
        return urlAvt;
    }

    public void setUrlAvt(String urlAvt) {
        this.urlAvt = urlAvt;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ProfileModel(String urlAvt, String roleName, String fullName) {
        this.urlAvt = urlAvt;
        this.roleName = roleName;
        this.fullName = fullName;
    }
}
