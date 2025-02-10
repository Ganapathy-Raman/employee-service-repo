package com.rts.tap.daoimplementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rts.tap.dao.RoleDao;
import com.rts.tap.model.Role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class RoleDaoImpli implements RoleDao {

	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpli.class);

	private EntityManager entityManager;

	public RoleDaoImpli(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRole() {
		String hql = "from Role";
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	public Role getRoleById(Long id) {
		return entityManager.find(Role.class, id);
	}

	public Role getRoleByName(String roleName) {
		try {
			return entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
					.setParameter("roleName", roleName).getSingleResult();
		} catch (NoResultException e) {

			logger.error("Role not found for roleName: {}", roleName);
			return null;
		}
	}

}
