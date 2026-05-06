package iuh.fit.son23641341.nhahanglau_phantan.dao;

// NOTE: Database-backed authentication removed; implementations now use mock data.
public interface IUserDAO {
	boolean authenticate(String username, String password);

}
