package Lib;
import java.util.ArrayList;
public class ShoppingCart {
    private final ArrayList<CartItem> items;
    private final PricingService pricingService;
    private final ProductCatalog productCatalog;

    /**
     * สร้าง ShoppingCart โดยรับบริการ PricingService และ ProductCatalog
     */
    public ShoppingCart(PricingService pricingService, ProductCatalog productCatalog) {
        this.pricingService = pricingService;
        this.productCatalog = productCatalog;
        this.items = new ArrayList<>();
        checkRep();
    }

    /**
     * เพิ่มสินค้าเข้าสู่ตะกร้า
     * @param productId รหัสสินค้า
     * @param quantity จำนวน
     */
    public void addItem(String productId, int quantity) {
        if (quantity <= 0) return;

        Product product = productCatalog.findById(productId);
        if (product == null) return;

        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.increaseQuantity(quantity);
                checkRep();
                return;
            }
        }

        items.add(new CartItem(product, quantity));
        checkRep();
    }

    /**
     * ลบสินค้าจากตะกร้า
     * @param productId รหัสสินค้า
     */
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getProductId().equals(productId));
        checkRep();
    }

    /**
     * คืนราคาสุทธิทั้งหมดของสินค้าในตะกร้า
     * @return ราคารวมสุทธิ
     */
    public double getTotalPrice() {
        double total = 0.0;
        for (CartItem item : items) {
            total += pricingService.calculateItemPrice(item);
        }
        return total;
    }

    /**
     * ล้างตะกร้าทั้งหมด
     */
    public void clearCart() {
        items.clear();
        checkRep();
    }

    /**
     * จำนวนสินค้าในตะกร้า (รายการที่แตกต่างกัน)
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * ตรวจสอบเงื่อนไข Representation Invariant
     */
    private void checkRep() {
        if (items == null) {
            throw new IllegalStateException("Items list must not be null.");
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) == null) {
                throw new IllegalStateException("CartItem must not be null.");
            }

            for (int j = i + 1; j < items.size(); j++) {
                if (items.get(i).getProduct().getProductId()
                        .equals(items.get(j).getProduct().getProductId())) {
                    throw new IllegalStateException("Duplicate product found in cart.");
                }
            }
        }
    }
}
