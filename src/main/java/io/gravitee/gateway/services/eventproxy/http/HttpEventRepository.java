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
package io.gravitee.gateway.services.eventproxy.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gravitee.common.data.domain.Page;
import io.gravitee.repository.exceptions.TechnicalException;
import io.gravitee.repository.management.api.EventRepository;
import io.gravitee.repository.management.api.search.EventCriteria;
import io.gravitee.repository.management.api.search.Pageable;
import io.gravitee.repository.management.model.Event;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class HttpEventRepository implements EventRepository {

    @Autowired
    private Vertx vertx;

    private final ObjectMapper mapper = new ObjectMapper();

    private final EventRepository eventRepository;

    public HttpEventRepository(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<Event> search(EventCriteria eventCriteria, Pageable pageable) {
        return eventRepository.search(eventCriteria, pageable);
    }

    @Override
    public List<Event> search(EventCriteria eventCriteria) {
        return eventRepository.search(eventCriteria);
    }

    @Override
    public Optional<Event> findById(String id) throws TechnicalException {
        return eventRepository.findById(id);
    }

    @Override
    public Event create(Event event) throws TechnicalException {
        try {
            mapper.writeValueAsString(event);

            return event;
        } catch (JsonProcessingException jpe) {
            throw new TechnicalException("Unable to create event", jpe);
        }
    }

    @Override
    public Event update(Event event) throws TechnicalException {
        try {
            mapper.writeValueAsString(event);

            return event;
        } catch (JsonProcessingException jpe) {
            throw new TechnicalException("Unable to create event", jpe);
        }
    }

    @Override
    public void delete(String s) throws TechnicalException {
        // Should not be called
        throw new IllegalStateException("Delete operation must not be called from the gateway");
    }
}
