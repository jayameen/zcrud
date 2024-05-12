package com.jayameen.zcrud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.List;

/**
 * @author Madan KN
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BulkDocumentsDto {
    private List<Document> documents;
}
