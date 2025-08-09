package MaslyakBank_Core.dao;


import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.dto.requests.JwtTokenRequestDTO;
import MaslyakBank_Core.dto.requests.admin.SAdminListDTO;
import entity.UsersTable;
import enums.UserRole;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserSecurityDAO {

    private final SessionFactory sessionFactory;

    public void registrationUser(UsersTable user) {
        Transaction  transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(user);
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



    public DeleteUsersDTO deleteUsers(DeleteUsersDTO dto) {
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

    public void deleteUser(String login) {
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UsersTable user = session.createQuery("FROM UsersTable WHERE login = :login", UsersTable.class)
                    .setParameter("login", login).getResultList()
                    .stream().findFirst().orElse(null);
            if (user != null) {
                session.remove(user);
            }
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


    public UsersTable login(JwtTokenRequestDTO dto) {
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UsersTable user = session.createQuery("FROM UsersTable WHERE password = :password AND login = :login", UsersTable.class)
                    .setParameter("login", dto.getLogin())
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

    public boolean existsByLogin(String login){
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            UsersTable user = session.createQuery("FROM UsersTable WHERE login = :login", UsersTable.class)
                    .setParameter("login", login)
                    .getResultList()
                    .stream().findFirst().orElse(null);
            transaction.commit();
            return user != null;
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

    public void updateUserRole(SAdminListDTO sAdminListDTO, UserRole role){
        Transaction   transaction = null;
        Session  session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            for (String login : sAdminListDTO.getAdminList()) {
                UsersTable user = session.createQuery("FROM UsersTable WHERE login = :login", UsersTable.class)
                        .setParameter("login", login).getResultList()
                        .stream().findFirst().orElse(null);
                if (user != null) {
                    user.setRole(role);
                    session.merge(user);
                }
            }
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
