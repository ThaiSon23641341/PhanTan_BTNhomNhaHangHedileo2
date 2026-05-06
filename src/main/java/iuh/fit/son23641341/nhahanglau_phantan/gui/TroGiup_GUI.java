package iuh.fit.son23641341.nhahanglau_phantan.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TroGiup_GUI extends JFrame {
    private SideBar_GUI sidebar;

    public TroGiup_GUI() {
        setTitle("Trợ Giúp - Hướng Dẫn Sử Dụng Hệ Thống");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full màn hình
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        sidebar = new SideBar_GUI();
        sidebar.setMauNutKhiChon("Trợ Giúp");
        add(sidebar, BorderLayout.WEST);

        // Panel chứa nội dung hướng dẫn
        JPanel pnlContent = new JPanel(new BorderLayout());

        JLabel lblTitle = new JLabel("HƯỚNG DẪN SỬ DỤNG HỆ THỐNG QUẢN LÝ NHÀ HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        pnlContent.add(lblTitle, BorderLayout.NORTH);

        JEditorPane txtGuide = new JEditorPane();
        txtGuide.setContentType("text/html");
        txtGuide.setEditable(false);
        txtGuide.setFont(new Font("Arial", Font.PLAIN, 15));
        txtGuide.setText(
            "<html><body style='font-family:Arial;font-size:15px;'>"
            + "<b style='font-size:18px;color:#E44433;'>I. TỔNG QUAN CHỨC NĂNG</b><br>"
            + "<span>- <b>Đặt bàn trước</b>: Đặt bàn cho khách đến sau, chọn ngày, giờ, nhập thông tin khách, xác nhận phiếu.<br>"
            + "- <b>Đặt bàn vãng lai</b>: Đặt bàn cho khách đến trực tiếp, sử dụng ngay không cần xác nhận.<br>"
            + "- <b>Quản lý phiếu đặt</b>, bắt đầu sử dụng bàn, <span style='color:#E44433;font-weight:bold;'>thanh toán</span>, hủy phiếu, xem chi tiết phiếu.<br></span>"
            + "<br>"
            + "<b style='font-size:18px;color:#E44433;'>II. HƯỚNG DẪN CHI TIẾT</b><br>"
            + "<b style='color:#4A90E2;'>1. ĐẶT BÀN THEO KHUNG GIỜ</b><br>"
            + "- Chọn tab <b style='color:#F5A623;'>Chọn bàn</b>.<br>"
            + "- Chọn ngày muốn đặt và nhấn vào <b>bàn còn trống</b> (<span style='color:#7AB750;'>màu xanh</span>).<br>"
            + "- Nhập thông tin khách đặt (<b>họ tên, số điện thoại</b>), chọn khung giờ, thêm món ăn nếu muốn.<br>"
            + "- Nếu đặt trước: Nhấn <b style='color:#E44433;'>Xác nhận đặt</b> để xác nhận phiếu đặt trước.<br>"
            + "- Nếu vãng lai: Sử dụng ngay không cần xác nhận.<br>"
            + "<br>"
            + "<b style='color:#4A90E2;'>2. HƯỚNG DẪN THANH TOÁN</b><br>"
            + "- Khi khách dùng xong, chọn <b>bàn đang sử dụng</b> (<span style='color:#EC893E;'>màu cam</span>).<br>"
            + "- Nhấn <b style='color:#E44433;'>Thanh toán</b> để thực hiện thanh toán hóa đơn.<br>"
            + "<br>"
            + "<b style='color:#4A90E2;'>3. THÊM SỐ ĐIỆN THOẠI LÀM THÀNH VIÊN</b><br>"
            + "- Khi nhập thông tin khách đặt bàn, nhập <b>số điện thoại</b> vào ô tương ứng.<br>"
            + "- Nếu số điện thoại chưa có trong hệ thống, nhập số điện thoại vào ô <b>số điện thoại thành viên</b> và tick chọn <span style='color:#7AB750;font-weight:bold;'>thêm thành viên</span> sẽ tự động thêm thành viên mới khi xác nhận phiếu.<br>"
            + "- Nếu số điện thoại đã có, hệ thống sẽ tự động nhận diện và cập nhật thông tin đặt bàn cho thành viên đó.<br>"
            + "- Có thể chỉnh sửa/thêm thông tin thành viên tại mục <b style='color:#F5A623;'>Quản lý khách hàng</b>.<br>"
            + "<br>"
            + "<b style='color:#E44433;'>4. LƯU Ý KHI SỬ DỤNG</b><br>"
            + "- <b>Chỉ bàn đặt trước</b> mới cần xác nhận phiếu.<br>"
            + "- <b>Bàn vãng lai</b> sử dụng ngay, không cần xác nhận.<br>"
            + "- Đảm bảo chọn đúng ngày khi thao tác với bàn đã đặt trước.<br>"
            + "- Có thể thêm món ăn, chỉnh sửa thông tin trước khi xác nhận hoặc sử dụng.<br>"
            + "- Khi thanh toán muốn áp dụng <b style='color:#7AB750;'>giảm giá thành viên</b>, nhập số điện thoại thành viên vào ô số điện thoại thành viên và ấn thanh toán, hệ thống sẽ tự động trừ tiền giảm giá.<br>"
            + "- Nếu bàn đã <b style='color:#D94B33;'>full lịch</b>, không thể đặt thêm.<br>"
            + "<br>"
            + "<b style='color:#4A90E2;'>5. TRUY CẬP TRỢ GIÚP</b><br>"
            + "- Nhấn nút <b style='color:#E44433;'>Trợ Giúp</b> ở thanh bên trái (SideBar) để mở lại hướng dẫn này bất cứ lúc nào.<br>"
            + "</body></html>"
        );
        JScrollPane scroll = new JScrollPane(txtGuide);
        scroll.setBorder(new EmptyBorder(10, 30, 10, 30));
        pnlContent.add(scroll, BorderLayout.CENTER);

        add(pnlContent, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TroGiup_GUI().setVisible(true));
    }
}
