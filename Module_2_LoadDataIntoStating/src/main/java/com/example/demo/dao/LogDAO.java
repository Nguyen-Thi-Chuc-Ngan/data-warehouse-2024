package com.example.demo.dao;


import com.example.demo.entities.Log;
import com.example.demo.entities.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Log> findLogsByStatusAndDate() {
        String queryStr = "SELECT l FROM Log l WHERE DATE(l.createTime) = CURRENT_DATE AND l.status = :status ORDER BY l.createTime DESC";
        TypedQuery<Log> query = entityManager.createQuery(queryStr, Log.class);
        query.setParameter("status", Status.SUCCESS_EXTRACT);
        return query.getResultList();
    }
}
