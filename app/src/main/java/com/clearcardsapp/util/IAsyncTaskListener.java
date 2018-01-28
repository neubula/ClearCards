package com.clearcardsapp.util;

public interface IAsyncTaskListener {
	/**
	* Method to be implemented when background process starts
	*/
	void processOnStart();
	/**
	* Method to be implemented when background process completes
	*/
	void processOnComplete();
}
