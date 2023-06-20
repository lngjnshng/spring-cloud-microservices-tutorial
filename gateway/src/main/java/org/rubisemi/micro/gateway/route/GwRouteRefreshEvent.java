package org.rubisemi.micro.gateway.route;

import org.springframework.context.ApplicationEvent;

public class GwRouteRefreshEvent extends ApplicationEvent {
    public GwRouteRefreshEvent(Object source) {
        super(source);
    }
}
