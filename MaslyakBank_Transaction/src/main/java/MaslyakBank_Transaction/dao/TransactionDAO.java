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
            session.persist(transactionT); // сохраняем как новую сущность
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void update(TransactionTable transactionT){
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(transactionT); // обновляем detached entity
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }




}
