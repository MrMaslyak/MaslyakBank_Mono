package MaslyakBank_Core.dao;

import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserPageDAO {

    private final SessionFactory sessionFactory;



    public int countUsers() {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Long count = session.createQuery("SELECT COUNT(u) FROM UsersTable u", Long.class)
                    .uniqueResult();

            transaction.commit();
            return count != null ? count.intValue() : 0;
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


    public List<UsersTable> findUsersPage (int size, int page) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<UsersTable> users = session.createQuery(
                            "FROM UsersTable u ORDER BY u.createdAt", UsersTable.class)
                    .setFirstResult((page - 1) * size)  // OFFSET
                    .setMaxResults(size)                // LIMIT
                    .list();
            transaction.commit();
            return users;
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
