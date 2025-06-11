package com.example.pharmacywebsite.seed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.pharmacywebsite.domain.Category;
import com.example.pharmacywebsite.domain.Medicine;
import com.example.pharmacywebsite.domain.MedicineDetail;
import com.example.pharmacywebsite.enums.MedicineDetailType;
import com.example.pharmacywebsite.repository.CategoryRepository;
import com.example.pharmacywebsite.repository.MedicineDetailRepository;
import com.example.pharmacywebsite.repository.MedicineRepository;

import lombok.RequiredArgsConstructor;

@Component
@Order(2) // chạy sau CategorySeeder
@RequiredArgsConstructor
public class MedicineSeeder implements CommandLineRunner {

        private final MedicineRepository medicineRepository;
        private final MedicineDetailRepository medicineDetailRepository;
        private final CategoryRepository categoryRepository;

        @Override
        public void run(String... args) throws Exception {

                if (medicineRepository.count() == 0) {
                        Medicine medicine = new Medicine();
                        medicine.setName(
                                        "Siro ống uống Canxi-D3-K2 5ml Kingphar bổ sung canxi & vitamin D3 cho cơ thể (6 vỉ x 5 ống)");
                        medicine.setPrice(105000.0);
                        medicine.setOriginalPrice(105000.0);
                        medicine.setUnit("Hộp");
                        medicine.setShortDescription("Bổ sung canxi & vitamin D3 cho cơ thể");
                        medicine.setBrandOrigin("Kingphar");
                        medicine.setManufacturer("CÔNG TY TNHH SẢN XUẤT VÀ THƯƠNG MẠI VINH THỊNH VƯỢNG");
                        medicine.setCountryOfManufacture("Việt Nam");
                        medicine.setImageUrl("product1.jpg");

                        Category category = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine.setCategory(category);

                        medicine = medicineRepository.save(medicine);

                        List<MedicineDetail> details = new ArrayList<>();

                        details.add(createDetail(medicine, MedicineDetailType.INGREDIENT,
                                        "Calci glucoheptonat 550mg, Vitamin D3 100iu, Vitamin K2 10mcg"));
                        details.add(createDetail(medicine, MedicineDetailType.EFFECT,
                                        "Hỗ trợ bổ sung canxi, vitamin D3, K2 cho cơ thể, giảm nguy cơ thiếu hụt."));
                        details.add(createDetail(medicine, MedicineDetailType.USAGE,
                                        "Trẻ 1-6 tuổi: 5ml/lần x 2 lần/ngày. Người lớn: 10ml/lần x 2 lần/ngày. Lắc đều trước khi dùng."));
                        details.add(createDetail(medicine, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details.add(createDetail(medicine, MedicineDetailType.NOTE,
                                        "Không dùng cho người dị ứng, sỏi thận, tiểu đường cần hỏi ý kiến bác sĩ."));
                        details.add(createDetail(medicine, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, tránh ánh sáng, dưới 30°C, xa tầm tay trẻ em."));
                        details.add(createDetail(medicine, MedicineDetailType.DESCRIPTION,
                                        "Bổ sung canxi & vitamin D3 cho cơ thể"));

                        medicineDetailRepository.saveAll(details);

                        Medicine medicine2 = new Medicine();
                        medicine2.setName(
                                        "Viên uống Prenatal One Vitamins For Life cung cấp DHA, Vitamin và khoáng chất thiết yếu (30 viên)");
                        medicine2.setPrice(292000.0);
                        medicine2.setOriginalPrice(365000.0);
                        medicine2.setUnit("Hộp");
                        medicine2.setShortDescription("Cung cấp DHA, Vitamin và khoáng chất thiết yếu");
                        medicine2.setBrandOrigin("Vitamins For Life");
                        medicine2.setManufacturer("VITAMINS FOR LIFE LABORATORIES");
                        medicine2.setCountryOfManufacture("Hoa Kỳ");
                        medicine2.setImageUrl(
                                        "product2.jpg");

                        // Gắn cùng Category như thuốc 1 (id = 2)
                        medicine2.setCategory(category);

                        medicine2 = medicineRepository.save(medicine2);

                        List<MedicineDetail> details2 = new ArrayList<>();

                        details2.add(createDetail(medicine2, MedicineDetailType.INGREDIENT,
                                        "Vitamin D3 400iu, Calcium Carbonate 110mg, Iron 45mg, Magnesium 25mg, Vitamin B3 18mg, Vitamin E 10iu, Vitamin B5 6mg, Vitamin B6 1.9mg, Acid folic 1mg"));
                        details2.add(createDetail(medicine2, MedicineDetailType.EFFECT,
                                        "Prenatal One cung cấp DHA, vitamin và khoáng chất thiết yếu, giúp cân bằng dinh dưỡng cho phụ nữ đang mang thai và sau khi sinh."));
                        details2.add(createDetail(medicine2, MedicineDetailType.USAGE,
                                        "Cách dùng: Uống 1 viên mỗi ngày.\nĐối tượng sử dụng: Phụ nữ mang thai và sau khi sinh."));
                        details2.add(createDetail(medicine2, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details2.add(createDetail(medicine2, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người mẫn cảm với bất cứ thành phần nào của sản phẩm.\nPhụ nữ đang cho con bú hoặc đang dùng bất kỳ loại thuốc nào cần tham khảo ý kiến bác sĩ trước khi dùng.\nSản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\nĐọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details2.add(createDetail(medicine2, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, tránh ánh nắng trực tiếp từ mặt trời. Để xa tầm tay trẻ em."));
                        details2.add(createDetail(medicine2, MedicineDetailType.DESCRIPTION,
                                        "Cung cấp DHA, Vitamin và khoáng chất thiết yếu"));

                        medicineDetailRepository.saveAll(details2);

                        Medicine medicine3 = new Medicine();
                        medicine3.setName(
                                        "Viên uống Golex Ocavill hỗ trợ chống lại sự phình to của tuyến tiền liệt, cải thiện các rối loạn tiểu tiện (30 viên)");
                        medicine3.setPrice(396000.0);
                        medicine3.setOriginalPrice(495000.0);
                        medicine3.setUnit("Hộp");
                        medicine3.setShortDescription(
                                        "Hỗ trợ chống lại sự phình to của tuyến tiền liệt, cải thiện các rối loạn tiểu tiện");
                        medicine3.setBrandOrigin("Vitamins For Life");
                        medicine3.setManufacturer("THE OXFORD HEALTH COMPANY LTD");
                        medicine3.setCountryOfManufacture("Anh");
                        medicine3.setImageUrl(
                                        "product3.jpg");

                        Category category3 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine3.setCategory(category3);

                        medicine3 = medicineRepository.save(medicine3);

                        List<MedicineDetail> details3 = new ArrayList<>();
                        details3.add(createDetail(medicine3, MedicineDetailType.INGREDIENT,
                                        "Chiết xuất Cây cọ lùn (Saw Palmetto) 650mg\nHạt bí ngô 600mg\nTầm ma 160mg\nKẽm 15mg\nLycopene 10mg\nVitamin E 10mg\nVitamin B6 1.5mg"));
                        details3.add(createDetail(medicine3, MedicineDetailType.EFFECT,
                                        "Golex hỗ trợ chống lại sự phình to của tuyến tiền liệt, cải thiện các rối loạn tiểu tiện ở bệnh nhân phì đại tiền liệt tuyến: Tiểu đêm, tiểu buốt, tiểu không hết, tiểu nhiều lần.\nHạn chế sự phát triển của u xơ tiền liệt tuyến lành tính hoặc sử dụng để hỗ trợ sau phẫu thuật."));
                        details3.add(createDetail(medicine3, MedicineDetailType.USAGE,
                                        "Cách dùng\nUống 2 viên mỗi ngày, uống sau bữa ăn.\nĐối tượng sử dụng\nGolex dùng trong các trường hợp:\n• Nam giới ở tuổi trung và cao niên, có các triệu chứng như rối loạn tiểu tiện như: Tiểu đêm, tiểu buốt, tiểu rắt, tiểu không hết, tiểu nhiều lần, tia nước tiểu yếu...\n• Nam giới bị u xơ tiền liệt tuyến lành tính hoặc sử dụng để hỗ trợ sau phẫu thuật."));
                        details3.add(createDetail(medicine3, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details3.add(createDetail(medicine3, MedicineDetailType.NOTE,
                                        "Không dùng cho người mẫn cảm với bất kỳ thành phần nào của sản phẩm.\nNgười đang dùng thuốc, phụ nữ mang thai hoặc cho con bú, hỏi ý kiến thầy thuốc trước khi dùng.\nThực phẩm bảo vệ sức khỏe, không thể thay thế chế độ ăn uống đa dạng và cân bằng.\nKhông dùng cho người dưới 18 tuổi.\nNgừng sử dụng và hỏi ý kiến bác sĩ nếu có phản ứng bất thường.\nKhông dùng quá liều khuyến cáo mỗi ngày.\nSản phẩm này không nhằm mục đích chữa bệnh, ngăn ngừa, chẩn đoán bệnh nào.\nĐọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details3.add(createDetail(medicine3, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, nhiệt độ không quá 30 độ C, tránh ánh sáng.\nĐể xa tầm tay trẻ em."));
                        details3.add(createDetail(medicine3, MedicineDetailType.DESCRIPTION,
                                        "Hỗ trợ chống lại sự phình to của tuyến tiền liệt, cải thiện các rối loạn tiểu tiện"));

                        medicineDetailRepository.saveAll(details3);

                        Medicine medicine4 = new Medicine();
                        medicine4.setName("Siro Ginkid Ho Cam NEW 80ml hỗ trợ bổ phế, giảm ho, nhuận phế");
                        medicine4.setPrice(59000.0);
                        medicine4.setOriginalPrice(59000.0);
                        medicine4.setUnit("Hộp");
                        medicine4.setShortDescription("Hỗ trợ bổ phế, giảm ho, nhuận phế");
                        medicine4.setBrandOrigin("GINKID");
                        medicine4.setManufacturer("CÔNG TY CỔ PHẦN DƯỢC PHẨM CÔNG NGHỆ CAO ABIPHA");
                        medicine4.setCountryOfManufacture("Việt Nam");
                        medicine4.setImageUrl(
                                        "product4.jpg");

                        Category category4 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine4.setCategory(category4);

                        medicine4 = medicineRepository.save(medicine4);

                        List<MedicineDetail> details4 = new ArrayList<>();
                        details4.add(createDetail(medicine4, MedicineDetailType.INGREDIENT,
                                        "Húng chanh 62.5mg\nMật ong 11250mg\nThymomodulin 312.5mg\nXuyên bối mẫu 7800mg\nTrần bì 7800mg\nKim ngân hoa 7800mg\nTang bạch bì 7800mg\nCát cánh 7800mg\nBách bộ 7800mg\nTỳ bà diệp 7800mg"));
                        details4.add(createDetail(medicine4, MedicineDetailType.EFFECT,
                                        "Ginkid Ho Cam hỗ trợ bổ phế, giảm ho, nhuận phế.\nHỗ trợ giảm các triệu chứng: Ho, cảm lạnh, hắt hơi, sổ mũi, ho do lạnh."));
                        details4.add(createDetail(medicine4, MedicineDetailType.USAGE,
                                        "Cách dùng\nTrẻ em 6 tháng đến 1 tuổi: Uống 2,5ml/lần x 2 lần/ngày.\nTrẻ em từ 1 - 3 tuổi: Uống 5ml/lần x 2 lần/ngày.\nTrẻ em từ 3 - 6 tuổi: Uống 10ml/lần x 2 lần/ngày.\nTrẻ em từ 6 - 12 tuổi: Uống 10ml/lần x 2 - 3 lần/ngày.\nTrẻ em từ 12 tuổi trở lên: Uống 15ml/lần x 2 lần/ngày.\n\nĐối tượng sử dụng\nGinkid Ho Cam dùng cho trẻ em từ 6 tháng tuổi trở lên trong các trường hợp:\n• Trẻ em ho do lạnh, ho do dị ứng thời tiết, ho gió, ho khan, viêm họng, viêm phế quản, sưng đau rát họng, khàn tiếng.\n• Trẻ bị cảm lạnh, hắt hơi, sổ mũi."));
                        details4.add(createDetail(medicine4, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details4.add(createDetail(medicine4, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người mẫn cảm với bất cứ thành phần nào của sản phẩm.\nKhông dùng quá liều khuyến cáo.\nSản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\nĐọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details4.add(createDetail(medicine4, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, nhiệt độ không quá 30 độ C, tránh ánh sáng.\nĐể xa tầm tay trẻ em."));
                        details4.add(createDetail(medicine4, MedicineDetailType.DESCRIPTION,
                                        "Ginkid Ho Cam là sản phẩm bảo vệ sức khỏe giúp bổ phế, giảm ho, nhuận phế, hỗ trợ giảm các triệu chứng ho, cảm lạnh, hắt hơi sổ mũi, ho do lạnh, đau họng. Sản phẩm có mùi vị thơm ngon, dễ uống, phù hợp với khẩu vị của trẻ nhỏ."));

                        medicineDetailRepository.saveAll(details4);

                        Medicine medicine5 = new Medicine();
                        medicine5.setName(
                                        "Bào tử lợi khuẩn Livespo DIA 30 hỗ trợ giảm các triệu chứng tiêu chảy cấp tính, rối loạn tiêu hoá (10 ống x 5ml)");
                        medicine5.setPrice(270000.0);
                        medicine5.setOriginalPrice(270000.0);
                        medicine5.setUnit("Hộp");
                        medicine5.setShortDescription(
                                        "Hỗ trợ giảm các triệu chứng tiêu chảy cấp tính, rối loạn tiêu hoá");
                        medicine5.setBrandOrigin("Livespo");
                        medicine5.setManufacturer("NHÀ MÁY SỐ 2 - CÔNG TY LIVESPO PHARMA");
                        medicine5.setCountryOfManufacture("Việt Nam");
                        medicine5.setImageUrl(
                                        "product5.jpg");

                        Category category5 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine5.setCategory(category5);

                        medicine5 = medicineRepository.save(medicine5);

                        List<MedicineDetail> details5 = new ArrayList<>();
                        details5.add(createDetail(medicine5, MedicineDetailType.INGREDIENT,
                                        "Bacillus coagulans\nBacillus subtilis\nBacillus clausii\n5000000000 bào tử/g"));
                        details5.add(createDetail(medicine5, MedicineDetailType.EFFECT,
                                        "LiveSpo DIA 30 hỗ trợ giảm các triệu chứng tiêu chảy cấp tính, rối loạn tiêu hoá.\nBổ sung lợi khuẩn ở dạng bào tử giúp hỗ trợ cân bằng hệ vi sinh đường ruột."));
                        details5.add(createDetail(medicine5, MedicineDetailType.USAGE,
                                        "Lắc kỹ ống trước khi dùng. Uống trước hoặc sau khi ăn.\n\n" +
                                                        "Người đang bị tiêu chảy và rối loạn tiêu hóa:\n" +
                                                        "Người lớn: Uống 2 ống/lần. Ngày 2 - 4 lần, mỗi lần cách nhau từ 3 giờ.\n"
                                                        +
                                                        "Trẻ từ 3 tháng tuổi trở lên: Uống 1 - 2 ống/lần. Ngày 2 - 4 lần, mỗi lần cách nhau từ 3 giờ.\n"
                                                        +
                                                        "Trẻ dưới 3 tháng tuổi: Uống theo chỉ dẫn của bác sĩ.\n\n" +
                                                        "Người duy trì cân bằng hệ vi sinh đường ruột:\n" +
                                                        "Uống 1 ống/lần. Ngày 3 lần vào các bữa ăn trong 1 tuần. Sau đó 1 ống/ngày trong ít nhất 1 tháng.\n\n"
                                                        +
                                                        "Đối tượng sử dụng:\n" +
                                                        "Trẻ em và người lớn bị rối loạn tiêu hoá, tiêu chảy cấp do loạn khuẩn đường ruột, ngộ độc thực phẩm, ngộ độc hóa chất.\n"
                                                        +
                                                        "Người muốn duy trì cân bằng hệ vi sinh đường ruột."));
                        details5.add(createDetail(medicine5, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details5.add(createDetail(medicine5, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người mẫn cảm với bất cứ thành phần nào của sản phẩm.\n" +
                                                        "Sản phẩm dùng được cho trẻ sơ sinh, phụ nữ mang thai và phụ nữ đang cho con bú.\n"
                                                        +
                                                        "Chỉ uống, không được tiêm.\n" +
                                                        "Có thể nhìn thấy các hạt nhỏ trong ống do sự tập hợp các bào tử lợi khuẩn Bacillus.\n"
                                                        +
                                                        "Không dùng quá liều khuyến cáo.\n" +
                                                        "Sản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\n"
                                                        +
                                                        "Đọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details5.add(createDetail(medicine5, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, nhiệt độ không quá 30 độ C, tránh ánh sáng.\n"
                                                        +
                                                        "Để xa tầm tay trẻ em."));
                        details5.add(createDetail(medicine5, MedicineDetailType.DESCRIPTION,
                                        "LiveSpo DIA 30 chứa 5 tỷ lợi khuẩn Bacillus Subtilis, Bacillus Clausii, Bacillus Coagulans cùng 5ml nước cất, ứng dụng công nghệ tiên tiến giúp giải quyết tiêu chảy cấp, rối loạn tiêu hóa nhanh hơn hẳn so với các sản phẩm thông thường."));

                        medicineDetailRepository.saveAll(details5);

                        Medicine medicine6 = new Medicine();
                        medicine6.setName("Siro Ginkid GINIC nhuận tràng, bổ sung chất xơ (3 vỉ x 5 ống x 10ml)");
                        medicine6.setPrice(100000.0);
                        medicine6.setOriginalPrice(100000.0);
                        medicine6.setUnit("Hộp");
                        medicine6.setShortDescription("Nhuận tràng, bổ sung chất xơ");
                        medicine6.setBrandOrigin("GINKID");
                        medicine6.setManufacturer("CÔNG TY CỔ PHẦN DƯỢC PHẨM CÔNG NGHỆ CAO ABIPHA");
                        medicine6.setCountryOfManufacture("Việt Nam");
                        medicine6.setImageUrl(
                                        "product6.jpg");

                        Category category6 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine6.setCategory(category6);

                        medicine6 = medicineRepository.save(medicine6);

                        List<MedicineDetail> details6 = new ArrayList<>();
                        details6.add(createDetail(medicine6, MedicineDetailType.INGREDIENT,
                                        "Tinh chất men bia 125mg\nInulin 15625mg\nFructooligosaccharides 15625mg\nPhan tả diệp 7813mg"));
                        details6.add(createDetail(medicine6, MedicineDetailType.EFFECT,
                                        "Ginkid Nhuận Tràng bổ sung chất xơ, giúp nhuận tràng giảm táo bón, giúp tiêu hóa tốt, hỗ trợ phát triển vi khuẩn có ích đường ruột."));
                        details6.add(createDetail(medicine6, MedicineDetailType.USAGE,
                                        "Cách dùng\nTrẻ em từ 1 - 3 tuổi: Uống 5ml/lần x 2 - 3 lần/ngày.\nTrẻ em từ 4 - 6 tuổi: Uống 10ml/lần x 2 lần/ngày.\nTrẻ em từ 6 - 9 tuổi: Uống 15ml/lần x 2 - 3 lần/ngày.\nTrẻ em từ 10 tuổi trở lên: Uống 20ml/lần x 2 lần/ngày.\n\n"
                                                        + "Đối tượng sử dụng\nGinkid Nhuận Tràng dùng cho trẻ em từ 1 tuổi trở lên trong các trường hợp sau:\n• Trẻ em bị táo bón, tiêu hóa kém.\n• Trẻ em không bổ sung đủ chất xơ trong chế độ ăn."));
                        details6.add(createDetail(medicine6, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details6.add(createDetail(medicine6, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người mẫn cảm với bất cứ thành phần nào của sản phẩm.\nKhông dùng quá liều khuyến cáo.\nSản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\nĐọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details6.add(createDetail(medicine6, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, nhiệt độ không quá 30 độ C, tránh ánh sáng.\nĐể xa tầm tay trẻ em."));
                        details6.add(createDetail(medicine6, MedicineDetailType.DESCRIPTION,
                                        "Ginkid Nhuận Tràng giúp bổ sung chất xơ, hỗ trợ hệ tiêu hóa, giúp giảm chứng táo bón, phân sống, đầy hơi, chướng bụng. "
                                                        + "Sản phẩm được cha mẹ tin dùng không chỉ bởi hiệu quả nó mang lại mà còn dễ dùng, dễ mang theo."));

                        medicineDetailRepository.saveAll(details6);

                        Medicine medicine7 = new Medicine();
                        medicine7.setName(
                                        "Viên uống Hato Gold Jpanwell cải thiện tim mạch, giảm khó thở, mệt mỏi, đau tức ngực (60 viên)");
                        medicine7.setPrice(796000.0);
                        medicine7.setOriginalPrice(995000.0);
                        medicine7.setUnit("Hộp");
                        medicine7.setShortDescription("Nhuận tràng, bổ sung chất xơ");
                        medicine7.setBrandOrigin("Jpanwell");
                        medicine7.setManufacturer("GENSEI CO.,LTD");
                        medicine7.setCountryOfManufacture("Nhật Bản");
                        medicine7.setImageUrl(
                                        "product7.jpg");

                        Category category7 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine7.setCategory(category7);

                        medicine7 = medicineRepository.save(medicine7);

                        List<MedicineDetail> details7 = new ArrayList<>();
                        details7.add(createDetail(medicine7, MedicineDetailType.INGREDIENT,
                                        "Nho 1mg\nBạch quả 1mg\nChiết xuất Maca 1mg\nVừng đen 5.24mg\nNattokinase 5.24mg\nVitamin B12 2mg\nVitamin B6 2mg\nVitamin B2 2mg\nVitamin B1 2mg\nEicosapentaenoic acid 1mg\nNhân sâm Hàn Quốc 21mg\nMagie 10mg\nDHA 9mg\nCoenzyme Q10 105mg"));
                        details7.add(createDetail(medicine7, MedicineDetailType.EFFECT,
                                        "Ginkid Nhuận Tràng bổ sung chất xơ, giúp nhuận tràng giảm táo bón, giúp tiêu hóa tốt, hỗ trợ phát triển vi khuẩn có ích đường ruột."));
                        details7.add(createDetail(medicine7, MedicineDetailType.USAGE,
                                        "Hato Gold hỗ trợ giúp trái tim khỏe mạnh.\nGiúp làm giảm khó thở, mệt mỏi, đau tức ngực.\nGiúp lưu thông máu ra vào tim, giảm ứ huyết tại tim, tăng lượng máu tới mạch vành, phục hồi chức năng tim.\nGiúp ngăn ngừa biến chứng nhồi máu cơ tim, suy tim hay rối loạn nhịp tim."));
                        details7.add(createDetail(medicine7, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details7.add(createDetail(medicine7, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người mẫn cảm với bất kỳ thành phần nào của sản phẩm.\nSản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\nĐọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details7.add(createDetail(medicine7, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, nhiệt độ không quá 30 độ C, tránh ánh sáng.\nĐể xa tầm tay trẻ em."));
                        details7.add(createDetail(medicine7, MedicineDetailType.DESCRIPTION,
                                        "Viên uống Hato Gold là sản phẩm bồi bổ sức khỏe tim mạch đến từ Nhật Bản, nổi tiếng tại thị trường nội địa với điểm ưu việt là chứa phong phú dưỡng chất bổ dưỡng cho trái tim như nhân sâm, nattokinase, CoQ10, nhân sâm maca, vừng đen, dầu cá và ginkgo biloba… Hato Gold không chỉ mang dưỡng chất bổ tim mà còn hỗ trợ cải thiện các vấn đề của tim, phòng ngừa các biến chứng tim mạch nguy hiểm."));

                        medicineDetailRepository.saveAll(details7);

                        Medicine medicine8 = new Medicine();
                        medicine8.setName(
                                        "Viên uống Co Enzyme Q10 & Evening primrose Thành Công hỗ trợ chức năng tim mạch (30 viên)");
                        medicine8.setPrice(141000.0);
                        medicine8.setOriginalPrice(141000.0);
                        medicine8.setUnit("Hộp");
                        medicine8.setShortDescription("Hỗ trợ chức năng tim mạch");
                        medicine8.setBrandOrigin("Thành Công");
                        medicine8.setManufacturer("CÔNG TY DƯỢC PHẨM VÀ THƯƠNG MẠI THÀNH CÔNG - TNHH");
                        medicine8.setCountryOfManufacture("Việt Nam");
                        medicine8.setImageUrl(
                                        "product8.jpg");

                        Category category8 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine8.setCategory(category8);

                        medicine8 = medicineRepository.save(medicine8);

                        List<MedicineDetail> details8 = new ArrayList<>();
                        details8.add(createDetail(medicine8, MedicineDetailType.INGREDIENT,
                                        "Tá dược vừa đủ\nVitamin E 5IU\nDầu cá 100mg\nTinh dầu hoa anh thảo 100mg\nCoenzyme Q10 40mg"));
                        details8.add(createDetail(medicine8, MedicineDetailType.EFFECT,
                                        "Co-Enzyme Q10 & Evening primrose là sản phẩm tốt cho sức khỏe tim mạch, giảm cholesterol máu, giảm nguy cơ xơ vữa mạch máu."));
                        details8.add(createDetail(medicine8, MedicineDetailType.USAGE,
                                        "Cách dùng\nNgười lớn uống 1 - 2 viên mỗi ngày sau khi ăn hoặc theo hướng dẫn của thầy thuốc và các chuyên gia dinh dưỡng.\n\n"
                                                        + "Đối tượng sử dụng\nCo-Enzyme Q10 & Evening primrose dùng trong các trường hợp:\n"
                                                        + "• Người đang bị hoặc có nguy cơ bị các bệnh về tim mạch như cholesterol trong máu cao, gây cao huyết áp, tiểu đường… đặc biệt ở người cao tuổi.\n"
                                                        + "• Người bị suy nhược cơ thể, mệt mỏi."));
                        details8.add(createDetail(medicine8, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details8.add(createDetail(medicine8, MedicineDetailType.NOTE,
                                        "Sản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\n"
                                                        + "Không sử dụng cho người có mẫn cảm với bất kỳ thành phần nào của sản phẩm.\n"
                                                        + "Đọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details8.add(createDetail(medicine8, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, tránh ánh nắng trực tiếp từ mặt trời.\n"
                                                        + "Để xa tầm tay trẻ em."));
                        details8.add(createDetail(medicine8, MedicineDetailType.DESCRIPTION,
                                        "Hỗ trợ chức năng tim mạch"));

                        medicineDetailRepository.saveAll(details8);

                        Medicine medicine9 = new Medicine();
                        medicine9.setName(
                                        "Sữa Nutifood Varna Colostrum 237ml hỗ trợ tăng cường sức đề kháng (24 chai)");
                        medicine9.setPrice(31500.0);
                        medicine9.setOriginalPrice(35000.0);
                        medicine9.setUnit("Chai");
                        medicine9.setShortDescription("Hỗ trợ tăng cường sức đề kháng");
                        medicine9.setBrandOrigin("NUTIFOOD");
                        medicine9.setManufacturer("CÔNG TY CỔ PHẦN THỰC PHẨM DINH DƯỠNG NUTIFOOD");
                        medicine9.setCountryOfManufacture("Việt Nam");
                        medicine9.setImageUrl(
                                        "product9.jpg");

                        Category category9 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine9.setCategory(category9);

                        medicine9 = medicineRepository.save(medicine9);

                        List<MedicineDetail> details9 = new ArrayList<>();
                        details9.add(createDetail(medicine9, MedicineDetailType.INGREDIENT,
                                        "Nước\nĐạm sữa\nMaltodextrin\nĐậu nành\nKhoáng chất\nMedium chain triglyceride\nVitamin\nImmunel"));
                        details9.add(createDetail(medicine9, MedicineDetailType.EFFECT,
                                        "Sữa Nutifood Varna Colostrum với Colos Immunel được phát triển và nghiên cứu bởi NNRIS (Viện Nghiên cứu Dinh Dưỡng Nutifoood Thụy Điển), "
                                                        + "và nguyên liệu Immunel độc quyền từ Sterling Technology USA. Immunel được chứng minh lâm sàng hỗ trợ tăng cường sức đề kháng nhanh.\n"
                                                        + "Varna Colostrum bổ sung hệ dưỡng chất kép từ sữa non và Immunel (phân đoạn sữa non phân tử lượng thấp)."));
                        details9.add(createDetail(medicine9, MedicineDetailType.USAGE,
                                        "Cách dùng\nUống 1 - 3 chai mỗi ngày.\nLắc đều trước khi uống. Ngon hơn khi uống lạnh.\n"
                                                        + "Varna Colostrum sử dụng nuôi ăn qua ống thông theo sự chỉ định của bác sĩ.\n\n"
                                                        + "Đối tượng sử dụng\nSữa Nutifood Varna Colostrum thích hợp dùng cho người lớn ăn uống kém, cần tăng cường sức đề kháng."));
                        details9.add(createDetail(medicine9, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details9.add(createDetail(medicine9, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người mẫn cảm với bất cứ thành phần nào của sản phẩm.\n"
                                                        + "Không dùng quá liều khuyến cáo.\n"
                                                        + "Sản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\n"
                                                        + "Đọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details9.add(createDetail(medicine9, MedicineDetailType.STORAGE,
                                        "Bảo quản chai chưa mở ở nhiệt độ phòng. Khi đã mở, phải sử dụng ngay, phần chưa sử dụng phải đậy kín, cho vào tủ lạnh và dùng trong vòng 24 giờ.\n"
                                                        + "Để xa tầm tay trẻ em."));
                        details9.add(createDetail(medicine9, MedicineDetailType.DESCRIPTION,
                                        "Hỗ trợ tăng cường sức đề kháng"));

                        medicineDetailRepository.saveAll(details9);

                        Medicine medicine10 = new Medicine();
                        medicine10.setName(
                                        "Sữa bột Nutren Junior 800g Nestlé bổ sung hoặc thay thế bữa ăn hàng ngày cho trẻ suy dinh dưỡng");
                        medicine10.setPrice(550000.0);
                        medicine10.setOriginalPrice(600000.0);
                        medicine10.setUnit("Hộp");
                        medicine10.setShortDescription("Bổ sung hoặc thay thế bữa ăn hàng ngày cho trẻ suy dinh dưỡng");
                        medicine10.setBrandOrigin("Nestlé");
                        medicine10.setManufacturer("NESTLÉ SUISSE S.A");
                        medicine10.setCountryOfManufacture("Thụy Sĩ");
                        medicine10.setImageUrl(
                                        "product10.jpg");

                        Category category10 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine10.setCategory(category10);

                        medicine10 = medicineRepository.save(medicine10);

                        List<MedicineDetail> details10 = new ArrayList<>();
                        details10.add(createDetail(medicine10, MedicineDetailType.INGREDIENT,
                                        "Maltodextrin\nSucrose\nMilk protein\nVegetable Oils\nMedium chain triglyceride\nMinerals\n"
                                                        + "Prebiotic Fibres\nEmulsifier\nNature Identical Flavour\nCholine bitartrate\nAcidity Probiotics\nTaurine\nL-Carnitine"));
                        details10.add(createDetail(medicine10, MedicineDetailType.EFFECT,
                                        "Sữa Nutren Junior 800g là sản phẩm dinh dưỡng có thể bổ sung hoặc thay thế hoàn toàn bữa ăn hàng ngày của bé suy dinh dưỡng hoặc gặp vấn đề về ăn uống.\n"
                                                        + "Sản phẩm chứa công thức dinh dưỡng cao năng lượng, cùng nguồn vitamin, khoáng chất cần thiết, giúp trẻ ăn uống kém, kém hấp thu, "
                                                        + "bé bị bệnh đang trong quá trình hồi phục hoặc trẻ bị suy dinh dưỡng, còi cọc nhanh chóng bắt kịp đà tăng trưởng."));
                        details10.add(createDetail(medicine10, MedicineDetailType.USAGE,
                                        "Cách dùng\nRửa tay sạch với xà phòng.\nCho 210ml nước đun sôi để nguội hoặc nước ấm không quá 45 độ vào ly hoặc bình sữa.\n"
                                                        + "Múc 7 muỗng sữa Nutren Junior tương đương 55g.\nKhuấy đều theo 1 chiều đến khi tan hoàn toàn.\n"
                                                        + "Sau khi sử dụng xong để muỗng lường trên giá bên trong hộp, đậy kín hộp.\n\n"
                                                        + "Đối tượng sử dụng\nSữa Nutren Junior 400g dùng cho bé từ 1 - 10 tuổi trong các trường hợp sau:\n"
                                                        + "• Trẻ bị suy dinh dưỡng hoặc có nguy cơ suy dinh dưỡng.\n"
                                                        + "• Trẻ có nhu cầu năng lượng cao trong lúc bị bệnh, trước hoặc sau phẫu thuật.\n"
                                                        + "• Trẻ gặp các vấn đề về ăn uống, kém hấp thu."));
                        details10.add(createDetail(medicine10, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));
                        details10.add(createDetail(medicine10, MedicineDetailType.NOTE,
                                        "Sữa mẹ là thức ăn tốt nhất cho sức khỏe và sự phát triển toàn diện của trẻ nhỏ. Các yếu tố chống nhiễm khuẩn, đặc biệt là kháng thể trong sữa mẹ "
                                                        + "có tác dụng tốt nhất giúp trẻ phòng, chống bệnh tiêu chảy, nhiễm khuẩn đường hô hấp và một số bệnh nhiễm khuẩn khác.\n"
                                                        + "Pha chế theo đúng hướng dẫn. Cho trẻ ăn bằng cốc, thìa hợp vệ sinh.\n"
                                                        + "Sử dụng sản phẩm theo sự tư vấn của nhân viên y tế.\n"
                                                        + "Không dùng để tiêm truyền.\n"
                                                        + "Không sử dụng cho bé dưới 1 tuổi.\n"
                                                        + "Không pha thêm dược phẩm hoặc thực phẩm nào khác.\n"
                                                        + "Không sử dụng cho người mẫn cảm với bất cứ thành phần nào của sản phẩm.\n"
                                                        + "Sản phẩm này không phải là thuốc và không có tác dụng thay thế thuốc chữa bệnh.\n"
                                                        + "Đọc kỹ hướng dẫn sử dụng trước khi dùng."));
                        details10.add(createDetail(medicine10, MedicineDetailType.STORAGE,
                                        "Bảo quản nơi khô ráo, thoáng mát, nhiệt độ không quá 30 độ C, tránh ánh sáng.\n"
                                                        + "Chỉ sử dụng sản phẩm trong vòng 1 tháng sau khi mở hộp.\n"
                                                        + "Sản phẩm sau khi pha nên được đậy kín và sử dụng trong vòng 4 giờ nếu để ở nhiệt độ phòng, hoặc 12 giờ nếu bảo quản trong tủ lạnh.\n"
                                                        + "Để xa tầm tay trẻ em."));
                        details10.add(createDetail(medicine10, MedicineDetailType.DESCRIPTION,
                                        "Bổ sung hoặc thay thế bữa ăn hàng ngày cho trẻ suy dinh dưỡng"));

                        medicineDetailRepository.saveAll(details10);

                        Medicine medicine11 = new Medicine();
                        medicine11.setName(
                                        "Thuốc mỡ bôi da Agiclovir 5% Agimexpharm điều trị nhiễm Herpes simplex, Herpes zoster, Herpes sinh dục (5g)");
                        medicine11.setPrice(10000.0);
                        medicine11.setOriginalPrice(10000.0);
                        medicine11.setUnit("Tuýp");
                        medicine11.setShortDescription(
                                        "Thuốc mỡ bôi da Agiclovir 5% là sản phẩm của Agimexpharm có thành phần chính là Aciclovir, hiệu quả trong điều trị nhiễm Herpes simplex trên da và niêm mạc, nhiễm Herpes zoster, Herpes sinh dục, Herpes môi khởi phát và tái phát.");
                        medicine11.setBrandOrigin("Agimexpharm");
                        medicine11.setManufacturer("AGIMEXPHARM");
                        medicine11.setCountryOfManufacture("Việt Nam");
                        medicine11.setImageUrl(
                                        "product11.jpg");

                        Category category11 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine11.setCategory(category11);

                        medicine11 = medicineRepository.save(medicine11);

                        List<MedicineDetail> details11 = new ArrayList<>();

                        details11.add(createDetail(medicine11, MedicineDetailType.INGREDIENT,
                                        "Aciclovir 0.25g\nTá dược vừa đủ 5g"));

                        details11.add(createDetail(medicine11, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n"
                                                        + "• Nhiễm Herpes simplex trên da và niêm mạc\n"
                                                        + "• Nhiễm Herpes zoster, Herpes sinh dục, Herpes môi khởi phát và tái phát.\n\n"
                                                        + "Dược lực học:\n"
                                                        + "Aciclovir là chất tương tự nucleosid có tác dụng chọn lọc trên tế bào nhiễm virus Herpes. Sau khi được phosphoryl hóa bởi enzym virus, Aciclovir triphosphat sẽ ức chế tổng hợp DNA của virus mà không ảnh hưởng đến tế bào bình thường.\n\n"
                                                        + "Dược động học:\n"
                                                        + "Aciclovir hấp thu qua da, đạt nồng độ ức chế tại vùng điều trị. Không phát hiện trong máu khi bôi ngoài."));

                        details11.add(createDetail(medicine11, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "Thuốc mỡ dùng bôi ngoài.\n\n"
                                                        + "Liều dùng:\n"
                                                        + "Thoa thuốc lên vùng da nhiễm, 5 - 6 lần/ngày, cách nhau 4 giờ. Dùng liên tục 5 - 7 ngày, nếu cần có thể kéo dài đến 10 ngày.\n"
                                                        + "Bắt đầu điều trị càng sớm càng tốt khi có triệu chứng.\n\n"
                                                        + "Lưu ý:\n"
                                                        + "Liều dùng có thể thay đổi tùy thể trạng. Tham khảo ý kiến bác sĩ.\n\n"
                                                        + "Quên liều:\n"
                                                        + "Dùng càng sớm càng tốt hoặc bỏ qua nếu gần liều kế tiếp.\n\n"
                                                        + "Quá liều:\n"
                                                        + "Chưa có dữ liệu lâm sàng."));

                        details11.add(createDetail(medicine11, MedicineDetailType.SIDE_EFFECT,
                                        "Tác dụng phụ:\n"
                                                        + "Thường gặp: Chưa có báo cáo.\n"
                                                        + "Ít gặp: Cảm giác rát bỏng hoặc đau nhói thoáng qua.\n\n"
                                                        + "Xử trí:\n"
                                                        + "Ngưng thuốc và liên hệ bác sĩ nếu gặp tác dụng phụ."));

                        details11.add(createDetail(medicine11, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n"
                                                        + "• Mẫn cảm với bất kỳ thành phần nào của thuốc.\n\n"
                                                        + "Thận trọng:\n"
                                                        + "• Không dùng nếu không có triệu chứng rõ ràng.\n"
                                                        + "• Không bôi vào miệng, âm đạo, mắt.\n"
                                                        + "• Không dùng để phòng tái phát.\n"
                                                        + "• Phụ nữ có thai và cho con bú: Thận trọng khi sử dụng.\n\n"
                                                        + "Tương tác:\n"
                                                        + "• Amphotericin B, ketoconazol, interferon có thể làm tăng tác dụng của aciclovir.\n"
                                                        + "• Không pha loãng hoặc trộn với thuốc khác.\n"
                                                        + "• Thông báo bác sĩ nếu đang dùng thuốc khác."));

                        details11.add(createDetail(medicine11, MedicineDetailType.STORAGE,
                                        "Để ở nhiệt độ dưới 30°C, tránh ẩm và ánh sáng."));

                        details11.add(createDetail(medicine11, MedicineDetailType.DESCRIPTION,
                                        "Thuốc mỡ bôi da Agiclovir 5% là sản phẩm của Agimexpharm có thành phần chính là Aciclovir, hiệu quả trong điều trị nhiễm Herpes simplex, Herpes zoster, Herpes sinh dục, Herpes môi khởi phát và tái phát."));

                        details11.forEach(detail -> medicineDetailRepository.save(detail));

                        Medicine medicine12 = new Medicine();
                        medicine12.setName("Cao Sao Vàng Danapha điều trị cảm cúm, nhức đầu (16g)");
                        medicine12.setPrice(29000.0);
                        medicine12.setOriginalPrice(29000.0);
                        medicine12.setUnit("Hộp");
                        medicine12.setShortDescription(
                                        "Cao xoa Sao Vàng là sản phẩm của Công ty Cổ phần Dược Danapha có thành phần chính là Menthol, long não, tinh dầu bạc hà, tinh dầu tràm, tinh dầu đinh hương, tinh dầu quế dùng trong các trường hợp cảm cúm, nhức đầu, sổ mũi, chóng mặt, đau khớp, bị muỗi và côn trùng khác đốt.");
                        medicine12.setBrandOrigin("Danapha");
                        medicine12.setManufacturer("DANAPHA");
                        medicine12.setCountryOfManufacture("Việt Nam");
                        medicine12.setImageUrl(
                                        "product12.jpg");

                        Category category12 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine12.setCategory(category12);

                        medicine12 = medicineRepository.save(medicine12);

                        List<MedicineDetail> details12 = new ArrayList<>();

                        details12.add(createDetail(medicine12, MedicineDetailType.INGREDIENT,
                                        "Camphor: 4.128g\n"
                                                        + "Menthol: 0.656g\n"
                                                        + "Tinh dầu bạc hà: 2g\n"
                                                        + "Tinh dầu tràm: 1.408g\n"
                                                        + "Tinh dầu đinh hương: 0.144g\n"
                                                        + "Tinh dầu quế: 0.224g\n"
                                                        + "Tá dược vừa đủ: 16g"));

                        details12.add(createDetail(medicine12, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n"
                                                        + "Cao xoa Sao Vàng chỉ định điều trị trong các trường hợp cảm cúm, đau đầu, sổ mũi, chóng mặt, đau khớp, bị muỗi và côn trùng khác đốt.\n\n"
                                                        + "Dược lực học:\n"
                                                        + "Sự phối hợp của các tinh dầu trong sản phẩm cao sao vàng làm cho thuốc có tác dụng hiệp lực với thủ pháp xoa lên huyệt, theo kinh nghiệm xoa dầu của Y học Cổ truyền Việt Nam.\n"
                                                        + "Menthol và tinh dầu bạc hà thường đi vào 2 kinh phế và can, có tác dụng tán phong nhiệt, ra mồ hôi, giảm uất.\n"
                                                        + "Long não: Có tác dụng khử trùng, tiêu viêm, gây tê làm giảm đau, lưu thông khí huyết.\n"
                                                        + "Tinh dầu Tràm: Đi vào 2 kinh tỳ, phế. Có tác dụng hoạt huyết khu phong, an thần chấn thống, tiêu đàm...\n"
                                                        + "Tinh dầu Đinh hương: Dùng chữa đau bụng, tiêu chảy, nôn mửa.\n"
                                                        + "Tinh dầu Quế: Kích thích tuần hoàn, trợ hô hấp, gây co mạch, sát trùng.\n\n"
                                                        + "Dược động học:\n"
                                                        + "Chưa có dữ liệu."));

                        details12.add(createDetail(medicine12, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "Chỉ dùng ngoài da.\n"
                                                        + "Bôi cao vào vùng thái dương, gáy khi bị cảm cúm, nhức đầu, sổ mũi, chóng mặt.\n"
                                                        + "Xoa bóp vào vùng bị đau nhức.\n"
                                                        + "Bôi vào nơi bị côn trùng đốt.\n\n"
                                                        + "Liều dùng:\n"
                                                        + "Tùy thuộc vào thể trạng và mức độ bệnh. Tham khảo ý kiến bác sĩ.\n\n"
                                                        + "Quá liều:\n"
                                                        + "Gọi Trung tâm cấp cứu 115 hoặc đến trạm Y tế gần nhất.\n\n"
                                                        + "Quên liều:\n"
                                                        + "Tiếp tục dùng theo liều."));

                        details12.add(createDetail(medicine12, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có dữ liệu.\n"
                                                        + "Khi gặp tác dụng phụ, ngưng sử dụng và thông báo bác sĩ hoặc đến cơ sở y tế gần nhất."));

                        details12.add(createDetail(medicine12, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n"
                                                        + "• Trẻ em dưới 30 tháng tuổi, trẻ em có tiền sử động kinh hoặc co giật do sốt cao.\n"
                                                        + "• Không bôi cao vào mắt hoặc vết thương hở.\n\n"
                                                        + "Thận trọng:\n"
                                                        + "• Không bôi vào mắt hoặc vết thương hở.\n\n"
                                                        + "Tương tác thuốc:\n"
                                                        + "• Chưa có dữ liệu."));

                        details12.add(createDetail(medicine12, MedicineDetailType.STORAGE,
                                        "Để thuốc nơi khô thoáng, tránh ánh sáng, nhiệt độ dưới 30°C."));

                        details12.add(createDetail(medicine12, MedicineDetailType.DESCRIPTION,
                                        "Điều trị cảm cúm, nhức đầu"));

                        details12.forEach(detail -> medicineDetailRepository.save(detail));

                        Medicine medicine13 = new Medicine();
                        medicine13
                                        .setName("Hỗn dịch uống Biviantac Kháng Acid 10ml Reliv điều trị ăn không tiêu, đầy hơi (20 gói)");
                        medicine13.setPrice(4300.0);
                        medicine13.setOriginalPrice(4300.0);
                        medicine13.setUnit("Gói");
                        medicine13.setShortDescription(
                                        "Thuốc Biviantac Kháng Acid là sản phẩm của Công ty TNHH BRV Healthcare dạng hỗn dịch uống chứa hoạt chất nhôm hydroxyd, magnesi hydroxyd, simethicon giúp điều trị triệu chứng trong các trường hợp ăn không tiêu, đầy hơi. Trung hòa acid dịch vị, điều trị triệu chứng các trường hợp tăng tiết acid dạ dày, trào ngược dạ dày thực quản, ợ nóng, ợ chua.");
                        medicine13.setBrandOrigin("Reliv, Ấn Độ");
                        medicine13.setManufacturer("CÔNG TY CỔ PHẦN DƯỢC PHẨM RELIV");
                        medicine13.setCountryOfManufacture("Việt Nam");
                        medicine13.setImageUrl(
                                        "product13.jpg");

                        Category category13 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine13.setCategory(category13);

                        medicine13 = medicineRepository.save(medicine13);

                        List<MedicineDetail> details13 = new ArrayList<>();

                        details13.add(createDetail(medicine13, MedicineDetailType.INGREDIENT,
                                        "Nhôm hydroxyd: 612mg\n" +
                                                        "Magnesi hydroxyd: 800.4mg\n" +
                                                        "Simethicon: 80mg"));

                        details13.add(createDetail(medicine13, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n" +
                                                        "• Điều trị triệu chứng trong các trường hợp ăn không tiêu, đầy hơi.\n"
                                                        +
                                                        "• Trung hòa acid dịch vị, điều trị triệu chứng các trường hợp tăng tiết acid dạ dày, trào ngược dạ dày thực quản, ợ nóng, ợ chua.\n\n"
                                                        +
                                                        "Dược lực học:\n" +
                                                        "Chưa có dữ liệu.\n\n" +
                                                        "Dược động học:\n" +
                                                        "Chưa có dữ liệu."));

                        details13.add(createDetail(medicine13, MedicineDetailType.USAGE,
                                        "Cách dùng:\n" +
                                                        "Thuốc Biviantac Kháng Acid dùng đường uống.\n\n" +
                                                        "Liều dùng:\n" +
                                                        "Tùy thuộc vào thể trạng và mức độ bệnh. Tham khảo ý kiến bác sĩ hoặc chuyên viên y tế.\n\n"
                                                        +
                                                        "Quá liều:\n" +
                                                        "Trong trường hợp khẩn cấp, gọi 115 hoặc đến trạm y tế gần nhất.\n\n"
                                                        +
                                                        "Quên liều:\n" +
                                                        "Dùng ngay khi nhớ. Nếu gần đến liều kế tiếp, bỏ qua liều đã quên và tiếp tục dùng như lịch trình. Không dùng gấp đôi liều."));

                        details13.add(createDetail(medicine13, MedicineDetailType.SIDE_EFFECT,
                                        "Thông báo cho thầy thuốc các tác dụng không mong muốn gặp phải khi sử dụng thuốc."));

                        details13.add(createDetail(medicine13, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n" +
                                                        "• Quá mẫn với bất cứ thành phần nào của thuốc.\n\n" +
                                                        "Thận trọng khi sử dụng:\n" +
                                                        "Vui lòng xem thêm các thông tin về thuốc trong tờ hướng dẫn sử dụng đính kèm.\n\n"
                                                        +
                                                        "Ảnh hưởng đến khả năng lái xe và vận hành máy móc:\n" +
                                                        "Chưa rõ ảnh hưởng.\n\n" +
                                                        "Sử dụng cho phụ nữ mang thai và cho con bú:\n" +
                                                        "Chỉ dùng khi có chỉ định của bác sĩ.\n\n" +
                                                        "Tương tác thuốc:\n" +
                                                        "Tương tác thuốc có thể ảnh hưởng đến hiệu quả điều trị hoặc gây tác dụng phụ. Tham khảo ý kiến bác sĩ về các thuốc và thực phẩm chức năng đang dùng."));

                        details13.add(createDetail(medicine13, MedicineDetailType.STORAGE,
                                        "Bảo quản ở nhiệt độ không quá 30°C trong bao bì gốc, tránh ẩm và tránh ánh sáng."));

                        details13.add(createDetail(medicine13, MedicineDetailType.DESCRIPTION,
                                        "Điều trị ăn không tiêu, đầy hơi"));

                        details13.forEach(detail -> medicineDetailRepository.save(detail));

                        Medicine medicine14 = new Medicine();
                        medicine14.setName(
                                        "Thuốc Đại Tràng Trường Phúc điều trị viêm loét đại tràng, rối loạn tiêu hóa (3 vỉ x 10 viên)");
                        medicine14.setPrice(105000.0);
                        medicine14.setOriginalPrice(105000.0);
                        medicine14.setUnit("Hộp");
                        medicine14.setShortDescription(
                                        "Thuốc Đại Tràng Trường Phúc là sản phẩm của Công ty TNHH Dược Thảo Hoàng Thành có thành phần dược chất là cao đặc hỗn hợp các dược liệu: Hoàng liên, Mộc hương, Bạch truật, Bạch thược, Ngũ bội tử, Hậu phác, Cam thảo, Xa tiền tử, Hoạt thạch. Thuốc được dùng điều trị viêm loét đại tràng, rối loạn tiêu hóa, tiêu chảy (không phải do nhiễm khuẩn). Dùng trong các trường hợp đau đầu chướng bụng, ăn không tiêu, sôi bụng, đi táo hoặc lỏng, nhầy máu mũi, phân sống...");
                        medicine14.setBrandOrigin("TRƯỜNG PHÚC");
                        medicine14.setManufacturer("CÔNG TY TNHH DƯỢC THẢO HOÀNG THÀNH");
                        medicine14.setCountryOfManufacture("Việt Nam");
                        medicine14.setImageUrl(
                                        "product14.jpg");

                        Category category14 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine14.setCategory(category14);

                        medicine14 = medicineRepository.save(medicine14);

                        List<MedicineDetail> details14 = new ArrayList<>();

                        details14.add(createDetail(medicine14, MedicineDetailType.INGREDIENT,
                                        "Hoàng liên: 1.35g\n" +
                                                        "Mộc hương: 1.2g\n" +
                                                        "Bạch truật: 0.9g\n" +
                                                        "Bạch thược: 0.9g\n" +
                                                        "Ngũ bội tử: 0.9g\n" +
                                                        "Hậu phác: 0.6g\n" +
                                                        "Cam thảo: 0.45g\n" +
                                                        "Xa tiền tử: 0.45g\n" +
                                                        "Hoạt thạch: 0.15g"));

                        details14.add(createDetail(medicine14, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n" +
                                                        "Thuốc Đại Tràng Trường Phúc chỉ định điều trị trong các trường hợp sau:\n"
                                                        +
                                                        "• Điều trị viêm loét đại tràng, rối loạn tiêu hóa, tiêu chảy (không phải do nhiễm khuẩn).\n"
                                                        +
                                                        "• Dùng trong các trường hợp đau đầy chướng bụng, ăn không tiêu, sôi bụng, đi táo hoặc lỏng, nhầy máu mũi, phân sống...\n\n"
                                                        +
                                                        "Dược lực học:\nChưa có dữ liệu.\n\n" +
                                                        "Dược động học:\nChưa có dữ liệu."));

                        details14.add(createDetail(medicine14, MedicineDetailType.USAGE,
                                        "Cách dùng:\n" +
                                                        "Thuốc Đại Tràng Trường Phúc dạng viên dùng đường uống. Uống trước hoặc sau khi ăn 1 giờ.\n\n"
                                                        +
                                                        "Liều dùng:\n" +
                                                        "• Người lớn: Mỗi lần 2 viên.\n" +
                                                        "• Trẻ em từ 12 - 15 tuổi: Mỗi lần 1 viên.\n" +
                                                        "• Liều tấn công: Ngày uống 3 lần. Đợt dùng 10 ngày. Có thể dùng 2 - 3 đợt.\n"
                                                        +
                                                        "• Liều duy trì dự phòng tái phát: Ngày uống 2 lần, từ 1 - 2 tháng.\n\n"
                                                        +
                                                        "Lưu ý: Liều dùng trên chỉ mang tính chất tham khảo. Liều cụ thể tùy thể trạng và mức độ bệnh. Tham khảo ý kiến bác sĩ.\n\n"
                                                        +
                                                        "Làm gì khi dùng quá liều?\n" +
                                                        "Trong trường hợp khẩn cấp, hãy gọi 115 hoặc đến trạm Y tế gần nhất.\n\n"
                                                        +
                                                        "Làm gì khi quên 1 liều?\n" +
                                                        "Dùng ngay khi nhớ ra. Nếu gần liều tiếp theo thì bỏ qua liều đã quên. Không dùng gấp đôi để bù."));

                        details14.add(createDetail(medicine14, MedicineDetailType.SIDE_EFFECT,
                                        "Thông báo cho thầy thuốc các tác dụng không mong muốn gặp phải khi sử dụng thuốc."));

                        details14.add(createDetail(medicine14, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n" +
                                                        "• Phụ nữ có thai.\n" +
                                                        "• Trẻ em dưới 12 tuổi.\n\n" +
                                                        "Thận trọng khi sử dụng:\n" +
                                                        "Không dùng cho người mẫn cảm với bất kỳ thành phần nào của thuốc.\n\n"
                                                        +
                                                        "Sử dụng cho phụ nữ mang thai và cho con bú:\n" +
                                                        "• Không dùng thuốc cho phụ nữ có thai.\n" +
                                                        "• Phụ nữ đang cho con bú: chỉ nên dùng nếu lợi ích vượt trội so với nguy cơ.\n\n"
                                                        +
                                                        "Ảnh hưởng lên khả năng lái xe và vận hành máy móc:\n" +
                                                        "Chưa có bằng chứng.\n\n" +
                                                        "Tương tác thuốc:\n" +
                                                        "Không ăn măng, đồ chiên xào, thức ăn cay khi dùng thuốc.\n" +
                                                        "Nên uống cách xa các thuốc khác 1 - 2 giờ."));

                        details14.add(createDetail(medicine14, MedicineDetailType.STORAGE,
                                        "Để nơi mát, tránh ánh sáng, nhiệt độ dưới 30⁰C."));

                        details14.add(createDetail(medicine14, MedicineDetailType.DESCRIPTION,
                                        "Điều trị viêm loét đại tràng, rối loạn tiêu hóa"));

                        details14.forEach(detail -> medicineDetailRepository.save(detail));

                        Medicine medicine15 = new Medicine();
                        medicine15
                                        .setName("Thuốc Bổ Gan Trường Phúc giải độc gan, chống dị ứng, mày đay, lở ngứa (3 vỉ x 10 viên)");
                        medicine15.setPrice(95000.0);
                        medicine15.setOriginalPrice(95000.0);
                        medicine15.setUnit("Hộp");
                        medicine15.setShortDescription(
                                        "Bổ Gan Trường Phúc là sản phẩm của Công ty TNHH Dược Thảo Hoàng Thành có thành phần chính là cao đặc hỗn hợp dược liệu diệp hạ châu, đảng sâm, nhân trần, bạch thược, bạch truật, cam thảo, đương quy, phục linh, trần bì có tác dụng bổ gan, giải độc, kiện tỳ, tăng cường khí huyết, chống dị ứng, mề đay, lở ngứa, rôm sảy, mụn nhọt. Hỗ trợ điều trị trong bệnh viêm gan với các triệu chứng mệt mỏi, vàng da, chán ăn khó tiêu, táo bón, đau tức vùng gan, suy giảm chức năng gan do dùng nhiều bia rượu, thuốc hóa dược.");
                        medicine15.setBrandOrigin("TRƯỜNG PHÚC");
                        medicine15.setManufacturer("CÔNG TY TNHH DƯỢC THẢO HOÀNG THÀNH");
                        medicine15.setCountryOfManufacture("Việt Nam");
                        medicine15.setImageUrl(
                                        "product15.jpg");

                        Category category15 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine15.setCategory(category15);

                        medicine15 = medicineRepository.save(medicine15);

                        List<MedicineDetail> details15 = new ArrayList<>();

                        details15.add(createDetail(medicine15, MedicineDetailType.INGREDIENT,
                                        "Diệp hạ châu: 1.2g\n" +
                                                        "Đảng Sâm: 1.2g\n" +
                                                        "Nhân trần: 1.2g\n" +
                                                        "Bạch truật: 0.6g\n" +
                                                        "Cam thảo: 0.6g\n" +
                                                        "Phục Linh: 0.6g\n" +
                                                        "Trần bì: 0.6g"));

                        details15.add(createDetail(medicine15, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n" +
                                                        "Bổ Gan Trường Phúc chỉ định điều trị trong các trường hợp sau:\n"
                                                        +
                                                        "• Hỗ trợ bổ gan, giải độc, kiện tỳ, tăng cường khí huyết.\n" +
                                                        "• Giải độc gan, chống dị ứng, mề đay, lở ngứa, rôm sảy, mụn nhọt.\n"
                                                        +
                                                        "• Hỗ trợ điều trị trong bệnh viêm gan với các triệu chứng mệt mỏi, vàng da, chán ăn khó tiêu, táo bón, đau tức vùng gan, suy giảm chức năng gan do dùng nhiều bia rượu, thuốc hóa dược.\n\n"
                                                        +
                                                        "Dược lực học:\nChưa có dữ liệu.\n\n" +
                                                        "Dược động học:\nChưa có dữ liệu."));

                        details15.add(createDetail(medicine15, MedicineDetailType.USAGE,
                                        "Cách dùng:\n" +
                                                        "Bổ Gan Trường Phúc dạng viên nén bao phim dùng đường uống, uống trước hoặc sau khi ăn 1 giờ.\n\n"
                                                        +
                                                        "Liều dùng:\n" +
                                                        "• Người lớn: Ngày uống 2 lần, mỗi lần 2 viên.\n" +
                                                        "• Trẻ em từ 12 - 15 tuổi: Ngày uống 2 lần, mỗi lần 1 viên.\n" +
                                                        "Người mắc bệnh mạn tính nên uống thuốc liên tục 3 tháng.\n\n" +
                                                        "Lưu ý: Liều dùng trên chỉ mang tính chất tham khảo. Liều dùng cụ thể tùy thuộc vào thể trạng và mức độ diễn tiến của bệnh. Tham khảo ý kiến bác sĩ hoặc chuyên viên y tế.\n\n"
                                                        +
                                                        "Làm gì khi dùng quá liều?\n" +
                                                        "Những dấu hiệu và triệu chứng: Nôn, buồn nôn, hoa mắt, chóng mặt nhẹ.\n"
                                                        +
                                                        "Cần theo dõi tích cực, đến cơ sở y tế gần nhất để được hỗ trợ.\n\n"
                                                        +
                                                        "Làm gì khi quên 1 liều?\n" +
                                                        "Bổ sung liều ngay khi nhớ. Nếu gần liều tiếp theo thì bỏ qua liều đã quên, không dùng gấp đôi."));

                        details15.add(createDetail(medicine15, MedicineDetailType.SIDE_EFFECT,
                                        "Thông báo cho thầy thuốc các tác dụng không mong muốn gặp phải khi sử dụng thuốc."));

                        details15.add(createDetail(medicine15, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n" +
                                                        "• Phụ nữ có thai.\n" +
                                                        "• Trẻ em dưới 12 tuổi.\n" +
                                                        "• Người mẫn cảm với các thành phần thuốc.\n\n" +
                                                        "Thận trọng khi sử dụng:\n" +
                                                        "Phụ nữ đang cho con bú cần thận trọng khi sử dụng thuốc.\n\n" +
                                                        "Tương tác thuốc:\n" +
                                                        "Uống Bổ Gan Trường Phúc cách xa thuốc khác 1 - 2 giờ."));

                        details15.add(createDetail(medicine15, MedicineDetailType.STORAGE,
                                        "Để nơi mát, tránh ánh sáng, nhiệt độ dưới 30⁰C.\nĐể xa tầm tay trẻ em."));

                        details15.add(createDetail(medicine15, MedicineDetailType.DESCRIPTION,
                                        "Giải độc gan, chống dị ứng, mày đay, lở ngứa"));

                        medicineDetailRepository.saveAll(details15);

                        Medicine medicine16 = new Medicine();
                        medicine16.setName(
                                        "Thuốc Boganic Forte Traphaco bổ gan, hỗ trợ điều trị suy giảm chức năng gan");
                        medicine16.setPrice(115000.0);
                        medicine16.setOriginalPrice(115000.0);
                        medicine16.setUnit("Hộp");
                        medicine16.setShortDescription(
                                        "Thuốc Boganic Forte của Công ty Cổ phần Công Nghệ Cao Traphaco, với hoạt chất chính là Cao Actisô, Cao Rau đắng đất, Cao Bìm bìm. "
                                                        + "Được dùng điều trị suy giảm chức năng gan, viêm gan do thuốc, hóa chất, viêm gan gây mệt mỏi, khó tiêu, vàng da, bí đại tiểu tiện, táo bón. "
                                                        + "Ngoài ra còn hỗ trợ điều trị dị ứng, mụn nhọt, lở ngứa, nổi mề đay và các vấn đề do gan, vữa xơ động mạch, mỡ trong máu cao.");
                        medicine16.setBrandOrigin("Traphaco");
                        medicine16.setManufacturer("TRAPHACO");
                        medicine16.setCountryOfManufacture("Việt Nam");
                        medicine16.setImageUrl(
                                        "product16.jpg");

                        // Lấy category từ repository
                        Category category16 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine16.setCategory(category16);

                        medicine16 = medicineRepository.save(medicine16);

                        // Tạo danh sách chi tiết thuốc
                        List<MedicineDetail> details16 = new ArrayList<>();

                        details16.add(createDetail(medicine16, MedicineDetailType.INGREDIENT,
                                        "Cao đặc bìm bìm: 13.6mg\n" +
                                                        "Cao đặc rau đắng đất: 128mg\n" +
                                                        "Cao khô actisô: 170mg"));

                        details16.add(createDetail(medicine16, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n" +
                                                        "• Suy giảm chức năng gan, đặc biệt do dùng nhiều bia, rượu.\n"
                                                        +
                                                        "• Phòng và hỗ trợ điều trị viêm gan do thuốc, hóa chất.\n" +
                                                        "• Viêm gan gây mệt mỏi, khó tiêu, vàng da, bí đại tiểu tiện, táo bón.\n"
                                                        +
                                                        "• Dị ứng, mụn nhọt, lở ngứa, nổi mề đay do gan.\n" +
                                                        "• Xơ vữa động mạch, mỡ trong máu cao.\n\n" +
                                                        "Dược lực học:\n" +
                                                        "• Actisô: tăng tiết mật, bảo vệ gan, giảm cholesterol.\n" +
                                                        "• Rau đắng đất: kháng sinh, lợi tiểu, nhuận gan.\n" +
                                                        "• Bìm bìm: nhuận tràng, sát trùng, thông tiểu.\n" +
                                                        "→ Tác dụng hiệp đồng: Nhuận gan - Lợi mật - Giải độc.\n\n" +
                                                        "Dược động học:\nChưa có báo cáo."));

                        details16.add(createDetail(medicine16, MedicineDetailType.USAGE,
                                        "Cách dùng:\n" +
                                                        "• Dùng đường uống.\n\n" +
                                                        "Liều dùng:\n" +
                                                        "• Người lớn: Mỗi lần 1 - 2 viên, ngày 3 lần.\n" +
                                                        "• Trẻ em trên 8 tuổi: Mỗi lần 1 viên, ngày 2 - 3 lần.\n\n" +
                                                        "Lưu ý: Liều dùng mang tính chất tham khảo. Tham khảo bác sĩ trước khi sử dụng.\n\n"
                                                        +
                                                        "Làm gì khi dùng quá liều?\n" +
                                                        "• Không có dữ liệu. Cần theo dõi và xử trí kịp thời.\n\n" +
                                                        "Làm gì khi quên liều?\n" +
                                                        "• Dùng càng sớm càng tốt. Nếu gần liều tiếp theo, bỏ qua liều đã quên. Không dùng gấp đôi."));

                        details16.add(createDetail(medicine16, MedicineDetailType.SIDE_EFFECT,
                                        "Khi sử dụng thuốc Boganic Forte, bạn có thể gặp các tác dụng không mong muốn:\n\n"
                                                        +
                                                        "Thường gặp (ADR > 1/100):\n• Chưa có báo cáo.\n\n" +
                                                        "Ít gặp (1/1000 < ADR < 1/100):\n• Chưa có báo cáo.\n\n" +
                                                        "Xử trí: Thông báo ngay cho bác sĩ nếu có bất thường."));

                        details16.add(createDetail(medicine16, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n" +
                                                        "• Mẫn cảm với bất kỳ thành phần nào của thuốc.\n" +
                                                        "• Viêm tắc ruột, tỳ vị hư hàn.\n\n" +
                                                        "Ghi chú:\n" +
                                                        "• Người tiểu đường có thể dùng dạng viên bao phim hoặc viên nang mềm.\n"
                                                        +
                                                        "• Thận trọng khi dùng cho phụ nữ có thai.\n" +
                                                        "• Khả năng lái xe và vận hành máy móc: chưa có báo cáo.\n" +
                                                        "• Cho con bú: chưa có dữ liệu.\n" +
                                                        "• Tương tác thuốc: chưa ghi nhận."));

                        details16.add(createDetail(medicine16, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, nhiệt độ không quá 30°C, tránh ánh sáng."));

                        details16.add(createDetail(medicine16, MedicineDetailType.DESCRIPTION,
                                        "Bổ gan, hỗ trợ điều trị suy giảm chức năng gan."));

                        medicineDetailRepository.saveAll(details16);

                        Medicine medicine17 = new Medicine();
                        medicine17.setName(
                                        "Trà Gừng Traphaco điều trị đau bụng do lạnh, đầy trướng, không tiêu (10 gói x 3g)");
                        medicine17.setPrice(14000.0);
                        medicine17.setOriginalPrice(14000.0);
                        medicine17.setUnit("Hộp");
                        medicine17.setShortDescription(
                                        "Trà gừng Traphaco là sản phẩm của công ty Cổ phần Công nghệ cao Traphaco, thành phần chính chứa gừng tươi. "
                                                        + "Sản phẩm giúp ôn trung, trục hàn, hồi dương, thông mạch dùng trị đau bụng do lạnh, đầy chướng không tiêu hoặc nôn, tiêu lỏng do lạnh, "
                                                        + "người nhiễm lạnh, chân tay lạnh, mạch nhỏ, ho do lạnh.");
                        medicine17.setBrandOrigin("Traphaco");
                        medicine17.setManufacturer("TRAPHACO");
                        medicine17.setCountryOfManufacture("Việt Nam");
                        medicine17.setImageUrl(
                                        "product17.jpg");

                        // Lấy category từ repository
                        Category category17 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine17.setCategory(category17);

                        medicine17 = medicineRepository.save(medicine17);

                        // Tạo danh sách chi tiết thuốc
                        List<MedicineDetail> details17 = new ArrayList<>();

                        details17.add(createDetail(medicine17, MedicineDetailType.INGREDIENT,
                                        "Gừng tươi\n1.6g"));

                        details17.add(createDetail(medicine17, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n"
                                                        + "Trà Gừng Traphaco được chỉ định dùng trong các trường hợp sau:\n"
                                                        + "• Ðiều trị đau bụng do lạnh, đầy chướng không tiêu hoặc nôn, tiêu lỏng do lạnh, người nhiễm lạnh, chân tay lạnh, mạch nhỏ, ho do lạnh.\n\n"
                                                        + "Dược lực học:\nChưa có dữ liệu.\n\n"
                                                        + "Dược động học:\nChưa có dữ liệu."));

                        details17.add(createDetail(medicine17, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "• Dùng đường uống.\n"
                                                        + "• Hòa mỗi túi vào khoảng 40 ml nước nóng. Thuốc nên dùng lúc ấm (khoảng 40 - 50°C), sử dụng trong vòng 2 giờ sau khi pha.\n\n"
                                                        + "Liều dùng:\n"
                                                        + "• Uống 1 lần 1 túi, ngày 2 - 3 lần.\n"
                                                        + "• Lưu ý: Liều dùng mang tính chất tham khảo. Tùy thể trạng và mức độ bệnh cần tham khảo bác sĩ.\n\n"
                                                        + "Làm gì khi dùng quá liều?\n"
                                                        + "• Không có dữ liệu về quá liều. Không dùng quá liều chỉ định.\n"
                                                        + "• Trong trường hợp khẩn cấp, hãy gọi cấp cứu hoặc đến cơ sở y tế gần nhất.\n\n"
                                                        + "Làm gì khi quên liều?\n"
                                                        + "• Uống càng sớm càng tốt. Nếu gần liều kế tiếp, bỏ qua liều quên. Không uống gấp đôi."));

                        details17.add(createDetail(medicine17, MedicineDetailType.SIDE_EFFECT,
                                        "Khi sử dụng thuốc Trà Gừng Traphaco bạn có thể gặp các tác dụng không mong muốn (ADR).\n"
                                                        + "• Chưa ghi nhận được báo cáo về phản ứng có hại.\n\n"
                                                        + "Hướng dẫn cách xử trí ADR:\n"
                                                        + "• Ngưng sử dụng và thông báo cho bác sĩ hoặc đến cơ sở y tế gần nhất."));

                        details17.add(createDetail(medicine17, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n"
                                                        + "• Người ra nhiều mồ hôi hoặc mất máu.\n"
                                                        + "• Người đái tháo đường, sốt cao.\n\n"
                                                        + "Thận trọng khi sử dụng: Chưa có thông tin.\n\n"
                                                        + "Khả năng lái xe và vận hành máy móc: Có thể sử dụng được.\n"
                                                        + "Thời kỳ mang thai: Sử dụng được cho phụ nữ có thai.\n"
                                                        + "Thời kỳ cho con bú: Sử dụng được cho phụ nữ đang cho con bú.\n"
                                                        + "Tương tác thuốc: Chưa có nghiên cứu, không trộn lẫn với thuốc khác."));

                        details17.add(createDetail(medicine17, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, tránh ánh sáng, nhiệt độ dưới 30 độ C.\n"
                                                        + "Hạn dùng: 36 tháng kể từ ngày sản xuất. Không dùng thuốc quá hạn."));

                        details17.add(createDetail(medicine17, MedicineDetailType.DESCRIPTION,
                                        "Điều trị đau bụng do lạnh, đầy trướng, không tiêu."));

                        medicineDetailRepository.saveAll(details17);

                        Medicine medicine18 = new Medicine();
                        medicine18.setName(
                                        "Thuốc Babygaz hương dâu giảm các triệu chứng đầy hơi ở đường tiêu hóa (30ml)");
                        medicine18.setPrice(28000.0);
                        medicine18.setOriginalPrice(28000.0);
                        medicine18.setUnit("Hộp");
                        medicine18.setShortDescription(
                                        "Thuốc Babygaz là sản phẩm của Công ty Cổ phần Dược phẩm OPV, có thành phần chính là Simethicon. Đây là thuốc được sử dụng để làm giảm các triệu chứng đầy hơi ở đường tiêu hóa.");
                        medicine18.setBrandOrigin("Opv");
                        medicine18.setManufacturer("CÔNG TY CỔ PHẦN DƯỢC PHẨM OPV");
                        medicine18.setCountryOfManufacture("Việt Nam");
                        medicine18.setImageUrl(
                                        "product18.jpg");

                        // Lấy category từ repository
                        Category category18 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine18.setCategory(category18);

                        // Lưu vào database
                        medicine18 = medicineRepository.save(medicine18);

                        // Tạo danh sách chi tiết thuốc
                        List<MedicineDetail> details18 = new ArrayList<>();

                        details18.add(createDetail(medicine18, MedicineDetailType.INGREDIENT,
                                        "Simethicon\n2000mg"));

                        details18.add(createDetail(medicine18, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n"
                                                        + "Thuốc Babygaz được chỉ định dùng trong các trường hợp sau:\n"
                                                        + "• Làm giảm các triệu chứng đầy hơi ở đường tiêu hóa.\n\n"
                                                        + "Dược lực học:\n"
                                                        + "Simethicon làm giảm sức căng bề mặt và giảm sự căng đầy khí tạo ra do liên kết các bọt khí trong đường tiêu hóa, do đó làm giảm sự đầy hơi.\n\n"
                                                        + "Dược động học:\n"
                                                        + "Simethicon là 1 chất trơ về mặt sinh lý học; dường như nó không được hấp thu qua đường tiêu hóa hay làm cản trở tiết dịch vị hay sự hấp thu chất bổ dưỡng. Sau khi uống, thuốc này được bài tiết ở dạng không đổi vào phân."));

                        details18.add(createDetail(medicine18, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "• Lắc chai kỹ trước khi dùng.\n"
                                                        + "• Dùng ống hút nhỏ giọt kèm theo chai để đo liều dùng.\n"
                                                        + "• Sau khi dùng, vặn chặt nắp và cất giữ cẩn thận.\n\n"
                                                        + "Liều dùng:\n"
                                                        + "• Trẻ em (<2 tuổi): 0,3 ml x 4 lần/ngày.\n"
                                                        + "• Trẻ 2–12 tuổi: 0,6 ml x 4 lần/ngày.\n"
                                                        + "• Người lớn và >12 tuổi: 0,6–1,8 ml x 4 lần/ngày.\n"
                                                        + "• Có thể pha với nước hoặc thức uống khác của trẻ.\n"
                                                        + "• Liều tham khảo, cần hỏi ý kiến bác sĩ.\n\n"
                                                        + "Quá liều:\n"
                                                        + "• Chưa có báo cáo. Nếu nghi ngờ quá liều, ngưng dùng thuốc và đến cơ sở y tế gần nhất.\n\n"
                                                        + "Quên liều:\n"
                                                        + "• Uống ngay khi nhớ ra. Nếu gần liều kế tiếp thì bỏ qua liều đã quên. Không dùng gấp đôi."));

                        details18.add(createDetail(medicine18, MedicineDetailType.SIDE_EFFECT,
                                        "Khi sử dụng thuốc Babygaz có thể gặp các tác dụng không mong muốn (ADR):\n\n"
                                                        + "Thường gặp (1/100 < ADR < 1/10):\n"
                                                        + "• Toàn thân: Đau bụng, đau đầu.\n"
                                                        + "• Tiêu hóa: Tiêu chảy, buồn nôn, nôn.\n\n"
                                                        + "Ít gặp (1/1000 < ADR < 1/100):\n"
                                                        + "• Đa hệ cơ quan: đau lưng, đau ngực, ớn lạnh, sốt, mệt mỏi...\n"
                                                        + "• Tim mạch, tiêu hóa, máu, thần kinh, hô hấp, da, tiết niệu đều có thể bị ảnh hưởng nhẹ.\n\n"
                                                        + "Xử trí ADR:\n"
                                                        + "• Thông báo cho bác sĩ khi gặp tác dụng không mong muốn."));

                        details18.add(createDetail(medicine18, MedicineDetailType.NOTE,
                                        "Lưu ý sử dụng:\n"
                                                        + "• Không dùng quá 12 liều/ngày trừ khi có chỉ định bác sĩ.\n"
                                                        + "• Thận trọng nếu bị trào ngược, nôn, hạn chế dịch.\n"
                                                        + "• Có thể gây chóng mặt, cần cẩn thận khi lái xe hoặc vận hành máy.\n"
                                                        + "• Phụ nữ có thai: chỉ dùng khi thật cần thiết.\n"
                                                        + "• Cho con bú: có thể dùng.\n"
                                                        + "• Tương tác thuốc: giảm hấp thu levothyroxin nếu dùng đồng thời.\n\n"
                                                        + "Chống chỉ định:\n"
                                                        + "• Dị ứng Simethicon hoặc thành phần thuốc.\n"
                                                        + "• Nghi ngờ thủng ruột hoặc tắc ruột."));

                        details18.add(createDetail(medicine18, MedicineDetailType.STORAGE,
                                        "• Bảo quản nơi mát, tránh ánh sáng.\n"
                                                        + "• Nhiệt độ dưới 30⁰C.\n"
                                                        + "• Để xa tầm tay trẻ em.\n"
                                                        + "• Đọc kỹ hướng dẫn sử dụng trước khi dùng."));

                        details18.add(createDetail(medicine18, MedicineDetailType.DESCRIPTION,
                                        "Giảm các triệu chứng đầy hơi ở đường tiêu hóa"));

                        for (MedicineDetail detail : details18) {
                                medicineDetailRepository.save(detail);
                        }

                        Medicine medicine19 = new Medicine();
                        medicine19.setName(
                                        "Hỗn dịch uống Yumangel Yuhan kháng acid và cải thiện loét dạ dày - tá tràng (20 gói x 15ml)");
                        medicine19.setPrice(5000.0);
                        medicine19.setOriginalPrice(5000.0);
                        medicine19.setUnit("Gói");
                        medicine19.setShortDescription(
                                        "Yumangel hay còn gọi là thuốc dạ dày chữ Y của công ty Yuhan Corporation (Hàn Quốc), thành phần chính almagate, "
                                                        + "là thuốc có tác dụng kháng acid và cải thiện các chứng bệnh sau: Loét dạ dày, loét tá tràng; viêm dạ dày; các chứng bệnh do tăng tiết acid "
                                                        + "(ợ nóng, buồn nôn, nôn, đau dạ dày, chứng ợ); bệnh trào ngược thực quản.");
                        medicine19.setBrandOrigin("Yuhan, Hàn Quốc");
                        medicine19.setManufacturer("YUHAN");
                        medicine19.setCountryOfManufacture("Hàn Quốc");
                        medicine19.setImageUrl(
                                        "product19.jpg");

                        // Gán category
                        Category category19 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine19.setCategory(category19);

                        medicine19 = medicineRepository.save(medicine19);

                        // Chi tiết thuốc
                        List<MedicineDetail> details19 = new ArrayList<>();

                        details19.add(createDetail(medicine19, MedicineDetailType.INGREDIENT, "Almagate\n\n1g"));

                        details19.add(createDetail(medicine19, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n"
                                                        + "Thuốc Yumangel được chỉ định dùng trong các trường hợp sau:\n"
                                                        + "• Thuốc có tác dụng kháng acid và cải thiện các chứng bệnh sau: Loét dạ dày, loét tá tràng; viêm dạ dày; các chứng bệnh do tăng tiết acid "
                                                        + "(ợ nóng, buồn nôn, nôn, đau dạ dày, chứng ợ); bệnh trào ngược thực quản.\n\n"
                                                        + "Dược lực học:\n"
                                                        + "• Yumangel có tác dụng trung hòa acid dạ dày nhanh chóng và kéo dài, duy trì pH dạ dày ở mức bình thường (pH = 3 - 5).\n"
                                                        + "• Tạo ra một lớp màng nhầy tương tự lớp chất nhầy trên bề mặt niêm mạc dạ dày, bảo vệ lớp niêm mạc.\n"
                                                        + "• Hấp thụ và làm mất hoạt tính acid mật, giảm hoạt động của pepsin.\n"
                                                        + "• Loại bỏ gốc tự do, yếu tố gây phá hủy lớp chất nhầy.\n\n"
                                                        + "Dược động học:\n"
                                                        + "• Almagate không hấp thu, được thải trừ qua phân."));

                        details19.add(createDetail(medicine19, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "• Thuốc dùng đường uống.\n\n"
                                                        + "Liều dùng:\n"
                                                        + "• Người lớn: 1 gói/lần x 4 lần/ngày, sau khi ăn 1 - 2 giờ và trước khi đi ngủ.\n"
                                                        + "• Trẻ em (6 - 12 tuổi): Dùng nửa liều người lớn.\n"
                                                        + "• Liều dùng cần điều chỉnh theo độ tuổi và triệu chứng.\n\n"
                                                        + "Lưu ý:\n"
                                                        + "• Liều mang tính tham khảo. Tham khảo ý kiến bác sĩ để điều chỉnh phù hợp.\n"
                                                        + "• Dùng quá liều có thể gây giảm phosphat huyết, loãng xương nếu kéo dài.\n"
                                                        + "• Nếu quên liều: dùng càng sớm càng tốt, nếu gần liều kế tiếp thì bỏ qua, không dùng gấp đôi."));

                        details19.add(createDetail(medicine19, MedicineDetailType.SIDE_EFFECT,
                                        "Tác dụng không mong muốn:\n"
                                                        + "• Chưa rõ tần suất.\n"
                                                        + "• Táo bón hoặc tiêu chảy có thể xảy ra.\n\n"
                                                        + "Xử trí:\n"
                                                        + "• Thông báo ngay cho bác sĩ nếu gặp tác dụng phụ."));

                        details19.add(createDetail(medicine19, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n"
                                                        + "• Không dùng cho người mẫn cảm với bất kỳ thành phần nào của thuốc.\n\n"
                                                        + "Thận trọng:\n"
                                                        + "• Cần thận trọng nếu đang dùng các thuốc khác.\n"
                                                        + "• Dùng đúng liều, đúng cách.\n"
                                                        + "• Trẻ em cần sự giám sát của người lớn.\n"
                                                        + "• Nếu sau 2 tuần không cải thiện, ngưng dùng và hỏi ý kiến bác sĩ.\n\n"
                                                        + "Khả năng lái xe và vận hành máy móc:\n"
                                                        + "• Không gây ảnh hưởng.\n\n"
                                                        + "Thai kỳ và cho con bú:\n"
                                                        + "• Độ an toàn chưa được thiết lập.\n\n"
                                                        + "Tương tác thuốc:\n"
                                                        + "• Không nên dùng cùng tetracyclin vì có thể cản trở hấp thu.\n"
                                                        + "• Tăng pH có thể ảnh hưởng hấp thu các thuốc khác."));

                        details19.add(createDetail(medicine19, MedicineDetailType.STORAGE,
                                        "Bảo quản dưới 30°C, trong bao bì kín và tránh ánh sáng."));

                        details19.add(createDetail(medicine19, MedicineDetailType.DESCRIPTION,
                                        "Kháng acid và cải thiện loét dạ dày - tá tràng"));

                        medicineDetailRepository.saveAll(details19);

                        Medicine medicine20 = new Medicine();
                        medicine20.setName(
                                        "Thuốc Berberin Mộc Hương Hadiphar điều trị lỵ trực khuẩn, hội chứng lỵ (100 viên)");
                        medicine20.setPrice(7000.0);
                        medicine20.setOriginalPrice(7000.0);
                        medicine20.setUnit("Hộp");
                        medicine20.setShortDescription(
                                        "Berberin Mộc Hương của công ty cổ phần Dược Hà Tĩnh, thành phần chính chứa berberin clorid, "
                                                        + "mộc hương (Radix Saussureae lappae), là thuốc dùng để trị lỵ trực khuẩn, hội chứng lỵ, ỉa chảy, "
                                                        + "viêm ruột, viêm ống mật, đầy bụng khó tiêu.");
                        medicine20.setBrandOrigin("Hadiphar, Việt Nam");
                        medicine20.setManufacturer("HADIPHAR");
                        medicine20.setCountryOfManufacture("Việt Nam");
                        medicine20.setImageUrl(
                                        "product20.jpg");

                        // Gán category
                        Category category20 = categoryRepository.findById(1)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine20.setCategory(category20);

                        medicine20 = medicineRepository.save(medicine20);

                        // Chi tiết thuốc
                        List<MedicineDetail> details20 = new ArrayList<>();

                        details20.add(createDetail(medicine20, MedicineDetailType.INGREDIENT,
                                        "Berberin Clorid\n\n5mg\n\nMộc hương\n\n15mg"));

                        details20.add(createDetail(medicine20, MedicineDetailType.EFFECT,
                                        "Chỉ định:\n"
                                                        + "Thuốc Berberin Mộc Hương được chỉ định dùng trong các trường hợp sau:\n"
                                                        + "• Ðiều trị lỵ trực khuẩn, hội chứng lỵ, ỉa chảy, viêm ruột, viêm ống mật, đầy bụng khó tiêu.\n\n"
                                                        + "Dược lực học:\nChưa có báo cáo.\n\n"
                                                        + "Dược động học:\nChưa có báo cáo."));

                        details20.add(createDetail(medicine20, MedicineDetailType.USAGE,
                                        "Cách dùng:\n• Thuốc dùng đường uống.\n\n"
                                                        + "Liều dùng:\n• Người lớn ngày uống 2 lần mỗi lần 5 - 10 viên.\n"
                                                        + "• Trẻ em dùng theo chỉ dẫn của bác sĩ.\n\n"
                                                        + "Lưu ý:\n• Liều dùng trên chỉ mang tính chất tham khảo. Liều dùng cụ thể tùy thuộc vào thể trạng và mức độ diễn tiến của bệnh. "
                                                        + "Để có liều dùng phù hợp, bạn cần tham khảo ý kiến bác sĩ hoặc chuyên viên y tế.\n\n"
                                                        + "Làm gì khi dùng quá liều?\n• Trong trường hợp khẩn cấp, hãy gọi ngay cho trung tâm cấp cứu 115 hoặc đến trạm y tế địa phương gần nhất.\n\n"
                                                        + "Làm gì khi quên 1 liều?\n• Nếu quên dùng một liều thuốc, hãy uống càng sớm càng tốt khi nhớ ra. Tuy nhiên, nếu gần với liều kế tiếp, "
                                                        + "hãy bỏ qua liều đã quên và uống liều kế tiếp vào thời điểm như kế hoạch. Không uống gấp đôi liều đã quy định."));

                        details20.add(createDetail(medicine20, MedicineDetailType.SIDE_EFFECT,
                                        "Khi sử dụng thuốc Berberin Mộc Hương, bạn có thể gặp các tác dụng không mong muốn (ADR).\n"
                                                        + "• Thuốc dễ gây kích thích co bóp dạ con.\n\n"
                                                        + "Hướng dẫn cách xử trí ADR:\n"
                                                        + "• Khi gặp tác dụng phụ của thuốc, cần ngưng sử dụng và thông báo cho bác sĩ hoặc đến cơ sở y tế gần nhất để được xử trí kịp thời."));

                        details20.add(createDetail(medicine20, MedicineDetailType.NOTE,
                                        "Chống chỉ định:\n• Phụ nữ có thai.\n\n"
                                                        + "Thận trọng khi sử dụng:\n• Chưa có báo cáo.\n\n"
                                                        + "Khả năng lái xe và vận hành máy móc:\n• Chưa có báo cáo.\n\n"
                                                        + "Thời kỳ mang thai:\n• Không nên dùng.\n\n"
                                                        + "Thời kỳ cho con bú:\n• Không nên dùng.\n\n"
                                                        + "Tương tác thuốc:\n• Chưa có báo cáo."));

                        details20.add(createDetail(medicine20, MedicineDetailType.STORAGE,
                                        "Nơi khô mát, nhiệt độ dưới 30oC, tránh ánh sáng."));

                        details20.add(createDetail(medicine20, MedicineDetailType.DESCRIPTION,
                                        "Kháng acid và cải thiện loét dạ dày - tá tràng"));

                        medicineDetailRepository.saveAll(details20);

                        Medicine medicine21 = new Medicine();
                        medicine21.setName(
                                        "Miếng dán mụn Elaband Hydro Dot Tea Tree hút dịch mụn, giảm sưng viêm (23 miếng)");
                        medicine21.setPrice(68000.0);
                        medicine21.setOriginalPrice(80000.0);
                        medicine21.setUnit("Hộp");
                        medicine21.setShortDescription(
                                        "Miếng dán mụn Elaband HydroDot Tea Tree là giải pháp cho mụn viêm chưa nặn. Miếng dán hút dịch mụn, giảm sưng viêm với tinh dầu tràm trà, "
                                                        + "đồng thời bảo vệ da khỏi tác động bên ngoài. Thiết kế trong suốt, mỏng nhẹ giúp che phủ nốt mụn một cách tự nhiên.");
                        medicine21.setBrandOrigin("ELABAND, Hàn Quốc");
                        medicine21.setManufacturer("YOUNGWOO CO., LTD");
                        medicine21.setCountryOfManufacture("Hàn Quốc");
                        medicine21.setImageUrl(
                                        "product21.jpg");

                        Category category21 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine21.setCategory(category21);

                        medicine21 = medicineRepository.save(medicine21);

                        List<MedicineDetail> details21 = new ArrayList<>();

                        details21.add(createDetail(medicine21, MedicineDetailType.INGREDIENT,
                                        "Cotton"));

                        details21.add(createDetail(medicine21, MedicineDetailType.EFFECT,
                                        "Miếng dán mụn Elaband HydroDot Tea Tree sử dụng khi mụn viêm và nhân mụn chưa được loại bỏ. "
                                                        + "Hút dịch thừa, giảm sưng viêm và làm dịu vết thương bằng tinh dầu tràm trà. Bảo vệ vùng da khỏi các tác nhân bên ngoài. "
                                                        + "Sản phẩm có màu trong suốt, mỏng nhẹ, tệp với màu da nên che đi nốt mụn một cách tự nhiên, che khuyết điểm khi dán trên da."));

                        details21.add(createDetail(medicine21, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "• Làm sạch và lau khô vùng da mụn: Trước khi dán, rửa sạch mặt bằng sữa rửa mặt dịu nhẹ và nước ấm để loại bỏ dầu nhờn và bụi bẩn. "
                                                        + "Sau đó, dùng khăn mềm lau khô hoàn toàn vùng da cần dán.\n"
                                                        + "• Dán miếng dán lên nốt mụn: Lấy miếng dán ra khỏi bao bì và nhẹ nhàng đặt trực tiếp lên nốt mụn, đảm bảo miếng dán che phủ hoàn toàn vùng mụn. "
                                                        + "Ấn nhẹ miếng dán trong khoảng 3-5 giây để tăng độ bám dính.\n"
                                                        + "• Thay miếng dán: Khi miếng dán chuyển sang màu trắng đục hoặc sau 8-12 giờ sử dụng, thay miếng dán mới để đảm bảo hiệu quả liên tục.\n\n"
                                                        + "Đối tượng sử dụng:\n"
                                                        + "Miếng dán mụn Elaband HydroDot Tea Tree phù hợp cho cả nam và nữ, đặc biệt là những người có làn da dễ bị mụn hoặc đang gặp các vấn đề về mụn như mụn viêm, mụn mủ. "
                                                        + "Ngoài ra, sản phẩm cũng hữu ích cho những ai muốn che khuyết điểm nốt mụn một cách tự nhiên và bảo vệ da khỏi tác nhân có hại bên ngoài."));

                        details21.add(createDetail(medicine21, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        details21.add(createDetail(medicine21, MedicineDetailType.NOTE,
                                        "Khi sử dụng sản phẩm hoặc sau khi sử dụng, nếu có các triệu chứng bất thường hoặc tác dụng phụ như ngứa cần tránh tiếp xúc trực tiếp ánh nắng mặt trời, "
                                                        + "cần tham khảo ý kiến chuyên gia, để xa tầm tay trẻ em."));

                        details21.add(createDetail(medicine21, MedicineDetailType.STORAGE,
                                        "Sản phẩm được bảo quản nơi khô ráo, sạch sẽ không nhiễm bụi bẩn và tránh xa nguồn nhiệt.\n"
                                                        + "Không để gần nơi chứa các hóa chất độc hại và không chồng chất vật nặng lên trên gây rách, vỡ bao bì đơn."));

                        details21.add(createDetail(medicine21, MedicineDetailType.DESCRIPTION,
                                        "Hút dịch mụn, giảm sưng viêm"));

                        medicineDetailRepository.saveAll(details21);

                        Medicine medicine22 = new Medicine();
                        medicine22.setName(
                                        "Nước súc miệng Pearlie White Chlor-Rinse Plus 250ml hỗ trợ giảm hôi miệng, mảng bám, sâu răng");
                        medicine22.setPrice(131250.0);
                        medicine22.setOriginalPrice(175000.0);
                        medicine22.setUnit("Chai");
                        medicine22.setShortDescription(
                                        "Nước súc miệng Pearlie White Chlor-Rinse Plus là sự kết hợp hài hòa giữa thành phần kháng khuẩn và nguyên liệu tự nhiên, "
                                                        + "hỗ trợ chống lại vi khuẩn gây hôi miệng, mảng bám, sâu răng; giảm và làm dịu cảm giác khó chịu ở miệng.");
                        medicine22.setBrandOrigin("PEARLIE WHITE");
                        medicine22.setManufacturer("CORLISON");
                        medicine22.setCountryOfManufacture("Singapore");
                        medicine22.setImageUrl(
                                        "product22.jpg");

                        // Gán category (ví dụ ID = 1)
                        Category category22 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine22.setCategory(category22);

                        medicine22 = medicineRepository.save(medicine22);

                        // Chi tiết thuốc
                        List<MedicineDetail> details22 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details22.add(createDetail(medicine22, MedicineDetailType.INGREDIENT,
                                        "Aqua\nSorbitol\nGlycerin\nPolysorbate 20\nPotassium acesulfanem\nChlohexidin digluconate\nPotassium sorbate\n"
                                                        + "Xylitol\nAllantoin\nMenthol\nTocopheryl acetate\nAloes (aloe barbadensis)\nThymol\nCetylpyridinium chloride\n"
                                                        + "Flavor\nLonicera Caprifolium Extract\nChamomilla Recutita Flower extract\nSalvia officinalis leaf extract\n"
                                                        + "Commiphora myrrha resin"));

                        // Công dụng (effect)
                        details22.add(createDetail(medicine22, MedicineDetailType.EFFECT,
                                        "Nước súc miệng Pearlie White Chlor-Rinse Plus hỗ trợ chống lại vi khuẩn gây hôi miệng, mảng bám, sâu răng.\n"
                                                        + "Giảm và làm dịu cảm giác khó chịu ở miệng."));

                        // Cách dùng (usage)
                        details22.add(createDetail(medicine22, MedicineDetailType.USAGE,
                                        "Súc miệng nhẹ nhàng với 10ml đến 15ml trong vòng 30 giây đảm bảo chất lỏng đến khu vực viêm nhiễm hoặc gây khó chịu trước khi nhổ ra. "
                                                        + "Lặp lại cứ sau bốn giờ và tham khảo ý kiến bác sĩ, nha sĩ hoặc dược sĩ nếu triệu chứng kéo dài."));

                        // Tác dụng phụ (side effect)
                        details22.add(createDetail(medicine22, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details22.add(createDetail(medicine22, MedicineDetailType.NOTE,
                                        "Không pha loãng hoặc nuốt. Để xa tầm tay trẻ em.\n"
                                                        + "Tất cả các loại nước súc miệng có chứa chlorhexidine có thể gây ố răng sau khi sử dụng trong thời gian dài. "
                                                        + "Nha sĩ có thể loại bỏ những vết ố này. Điều quan trọng là phải đến gặp nha sĩ ít nhất một lần sau mỗi sáu tháng."));

                        // Bảo quản (storage)
                        details22.add(createDetail(medicine22, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details22.add(createDetail(medicine22, MedicineDetailType.DESCRIPTION,
                                        "Nước súc miệng bổ sung thành phần kháng khuẩn\n"
                                                        + "Nước súc miệng Pearlie White Chlor-Rinse Plus là sự kết hợp hài hòa giữa thành phần kháng khuẩn và nguyên liệu tự nhiên, "
                                                        + "hỗ trợ chống lại vi khuẩn gây hôi miệng, mảng bám, sâu răng; giảm và làm dịu cảm giác khó chịu ở miệng."));

                        medicineDetailRepository.saveAll(details22);

                        Medicine medicine23 = new Medicine();
                        medicine23.setName("Bột Điện Giải Vị Chanh Dây Kamizol Sports Drink Powder (5 gói x 25g)");
                        medicine23.setPrice(32000.0);
                        medicine23.setOriginalPrice(40000.0);
                        medicine23.setUnit("Hộp");
                        medicine23.setShortDescription(
                                        "Kamizol Sports Drink Powder là bột pha nước uống bù điện giải.");
                        medicine23.setBrandOrigin("KAMIZOL");
                        medicine23.setManufacturer("CÔNG TY TNHH SẢN XUẤT VÀ THƯƠNG MẠI VINH THỊNH VƯỢNG");
                        medicine23.setCountryOfManufacture("Việt Nam");
                        medicine23.setImageUrl(
                                        "product23.jpg");

                        // Gán category (ví dụ ID = 2)
                        Category category23 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine23.setCategory(category23);

                        medicine23 = medicineRepository.save(medicine23);

                        // Chi tiết thuốc
                        List<MedicineDetail> details23 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details23.add(createDetail(medicine23, MedicineDetailType.INGREDIENT,
                                        "Năng lượng\nCarbohydrate\nProtein\nLipid\nNatri clorid\nKali clorid\nMagie citrate\nKẽm Gluconat\nGlucose\nVitamin C\nL-Carnitine"));

                        // Công dụng (effect)
                        details23.add(createDetail(medicine23, MedicineDetailType.EFFECT,
                                        "Kamizol Sports Drink Powder là bột pha nước uống bù điện giải giúp cung cấp năng lượng và các chất điện giải cho cơ thể."));

                        // Cách dùng & Đối tượng sử dụng (usage)
                        details23.add(createDetail(medicine23, MedicineDetailType.USAGE,
                                        "Cách dùng:\nNgười lớn và trẻ em trên 6 tuổi: Cho 1 gói vào bình nước 500ml, pha lắc đều rồi thưởng thức.\nMỗi ngày 1 - 2 gói.\n\n"
                                                        + "Đối tượng sử dụng:\nNhững người vận động với cường độ cao, người chơi thể thao hoặc làm việc trong môi trường nắng nóng ra nhiều mồ hôi gây mất nước và chất điện giải.\n"
                                                        + "Người bị ốm sốt, tiêu chảy, nôn mửa."));

                        // Tác dụng phụ (side effect)
                        details23.add(createDetail(medicine23, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details23.add(createDetail(medicine23, MedicineDetailType.NOTE,
                                        "Không sử dụng cho người có mẫn cảm, kiêng kỵ với bất kỳ thành phần nào của sản phẩm.\n"
                                                        + "Thành phần sản phẩm có glucose, natri và kali. Vì vậy, thận trọng sử dụng cho người tiểu đường, đang có chế độ ăn kiêng natri và kali, người bị rối loạn dung nạp glucose, tắc ruột, suy thận cấp."));

                        // Bảo quản (storage)
                        details23.add(createDetail(medicine23, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details23.add(createDetail(medicine23, MedicineDetailType.DESCRIPTION,
                                        "Cung cấp năng lượng, bổ sung điện giải.\n"
                                                        + "Bột điện giải vị chanh dây Kamizol Sport Drink Powder giúp cung cấp năng lượng và các chất điện giải cho cơ thể. "
                                                        + "Sản phẩm phù hợp với người vận động với cường độ cao, người chơi thể thao hoặc làm việc trong môi trường nắng nóng. "
                                                        + "Người bị ốm sốt, tiêu chảy, nôn mửa cũng có thể sử dụng để bổ sung điện giải."));

                        medicineDetailRepository.saveAll(details23);

                        Medicine medicine24 = new Medicine();
                        medicine24.setName("Trà Thảo Dược Cà Gai Leo Kami Tea (20 túi lọc x 2g)");
                        medicine24.setPrice(45000.0);
                        medicine24.setOriginalPrice(60000.0);
                        medicine24.setUnit("Hộp");
                        medicine24.setShortDescription(
                                        "Trà Cà Gai Leo Kami tea với thành phần từ thảo dược thiên nhiên là sản phẩm giúp hỗ trợ các bệnh về gan, hỗ trợ giải độc gan, giảm men gan hiệu quả.");
                        medicine24.setBrandOrigin("KAMI TEA");
                        medicine24.setManufacturer("CÔNG TY CỔ PHẦN DƯỢC PHẨM PHARVINA");
                        medicine24.setCountryOfManufacture("Việt Nam");
                        medicine24.setImageUrl(
                                        "product24.jpg");

                        // Gán category (ví dụ ID = 2)
                        Category category24 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine24.setCategory(category24);

                        medicine24 = medicineRepository.save(medicine24);

                        // Chi tiết thuốc
                        List<MedicineDetail> details24 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details24.add(createDetail(medicine24, MedicineDetailType.INGREDIENT,
                                        "Cà gai leo - 1.45g\nNhân trần - 0.4g\nCỏ ngọt - 0.14g\nKẽm Gluconat - 10mg"));

                        // Công dụng (effect)
                        details24.add(createDetail(medicine24, MedicineDetailType.EFFECT,
                                        "Trà Cà Gai Leo Kami tea hỗ trợ các bệnh về gan.\nGiúp hỗ trợ giải độc gan, giảm men gan."));

                        // Cách dùng & Đối tượng sử dụng (usage)
                        details24.add(createDetail(medicine24, MedicineDetailType.USAGE,
                                        "Cách dùng:\nNhúng 1 gói trà vào 200ml nước sôi, chờ cho trà ngấm 5 - 10 phút rồi uống. "
                                                        + "Thêm nước sôi để uống từ 2 - 3 lần cho đến khi vị trà nhạt dần. Ngày có thể uống 3 - 4 túi trà.\n"
                                                        + "Nên uống cách bữa ăn khoảng 30 phút.\n\n"
                                                        + "Đối tượng sử dụng:\nTrà Cà Gai Leo Kami tea dùng được cho mọi đối tượng."));

                        // Tác dụng phụ (side effect)
                        details24.add(createDetail(medicine24, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details24.add(createDetail(medicine24, MedicineDetailType.NOTE,
                                        "Thực phẩm này không phải là thuốc, không có tác dụng thay thế thuốc chữa bệnh.\n"
                                                        + "Không dùng cho phụ nữ mang thai, người có bệnh thận, người mẫn cảm, kiêng kỵ với bất cứ thành phần nào của sản phẩm."));

                        // Bảo quản (storage)
                        details24.add(createDetail(medicine24, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details24.add(createDetail(medicine24, MedicineDetailType.DESCRIPTION,
                                        "Hỗ trợ lá gan khỏe mạnh\nTrà Cà Gai Leo Kami tea với thành phần từ thảo dược thiên nhiên như cà gai leo, nhân trần, cỏ ngọt, là sản phẩm giúp hỗ trợ các bệnh về gan, hỗ trợ giải độc gan, giảm men gan hiệu quả."));

                        medicineDetailRepository.saveAll(details24);

                        Medicine medicine25 = new Medicine();
                        medicine25.setName("Dầu Húng Chanh Hỗ Trợ Lợi Phế Trừ Đờm Giảm Ho Labebé (30ml)");
                        medicine25.setPrice(144000.0);
                        medicine25.setOriginalPrice(180000.0);
                        medicine25.setUnit("Hộp");
                        medicine25.setShortDescription(
                                        "Dầu Húng Chanh Lábebé với thành phần chứa các thảo dược thiên nhiên an toàn và lành tính như húng chanh, quả cơm cháy, quế, bạc hà, gừng.");
                        medicine25.setBrandOrigin("LÁBEBÉ");
                        medicine25.setManufacturer("CÔNG TY CỔ PHẦN DƯỢC PHẨM PHARVINA");
                        medicine25.setCountryOfManufacture("Việt Nam");
                        medicine25.setImageUrl(
                                        "product25.jpg");

                        // Gán category (ví dụ ID = 2)
                        Category category25 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine25.setCategory(category25);

                        medicine25 = medicineRepository.save(medicine25);

                        // Chi tiết thuốc
                        List<MedicineDetail> details25 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details25.add(createDetail(medicine25, MedicineDetailType.INGREDIENT,
                                        "Nước tinh khiết\n"
                                                        + "Đường phèn\n"
                                                        + "Bột nước ép cô đặc quả cơm cháy - 20mg\n"
                                                        + "Kẽm Gluconat - 0.5mg\n"
                                                        + "Tinh dầu húng chanh - 0.1mg\n"
                                                        + "Tinh dầu quế - 0.05mg\n"
                                                        + "Tinh dầu bạc hà - 0.05mg\n"
                                                        + "Tinh dầu gừng - 0.03mg"));

                        // Công dụng (effect)
                        details25.add(createDetail(medicine25, MedicineDetailType.EFFECT,
                                        "Dầu Húng Chanh Lábebé hỗ trợ lợi phế trừ đờm, giảm ho."));

                        // Cách dùng & Đối tượng sử dụng (usage)
                        details25.add(createDetail(medicine25, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "Mỗi lần uống 3 đến 5 giọt, ngày uống 1 đến 3 lần tùy theo nhu cầu.\n"
                                                        + "Nên uống vào sáng sớm, trước ăn trưa, trước giấc ngủ tối.\n"
                                                        + "Lắc kỹ trước khi uống.\n"
                                                        + "Có thể pha loãng với nước lọc hoặc uống trực tiếp.\n\n"
                                                        + "Đối tượng sử dụng:\n"
                                                        + "Dầu Húng Chanh Lábebé dùng được cho trẻ sơ sinh từ 1 tháng tuổi, trẻ nhỏ, phụ nữ mang thai, mẹ cho con bú."));

                        // Tác dụng phụ (side effect)
                        details25.add(createDetail(medicine25, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details25.add(createDetail(medicine25, MedicineDetailType.NOTE,
                                        "Thực phẩm này không phải là thuốc, không có tác dụng thay thế thuốc chữa bệnh.\n"
                                                        + "Không dùng cho người mẫn cảm, kiêng kỵ với bất kỳ thành phần nào của sản phẩm."));

                        // Bảo quản (storage)
                        details25.add(createDetail(medicine25, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details25.add(createDetail(medicine25, MedicineDetailType.DESCRIPTION,
                                        "Hỗ trợ lợi phế trừ đàm giảm ho."));

                        medicineDetailRepository.saveAll(details25);

                        Medicine medicine26 = new Medicine();
                        medicine26.setName("Cao Đuổi Muỗi Cho Bé, Giảm Sưng Tấy Do Côn Trùng Đốt Ola Papi (20g)");
                        medicine26.setPrice(76000.0);
                        medicine26.setOriginalPrice(95000.0);
                        medicine26.setUnit("Hộp");
                        medicine26.setShortDescription(
                                        "Cao Đuổi Muỗi Ola Papi hỗ trợ xua đuổi muỗi hiệu quả. Thành phần chính là tinh dầu tràm và sả chanh được thu hoạch tại vùng dược liệu canh tác tự nhiên, chưng cất thủ công tại hợp tác xã. Sản phẩm an toàn cho trẻ nhỏ từ 3 tháng tuổi, mẹ bầu và phụ nữ sau sinh.");
                        medicine26.setBrandOrigin("OLA PAPI");
                        medicine26.setManufacturer("HỢP TÁC XÃ DƯỢC LIỆU TRƯỜNG SƠN");
                        medicine26.setCountryOfManufacture("Việt Nam");
                        medicine26.setImageUrl(
                                        "product26.jpg");

                        // Gán category (ví dụ ID = 2)
                        Category category26 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine26.setCategory(category26);

                        medicine26 = medicineRepository.save(medicine26);

                        // Chi tiết thuốc
                        List<MedicineDetail> details26 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details26.add(createDetail(medicine26, MedicineDetailType.INGREDIENT,
                                        "Tinh dầu tràm\n"
                                                        + "Tinh dầu Sả chanh\n"
                                                        + "Vaseline dưỡng da chiết xuất từ thiên nhiên"));

                        // Công dụng (effect)
                        details26.add(createDetail(medicine26, MedicineDetailType.EFFECT,
                                        "Cao Đuổi Muỗi Ola Papi xua đuổi muỗi hiệu quả."));

                        // Cách dùng & Đối tượng sử dụng (usage)
                        details26.add(createDetail(medicine26, MedicineDetailType.USAGE,
                                        "Cách dùng:\n"
                                                        + "Bôi một lượng vừa đủ lên vết muỗi cắn hoặc vùng da mẩn ngứa, mẹ xoa đều da cho bé nhẹ nhàng từ 5 - 10 giây.\n"
                                                        + "Bôi cao vào lòng bàn chân, những vùng da hở để xua đuổi, chống muỗi và côn trùng cắn.\n"
                                                        + "Có thể bôi ngực, cổ, bụng và gan bàn chân để giữ ấm, phòng cảm lạnh, tránh gió.\n\n"
                                                        + "Đối tượng sử dụng:\n"
                                                        + "Cao Đuổi Muỗi Ola Papi an toàn cho trẻ nhỏ từ 3 tháng tuổi, mẹ bầu và phụ nữ sau sinh."));

                        // Tác dụng phụ (side effect)
                        details26.add(createDetail(medicine26, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details26.add(createDetail(medicine26, MedicineDetailType.NOTE,
                                        "Không bôi lên vết thương hở, đọc kỹ hướng dẫn sử dụng trước khi dùng.\n"
                                                        + "Dùng cho bé từ 3 tháng tuổi trở lên.\n"
                                                        + "Không dùng cho người mẫn cảm với thành phần của sản phẩm.\n"
                                                        + "Không được ăn, uống sản phẩm."));

                        // Bảo quản (storage)
                        details26.add(createDetail(medicine26, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details26.add(createDetail(medicine26, MedicineDetailType.DESCRIPTION,
                                        "Đuổi muỗi, giảm sưng tấy do côn trùng đốt"));

                        medicineDetailRepository.saveAll(details26);

                        Medicine medicine27 = new Medicine();
                        medicine27.setName("Nước Hồng Sâm Đông Trùng Hạ Thảo Biok (10 chai x 100ml)");
                        medicine27.setPrice(260000.0);
                        medicine27.setOriginalPrice(325000.0);
                        medicine27.setUnit("Hộp");
                        medicine27.setShortDescription(
                                        "Nước hồng sâm đông trùng hạ thảo Biok chứa chiết xuất hồng sâm, đông trùng hạ thảo cùng một số dược liệu quý giá khác như linh chi, đương quy, sinh địa, xà sàng, cam thảo...");
                        medicine27.setBrandOrigin("Biok");
                        medicine27.setManufacturer("Well Bio");
                        medicine27.setCountryOfManufacture("Hàn Quốc");
                        medicine27.setImageUrl(
                                        "product27.jpg");

                        // Gán category (ví dụ ID = 2)
                        Category category27 = categoryRepository.findById(2)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine27.setCategory(category27);

                        medicine27 = medicineRepository.save(medicine27);

                        // Chi tiết thuốc
                        List<MedicineDetail> details27 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details27.add(createDetail(medicine27, MedicineDetailType.INGREDIENT,
                                        "Đông trùng hạ thảo (Hàn Quốc) - 0.25%\n" +
                                                        "Linh chi (Trung Quốc)\n" +
                                                        "Đương quy\n" +
                                                        "Sinh Địa\n" +
                                                        "Xà sàng\n" +
                                                        "Cam thảo\n" +
                                                        "Nước tinh khiết\n" +
                                                        "Đường fructose - 13%\n" +
                                                        "Tinh chất hồng sâm cô đặc - 0.2%\n" +
                                                        "Acid citric - 0.18%\n" +
                                                        "Táo đỏ cô đặc - 0.1%\n" +
                                                        "Sodium benzoate - 0.055%\n" +
                                                        "Đường Stevia - 0.04%\n" +
                                                        "Glycine - 0.03%\n" +
                                                        "Sodium Citrate - 0.02%\n" +
                                                        "Vitamin B1 - 0.002%\n" +
                                                        "Vitamin B2 - 0.002%"));

                        // Công dụng (effect)
                        details27.add(createDetail(medicine27, MedicineDetailType.EFFECT,
                                        "Tăng khả năng miễn dịch."));

                        // Cách dùng (usage)
                        details27.add(createDetail(medicine27, MedicineDetailType.USAGE,
                                        "Uống trực tiếp."));

                        // Tác dụng phụ (side effect)
                        details27.add(createDetail(medicine27, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details27.add(createDetail(medicine27, MedicineDetailType.NOTE,
                                        "Không sử dụng sản phẩm khi hết hạn hoặc có mùi vị lạ.\n" +
                                                        "Sản phẩm có thể bị lắng do thành phần của sản phẩm, lắc đều trước khi sử dụng.\n"
                                                        +
                                                        "Không đun trên lò vi sóng hoặc trên bếp."));

                        // Bảo quản (storage)
                        details27.add(createDetail(medicine27, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details27.add(createDetail(medicine27, MedicineDetailType.DESCRIPTION,
                                        "Nước hồng sâm đông trùng hạ thảo"));

                        medicineDetailRepository.saveAll(details27);

                        Medicine medicine28 = new Medicine();
                        medicine28.setName("Nước Yến Sào Cao Cấp Cho Trẻ Em Nunest Kid Vị Chuối (6 hũ x 70ml)");
                        medicine28.setPrice(168750.0);
                        medicine28.setOriginalPrice(225000.0);
                        medicine28.setUnit("Hộp");
                        medicine28.setShortDescription(
                                        "Nước yến sào cao cấp Nunest Kid vị Chuối là sản phẩm kết hợp giữa yến sào tự nhiên từ Khánh Hòa, Ninh Thuận cùng bột Chuối tự nhiên và các dưỡng chất bổ dưỡng. Yến sào bổ dưỡng cho sức khoẻ. Lysin giúp bé ăn ngon miệng cùng Vitamin D3 hỗ trợ phát triển chiều cao. Đặc biệt, tăng cường sức đề kháng nhờ Beta-glucan 1,3/1,6.");
                        medicine28.setBrandOrigin("Nunest");
                        medicine28.setManufacturer("CÔNG TY CP DINH DƯỠNG Y HỌC QUỐC TẾ");
                        medicine28.setCountryOfManufacture("Việt Nam");
                        medicine28.setImageUrl(
                                        "product28.jpg");

                        // Gán category (ví dụ ID = 2)
                        Category category28 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine28.setCategory(category28);

                        medicine28 = medicineRepository.save(medicine28);

                        // Chi tiết thuốc
                        List<MedicineDetail> details28 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details28.add(createDetail(medicine28, MedicineDetailType.INGREDIENT,
                                        "Yến sào chưng sẵn\n" +
                                                        "Beta Glucan 15%\n" +
                                                        "42.8mg Lysine\n" +
                                                        "85.7mg Vitamin D3\n" +
                                                        "7mcg Nước\n" +
                                                        "Đường phèn\n" +
                                                        "Sucrose\n" +
                                                        "Canxi lactate\n" +
                                                        "Chất ổn định\n" +
                                                        "Chuối\n" +
                                                        "Hương chuối tổng hợp\n" +
                                                        "Chất bảo quản"));

                        // Công dụng (effect)
                        details28.add(createDetail(medicine28, MedicineDetailType.EFFECT,
                                        "Giúp bé ăn ngon miệng, hỗ trợ phát triển chiều cao, tăng sức đề kháng."));

                        // Cách dùng (usage)
                        details28.add(createDetail(medicine28, MedicineDetailType.USAGE,
                                        "Cách dùng:\n" +
                                                        "Sử dụng trực tiếp, lắc nhẹ trước khi dùng, ngon hơn khi uống lạnh.\n"
                                                        +
                                                        "Uống 2 - 3 lọ/ngày.\n\n" +
                                                        "Đối tượng sử dụng:\n" +
                                                        "Nước yến sào cao cấp cho trẻ em Nunest Kid vị chuối thích hợp cho trẻ biếng ăn, mệt mỏi, sức đề kháng kém.\n"
                                                        +
                                                        "Trẻ dưới 2 tuổi tham khảo ý kiến của bác sĩ/chuyên gia dinh dưỡng, đặc biệt trẻ từ 6 tháng đến 1 tuổi sử dụng dưới sự giám sát của người lớn."));

                        // Tác dụng phụ (side effect)
                        details28.add(createDetail(medicine28, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details28.add(createDetail(medicine28, MedicineDetailType.NOTE,
                                        "Không sử dụng nếu bao bì bị hư hỏng hoặc sản phẩm bên trong có dấu hiệu bất thường. Sau khi mở nắp, bảo quản lạnh hoặc dùng ngay."));

                        // Bảo quản (storage)
                        details28.add(createDetail(medicine28, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, thoáng mát, tránh xa ánh nắng mặt trời."));

                        // Mô tả (description)
                        details28.add(createDetail(medicine28, MedicineDetailType.DESCRIPTION,
                                        "Giúp bé ăn ngon miệng, hỗ trợ phát triển chiều cao, tăng sức đề kháng."));

                        medicineDetailRepository.saveAll(details28);

                        Medicine medicine29 = new Medicine();
                        medicine29.setName(
                                        "Bông tẩy trang tròn KamiCare Cotton Pads thấm hút tốt giúp sạch bụi bẩn, tẩy tế bào chết trên da (120 miếng)");
                        medicine29.setPrice(26400.0);
                        medicine29.setOriginalPrice(33000.0);
                        medicine29.setUnit("Túi");
                        medicine29.setShortDescription(
                                        "Bông tẩy trang tròn Kamicare 120 miếng được làm từ 100% bông cotton thiên nhiên. Sản phẩm có 2 mặt mềm mịn, khả năng thấm hút tốt, giúp tẩy trang hiệu quả, làm sạch bụi bẩn và tế bào chết trên da, mang đến làn da mịn màng.");
                        medicine29.setBrandOrigin("KamiCARE");
                        medicine29.setManufacturer("VPC");
                        medicine29.setCountryOfManufacture("Việt Nam");
                        medicine29.setImageUrl(
                                        "product29.jpg");

                        // Gán category (ví dụ ID = 2 - mỹ phẩm hoặc chăm sóc da)
                        Category category29 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine29.setCategory(category29);

                        medicine29 = medicineRepository.save(medicine29);

                        // Chi tiết thuốc
                        List<MedicineDetail> details29 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details29.add(createDetail(medicine29, MedicineDetailType.INGREDIENT,
                                        "Bông tự nhiên\n\n100%\n\nChất bảo quản"));

                        // Công dụng (effect)
                        details29.add(createDetail(medicine29, MedicineDetailType.EFFECT,
                                        "Bông tẩy trang Kamicare được làm từ 100% cotton thiên nhiên với hai mặt mềm mịn, độ thấm hút tối đa giúp bạn tẩy trang hiệu quả, làm sạch bụi bẩn trên da và những tế bào chết, làm thoáng lỗ chân lông, giúp da sạch sẽ sáng mịn màng một cách tự nhiên."));

                        // Cách dùng (usage)
                        details29.add(createDetail(medicine29, MedicineDetailType.USAGE,
                                        "Cách dùng:\nDùng miếng bông thấm dung dịch làm sạch (nước hoa hồng...) hoặc mỹ phẩm (nước dưỡng, lotion) lau nhẹ nhàng lên da mặt (cổ, tay...) khi tẩy trang, làm sạch hoặc trang điểm.\n\nĐối tượng sử dụng:\nBông tẩy trang Kamicare dùng được cho mọi đối tượng."));

                        // Tác dụng phụ (side effect)
                        details29.add(createDetail(medicine29, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details29.add(createDetail(medicine29, MedicineDetailType.NOTE,
                                        "Đọc kỹ hướng dẫn sử dụng trước khi dùng.\nKhông dùng sản phẩm đã hết hạn sử dụng."));

                        // Bảo quản (storage)
                        details29.add(createDetail(medicine29, MedicineDetailType.STORAGE,
                                        "Nơi khô ráo, tránh xa tầm tay với của trẻ em."));

                        // Mô tả (description)
                        details29.add(createDetail(medicine29, MedicineDetailType.DESCRIPTION,
                                        "Bông tẩy trang 100% từ cotton thiên nhiên\nBông tẩy trang tròn Kamicare 120 miếng được làm từ 100% bông cotton thiên nhiên. Sản phẩm có 2 mặt mềm mịn, khả năng thấm hút tốt, giúp tẩy trang hiệu quả, làm sạch bụi bẩn và tế bào chết trên da, mang đến làn da mịn màng."));

                        medicineDetailRepository.saveAll(details29);

                        Medicine medicine30 = new Medicine();
                        medicine30.setName("Viên Nghệ Mật Ong Royal Honey (250g)");
                        medicine30.setPrice(46125.0);
                        medicine30.setOriginalPrice(61500.0);
                        medicine30.setUnit("Chai");
                        medicine30.setShortDescription(
                                        "Viên nghệ mật ong Royal Honey 250g là sự kết hợp hài hòa giữa những nguyên liệu tốt cho sức khỏe là mật ong, nghệ và sữa ong chúa. Sản phẩm có mùi vị thơm ngon, tiện lợi khi sử dụng và rất tốt cho sức khỏe.");
                        medicine30.setBrandOrigin("Royal Honey");
                        medicine30.setManufacturer("CÔNG TY CỔ PHẦN ONG MẬT TIỀN GIANG");
                        medicine30.setCountryOfManufacture("Việt Nam");
                        medicine30.setImageUrl(
                                        "product30.jpg");

                        // Gán category (ví dụ ID = 1 - thực phẩm chức năng hoặc chăm sóc sức khỏe)
                        Category category30 = categoryRepository.findById(3)
                                        .orElseThrow(() -> new RuntimeException("Category not found"));
                        medicine30.setCategory(category30);

                        medicine30 = medicineRepository.save(medicine30);

                        // Chi tiết thuốc
                        List<MedicineDetail> details30 = new ArrayList<>();

                        // Thành phần (ingredient)
                        details30.add(createDetail(medicine30, MedicineDetailType.INGREDIENT,
                                        "Mật ong\n\n27%\n\nSữa ong chúa\n\n3%\n\nNghệ\n\n70%\nChất bảo quản"));

                        // Công dụng (effect)
                        details30.add(createDetail(medicine30, MedicineDetailType.EFFECT,
                                        "Tăng cường hệ thống miễn dịch, giải độc cơ thể, tốt cho tiêu hóa, kiểm soát được bệnh tiểu đường. "
                                                        + "Viên nghệ mật ong còn có khả năng ngăn ngừa bệnh gút, viêm cơ khớp, thanh lọc gan giúp cơ thể luôn được khỏe mạnh. "
                                                        + "Uống nghệ viên mật ong mỗi ngày giúp an thần, ngủ ngon, giảm căng thẳng và làm giảm quá trình lão hoá."));

                        // Cách dùng (usage)
                        details30.add(createDetail(medicine30, MedicineDetailType.USAGE,
                                        "Cách dùng:\nMở bao bì, uống trực tiếp với nước ấm từ 15 - 20 viên ngày 2 lần.\n\nĐối tượng sử dụng:\nDùng cho người lớn."));

                        // Tác dụng phụ (side effect)
                        details30.add(createDetail(medicine30, MedicineDetailType.SIDE_EFFECT,
                                        "Chưa có thông tin về tác dụng phụ của sản phẩm."));

                        // Lưu ý (note)
                        details30.add(createDetail(medicine30, MedicineDetailType.NOTE,
                                        "Không sử dụng sản phẩm khi đã hết hạn."));

                        // Bảo quản (storage)
                        details30.add(createDetail(medicine30, MedicineDetailType.STORAGE,
                                        "Nơi sạch sẽ, khô ráo, tránh ánh nắng trực tiếp.\nĐậy kín nắp sau khi mở lọ."));

                        // Mô tả (description)
                        details30.add(createDetail(medicine30, MedicineDetailType.DESCRIPTION,
                                        "Bông tẩy trang 100% từ cotton thiên nhiên\n"
                                                        + "Bông tẩy trang tròn Kamicare 120 miếng được làm từ 100% bông cotton thiên nhiên. "
                                                        + "Sản phẩm có 2 mặt mềm mịn, khả năng thấm hút tốt, giúp tẩy trang hiệu quả, làm sạch bụi bẩn và tế bào chết trên da, "
                                                        + "mang đến làn da mịn màng.")); // <- nếu có mô tả riêng, có
                                                                                         // thể thay thế đoạn này

                        medicineDetailRepository.saveAll(details30);

                }
        }

        private MedicineDetail createDetail(Medicine medicine, MedicineDetailType type, String content) {
                MedicineDetail detail = new MedicineDetail();
                detail.setMedicine(medicine);
                detail.setType(type);
                detail.setContent(content);
                return detail;
        }
}
