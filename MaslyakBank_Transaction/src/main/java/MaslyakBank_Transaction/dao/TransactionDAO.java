package MaslyakBank_Transaction.dao;


import MaslyakBank_Transaction.entity.TransactionTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAO {

    private final SessionFactory sessionFactory;


    public TransactionDAO(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    public void save(TransactionTable transactionT){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(transactionT);
            transaction.commit();
        }catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }finally   {
            if (session != null) {
                session.close();
            }
        }
    }

    public void update(TransactionTable transactionT){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.merge(transactionT);
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
