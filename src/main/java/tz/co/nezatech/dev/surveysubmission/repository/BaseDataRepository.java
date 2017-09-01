package tz.co.nezatech.dev.surveysubmission.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import tz.co.nezatech.dev.surveysubmission.model.Status;

public abstract class BaseDataRepository<T extends Object> implements IDataRepository<T> {
	@Override
	public List<T> getAll() {
		return getJdbcTemplate().query(sqlFindAll(), getRowMapper());
	}

	@Override
	public Status create(final T entity) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			int res = getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					return psCreate(entity, conn);
				}
			}, keyHolder);
			return res == 1 ? new Status(200, "Successfully created enity " + entity, keyHolder.getKey().intValue())
					: new Status(500, "Failed to creat role " + entity + ". Error msg: No role created");
		} catch (DataAccessException e) {
			if (e.getMessage().contains("Duplicate entry") && updateOnDuplicate()) {
				return this.update(entity);
			} else {
				e.printStackTrace();
				return new Status(500, "Failed to creat role " + entity + ". Error msg: " + e.getMessage());
			}
		}
	}

	@Override
	public T findById(int id) {
		List<T> entities = getJdbcTemplate().query(sqlFindById(), new Object[] { id }, getRowMapper());
		if (entities != null && !entities.isEmpty()) {
			return entities.get(0);
		}
		return null;
	}

	@Override
	public Status update(Integer id, T entity) {
		try {
			int res = getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					return psUpdate(entity, conn);
				}
			});
			return res == 1 ? new Status(200, "Successfully updated entity " + entity)
					: new Status(500, "Failed to update role " + entity + ". Error msg: No role created");
		} catch (DataAccessException e) {
			e.printStackTrace();
			return new Status(500, "Failed to update entity " + entity + ". Error msg: " + e.getMessage());
		}
	}

	@Override
	public Status update(T entity) {
		try {
			int res = getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					return psUpdateByKey(entity, conn);
				}
			});
			return res == 1 ? new Status(200, "Successfully updated entity " + entity)
					: new Status(500, "Failed to update role " + entity + ". Error msg: No role created");
		} catch (DataAccessException e) {
			e.printStackTrace();
			return new Status(500, "Failed to update entity " + entity + ". Error msg: " + e.getMessage());
		}
	}

	@Override
	public PreparedStatement psUpdateByKey(T entity, Connection conn) {
		PreparedStatement ps = null;

		return ps;
	}

	@Override
	public Status delete(Integer id) {
		try {
			int res = getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					return psDelete(id, conn);
				}
			});
			return res == 1 ? new Status(200, "Successfully deleted entity " + id)
					: new Status(500, "Failed to delete entity " + id + ". Error msg: No entity deleted");
		} catch (DataAccessException e) {
			e.printStackTrace();
			return new Status(500, "Failed to delete entity " + id + ". Error msg: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T schema() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		Class<T> cls = (Class<T>) (paramType.getActualTypeArguments()[0]);
		try {
			return cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean updateOnDuplicate() {
		return false;
	}
}
