package com.dt.virtualtubing.persistence;

import static com.datastax.driver.core.DataType.cboolean;
import static com.datastax.driver.core.DataType.list;
import static com.datastax.driver.core.DataType.text;
import static com.datastax.driver.core.DataType.timestamp;
import static com.datastax.driver.core.DataType.uuid;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.dt.virtualtubing.persistence.entity.Tubing;

@Repository
public class TubingRepository {

	private Mapper<Tubing> mapper;

	private Session session;

	private static final String TABLE = "tubing";

	public TubingRepository(MappingManager mappingManager) {
		createTable(mappingManager.getSession());
		this.mapper = mappingManager.mapper(Tubing.class);
		this.session = mappingManager.getSession();
	}

	private void createTable(Session session) {
		session.execute(
				SchemaBuilder.createTable(TABLE)
						.ifNotExists()
						.addPartitionKey("tubing_id", uuid())
						.addColumn("name", text())
						.addColumn("tubing_info", text())
						.addColumn("pdg_id_list", list(uuid()))
						.addColumn("icv_valve_is_open", cboolean())
						.addColumn("creation_date_time", timestamp()));
	}

	public Tubing find(UUID id) {
		return mapper.get(id);
	}

	public List<Tubing> findAll() {
		final ResultSet result = session.execute(select().all().from(TABLE));
		return mapper.map(result).all();
	}

	public void delete(UUID id) {
		mapper.delete(id);
	}

	public Tubing save(Tubing tubing) {
		mapper.save(tubing);
		return tubing;
	}

}
