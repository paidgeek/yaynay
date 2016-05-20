package com.moybl.yaynay.backend;

public class ObjectResult implements YayNayResult {

	private boolean mIsSuccess;
	private Object mData;

	public ObjectResult(boolean isSuccess, Object data) {
		mIsSuccess = isSuccess;
		mData = data;
	}

	@Override
	public boolean isSuccess() {
		return mIsSuccess;
	}

	public Object getData() {
		return mData;
	}

}
