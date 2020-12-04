/**
 *
 */
package de.immomio.exporter.immoscout;

import de.is24.rest.api.export.api.ObjectApi.ObjectId;

/**
 * @author Johannes Hiemer.
 */
public class ImmomioObjectId implements ObjectId {

    private String objectId;

    public ImmomioObjectId(org.bson.types.ObjectId id) {
        this.objectId = "ext-".concat(id.toString());
    }

    public ImmomioObjectId(String id) {
        this.objectId = "ext-".concat(id);
    }

    @Override
    public String getId() {
        return this.objectId;
    }

}
