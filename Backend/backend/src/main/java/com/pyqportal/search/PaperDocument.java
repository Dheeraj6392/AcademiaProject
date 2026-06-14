package com.pyqportal.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "papers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaperDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String subject;

    @Field(type = FieldType.Keyword)
    private String branch;

    @Field(type = FieldType.Integer)
    private Integer year;

    @Field(type = FieldType.Keyword)
    private String examType;

    /** Full text extracted from PDF by PDFBox */
    @Field(type = FieldType.Text, analyzer = "english")
    private String content;
}
