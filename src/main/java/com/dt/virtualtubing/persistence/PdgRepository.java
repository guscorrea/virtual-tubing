package com.dt.virtualtubing.persistence;

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
import com.dt.virtualtubing.persistence.entity.Pdg;

@Repository
public class PdgRepository {

	private Mapper<Pdg> mapper;

	private Session session;

	private static final String TABLE = "pdg";

	public PdgRepository(MappingManager mappingManager) {
		createTable(mappingManager.getSession());
		this.mapper = mappingManager.mapper(Pdg.class);
		this.session = mappingManager.getSession();
	}

	private void createTable(Session session) {
		session.execute(
				SchemaBuilder.createTable(TABLE)
						.ifNotExists()
						.addPartitionKey("pdg_id", uuid())
						.addColumn("tubing_id", uuid())
						.addColumn("name", text())
						.addColumn("pdg_info", text())
						.addColumn("creation_date_time", timestamp()));
	}

	public Pdg find(UUID id) {
		return mapper.get(id);
	}

	public List<Pdg> findAll() {
		final ResultSet result = session.execute(select().all().from(TABLE));
		return mapper.map(result).all();
	}

	public void delete(UUID id) {
		mapper.delete(id);
	}

	public Pdg save(Pdg pdg) {
		mapper.save(pdg);
		return pdg;
	}

}
