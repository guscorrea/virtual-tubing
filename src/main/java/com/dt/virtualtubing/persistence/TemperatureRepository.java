package com.dt.virtualtubing.persistence;

import static com.datastax.driver.core.DataType.text;
import static com.datastax.driver.core.DataType.timestamp;
import static com.datastax.driver.core.DataType.uuid;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.lte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.dt.virtualtubing.persistence.entity.Temperature;

@Repository
public class TemperatureRepository {

	private Mapper<Temperature> mapper;

	private Session session;

	private static final String TABLE = "temperature";

	public TemperatureRepository(MappingManager mappingManager) {
		createTable(mappingManager.getSession());
		this.mapper = mappingManager.mapper(Temperature.class);
		this.session = mappingManager.getSession();
	}

	private void createTable(Session session) {
		session.execute(
				SchemaBuilder.createTable(TABLE)
						.ifNotExists()
						.addPartitionKey("component_id", uuid())
						.addClusteringColumn("timestamp", timestamp())
						.addColumn("value", text()));
	}

	public List<Temperature> findAll() {
		final ResultSet result = session.execute(select().all().from(TABLE));
		return mapper.map(result).all();
	}

	public List<Temperature> findAllByTubingId(UUID tubingId) {
		final ResultSet result = session.execute(select().all().from(TABLE).where(eq("component_id", tubingId)));
		return mapper.map(result).all();
	}

	public List<Temperature> findAllByTubingIdAndDateTime(UUID tubingId, String startDateTime, String endDateTime) {
		final ResultSet result = session.execute(
				select().all().from(TABLE).where(eq("component_id", tubingId))
						.and(gte("timestamp", LocalDateTime.parse(startDateTime)))
						.and(lte("timestamp", LocalDateTime.parse(endDateTime))));
		return mapper.map(result).all();
	}

	public Temperature save(Temperature temperature) {
		mapper.save(temperature);
		return temperature;
	}

}
