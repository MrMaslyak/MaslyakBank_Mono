package dao;


import entity.RefreshTokenTable;
import entity.UserTokenTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import system.JwtTokenProvider;

@Repository
@AllArgsConstructor
public class RefreshTokenDAO {


    private final SessionFactory sessionFactory;
    private final JwtTokenProvider jwtTokenProvider;

    public RefreshTokenTable saveToken(RefreshTokenTable refreshToken) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            RefreshTokenTable saved = session.merge(refreshToken);
            transaction.commit();
            return saved;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
