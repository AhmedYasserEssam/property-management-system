package PresentationLayer.UI;

import BusinessLayer.Controller.IPropertyController;
import BusinessLayer.Domain.Property;

/**
 * Bridge abstraction for property forms.
 */
public class PropertyFormUI {
    protected final IPropertyController propertyController;

    /**
     * Bridge constructor.
     */
    public PropertyFormUI(IPropertyController propertyController) {
        this.propertyController = propertyController;
    }

    /**
     * Legacy method kept for backward compatibility with scaffold usage.
     */
    public void submitAddProperty() {
        throw new UnsupportedOperationException(
                "Use submitAddProperty(address, type, ownerID) to delegate to the bridge controller.");
    }

    public Property submitAddProperty(String address, String type, int ownerID) {
        return propertyController.addProperty(address, type, ownerID);
    }

}