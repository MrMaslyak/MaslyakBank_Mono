package dao;


import entity.UserTokenTable;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import system.JwtTokenProvider;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserTokenDAO {

    private final SessionFactory sessionFactory;

    public UserTokenTable saveToken(UserTokenTable userToken) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UserTokenTable saved = session.merge(userToken);
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

    public void deleteToken (String login){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UserTokenTable result = session.createQuery(
                            "FROM UserTokenTable WHERE user.login = :login", UserTokenTable.class)
                    .setParameter("login", login)
                    .uniqueResult();
            session.remove(result);
            transaction.commit();
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

    public List<UserTokenTable> findTokensByUser(UsersTable user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<UserTokenTable> results = session.createQuery(
                            "FROM UserTokenTable WHERE user.id = :id ORDER BY createdAt DESC", UserTokenTable.class)
                    .setParameter("id", user.getId())
                    .list();
            transaction.commit();
            return results;
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

    public void deleteByUserId (UUID id){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM UserTokenTable ut WHERE ut.user.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            transaction.commit();
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


    public List<UserTokenTable> findAllByUserId (UUID id){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<UserTokenTable> results = session.createQuery(
                            "FROM UserTokenTable WHERE user.id = :id", UserTokenTable.class)
                    .setParameter("id", id)
                    .list();
            transaction.commit();
            return results;
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
