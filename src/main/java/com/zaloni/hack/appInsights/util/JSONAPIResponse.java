package com.zaloni.hack.appInsights.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaloni.hack.appInsights.dto.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONAPIResponse<T> {

     private static final Logger LOGGER = LoggerFactory.getLogger(JSONAPIResponse.class);
     
     /**
      * Depicts message from server to client whether it's for success or failure
      */
     private String responseMessage = null;
     /**
      * Captures the URI location of the invoked REST service
      */
     private String restUri = null;
     /**
      * Captures the result of the service invocation.
      */
     private T result = null;

     /**
      * Captures the pagination information of a type. This is useful when returning lists.
      */
     private Page page = null;

     public JSONAPIResponse() {
         this (null, null);
     }
     
     public JSONAPIResponse(T result){
    	 this(null,result);
     }
     
     public JSONAPIResponse(String responseMessage, T result) {
    	 this (responseMessage, result, null);
     }
     
     public JSONAPIResponse(String responseMessage, T result, String restUri) {
    	 this (responseMessage, result, restUri, null);
     }
     
     public JSONAPIResponse(String responseMessage, T result, String restUri, Page page) {
         this.responseMessage =  responseMessage;
         this.restUri = restUri;
         this.result=result;
         this.page = page;
     }

     public T getResult() {
          return result;
     }

     public void setResult(T result) {
          this.result = result;
     }

     public String getResponseMessage() {
          return responseMessage;
     }

     public void setResponseMessage(String responseMessage) {
          this.responseMessage = responseMessage;
     }
     
     public String getRestUri() {
    	 return restUri;
     }

     public void setRestUri(String restUri) {
    	 this.restUri = restUri;
     }

     public Page getPage() {
		return page;
     }

     public void setPage(Page page) {
		this.page = page;
     }
     
     @Override
     public String toString() {

          ObjectMapper objectMapper=new ObjectMapper();
          try {
               return objectMapper.writeValueAsString(this);
          } catch (JsonProcessingException e) {
               LOGGER.error("Error converting JSON ",e);
          }
          return null;

     }
}
