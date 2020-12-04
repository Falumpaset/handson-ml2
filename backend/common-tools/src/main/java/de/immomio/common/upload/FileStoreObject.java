/**
 *
 */
package de.immomio.common.upload;

import de.immomio.data.base.type.common.FileType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Getter
@Setter
public class FileStoreObject {

    private String url;

    private FileType type;

    private String identifier;

    private String extension;

    private boolean encrypted;

    private String name;

    public FileStoreObject() {
        super();
    }

    public FileStoreObject(String url, FileType type, String identifier, String extension) {
        super();
        this.url = url;
        this.type = type;
        this.identifier = identifier;
        this.extension = extension;
    }

    public static FileStoreObject parse(String url) {
        String file = url.substring(url.lastIndexOf("/"), url.length());

        String extension = file.substring(file.lastIndexOf(".") + 1, file.length());
        FileType type = FileType.valueOf(file.substring(1, file.indexOf("-")));
        String identifier = file.substring(file.indexOf("-") + 1, file.lastIndexOf("."));

        return new FileStoreObject(url, type, identifier, extension);
    }

}
