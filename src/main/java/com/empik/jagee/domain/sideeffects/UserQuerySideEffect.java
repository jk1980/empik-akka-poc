package com.empik.jagee.domain.sideeffects;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import com.empik.jagee.domain.actors.RequestSavingActor;
import com.empik.jagee.domain.queries.IQuery;
import com.empik.jagee.domain.queries.UserQuery;
import com.empik.jagee.domain.queries.results.IQueryResult;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.empik.jagee.configuration.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Component
public class UserQuerySideEffect implements ISideEffect {

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

        val login = ((UserQuery)query).getLogin();

        val reqSaver = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem)
                .props("requestSavingActor"), "RSA_" + UUID.randomUUID().toString());

        reqSaver.tell(new RequestSavingActor.RequestSaveMsg(login), ActorRef.noSender());

        reqSaver.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
