package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.BaseEntityRepository;
import com.julianduru.messingjarservice.entities.GroupMessage;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * created by julian on 14/02/2023
 */
@Repository
public interface GroupMessageRepository extends BaseEntityRepository<GroupMessage> {


    Flux<GroupMessage> findByGroupId(ObjectId groupId);


    Flux<GroupMessage> findByGroupId(ObjectId groupId, Pageable pageable);


    Mono<GroupMessage> findFirstByGroupIdOrderByCreatedDateDesc(ObjectId groupId);


    Flux<GroupMessage> findByGroupIdInOrderByCreatedDateDesc(List<ObjectId> groupIds, Pageable pageable);


}
