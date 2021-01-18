package com.empik.jagee.domain.sideeffects;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.empik.jagee.db.RequestRepository;
import com.empik.jagee.db.entity.RequestEntity;
import com.empik.jagee.domain.actors.RequestSavingActor;
import com.empik.jagee.domain.queries.IQuery;
import com.empik.jagee.domain.queries.UserQuery;
import com.empik.jagee.domain.queries.results.IQueryResult;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.empik.jagee.configuration.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Component
public class UserQuerySideEffect implements ISideEffect {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ActorSystem actorSystem;

    @Override
    public boolean hasSideEffect(IQuery query, IQueryResult queryResult) {

        if (!(query instanceof UserQuery))
            return false;

        return Strings.isNotBlank(((UserQuery) query).getLogin());
    }

    @Override
    public void doSideEffect(IQuery query, IQueryResult queryResult) {

        val reqSaver = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props("requestSavingActor"), "RSA_" + UUID.randomUUID().toString());

        reqSaver.tell(new RequestSavingActor.RequestSaveMsg("Jacek") , ActorRef.noSender());

        val request = requestRepository.findByLogin(((UserQuery)query).getLogin())
                .orElse(createDefaultRequest(((UserQuery)query).getLogin()));

        request.setRequestCount(request.getRequestCount() + 1);

        requestRepository.save(request);
    }

    private RequestEntity createDefaultRequest(String login) {

        val result = new RequestEntity();

        result.setLogin(login);
        result.setRequestCount(0);

        return result;
    }
}
