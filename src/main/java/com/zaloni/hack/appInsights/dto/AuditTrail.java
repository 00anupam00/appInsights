package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zaloni.hack.appInsights.util.JodaDateTimeJsonDeserializer;
import com.zaloni.hack.appInsights.util.JodaDateTimeJsonSerializer;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;

@Document(indexName="audit", type="audittrail")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditTrail {

	@Id
	@Field(type = FieldType.Text)
	private String id;
	@Field(type = FieldType.Text, index = false)
	private String logType;
	@Field(type = FieldType.Text, index = false)
	private String logTypeName;
	@Field(type = FieldType.Text, index = false)
	private String userId;

	@JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	@JsonDeserialize(using = JodaDateTimeJsonDeserializer.class)
	@Field(type = FieldType.Date, format=DateFormat.date_optional_time, index = false)
	private DateTime logTime;

	@Field(type = FieldType.Nested)
	private List<AuditTrailField> auditTrailFields;
	
	private String artifactType;
	
	private String operationType;
	
	private String artifactId;
	
	private Integer projectId;
	
	private String businessName;
	
	public AuditTrail() {
		
	}
	
	/**
	 * @param auditTrailBuilder
	 */
	public AuditTrail(Builder auditTrailBuilder) {
		this.id = auditTrailBuilder.id;
		this.logType = auditTrailBuilder.logType;
		this.logTypeName = auditTrailBuilder.logTypeName;
		this.userId = auditTrailBuilder.userId;
		this.logTime = auditTrailBuilder.logTime;
		this.auditTrailFields = auditTrailBuilder.auditTrailFields;
		this.artifactType = auditTrailBuilder.artifactType;
		this.artifactId = auditTrailBuilder.artifactId;
		this.operationType = auditTrailBuilder.operationType;
		this.businessName = auditTrailBuilder.businessName;
		this.projectId = auditTrailBuilder.projectId;
	}

	public String getId() {
		return id;
	}

	public String getLogType() {
		return logType;
	}
	
	public String getLogTypeName() {
		return logTypeName;
	}

	public String getUserId() {
		return userId;
	}

	public DateTime getLogTime() {
		return logTime;
	}

	public List<AuditTrailField> getAuditTrailFields() {
		return auditTrailFields;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuditTrail [id=").append(id).append(", logType=").append(logType).append(", logTypeName=")
				.append(logTypeName).append(", userId=").append(userId).append(", logTime=").append(logTime)
				.append(", auditTrailFields=").append(auditTrailFields).append("]");
		return builder.toString();
	}

	public static class Builder {
		private final String id;
		private final String logType;
		private final String logTypeName;
		private final String userId;
		private final DateTime logTime;
		private final List<AuditTrailField> auditTrailFields;
		
		private final String artifactType;
	     
	     private final String operationType;
	     
	     private final String artifactId;
	     
	     private final String businessName;
	     
	     private final Integer projectId;

		/**
		 * @param id
		 * @param logType
		 * @param userId
		 * @param logTime
		 * @param auditTrailFields
		 * @param logTypeName
		 */
		public Builder(String id, String logType, String userId, DateTime logTime,
				List<AuditTrailField> auditTrailFields, String logTypeName ) {
			super();
			this.id = id;
			this.logType = logType;
			this.logTypeName = logTypeName;
			this.userId = userId;
			this.logTime = logTime;
			this.auditTrailFields = auditTrailFields;
			this.artifactType = null;
	          this.artifactId = null;
	          this.operationType = null;
	          this.businessName= null;
	          this.projectId = null;
		}
		
		public Builder(String id, String logType, String userId, DateTime logTime,
                    List<AuditTrailField> auditTrailFields, String logTypeName, String artifactType,String operationType, String artifactId, String businessName, Integer projectId ) {
               super();
               this.id = id;
               this.logType = logType;
               this.logTypeName = logTypeName;
               this.userId = userId;
               this.logTime = logTime;
               this.auditTrailFields = auditTrailFields;
               this.artifactType = artifactType;
               this.artifactId = artifactId;
               this.operationType = operationType;
               this.businessName = businessName;
               this.projectId = projectId;
          }
		
		public AuditTrail build() {
			return new AuditTrail(this);
		}
	}

     public String getArtifactType() {
          return artifactType;
     }

     public void setArtifactType(String artifactType) {
          this.artifactType = artifactType;
     }

     public String getOperationType() {
          return operationType;
     }

     public void setOperationType(String operationType) {
          this.operationType = operationType;
     }

     public String getArtifactId() {
          return artifactId;
     }

     public void setArtifactId(String artifactId) {
          this.artifactId = artifactId;
     }

     public Integer getProjectId() {
          return projectId;
     }

     public void setProjectId(Integer projectId) {
          this.projectId = projectId;
     }

     public String getBusinessName() {
          return businessName;
     }

     public void setBusinessName(String businessName) {
          this.businessName = businessName;
     }

}
