package de.immomio.importer.worker.is24;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Immoscout24ImportStatistics {

    private int propertyCount = 0;

    private int filesSkippedCount = 0;

    private int filesCount = 0;

    private int excludedCount = 0;

    public void increasePropertyCount() {
        this.propertyCount++;
    }

    public void increaseFilesSkippedCount() {
        this.filesSkippedCount++;
    }

    public void increaseFilesCount() {
        this.filesCount++;
    }

    public void increaseExcludedCount() {
        this.excludedCount++;
    }

    public void increasePropertyCount(int value) {
        this.propertyCount += value;
    }

    public void increaseFilesSkippedCount(int value) {
        this.filesSkippedCount += value;
    }

    public void increaseFilesCount(int value) {
        this.filesCount += value;
    }

    public void increaseExcludedCount(int value) {
        this.excludedCount += value;
    }

}
