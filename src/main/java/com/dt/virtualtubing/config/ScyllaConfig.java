package com.dt.virtualtubing.config;

import static com.datastax.driver.core.schemabuilder.SchemaBuilder.createKeyspace;
import static com.datastax.driver.mapping.NamingConventions.LOWER_CAMEL_CASE;
import static com.datastax.driver.mapping.NamingConventions.LOWER_SNAKE_CASE;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.extras.codecs.jdk8.LocalDateTimeCodec;
import com.datastax.driver.mapping.DefaultNamingStrategy;
import com.datastax.driver.mapping.DefaultPropertyMapper;
import com.datastax.driver.mapping.MappingConfiguration;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.PropertyMapper;

@Configuration
public class ScyllaConfig {

	@Bean
	public Cluster cluster(@Value("${SCYLLA_NODE1_URL}") String node1, @Value("${SCYLLA_NODE1_URL}") String node2,
			@Value("${SCYLLA_NODE1_URL}") String node3) {
		Cluster cluster = Cluster.builder().addContactPoints(node1, node2, node3).build();
		cluster.getConfiguration().getCodecRegistry().register(LocalDateTimeCodec.instance);
		return cluster;
	}

	@Bean
	public Session session(Cluster cluster, @Value("${scylla.keyspace}") String keyspace) {
		final Session session = cluster.connect();
		setupKeyspace(session, keyspace);
		return session;
	}

	@Bean
	public MappingManager mappingManager(Session session) {
		final PropertyMapper propertyMapper = new DefaultPropertyMapper().setNamingStrategy(
				new DefaultNamingStrategy(LOWER_CAMEL_CASE, LOWER_SNAKE_CASE));
		final MappingConfiguration configuration = MappingConfiguration.builder().withPropertyMapper(propertyMapper).build();
		return new MappingManager(session, configuration);
	}

	private void setupKeyspace(Session session, String keyspace) {
		final Map<String, Object> replication = new HashMap<>();
		replication.put("class", "NetworkTopologyStrategy");
		replication.put("DC1", 3);
		session.execute(createKeyspace(keyspace).ifNotExists().with().replication(replication));
		session.execute("USE " + keyspace);
	}

}
