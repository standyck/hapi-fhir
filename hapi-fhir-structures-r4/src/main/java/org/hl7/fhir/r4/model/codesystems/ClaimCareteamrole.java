package org.hl7.fhir.r4.model.codesystems;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ClaimCareteamrole {

        /**
         * The primary care provider.
         */
        PRIMARY, 
        /**
         * Assisting care provider.
         */
        ASSIST, 
        /**
         * Supervising care provider.
         */
        SUPERVISOR, 
        /**
         * Other role on the care team.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimCareteamrole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("primary".equals(codeString))
          return PRIMARY;
        if ("assist".equals(codeString))
          return ASSIST;
        if ("supervisor".equals(codeString))
          return SUPERVISOR;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown ClaimCareteamrole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRIMARY: return "primary";
            case ASSIST: return "assist";
            case SUPERVISOR: return "supervisor";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/claimcareteamrole";
        }
        public String getDefinition() {
          switch (this) {
            case PRIMARY: return "The primary care provider.";
            case ASSIST: return "Assisting care provider.";
            case SUPERVISOR: return "Supervising care provider.";
            case OTHER: return "Other role on the care team.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRIMARY: return "Primary provider";
            case ASSIST: return "Assisting Provider";
            case SUPERVISOR: return "Supervising Provider";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}
