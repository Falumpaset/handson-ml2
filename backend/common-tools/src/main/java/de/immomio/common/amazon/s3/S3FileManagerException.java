package de.immomio.common.amazon.s3;

/**
 * @author Johannes Hiemer.
 */
public class S3FileManagerException extends Exception {

    private static final long serialVersionUID = 1743614257530625320L;

    public S3FileManagerException(Exception e) {
        super(e);
    }

    public S3FileManagerException(String s) {
        super(s);
    }

}
