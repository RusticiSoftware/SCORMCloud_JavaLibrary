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

package com.rusticisoftware.hostedengine.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class RequestSigner {
	
	private static List<String> excludedParams = Arrays.asList(new String[]{ "sig", "filedata" });
	public static boolean isExcludedParam(String paramName){
		return excludedParams.contains(paramName);
	}
	
	// Return a hex string representing the MD5 hash of the secret key and request params
	// *In a client library, the secret key could be made into a class member of this class.
	//public static String getSignatureForRequest(Map requestParams, String secretKey) throws WebServiceException
	public static String getSignatureForRequest(Map requestParams, String secretKey) throws Exception
	{	
		try {
			String serializedRequestString = getSerializedParams(requestParams);
			
			String signatureParts = secretKey + serializedRequestString;
			byte[] encodedSigParts = signatureParts.getBytes("UTF8");
			byte[] generatedSignature = MessageDigest.getInstance("MD5").digest(encodedSigParts);

			return getHexString(generatedSignature);
			
		} catch (NoSuchAlgorithmException nsae) {
			return "";
		} catch (UnsupportedEncodingException uee) {
			return "";
		}
	}
	
	// Return the serialized request string. 
	public static String getSerializedParams(Map<String, String[]> requestParams)
	{	
		StringBuilder paramString = new StringBuilder();
		List<String> paramNames = new ArrayList<String>(requestParams.keySet());
		Collections.sort(paramNames, String.CASE_INSENSITIVE_ORDER);
		
		for (String paramName : paramNames) {			
			if (!isExcludedParam(paramName)){				
				paramString.append(paramName);
				
				Object val = requestParams.get(paramName);
				
				// Allow the value to be a single string or
				// an array of strings as is returned by
				// request.getParameterMap()
				if (val instanceof String[]) {	
					List<String> valList = Arrays.asList((String[])val);
					Collections.sort(valList);
					for (String v : valList){
						paramString.append(v);
					}
				} else {
					paramString.append((String)val);
				}
			}
		}
		return paramString.toString();
	}
	
	// Return a hex string representation of the passed in byte array
	protected static String getHexString(byte[] input){
		char hexDigit[] = {
	       '0', '1', '2', '3', '4', '5', '6', '7',
	       '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
	    };
	    
		StringBuilder hexString = new StringBuilder();
		for (byte b : input){
			char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
	    	hexString.append(array);
		}
		return hexString.toString();
	}
	
}
