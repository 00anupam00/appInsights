package com.zaloni.hack.appInsights.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.StringJoiner;

@Document(indexName = "hack19", type = "app_insights")
public class Insight {

    @Id
    private String docId;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Object doc;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Object getDoc() {
        return doc;
    }

    public void setDoc(Object doc) {
        this.doc = doc;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Insight.class.getSimpleName() + "[", "]")
                .add("docId='" + docId + "'")
                .add("doc=" + doc)
                .toString();
    }
}
