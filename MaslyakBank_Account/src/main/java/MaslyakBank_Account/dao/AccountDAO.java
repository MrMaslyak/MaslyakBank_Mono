package MaslyakBank_Account.dao;


import entity.AccountTable;
import enums.Currency;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class AccountDAO {

    private final SessionFactory sessionFactory;

    public AccountTable saveAccount (AccountTable account){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(account);
            transaction.commit();
            return account;
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

    public AccountTable findByCurrency(UUID userId, Currency currency) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            AccountTable account = session.createQuery(
                            "FROM AccountTable WHERE currency = :currency AND user.id = :userId", AccountTable.class)
                    .setParameter("currency", currency)
                    .setParameter("userId", userId)
                    .uniqueResult();

            transaction.commit();
            return account;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public AccountTable findByIban(String ibanNumber){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            AccountTable account = session.createQuery(
                            "FROM AccountTable WHERE accountNumber = :ibanNumber", AccountTable.class)
                    .setParameter("ibanNumber", ibanNumber)
                    .uniqueResult();

            transaction.commit();
            return account;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }


}
