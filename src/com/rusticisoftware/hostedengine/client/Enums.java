package com.rusticisoftware.hostedengine.client;

public class Enums
{
    public enum MetadataScope {
        COURSE, ACTIVITY;
        
        public String toString() {
            return this.name().toLowerCase();
        }
        
        public static MetadataScope getValue(String name) {
            if (name == null)
                return null;
            
            for(MetadataScope value : MetadataScope.values()){
                if (value.name().equalsIgnoreCase(name)){
                    return value;
                }
            }
            return null;    
        }

    }
    
    public enum MetadataFormat { 
        SUMMARY, DETAIL;
        
        public String toString() {
            return this.name().toLowerCase();
        }
        

        public static MetadataFormat getValue(String name) {
            if (name == null)
                return null;
            
            for(MetadataFormat value : MetadataFormat.values()){
                if (value.name().equalsIgnoreCase(name)){
                    return value;
                }
            }
            return null;    
        }
        
    }
    
    public enum RegistrationResultsFormat { 
        COURSE_SUMMARY, ACTIVITY_SUMMARY, FULL_DETAIL; 
        
        public String toString() {
            if (this == COURSE_SUMMARY){
                return "course";
            }
            else if (this == ACTIVITY_SUMMARY){
                return "activity";
            }
            else if (this == FULL_DETAIL) {
                return "full";
            }
            else
                return this.name();
        }
        

        public static RegistrationResultsFormat getValue(String name) {
            if (name == null)
                return null;
            
            if (name.equals("course")){
                return COURSE_SUMMARY;
            }
            else if (name.equals("activity")){
                return ACTIVITY_SUMMARY;
            }
            else if (name.equals("full")) {
                return FULL_DETAIL;
            }
            else {
                for(RegistrationResultsFormat value : RegistrationResultsFormat.values()){
                    if (value.name().equalsIgnoreCase(name)){
                        return value;
                    }
                }
                return null;
            }       
        }   
    }
    
    public enum DataFormat {
        XML, JSON;
    }
    
    public enum RegistrationResultsAuthType {
        FORM, HTTPBASIC;
        
        public static RegistrationResultsAuthType getValue(String name) {
            if (name == null)
                return null;

            for(RegistrationResultsAuthType value : RegistrationResultsAuthType.values()){
                if (value.name().equalsIgnoreCase(name)){
                    return value;
                }
            }
            return null;    
        }
        
        public String toString(){
            return this.name().toLowerCase();
        }
    }
    
    public enum ReportageNavPermission {
    	NONAV, DOWNONLY, FREENAV;
    	
    	public static ReportageNavPermission getValue(String name) {
            if (name == null)
                return null;

            for(ReportageNavPermission value : ReportageNavPermission.values()){
                if (value.name().equalsIgnoreCase(name)){
                    return value;
                }
            }
            return null;    
        }
        
        public String toString(){
            return this.name().toLowerCase();
        }
    }
}
