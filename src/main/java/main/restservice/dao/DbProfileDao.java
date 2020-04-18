package main.restservice.dao;

import main.restservice.dao.api.ProfileDao;
import main.restservice.domain.Match;
import main.restservice.domain.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbProfileDao implements ProfileDao {
    private static final Logger log = LoggerFactory.getLogger(DbProfileDao.class);

    @PersistenceContext
    private EntityManager entityManager;
    private long count = 1;

    @Override
    public Profile getRandomProfile() {
        log.debug("Получение случайного профиля");
        Long firstResult = (Long) entityManager.createQuery("SELECT COUNT (p) from Profile p").getSingleResult();
        if (count > firstResult) {
            count = 1;
        }
        log.debug("Профиль полученн");
        return getProfileById(count++);
    }

    @Override
    public Profile getProfileById(Long id) {
        log.debug("Получение профиля по id");
        return entityManager.find(Profile.class, id);
    }

    @Override
    public List<Profile> getAllProfile() {
        log.debug("Получение всех профилей");
        String sql = "SELECT e FROM Profile e";
        return entityManager.createQuery(sql).getResultList();
    }

    @Override
    @Transactional
    public Profile getProfileByName(String username) {
        log.debug("Получение профиля по имени");
        Profile profile = null;
        Query query = entityManager.createQuery("SELECT e FROM Profile e WHERE e.userName = :username");
        query.setParameter("username", username);
        try {
            profile = (Profile) query.getSingleResult();
        } catch (NoResultException e) {
            log.debug("Профиля не существует");

        }
        log.debug("Профиль получен");
        return profile;
    }

    @Transactional
    @Override
    public void save(Profile profile) {
        log.debug("Сохранение профиля");
        entityManager.persist(profile);
    }

    @Override
    @Transactional
    public List<Profile> getMatchList(String name) {
        log.debug("Получение матчей пользователя");
        Query query = entityManager.createQuery("SELECT e.lovers FROM MATCH e WHERE e.name = :username");
        query.setParameter("username", name);
        List<Profile> resultList = (List<Profile>) query.getResultList().stream().map(result -> getProfileByName((String) result)).collect(Collectors.toList());
        return resultList;
    }

    @Override
    @Transactional
    public void deleteProfile(String name) {
        log.debug("Удаление профиля");
        entityManager.remove(getProfileByName(name));
    }

    @Override
    @Transactional
    public void changeDescription(String name, String description) {
        log.debug("Изменение описания");
        Profile profileByName = getProfileByName(name);
        profileByName.setDescription(description);
    }

    @Override
    @Transactional
    public void saveMatch(Match match) {
        log.debug("Сохранение матча");
        entityManager.persist(match);
    }

    @Override
    public Match getMatch(String name, String matchName) {
        log.debug("Получение матча");
        Match match = null;
        Query query = entityManager.createQuery("SELECT e FROM MATCH e WHERE e.name = :username AND e.lovers = :matchName");
        query.setParameter("username", name);
        query.setParameter("matchName", matchName);
        try {
            match = (Match) query.getSingleResult();
        } catch (NoResultException e) {
            log.debug("Матча не существует");
        }
        return match;
    }

    @Override
    public List<Profile> getMansProfiles() {
        Query query = entityManager.createQuery("SELECT e FROM Profile e WHERE e.gender = :gender");
        query.setParameter("gender", "сударь");
        return query.getResultList();
    }

    @Override
    public List<Profile> getWomansProfiles() {
        Query query = entityManager.createQuery("SELECT e FROM Profile e WHERE e.gender = :gender");
        query.setParameter("gender", "сударыня");
        return query.getResultList();
    }
}
