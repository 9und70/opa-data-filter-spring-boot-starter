package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author joffryferrater
 *
 * @param <T> The class type
 * @param <ID> The class id
 */
public class OpaMongoRepositoryFactory<T, ID> extends MongoRepositoryFactory {

    private final MongoQueryService<T> mongoQueryService;
    private final @Nullable MongoOperations mongoOperations;
    private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

    /**
     * Creates a new {@link MongoRepositoryFactory} with the given {@link MongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     * @param mongoQueryService {@link MongoQueryService}
     */
    public OpaMongoRepositoryFactory(MongoOperations mongoOperations, MongoQueryService<T> mongoQueryService) {
        super(mongoOperations);
        this.mongoOperations = mongoOperations;
        this.mappingContext = mongoOperations.getConverter().getMappingContext();
        this.mongoQueryService = mongoQueryService;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation information) {
        MongoEntityInformation<T, ID> entityInformation = getMongoEntityInformation((Class<T>)information.getDomainType(), information);
        return new OpaDataFilterMongoRepositoryImpl<>(entityInformation, mongoOperations, mongoQueryService);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return OpaDataFilterMongoRepository.class;
    }

    private MongoEntityInformation<T, ID> getMongoEntityInformation(Class<T> domainClass, @Nullable RepositoryMetadata metadata) {
        MongoPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(domainClass);
        Assert.notNull(entity, "Entity must not be null!");
        Class<ID> classIdType = metadata != null ? (Class<ID>)metadata.getIdType() : null;
        return new MappingMongoEntityInformation<>((MongoPersistentEntity<T>)entity, classIdType);
    }
}