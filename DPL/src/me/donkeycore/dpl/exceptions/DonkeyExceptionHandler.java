package me.donkeycore.dpl.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;

import me.donkeycore.dpl.Donkey;

public class DonkeyExceptionHandler implements UncaughtExceptionHandler {
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Donkey.printError(e);
		System.exit(1);
	}
}
