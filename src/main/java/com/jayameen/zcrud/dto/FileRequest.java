package com.jayameen.zcrud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Madan KN
 */

@RequiredArgsConstructor
public @Data class FileRequest implements Serializable {

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("is_private")
    private Boolean isPrivate = true;

    @JsonProperty("base64_data")
    private String base64Data;

    @JsonProperty("byte_array")
    private byte[] byteArray;

}
