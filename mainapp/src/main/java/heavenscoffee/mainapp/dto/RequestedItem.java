package heavenscoffee.mainapp.dto;

public class RequestedItem {
    private String productId;
    private int quantity;

    // Getters and setters
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

