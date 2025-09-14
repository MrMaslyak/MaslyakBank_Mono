package MaslyakBank_Transaction.dao;

import MaslyakBank_Transaction.entity.TransactionDetailsTable;
import MaslyakBank_Transaction.entity.TransactionTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class DetailsDAO {

    private final SessionFactory sessionFactory;

    public DetailsDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(TransactionDetailsTable detailsT){
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.merge(detailsT);
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
}
