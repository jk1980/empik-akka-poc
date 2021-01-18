package com.empik.jagee.domain.actors;

import akka.actor.UntypedActor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class RequestSavingActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("Receive message");
    }

    @AllArgsConstructor
    @Getter
    public static class RequestSaveMsg {

        private String login;
    }
}
