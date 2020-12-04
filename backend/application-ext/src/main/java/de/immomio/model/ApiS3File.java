package de.immomio.model;

import de.immomio.model.selfdisclosure.ApiFileType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.io.Serializable;

/**
 * @author Bastian Bliemeister, Maik Kingma
 */

@Getter
@Setter
@EqualsAndHashCode
public class ApiS3File implements Serializable {

    private static final long serialVersionUID = -6134895717277855713L;

    @NotNull
    @Min(value = 1, message = "POSITIVE_INDEXES_ONLY_L")
    private Long index;

    private String title;

    private ApiFileType type;

    private String url;

    private String identifier;

    private String extension;

    private boolean encrypted;

    private String name;

    @Transient
    public String getFilename() {
        StringBuilder sb = new StringBuilder();

        sb.append(type);
        sb.append("-");
        sb.append(identifier);
        sb.append(".");
        sb.append(extension);

        return sb.toString();
    }

    public Long getIndex() {
        if (index == null) {
            return 0L;
        } else {
            return index;
        }
    }

    public static ApiS3File parse(String url) {
        String fileName = url.substring(url.lastIndexOf('/'), url.length());

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
        ApiFileType type = ApiFileType.valueOf(fileName.substring(1, fileName.indexOf('-')).toUpperCase());
        String identifier = fileName.substring(fileName.indexOf('-') + 1, fileName.lastIndexOf('.'));
        ApiS3File file = new ApiS3File();
        file.setType(type);
        file.setUrl(url);
        file.setExtension(extension);
        file.setIdentifier(identifier);
        file.setEncrypted(false);
        //TODO set the correct file title
        file.setTitle("");

        return file;
    }
}
