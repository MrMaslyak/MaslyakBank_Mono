package dao;

import entity.UsersTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserDAO {

    private final SessionFactory sessionFactory;

    public UsersTable findById (UUID id){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UsersTable user = session.find(UsersTable.class, id);
            transaction.commit();
            return user;
        }catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateUser(UsersTable user) {
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        }catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }finally {
            if (session != null) {
                session.close();
            }
        }
    }


}
