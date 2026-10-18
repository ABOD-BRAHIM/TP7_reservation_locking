package com.example.service;

import com.example.model.Reservation;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.OptimisticLockException;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("reservationPU");

    @Override
    public Reservation creerReservation(Reservation reservation) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(reservation);
            em.getTransaction().commit();
            return reservation;
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation modifierReservation(Reservation reservation) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Reservation merged = em.merge(reservation);
            em.getTransaction().commit();
            return merged;
        } catch (OptimisticLockException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof OptimisticLockException) {
                    throw (OptimisticLockException) cause;
                }
                if (cause instanceof org.hibernate.dialect.lock.OptimisticEntityLockException
                        || cause instanceof org.hibernate.StaleObjectStateException) {
                    throw new OptimisticLockException(cause);
                }
                cause = cause.getCause();
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation trouverParId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Reservation.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reservation> listerReservations() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reservation r", Reservation.class).getResultList();
        } finally {
            em.close();
        }
    }
}
