package tz.co.nezatech.dev.surveysubmission.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.dev.surveysubmission.model.Role;
import tz.co.nezatech.dev.surveysubmission.model.Status;

@Repository
public class RoleRepository extends BaseDataRepository<Role> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public RowMapper<Role> getRowMapper() {
		return new RowMapper<Role>() {

			@Override
			public Role mapRow(ResultSet rs, int i) throws SQLException {
				Role role = new Role(rs.getString("name"), rs.getString("description"));
				role.setId(rs.getInt("id"));
				return role;
			}
		};
	}

	@Override
	public String sqlFindAll() {
		return "select r.* from tbl_role r";
	}

	@Override
	public String sqlFindById() {
		return "select r.* from tbl_role r where r.id = ?";
	}

	@Override
	public PreparedStatement psCreate(Role entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into tbl_role(name, description) values (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psUpdate(Role entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update tbl_role set name=?, description=? where id=?");
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
			ps.setInt(3, entity.getId());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psDelete(Integer id, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from tbl_role where id=?");
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
	public Status onSave(Role entity, Status status) {
		// TODO Auto-generated method stub
		return status;
	}
}
