package dao;


import entity.RefreshTokenTable;
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


    public RefreshTokenTable findRefreshToken(String token){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            RefreshTokenTable result = session.createQuery(
                            "FROM RefreshTokenTable WHERE token = :token", RefreshTokenTable.class)
                    .setParameter("token", token)
                    .uniqueResult();
            transaction.commit();
            return result;
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
