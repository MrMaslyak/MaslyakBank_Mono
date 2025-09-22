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
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            if (transactionT.getId() == null) {
                session.persist(transactionT);
            } else {
                session.merge(transactionT);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }






}
