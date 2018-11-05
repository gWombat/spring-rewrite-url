package fr.gwombat.springrewriteurl.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by guillaume.
 *
 * @since 26/07/2018
 */
@RestController
@RequestMapping("/")
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    private MessageSourceAccessor messageSourceAccessor;

    @GetMapping
    public String hello(HttpServletRequest request) {
        return messageSourceAccessor.getMessage("message.hello");
    }

    @Autowired
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }
}
