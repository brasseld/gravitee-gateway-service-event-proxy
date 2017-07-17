/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.services.eventproxy;

import io.gravitee.common.service.AbstractService;
import io.gravitee.gateway.services.eventproxy.http.HttpEventRepository;
import io.gravitee.repository.management.api.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class EventProxyService extends AbstractService {

    private final Logger LOGGER = LoggerFactory.getLogger(EventProxyService.class);

    @Value("${services.eventproxy.enabled:true}")
    private boolean enabled;

    @Override
    protected String name() {
        return "Event HTTP proxy";
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        if (enabled) {
            LOGGER.info("Overriding Event repository implementation with HTTP Event repository");

            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext.getParent()).getBeanFactory();
            EventRepository eventRepository = beanFactory.getBean(EventRepository.class);

            LOGGER.debug("Current Event repository implementation is {}", eventRepository.getClass().getName());

            String [] beanNames = beanFactory.getBeanNamesForType(EventRepository.class);
            String oldBeanName = beanNames[0];

            beanFactory.destroySingleton(oldBeanName);

            LOGGER.debug("Register Event repository implementation {}", HttpEventRepository.class.getName());
            HttpEventRepository httpEventRepository = new HttpEventRepository(eventRepository);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(httpEventRepository);

            beanFactory.registerSingleton(EventRepository.class.getName(), httpEventRepository);
        }
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }
}
