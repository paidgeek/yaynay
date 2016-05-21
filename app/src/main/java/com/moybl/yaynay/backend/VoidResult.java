package com.moybl.yaynay.backend;

public class VoidResult implements YayNayResult {

	private boolean mIsSuccess;

	public VoidResult(boolean isSuccess) {
		this.mIsSuccess = isSuccess;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

}
