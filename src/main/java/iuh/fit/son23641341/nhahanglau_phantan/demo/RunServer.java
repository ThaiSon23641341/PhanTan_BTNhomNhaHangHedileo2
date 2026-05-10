package iuh.fit.son23641341.nhahanglau_phantan.demo;

import iuh.fit.son23641341.nhahanglau_phantan.server.Server;

/**
 * Chạy file này trên MÁY SERVER (máy có kết nối CSDL).
 *
 * Cách chạy từ terminal:
 *   java -cp <classpath> iuh.fit.son23641341.nhahanglau_phantan.demo.RunServer
 *
 * Hoặc build JAR rồi chạy:
 *   java -jar server.jar
 */
public class RunServer {
    public static void main(String[] args) {
        System.out.println("=== KHỞI ĐỘNG SERVER NHÀ HÀNG HEDILEO ===");
        System.out.println("Server sẽ lắng nghe trên port 6789...");
        System.out.println("Các máy Client hãy kết nối vào IP của máy này.");
        System.out.println("==========================================");
        Server.main(args);
    }
}
