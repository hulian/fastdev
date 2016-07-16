package com.fastdev.core.handler;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface OnCommand {
	String name();
	String[] rolesAllowed();
}
