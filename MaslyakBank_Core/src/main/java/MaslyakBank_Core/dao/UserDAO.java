package MaslyakBank_Core.dao;


import MaslyakBank_Core.dto.DeleteUsersDTO;
import MaslyakBank_Core.entity.UsersTable;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

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

}
