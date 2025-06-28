package MaslyakBank_Core.dao;


import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.LoginRequestDTO;
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

    public UsersTable registrationUser(UsersTable user) {
        Transaction  transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(user);
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

    public UsersTable findById (UUID id){
        Transaction   transaction = null;
        Session  session = null;
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

    public DeleteUsersDTO deleteUser(DeleteUsersDTO dto) {
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            for (String login : dto.getLogin()) {
                UsersTable user = session.createQuery("FROM UsersTable WHERE login = :login", UsersTable.class)
                        .setParameter("login", login).getResultList()
                        .stream().findFirst().orElse(null);
                if (user != null) {
                    session.remove(user);
                }
            }
            transaction.commit();
            return dto;
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

    public UsersTable login(LoginRequestDTO dto) {
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UsersTable user = session.createQuery("FROM UsersTable WHERE email = :email AND password = :password AND login = :login", UsersTable.class)
                    .setParameter("login", dto.getLogin())
                    .setParameter("email", dto.getEmail())
                    .setParameter("password", dto.getPassword())
                    .getResultList()
                    .stream().findFirst().orElse(null);
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
}
