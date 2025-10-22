package dao;


import entity.RefreshTokenTable;
import entity.UserTokenTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import system.JwtTokenProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class RefreshTokenDAO {


    private final SessionFactory sessionFactory;

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

    public Optional<RefreshTokenTable> findByToken(String refresh){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            RefreshTokenTable token = session.createQuery("FROM RefreshTokenTable WHERE token = :token", RefreshTokenTable.class)
                    .setParameter("token", refresh)
                    .uniqueResult();
            transaction.commit();
            return Optional.ofNullable(token);
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


    public void deleteAll (){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM RefreshTokenTable").executeUpdate();
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


    public List<RefreshTokenTable> findAllByUserId (UUID id){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<RefreshTokenTable> results = session.createQuery(
                            "SELECT rt FROM RefreshTokenTable rt " +
                                    "JOIN rt.userTokenTable ut " +
                                    "WHERE ut.user.id = :id", RefreshTokenTable.class)
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
