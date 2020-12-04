package de.immomio.exporter.config;

public enum FtpType {
    FTP,
    SFTP,
    FTPS;

    public static FtpType getByName(String value) {
        for (FtpType type : values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
