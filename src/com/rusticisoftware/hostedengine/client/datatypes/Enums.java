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

public class Enums {
    public enum MetadataScope {
        COURSE, ACTIVITY;

        public static MetadataScope getValue(String name) {
            if (name == null) {
                return null;
            }

            for (MetadataScope value : MetadataScope.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }

        public String toString() {
            return this.name().toLowerCase();
        }

    }

    public enum MetadataFormat {
        SUMMARY, DETAIL;

        public static MetadataFormat getValue(String name) {
            if (name == null) {
                return null;
            }

            for (MetadataFormat value : MetadataFormat.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }

        public String toString() {
            return this.name().toLowerCase();
        }

    }

    public enum RegistrationResultsFormat {
        COURSE_SUMMARY, ACTIVITY_SUMMARY, FULL_DETAIL;

        public static RegistrationResultsFormat getValue(String name) {
            if (name == null) {
                return null;
            }

            if (name.equals("course")) {
                return COURSE_SUMMARY;
            } else if (name.equals("activity")) {
                return ACTIVITY_SUMMARY;
            } else if (name.equals("full")) {
                return FULL_DETAIL;
            } else {
                for (RegistrationResultsFormat value : RegistrationResultsFormat.values()) {
                    if (value.name().equalsIgnoreCase(name)) {
                        return value;
                    }
                }
                return null;
            }
        }

        public String toString() {
            if (this == COURSE_SUMMARY) {
                return "course";
            } else if (this == ACTIVITY_SUMMARY) {
                return "activity";
            } else if (this == FULL_DETAIL) {
                return "full";
            } else {
                return this.name();
            }
        }
    }

    public enum DataFormat {
        XML, JSON
    }

    public enum RegistrationResultsAuthType {
        FORM, HTTPBASIC;

        public static RegistrationResultsAuthType getValue(String name) {
            if (name == null) {
                return null;
            }

            for (RegistrationResultsAuthType value : RegistrationResultsAuthType.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }

        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public enum ReportageNavPermission {
        NONAV, DOWNONLY, FREENAV;

        public static ReportageNavPermission getValue(String name) {
            if (name == null) {
                return null;
            }

            for (ReportageNavPermission value : ReportageNavPermission.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }

        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
