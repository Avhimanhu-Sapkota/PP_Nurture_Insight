package com.example.nurture_insight.Model;

public class Self_Care_Packages {
    Integer selfCarePackages;
    String selfCarePackagesTitle, selfCarePackagesDesc;

    public Self_Care_Packages(){

    }

    public Self_Care_Packages(Integer selfCarePackages, String selfCarePackagesTitle, String selfCarePackagesDesc) {
        this.selfCarePackages = selfCarePackages;
        this.selfCarePackagesTitle = selfCarePackagesTitle;
        this.selfCarePackagesDesc = selfCarePackagesDesc;
    }

    public Integer getSelfCarePackages() {
        return selfCarePackages;
    }

    public void setSelfCarePackages(Integer selfCarePackages) {
        this.selfCarePackages = selfCarePackages;
    }

    public String getSelfCarePackagesTitle() {
        return selfCarePackagesTitle;
    }

    public void setSelfCarePackagesTitle(String selfCarePackagesTitle) {
        this.selfCarePackagesTitle = selfCarePackagesTitle;
    }

    public String getSelfCarePackagesDesc() {
        return selfCarePackagesDesc;
    }

    public void setSelfCarePackagesDesc(String selfCarePackagesDesc) {
        this.selfCarePackagesDesc = selfCarePackagesDesc;
    }
}
