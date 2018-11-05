package fr.gwombat.springrewriteurl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Created by guillaume.
 *
 * @since 27/07/2018
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private MessageSourceAccessor messageSourceAccessor;

    @GetMapping
    public String findRandomProduct() {
        return findProduct(new Random().nextLong());
    }

    @GetMapping("/{id}")
    public String findProduct(@PathVariable("id") Long id) {
        return messageSourceAccessor.getMessage("message.product", new Object[]{id});
    }

    @Autowired
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }
}
