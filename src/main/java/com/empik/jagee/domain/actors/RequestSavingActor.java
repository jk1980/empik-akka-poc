package com.empik.jagee.domain.actors;

import akka.actor.UntypedActor;
import com.empik.jagee.db.RequestRepository;
import com.empik.jagee.db.entity.RequestEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class RequestSavingActor extends UntypedActor {

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public void onReceive(Object msg) throws Throwable {

        log.info("Receive message {}", msg.toString());

        if (msg instanceof RequestSaveMsg) {

            val login = ((RequestSaveMsg) msg).getLogin();

            val request = requestRepository.findByLogin(login)
                    .orElse(createDefaultRequest(login));

            request.setRequestCount(request.getRequestCount() + 1);

            requestRepository.save(request);

        } else {

            unhandled(msg);
        }
    }

    @AllArgsConstructor
    @Getter
    public static class RequestSaveMsg {

        private String login;
    }

    private RequestEntity createDefaultRequest(String login) {

        val result = new RequestEntity();

        result.setLogin(login);
        result.setRequestCount(0);

        return result;
    }
}
