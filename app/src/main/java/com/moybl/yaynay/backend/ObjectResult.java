package com.moybl.yaynay.backend;

public class ObjectResult<T> implements YayNayResult {

	private boolean mIsSuccess;
	private T mData;

	public ObjectResult() {
		mIsSuccess = false;
	}

	public ObjectResult(T data) {
		mIsSuccess = true;
		mData = data;
	}

	@Override
	public boolean isSuccess() {
		return mIsSuccess;
	}

	public T getObject() {
		return mData;
	}

}
