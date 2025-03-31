package com.ygn.elasticsearch.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.json.JsonData;
import com.ygn.elasticsearch.Helper.Indices;
import com.ygn.elasticsearch.Helper.Util;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Service
public class IndexService {
    private static final Logger LOG = LoggerFactory.getLogger(IndexService.class);

    private final List<String> INDICES_TO_CREATE = List.of(Indices.VEHICLE_INDEX);
    private final ElasticsearchClient elasticSearchClient;

    @Autowired
    public IndexService(ElasticsearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    @PostConstruct
    public void tryToCreateIndices() {
        for (final String indexName : INDICES_TO_CREATE) {
            try {
                boolean indexExists = elasticSearchClient.indices()
                        .exists(e -> e.index(indexName))
                        .value();
                if (indexExists) {
                    LOG.info("Index '{}' already exists. Skipping creation.", indexName);
                    continue;
                }

                // Load settings and mappings JSON as Strings
                String settingsJson = Util.loadAsString("static/es-settings.json");
                String mappingsJson = Util.loadAsString("static/mappings/" + indexName + ".json");

                if (settingsJson == null || mappingsJson == null) {
                    LOG.error("Failed to load settings or mappings for index '{}'", indexName);
                    continue;
                }

                // Create index with settings and mappings using StringReader
                CreateIndexResponse createIndexResponse = elasticSearchClient.indices().create(c -> c
                        .index(indexName)
                        .settings(s -> s
                                .withJson(new StringReader(settingsJson))  // Correct way to pass JSON settings
                        )
                        .mappings(m -> m
                                .withJson(new StringReader(mappingsJson))  // Correct way to pass JSON mappings
                        )
                );

                if (createIndexResponse.acknowledged()) {
                    LOG.info("Successfully created index '{}'", indexName);
                } else {
                    LOG.warn("Index '{}' creation was not acknowledged.", indexName);
                }

            } catch (IOException e) {
                LOG.error("Error creating index '{}': {}", indexName, e.getMessage(), e);
            }
        }
    }


        public void recreateIndices() {
            for (final String indexName : INDICES_TO_CREATE) {
                try {
                    // Delete index if it exists
                    boolean indexExists = elasticSearchClient.indices()
                            .exists(e -> e.index(indexName))
                            .value();
                    if (indexExists) {
                        elasticSearchClient.indices().delete(new DeleteIndexRequest.Builder().index(indexName).build());
                        LOG.info("Deleted existing index '{}'", indexName);
                    }

                    // Load settings and mappings JSON as Strings
                    String settingsJson = Util.loadAsString("static/es-settings.json");
                    String mappingsJson = Util.loadAsString("static/mappings/" + indexName + ".json");

                    if (settingsJson == null || mappingsJson == null) {
                        LOG.error("Failed to load settings or mappings for index '{}'", indexName);
                        continue;
                    }

                    // Create index
                    CreateIndexResponse createIndexResponse = elasticSearchClient.indices().create(c -> c
                            .index(indexName)
                            .settings(s -> s.withJson(new StringReader(settingsJson)))
                            .mappings(m -> m.withJson(new StringReader(mappingsJson)))
                    );

                    if (createIndexResponse.acknowledged()) {
                        LOG.info("Successfully created index '{}'", indexName);
                    } else {
                        LOG.warn("Index '{}' creation was not acknowledged.", indexName);
                    }

                } catch (IOException e) {
                    LOG.error("Error recreating index '{}': {}", indexName, e.getMessage(), e);
                }
            }
    }
}
