package com.fastdev.core.transaction;

@FunctionalInterface
public interface WithoutTransaction<T> {
	T call( );
}
