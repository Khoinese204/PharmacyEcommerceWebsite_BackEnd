// package com.example.pharmacywebsite.designpattern.CoR;

// import com.example.pharmacywebsite.designpattern.Decorator.BasePriceCalculator;
// import com.example.pharmacywebsite.designpattern.Decorator.DiscountDecorator;
// import com.example.pharmacywebsite.designpattern.Decorator.InsuranceDecorator;
// import com.example.pharmacywebsite.designpattern.Decorator.MedicinePriceCalculator;
// import com.example.pharmacywebsite.designpattern.Decorator.VatDecorator;
// import com.example.pharmacywebsite.domain.Medicine;
// import com.example.pharmacywebsite.domain.Order;
// import com.example.pharmacywebsite.dto.CartItemDto;
// import com.example.pharmacywebsite.repository.MedicineRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// @Service
// @RequiredArgsConstructor
// public class PriceCalculationHandler extends OrderHandler {

//     private final MedicineRepository medicineRepo;

//     @Override
//     public void handle(OrderContext context) {
//         double total = 0.0;

//         for (CartItemDto item : context.getCart().getItems()) {
//             Medicine medicine = medicineRepo.findById(item.getMedicineId())
//                     .orElseThrow(() -> new RuntimeException("Medicine not found: ID = " + item.getMedicineId()));

//             // Bắt đầu với giá gốc
//             MedicinePriceCalculator calculator = new BasePriceCalculator(medicine);

//             // Áp dụng giảm giá nếu có (ví dụ: 15%)
//             calculator = new DiscountDecorator(calculator, 15);

//             // Áp dụng thuế VAT (ví dụ: 10%)
//             calculator = new VatDecorator(calculator, 10);

//             // Áp dụng bảo hiểm y tế nếu người dùng có
//             // if (context.isHasInsurance()) {
//             // calculator = new InsuranceDecorator(calculator, 20); // giảm thêm 20% nếu có
//             // BHYT
//             // }

//             // Tính giá cuối cùng cho một sản phẩm
//             double finalPrice = calculator.calculate();
//             total += finalPrice * item.getQuantity();
//         }

//         // Cập nhật tổng giá vào đơn hàng
//         Order order = context.getOrder();
//         order.setTotalPrice(total);

//         // Gọi handler tiếp theo nếu có
//         if (next != null) {
//             next.handle(context);
//         }
//     }
// }
