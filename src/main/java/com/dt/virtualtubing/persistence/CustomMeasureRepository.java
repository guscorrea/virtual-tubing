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
import com.dt.virtualtubing.persistence.entity.CustomMeasure;

@Repository
public class CustomMeasureRepository {

	private static final String TABLE = "custom_measure";

	private final Mapper<CustomMeasure> mapper;

	private final Session session;

	public CustomMeasureRepository(MappingManager mappingManager) {
		createTable(mappingManager.getSession());
		this.mapper = mappingManager.mapper(CustomMeasure.class);
		this.session = mappingManager.getSession();
	}

	private void createTable(Session session) {
		session.execute(
				SchemaBuilder.createTable(TABLE)
						.ifNotExists()
						.addPartitionKey("tubing_id", uuid())
						.addClusteringColumn("property_type", text())
						.addClusteringColumn("timestamp", timestamp())
						.addColumn("value", text()));
	}

	public List<CustomMeasure> findAll() {
		final ResultSet result = session.execute(select().all().from(TABLE));
		return mapper.map(result).all();
	}

	public List<CustomMeasure> findAllByTubing(UUID tubingId) {
		final ResultSet result = session.execute(select().all().from(TABLE).where(eq("tubing_id", tubingId)));
		return mapper.map(result).all();
	}

	public List<CustomMeasure> findAllByTubingIdAndType(UUID tubingId, String propertyType) {
		final ResultSet result = session.execute(
				select().all().from(TABLE)
						.where(eq("tubing_id", tubingId))
						.and(eq("property_type", propertyType)));
		return mapper.map(result).all();
	}

	public List<CustomMeasure> findAllByTubingIdAndTypeAndDateTime(UUID tubingId, String propertyType, String startDateTime,
			String endDateTime) {
		final ResultSet result = session.execute(
				select().all().from(TABLE)
						.where(eq("tubing_id", tubingId))
						.and(eq("property_type", propertyType))
						.and(gte("timestamp", LocalDateTime.parse(startDateTime)))
						.and(lte("timestamp", LocalDateTime.parse(endDateTime))));
		return mapper.map(result).all();
	}

	public CustomMeasure save(CustomMeasure customMeasure) {
		mapper.save(customMeasure);
		return customMeasure;
	}

}
