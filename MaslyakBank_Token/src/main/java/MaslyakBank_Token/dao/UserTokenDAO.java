package MaslyakBank_Token.dao;


import MaslyakBank_Token.entity.TokenTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserTokenDAO {

    private final SessionFactory sessionFactory;

    public TokenTable saveToken(TokenTable userToken) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.merge(userToken);
            transaction.commit();
            return userToken;
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
