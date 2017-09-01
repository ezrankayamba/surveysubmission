package tz.co.nezatech.dev.surveysubmission.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.dev.surveysubmission.model.Form;
import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.Project;
import tz.co.nezatech.dev.surveysubmission.model.User;

@Repository
public class FormRepository extends BaseDataRepository<Form> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public RowMapper<Form> getRowMapper() {
		return new RowMapper<Form>() {

			@Override
			public Form mapRow(ResultSet rs, int i) throws SQLException {
				Form entity = new Form(rs.getString("name"),
						new FormRepos(rs.getString("repos_name"), rs.getString("repos_description"),
								rs.getString("repos_filepath"),
								new Project(rs.getInt("project_id"), rs.getString("proj_name"),
										rs.getString("proj_status"))),
						new User(rs.getString("username"), null, rs.getString("email"), null));
				entity.setId(rs.getInt("id"));
				return entity;
			}
		};
	}

	@Override
	public String sqlFindAll() {
		return "select fm.*, fr.name as repos_name, "
				+ "fr.description as repos_description, fr.filepath as repos_filepath, fr.project_id, "
				+ "pr.name as proj_name, pr.status as proj_status," + " u.username, u.email " + "from tbl_form fm "
				+ "left join tbl_form_repository fr on fm.repository_id=fr.id "
				+ "left join tbl_project pr on fr.project_id=pr.id " + "left join tbl_user u on fm.user_id=u.id ";
	}

	@Override
	public String sqlFindById() {
		return "select fm.*, fr.name as repos_name, "
				+ "fr.description as repos_description, fr.filepath as repos_filepath, fr.project_id, "
				+ "pr.name as proj_name, pr.status as proj_status," + " u.username, u.email " + "from tbl_form_data fm "
				+ "left join tbl_form_repository fr on fm.repository_id=fr.id "
				+ "left join tbl_project pr on fr.project_id=pr.id "
				+ "left join tbl_user u on fm.user_id=u.id where fm.id=? ";
	}

	@Override
	public PreparedStatement psCreate(Form entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into tbl_form(name, repository_id,user_id) values (?,?,?)");
			ps.setString(1, entity.getName());
			ps.setInt(2, entity.getFormRepos().getId());
			ps.setInt(3, entity.getCapturedBy().getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psUpdate(Form entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update tbl_form set name=?,  repository_id=?,user_id=? where id=?");
			ps.setString(1, entity.getName());
			ps.setInt(2, entity.getFormRepos().getId());
			ps.setInt(3, entity.getCapturedBy().getId());
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
			ps = conn.prepareStatement("delete from tbl_form where id=?");
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
