package sw.swe.repository;

import sw.swe.domain.User;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    // User 객체를 받아 DB에 저장. (persist는 영속하여 저장함을 의미함)
    public void save(User user) {
        em.persist(user);
    }

    // User 객체의 고유 id값을 통해 해당하는 유저를 반환함
    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    // DB에 저장된 모든 User 객체 반환
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    // User의 userName을 통해 해당하는 User 객체(들)을 반환함.
    public List<User> findByName(String userName) {
        return em.createQuery("select u from User u join fetch u.project where u.userName = :userName", User.class)
                .setParameter("userName", userName)
                .getResultList();
    }

    public void delete(User user) {
        em.remove(user);
    }
}
