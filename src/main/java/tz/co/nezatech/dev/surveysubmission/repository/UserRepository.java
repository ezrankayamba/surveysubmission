package tz.co.nezatech.dev.surveysubmission.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.dev.surveysubmission.model.Role;
import tz.co.nezatech.dev.surveysubmission.model.User;

@Repository
public class UserRepository extends BaseDataRepository<User> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public RowMapper<User> getRowMapper() {
		return new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int i) throws SQLException {
				User entity = new User(rs.getString("username"), null, rs.getString("email"),
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
		return "select u.*, r.name as role_name, r.description as role_description from tbl_user u left join tbl_role r on u.role_id=r.id where u.id = ?";
	}

	@Override
	public PreparedStatement psCreate(User entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into tbl_user(username, password,email, role_id, enabled) values (?,?,?,?, ?)");
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
}
