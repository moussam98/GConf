package com.ensao.gi4.utils;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public record MessageSourceUtils(MessageSource messageSource) {

    public String getMessage(String code){
        return  getMessage(code, null);
    }

    public String getMessage(String code, Object[] args){
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] args, Locale locale){
        return messageSource.getMessage(code, args, locale);
    }

}
