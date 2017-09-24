package tz.co.nezatech.dev.surveysubmission.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.dev.surveysubmission.model.Project;
import tz.co.nezatech.dev.surveysubmission.model.Status;

@Repository
public class ProjectRepository extends BaseDataRepository<Project> {
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	FormReposRepository formReposRepository;

	@Override
	public RowMapper<Project> getRowMapper() {
		return new RowMapper<Project>() {

			@Override
			public Project mapRow(ResultSet rs, int i) throws SQLException {
				Project entity = new Project(0, rs.getString("name"), rs.getString("status"));
				entity.setId(rs.getInt("id"));
				return entity;
			}
		};
	}

	@Override
	public String sqlFindAll() {
		return "select pr.* from tbl_project pr";
	}

	@Override
	public String sqlFindById() {
		return sqlFindAll() + " where pr.id = ?";
	}

	@Override
	public PreparedStatement psCreate(Project entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into tbl_project(name, status) values (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getStatus());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psUpdate(Project entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update tbl_project set name=?,  status=? where id=?");
			ps.setString(1, entity.getName());
			ps.setString(2, entity.getStatus());
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
			ps = conn.prepareStatement("delete from tbl_project where id=?");
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
	public Status onSave(Project entity, Status status) {
		// TODO Auto-generated method stub
		return status;
	}

	@Override
	public List<Project> onList(List<Project> list) {
		if (list != null && !list.isEmpty()) {
			for (Iterator<Project> iterator = list.iterator(); iterator.hasNext();) {
				final Project project = (Project) iterator.next();
				project.getRepos().addAll(formReposRepository.getAll("project_id", project.getId()));
			}
		}
		return list;
	}
}
