# PHẦN MỀM QUẢN LÝ NHÂN SỰ

## 1. Mô tả phần mềm
Phần mềm Quản Lý Nhân Sự là ứng dụng Java Swing phát triển trên nền tảng NetBeans, giúp quản lý thông tin nhân viên, hỗ trợ các nghiệp vụ cơ bản như thêm, sửa, xóa, tìm kiếm, thống kê nhân sự. Dữ liệu được lưu trữ hoàn toàn bằng file XML.

## 2. Các chức năng chính
- Đăng nhập hệ thống
- Quản lý danh sách nhân viên (thêm, sửa, xóa, tìm kiếm)
- Tìm kiếm theo tên, quê quán, năm sinh (gần đúng)
- Thống kê tuổi, chức vụ
- Hiển thị dữ liệu dạng bảng (JTable)
- Chọn ngày sinh bằng lịch (JDateChooser)
- Định dạng tiền tệ có dấu phẩy
- Kiểm tra trùng số điện thoại, validate dữ liệu đầu vào

## 3. Thông tin tài khoản đăng nhập
- **Tên đăng nhập:** `admin`
- **Mật khẩu:** `123`

## 4. Hướng dẫn chạy chương trình
### a) Chạy bằng NetBeans
1. Mở NetBeans, chọn **File > Open Project...** và trỏ tới thư mục `PhanMemQuanLyNhanSu/PhanMemQuanLyNhanSu`.
2. Nhấn chuột phải vào tên project, chọn **Run** (hoặc nhấn F6).
3. Giao diện đăng nhập sẽ xuất hiện, nhập tài khoản như trên để sử dụng.

### b) Chạy bằng file JAR đã đóng gói
1. Đảm bảo máy tính đã cài Java (JRE/JDK 17 trở lên).
2. Mở terminal/cmd, chuyển đến thư mục chứa file JAR (`target/` hoặc `dist/`).
3. Chạy lệnh:
   ```sh
   java -jar PhanMemQuanLyNhanSu-1.0-SNAPSHOT.jar
   ```
4. Đăng nhập và sử dụng phần mềm.

## 5. Lưu ý
- Dữ liệu lưu trong file XML, không dùng CSDL khác.

## 6. Liên hệ
- Tác giả: Trần Tuấn Anh    : 23017118@st.phenikaa-uni.edu.vn
           Ngô Quang Trung  : 23017195@st.phenikaa-uni.edu.vn
