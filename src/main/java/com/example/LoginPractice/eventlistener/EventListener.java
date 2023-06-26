package com.example.LoginPractice.eventlistener;

import com.example.LoginPractice.service.outbound.dto.MemberCommittedEvent;
import com.example.LoginPractice.service.outbound.dto.SentEmailCodeEvent;
import com.example.LoginPractice.service.outbound.dto.SentMobileCodeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Subscribe
    public void sendEmailCodeEvent(SentEmailCodeEvent event) throws JsonProcessingException {
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
    }

    @Subscribe
    public void sendMobileCodeEvent(SentMobileCodeEvent event) throws JsonProcessingException {
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
    }

    @Subscribe
    public void memberCommittedEvent(MemberCommittedEvent event) throws JsonProcessingException {
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
    }
}
