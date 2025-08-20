package dao;


import entity.UserTokenTable;
import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import system.JwtTokenProvider;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserTokenDAO {

    private final SessionFactory sessionFactory;
    private final JwtTokenProvider jwtTokenProvider;

    public UserTokenTable saveToken(UserTokenTable userToken) {
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

    public void deleteToken (String login){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UserTokenTable result = session.createQuery(
                            "FROM TokenTable WHERE user.login = :login", UserTokenTable.class)
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

    public void updateExpiredTokens() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Date now = new Date();

            List<UserTokenTable> tokens = session.createQuery(
                    "FROM TokenTable WHERE isExpired = false", UserTokenTable.class
            ).list();

            for (UserTokenTable token : tokens) {
                if (jwtTokenProvider.isTokenExpired(token.getToken())) {
                    token.setExpired(true);
                    token.setValid(false);
                    token.setUpdatedAt(now);
                    session.merge(token);
                    System.out.println(("Token {} marked as expired " + token.getId()));
                }
                token.setUpdatedAt(now);
                session.merge(token);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    public void deleteUsersByIds(List<UUID> userIds) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            List<UsersTable> users = session.createQuery(
                    "FROM UsersTable WHERE id IN (:ids)", UsersTable.class
            ).setParameter("ids", userIds).list();

            for (UsersTable user : users) {
                session.remove(user);
            }

            transaction.commit();
            System.out.println("Deleted users with expired tokens: " + users.size());

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public boolean findTokenByUser (UsersTable user){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UserTokenTable result = session.createQuery(
                            "FROM TokenTable WHERE user.id = :id", UserTokenTable.class)
                    .setParameter("id", user.getId())
                    .uniqueResult();
            transaction.commit();
            return result != null;
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
