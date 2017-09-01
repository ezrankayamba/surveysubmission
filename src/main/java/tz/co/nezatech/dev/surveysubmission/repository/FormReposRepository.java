package tz.co.nezatech.dev.surveysubmission.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;

import tz.co.nezatech.dev.surveysubmission.model.FormRepos;
import tz.co.nezatech.dev.surveysubmission.model.Project;

@Repository
public class FormReposRepository extends BaseDataRepository<FormRepos> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public RowMapper<FormRepos> getRowMapper() {
		return new RowMapper<FormRepos>() {

			@Override
			public FormRepos mapRow(ResultSet rs, int i) throws SQLException {
				FormRepos entity = new FormRepos(rs.getString("name"), rs.getString("description"),
						rs.getString("filepath"),
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
		return "select fr.*, " + "pr.name as proj_name, pr.status as proj_status "
				+ "from tbl_form_repository fr left join tbl_project pr on fr.project_id=pr.id where fr.id = ?";
	}

	@Override
	public PreparedStatement psCreate(FormRepos entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"insert into tbl_form_repository(name, description,filepath, project_id) values (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
			ps.setString(3, entity.getFilepath());
			ps.setInt(4, entity.getProject().getId());
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
					"update tbl_form_repository set name=?,  description=?, filepath=?, project_id=? where id=?");
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getDescription());
			ps.setString(3, entity.getFilepath());
			ps.setInt(4, entity.getProject().getId());
			ps.setInt(5, entity.getId());
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
}
