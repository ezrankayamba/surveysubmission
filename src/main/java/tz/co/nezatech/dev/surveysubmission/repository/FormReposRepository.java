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

import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.Project;
import tz.co.nezatech.dev.surveysubmission.model.Status;

@Repository
public class FormReposRepository extends BaseDataRepository<FormRepos> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public RowMapper<FormRepos> getRowMapper() {
		return new RowMapper<FormRepos>() {

			@Override
			public FormRepos mapRow(ResultSet rs, int i) throws SQLException {
				FormRepos entity = new FormRepos(rs.getString("version"), rs.getString("name"),
						rs.getString("description"), rs.getString("filepath"),
						new Project(rs.getInt("project_id"), rs.getString("proj_name"), rs.getString("proj_status")));
				entity.setId(rs.getInt("id"));
				return entity;
			}
		};
	}

	@Override
	public String sqlFindAll() {
		return "select fr.*, " + "pr.name as proj_name, pr.status as proj_status "
				+ "from tbl_form_repository fr left join tbl_project pr on fr.project_id=pr.id ";
	}

	@Override
	public String sqlFindById() {
		return sqlFindAll() + " where fr.id = ?";
	}

	@Override
	public PreparedStatement psCreate(FormRepos entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"insert into tbl_form_repository(name, description,filepath, project_id, version) values (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
			ps.setString(3, entity.getFilepath());
			ps.setInt(4, entity.getProject().getId());
			ps.setString(5, entity.getVersion());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psUpdate(FormRepos entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"update tbl_form_repository set name=?,  description=?, filepath=?, version=?, project_id=? where id=?");
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
			ps.setString(3, entity.getFilepath());
			ps.setString(4, entity.getVersion());
			ps.setInt(5, entity.getProject().getId());
			ps.setInt(6, entity.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psDelete(Integer id, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from tbl_form_repository where id=?");
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
	public Status onSave(FormRepos entity, Status status) {
		// TODO Auto-generated method stub
		return status;
	}

}
