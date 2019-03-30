package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 
 */
@Document(indexName = "audit", type = "audittrail")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecAuditTypeFieldsVO implements java.io.Serializable {

     private static final long serialVersionUID = 8504696642349406116L;

     public enum DataType {
          STRING, INT, FLOAT
     }
     @Field(type = FieldType.Long, index = false)
     private Integer id;
     @Field(type = FieldType.Text, index = false)
     private String technicalName;
     @Field(type = FieldType.Text, index = false)
     private String businessName;
     @Field(type = FieldType.Text, index = false)
     private DataType dataType;

     public static long getSerialVersionUID() {
          return serialVersionUID;
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getTechnicalName() {
          return technicalName;
     }

     public void setTechnicalName(String technicalName) {
          this.technicalName = technicalName;
     }

     public String getBusinessName() {
          return businessName;
     }

     public void setBusinessName(String businessName) {
          this.businessName = businessName;
     }

     public DataType getDataType() {
          return dataType;
     }

     public void setDataType(DataType dataType) {
          this.dataType = dataType;
     }

     @Override
     public String toString() {
          StringBuilder builder = new StringBuilder();
          builder.append("SecAuditTypeFieldsVO [id=").append(id).append(", technicalName=").append(technicalName)
                    .append(", businessName=").append(businessName).append(", dataType=").append(dataType).append("]");
          return builder.toString();
     }
}
