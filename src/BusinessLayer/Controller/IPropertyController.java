package BusinessLayer.Controller;

import BusinessLayer.Domain.Property;

/**
 * Bridge implementor interface for property use cases.
 */
public interface IPropertyController {
    Property addProperty(String address, String type, int ownerID);
}