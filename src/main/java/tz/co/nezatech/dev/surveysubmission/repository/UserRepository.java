package tz.co.nezatech.dev.surveysubmission.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.dev.surveysubmission.model.Role;
import tz.co.nezatech.dev.surveysubmission.model.Status;
import tz.co.nezatech.dev.surveysubmission.model.User;

@Repository
public class UserRepository extends BaseDataRepository<User> {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ProjectRepository projectRepository;

	@Override
	public RowMapper<User> getRowMapper() {
		return new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int i) throws SQLException {
				User entity = new User(rs.getInt("id"), rs.getString("username"), null, rs.getString("email"),
						new Role(rs.getInt("role_id"), rs.getString("role_name"), rs.getString("role_description")));
				entity.setId(rs.getInt("id"));
				return entity;
			}
		};
	}

	@Override
	public String sqlFindAll() {
		return "select u.*, r.name as role_name, r.description as role_description from tbl_user u left join tbl_role r on u.role_id=r.id ";
	}

	@Override
	public String sqlFindById() {
		return sqlFindAll() + " where u.id = ?";
	}

	@Override
	public PreparedStatement psCreate(User entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"insert into tbl_user(username, password,email, role_id, enabled) values (?,?,?,?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getUsername());
			ps.setString(2, entity.getPassword());
			ps.setString(3, entity.getEmail());
			ps.setInt(4, entity.getRole().getId());
			ps.setBoolean(5, entity.isEnabled());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psUpdate(User entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update tbl_user set username=?,  email=?,role_id=? where id=?");
			ps.setString(1, entity.getUsername());
			ps.setString(2, entity.getEmail());
			ps.setInt(3, entity.getRole().getId());
			ps.setInt(4, entity.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psDelete(Integer id, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from tbl_user where id=?");
			ps.setInt(1, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	@Override
	public Status onSave(final User entity, Status status) {
		if (status.getGeneratedId() > 0)
			entity.setId(status.getGeneratedId());
		System.out.println("Updated user successfully: " + entity.getId() + " Projects? " + entity.getProjects());
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = null;
				try {
					ps = conn.prepareStatement("delete from tbl_user_project where user_id=?");
					ps.setInt(1, entity.getId());
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return ps;
			}
		});
		String sql = "insert into tbl_user_project (user_id, project_id) values (?,?)";
		if (!entity.getProjects().isEmpty()) {

			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					int userId = entity.getId();
					int projectId = entity.getProjects().get(index).getId();
					System.out.println(
							String.format("Record user project(UserId: %d, ProjectId: %d)", userId, projectId));
					ps.setInt(1, userId);
					ps.setInt(2, projectId);

				}

				@Override
				public int getBatchSize() {
					return entity.getProjects().size();
				}
			});
		}
		return status;
	}

	@Override
	public List<User> onList(List<User> list) {
		if (list != null && !list.isEmpty()) {
			for (Iterator<User> iterator = list.iterator(); iterator.hasNext();) {
				final User user = (User) iterator.next();
				getJdbcTemplate().query("select * from tbl_user_project where user_id=" + user.getId(),
						new RowCallbackHandler() {

							@Override
							public void processRow(ResultSet rs) throws SQLException {
								user.getProjects().add(projectRepository.findById(rs.getInt("project_id")));
							}
						});
			}
		}
		return list;
	}
}
