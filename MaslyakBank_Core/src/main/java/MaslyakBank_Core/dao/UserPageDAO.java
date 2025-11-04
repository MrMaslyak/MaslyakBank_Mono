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


    public List<UsersTable> findUsersPage(int size, int offset) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<UsersTable> users = session.createQuery(
                            "FROM UsersTable u ORDER BY u.createdAt ASC, u.id ASC", UsersTable.class)
                    .setFirstResult(offset)
                    .setMaxResults(size)
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

    public List<UsersTable> getFirstPage(int limit){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<UsersTable> users = session.createQuery(
                            "FROM UsersTable u ORDER BY u.createdAt ASC, u.id ASC", UsersTable.class)
                    .setMaxResults(limit)
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

    public List<UsersTable> getNextPage (int limit, UsersTable lastUser){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<UsersTable> users = session.createQuery(
                            "FROM UsersTable u WHERE u.createdAt > :createdAt OR (u.createdAt = :createdAt AND u.id > :id) ORDER BY u.createdAt ASC, u.id ASC", UsersTable.class)
                    .setParameter("createdAt", lastUser.getCreatedAt())
                    .setParameter("id", lastUser.getId())
                    .setMaxResults(limit)
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
