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

import tz.co.nezatech.dev.surveysubmission.model.Form;
import tz.co.nezatech.dev.surveysubmission.model.FormData;
import tz.co.nezatech.dev.surveysubmission.model.Status;

@Repository
public class FormDataRepository extends BaseDataRepository<FormData> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public RowMapper<FormData> getRowMapper() {
		return new RowMapper<FormData>() {

			@Override
			public FormData mapRow(ResultSet rs, int i) throws SQLException {
				FormData entity = new FormData(rs.getInt("id"), rs.getString("category"), rs.getString("type"),
						rs.getString("name"), rs.getString("metadata"), rs.getString("rawvalue"),
						new Form(rs.getInt("form_id"), rs.getString("repos_path"), rs.getString("repos_version"),
								rs.getString("form_name"), null, null));
				entity.setId(rs.getInt("id"));
				return entity;
			}
		};
	}

	@Override
	public String sqlFindAll() {
		return "select fd.*, fm.id as form_id, fm.repos_path as repos_path, fm.repos_version as repos_version,fm.name as form_name, fr.name as repos_name, "
				+ "fr.description as repos_description, fr.filepath as repos_filepath, fr.project_id, "
				+ "pr.name as proj_name, pr.status as proj_status," + " u.id as user_id, u.username, u.email "
				+ "from tbl_form_data fd left join tbl_form fm on fd.form_id=fm.id "
				+ "left join tbl_form_repository fr on fm.repository_id=fr.id "
				+ "left join tbl_project pr on fr.project_id=pr.id " + "left join tbl_user u on fm.user_id=u.id ";
	}

	@Override
	public String sqlFindById() {
		return sqlFindAll() + " where fd.id = ?";
	}

	@Override
	public PreparedStatement psCreate(FormData entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"insert into tbl_form_data(metadata, rawvalue,form_id, category, type, name) values (?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, entity.getMetadata());
			ps.setString(2, entity.getRawvalue());
			ps.setInt(3, entity.getForm().getId());
			ps.setString(4, entity.getCategory());
			ps.setString(5, entity.getType());
			ps.setString(6, entity.getName());

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}

	@Override
	public PreparedStatement psUpdate(FormData entity, Connection conn) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update tbl_user set metadata=?,  rawvalue=?,form_id=?, type=? where id=?");
			ps.setString(1, entity.getMetadata());
			ps.setString(2, entity.getRawvalue());
			ps.setInt(3, entity.getForm().getId());
			ps.setString(4, entity.getType());
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
			ps = conn.prepareStatement("delete from tbl_form_data where id=?");
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
	public Status onSave(FormData entity, Status status) {
		return status;
	}
}
