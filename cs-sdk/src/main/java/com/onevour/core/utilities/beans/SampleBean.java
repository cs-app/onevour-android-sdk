package com.onevour.core.utilities.beans;

import java.util.Date;
import java.util.List;

public class SampleBean {

    String name;

    String lastName;

    private Date bod;

    Education education;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    private List<Education> educations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBod() {
        return bod;
    }

    public void setBod(Date bod) {
        this.bod = bod;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public static class Education {
        private String education;
        private Date graduate;

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public Date getGraduate() {
            return graduate;
        }

        public void setGraduate(Date graduate) {
            this.graduate = graduate;
        }
    }
}
